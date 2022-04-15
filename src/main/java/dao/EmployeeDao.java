package dao;

import entity.Employee;
import org.apache.ibatis.annotations.Param;

public interface EmployeeDao {

    public Employee selectById(Long employeeId);

    /**
     * 根据传入的员工信息获取上级主管对象
     * @param employee 传入员工对象
     * @return 上级主管对象
     */
    public Employee selectLeader(@Param("emp") Employee employee);

    public void insertEmployee(Employee employee);
}
