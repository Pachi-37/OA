package service;

import entity.LeaveForm;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class LeaveFormServiceTest {

    LeaveFormService leaveFormService = new LeaveFormService();

    @Test
    public void createLeaveForm() throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");

        LeaveForm leaveForm = new LeaveForm();
        leaveForm.setEmployeeId(8l);
        leaveForm.setStartTime(sdf.parse("2022-03-26 08"));
        leaveForm.setEndTime(sdf.parse("2020-04-01 18"));
        leaveForm.setFormType(1);
        leaveForm.setReason("请假");
        leaveForm.setCreateTime(new Date());
        LeaveForm saveForm = leaveFormService.createLeaveForm(leaveForm);

        System.out.println(saveForm.getFormId());
    }

    @Test
    public void audit() {
        leaveFormService.audit(8l,2l,"approved","早日回来");
        leaveFormService.audit(8l,1l,"approved","早日回来");
    }
}