package dao;

import entity.LeaveForm;
import org.junit.Test;
import utils.MybatisUtils;

import javax.xml.crypto.Data;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class LeaveFormDaoTest {

    @Test
    public void insert() {

        MybatisUtils.executeUpdate(sqlSession -> {
            LeaveFormDao leaveFormDao = sqlSession.getMapper(LeaveFormDao.class);
            LeaveForm form = new LeaveForm();
            form.setEmployeeId(4l);
            form.setFormType(1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startTime = null;
            Date endTime = null;

            try {
                startTime = sdf.parse("2020-03-25 08:00:00");
                endTime = sdf.parse("2020-04-01 18:00:00");
            } catch (ParseException e) {
                e.printStackTrace();
            }

            form.setStartTime(startTime);
            form.setEndTime(endTime);
            form.setReason("回家买房");
            form.setCreateTime(new Date());
            form.setState("processing");
            leaveFormDao.insert(form);
            return null;
        });
    }
}