package dao;

import entity.LeaveForm;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface LeaveFormDao {

    public LeaveForm selectById(Long formId);

    public void insert(LeaveForm leaveForm);

    public void update(LeaveForm leaveForm);

    public List<Map> selectByParams(@Param("pf_state") String state, @Param("pf_operator_id") Long operatorId);
}
