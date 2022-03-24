package dao;

import entity.LeaveForm;

public interface LeaveFormDao {

    public LeaveForm selectById(Long formId);

    public void insert(LeaveForm leaveForm);
}
