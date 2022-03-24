package dao;

import entity.ProcessFlow;
import org.junit.Test;
import utils.MybatisUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class ProcessFlowDaoTest {

    @Test
    public void insert() {
        MybatisUtils.executeUpdate(sqlSession -> {
            ProcessFlowDao dao = sqlSession.getMapper(ProcessFlowDao.class);

            ProcessFlow processFlow = new ProcessFlow();

            processFlow.setFormId(4l);
            processFlow.setOperatorId(2l);
            processFlow.setAction("audit");
            processFlow.setReason("approve");
            processFlow.setCreateTime(new Date());
            processFlow.setOrderNo(1);
            processFlow.setState("ready");
            processFlow.setIsLast(1);

            dao.insert(processFlow);

            return null;
        });
    }
}