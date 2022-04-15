package dao;

import entity.Department;

import java.util.List;

public interface DepartmentDao {
    public Department selectById(Long departmentId);

    public List<Department> selectAllDepartments();

    public Department selectByDepartmentName(String departmentName);
}


