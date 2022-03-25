package service;

import dao.EmployeeDao;
import dao.LeaveFormDao;
import dao.ProcessFlowDao;
import entity.Employee;
import entity.LeaveForm;
import entity.ProcessFlow;
import service.exception.BusinessException;
import utils.MybatisUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LeaveFormService {

    public LeaveForm selectById(Long formId) {
        return (LeaveForm) MybatisUtils.executeQuery(sqlSession -> sqlSession.getMapper(LeaveFormDao.class).selectById(formId));
    }

    /**
     * 创建请假单
     *
     * @param form 前端输入的请假单数据
     * @return 持久化之后的请假单对象
     */
    public LeaveForm createLeaveForm(LeaveForm form) {

        LeaveForm savedForm = (LeaveForm) MybatisUtils.executeUpdate(sqlSession -> {
            // 根据职位不同确定提交之后的状态
            EmployeeDao employeeDao = sqlSession.getMapper(EmployeeDao.class);
            Employee employee = employeeDao.selectById(form.getEmployeeId());
            if (employee.getLevel() == 8) {
                form.setState("approved");
            } else {
                form.setState("processing");
            }
            LeaveFormDao leaveFormDao = sqlSession.getMapper(LeaveFormDao.class);
            leaveFormDao.insert(form);

            // 增加第一条流程数据,状态为complete
            ProcessFlowDao processFlowDao = sqlSession.getMapper(ProcessFlowDao.class);
            ProcessFlow flow1 = new ProcessFlow();
            flow1.setFormId(form.getFormId());
            flow1.setOperatorId(employee.getEmployeeId());
            flow1.setAction("apply");
            flow1.setCreateTime(new Date());
            flow1.setOrderNo(1);
            flow1.setState("complete");
            flow1.setIsLast(0);
            processFlowDao.insert(flow1);

            // 分情况创建其余流程数据
            if (employee.getLevel() < 7) {
                Employee dmanager = employeeDao.selectLeader(employee);
                ProcessFlow flow2 = new ProcessFlow();
                flow2.setFormId(form.getFormId());
                flow2.setOperatorId(dmanager.getEmployeeId());
                flow2.setAction("audit");
                flow2.setCreateTime(new Date());
                flow2.setOrderNo(2);
                flow2.setState("process");

                long diff = form.getEndTime().getTime() - form.getStartTime().getTime();
                float hours = diff / (1000 * 60 * 60) * 1f;

                if (hours >= BussinessConstants.MANAGER_AUDIT_HOURS) {

                    flow2.setIsLast(0);
                    processFlowDao.insert(flow2);
                    Employee manager = employeeDao.selectLeader(dmanager);
                    ProcessFlow flow3 = new ProcessFlow();
                    flow3.setFormId(form.getFormId());
                    flow3.setOperatorId(manager.getEmployeeId());
                    flow3.setAction("audit");
                    flow3.setCreateTime(new Date());
                    flow3.setState("ready");
                    flow3.setOrderNo(3);
                    flow3.setIsLast(1);

                    processFlowDao.insert(flow3);
                } else {
                    flow2.setIsLast(1);

                    processFlowDao.insert(flow2);
                }
            } else if (employee.getLevel() == 7) {
                // 7级员工,生成总经理审批任务
                Employee manager = employeeDao.selectLeader(employee);
                ProcessFlow flow = new ProcessFlow();
                flow.setFormId(form.getFormId());
                flow.setOperatorId(manager.getEmployeeId());
                flow.setAction("audit");
                flow.setCreateTime(new Date());
                flow.setState("process");
                flow.setOrderNo(2);
                flow.setIsLast(1);

                processFlowDao.insert(flow);
            } else if (employee.getLevel() == 8) {
                // 8级员工,生成总经理审批任务,系统自动通过
                ProcessFlow flow = new ProcessFlow();
                flow.setFormId(form.getFormId());
                flow.setOperatorId(employee.getEmployeeId());
                flow.setAction("audit");
                flow.setResult("approved");
                flow.setReason("自动通过");
                flow.setCreateTime(new Date());
                flow.setAuditTime(new Date());
                flow.setState("complete");
                flow.setOrderNo(2);
                flow.setIsLast(1);

                processFlowDao.insert(flow);
            }
            return form;
        });
        return savedForm;
    }

    public List<Map> getLeaveFormList(String pfState, Long operatorId) {
        return (List<Map>) MybatisUtils.executeQuery(sqlSession -> {
            LeaveFormDao leaveFormDao = sqlSession.getMapper(LeaveFormDao.class);
            List<Map> list = leaveFormDao.selectByParams(pfState, operatorId);
            return list;
        });
    }

    /**
     * 请假审核
     *
     * @param formId     表单编号
     * @param operatorId 经办人编号
     * @param result     审批结果
     * @param reason     审批原因
     */
    public void audit(Long formId, Long operatorId, String result, String reason) {
        MybatisUtils.executeUpdate(sqlSession -> {
            ProcessFlowDao processFlowDao = sqlSession.getMapper(ProcessFlowDao.class);
            List<ProcessFlow> processFlowList = processFlowDao.selectByFormId(formId);

            if (processFlowList.size() == 0) {
                throw new BusinessException("PF001", "无效审批流程");
            }

            // 过滤得到当前任务对象
            List<ProcessFlow> list = processFlowList.stream().filter(processFlow -> processFlow.getOperatorId() == operatorId && processFlow.getState().equals("process")).collect(Collectors.toList());
            ProcessFlow flow = null;
            if (list.size() == 0) {
                throw new BusinessException("PF002", "无待处理审批任务");
            } else {
                flow = list.get(0);
                flow.setState("complete");
                flow.setResult(result);
                flow.setReason(reason);
                flow.setAuditTime(new Date());

                processFlowDao.update(flow);

            }

            LeaveFormDao leaveFormDao = sqlSession.getMapper(LeaveFormDao.class);
            LeaveForm form = leaveFormDao.selectById(formId);
            // 判断当前任务是否为最后一个节点
            if (flow.getIsLast() == 1) {
                form.setState(result);
                leaveFormDao.update(form);
            } else {

                // 取出后续节点
                List<ProcessFlow> flows = processFlowList.stream().filter(processFlow -> processFlow.getState().equals("ready")).collect(Collectors.toList());

                // 当前节点审批驳回
                if (result.equals("refused")) {
                    form.setState(result);
                    leaveFormDao.update(form);

                    for (ProcessFlow p : flows) {
                        p.setState("cancel");
                        processFlowDao.update(p);
                    }
                } else if (result.equals("approved")) {
                    ProcessFlow readyFlow = flows.get(0);
                    readyFlow.setState("process");
                    processFlowDao.update(readyFlow);
                }
            }

            return null;
        });
    }
}
