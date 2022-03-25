package controller;

import com.alibaba.fastjson.JSON;
import entity.LeaveForm;
import entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.LeaveFormService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@WebServlet(name = "LeaveFormServlet", value = "/leave/*")
public class LeaveFormServlet extends HttpServlet {

    private LeaveFormService leaveFormService = new LeaveFormService();
    private Logger logger = LoggerFactory.getLogger(LeaveFormServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");

        // 截取方法参数
        String uri = request.getRequestURI();
        String method = uri.substring(uri.lastIndexOf("/") + 1);

        if (method.equals("create")) {
            this.create(request, response);
        } else if (method.equals("list")) {
            this.getLeaveFormList(request, response);
        } else if (method.equals("audit")) {
            this.audit(request, response);
        }
    }

    /**
     * 创建请假单
     *
     * @param request
     * @param response
     * @throws IOException
     */
    private void create(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();

        // 接收请假单数据
        User user = (User) session.getAttribute("login_user");
        String formType = request.getParameter("formType");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String reason = request.getParameter("reason");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH");

        Map result = new HashMap();

        try {
            LeaveForm form = new LeaveForm();
            form.setEmployeeId(user.getEmployeeId());
            form.setStartTime(sdf.parse(startTime));
            form.setEndTime(sdf.parse(endTime));
            form.setFormType(Integer.parseInt(formType));
            form.setReason(reason);
            form.setCreateTime(new Date());

            // 调用业务逻辑
            leaveFormService.createLeaveForm(form);

            result.put("code", "0");
            result.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("请假申请异常", e);
            result.put("code", e.getClass().getName());
            result.put("message", e.getMessage());
        }

        // 返回响应数据
        String json = JSON.toJSONString(result);
        response.getWriter().println(json);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * 查询需要审批的请假单列表
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void getLeaveFormList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("login_user");

        List<Map> leaveFormList = leaveFormService.getLeaveFormList("process", user.getEmployeeId());

        Map result = new HashMap();
        result.put("code", "0");
        result.put("message", "success");
        result.put("count", leaveFormList.size());
        result.put("data", leaveFormList);

        String jsonString = JSON.toJSONString(result);
        response.getWriter().println(jsonString);
    }

    private void audit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        User user = (User) session.getAttribute("login_user");

        String formId = request.getParameter("formId");
        String result = request.getParameter("result");
        String reason = request.getParameter("reason");

        Map map = new HashMap();
        try {
            leaveFormService.audit(Long.parseLong(formId), user.getEmployeeId(), result, reason);

            map.put("code", "0");
            map.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("请假失败", e);

            map.put("code", e.getClass().getName());
            map.put("message", e.getMessage());
        }

        String jsonString = JSON.toJSONString(map);
        response.getWriter().println(jsonString);
    }
}
