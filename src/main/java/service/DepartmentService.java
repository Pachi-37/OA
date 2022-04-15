package service;

import dao.DepartmentDao;
import entity.Department;
import utils.MybatisUtils;

import java.util.List;

public class DepartmentService {

    public Department selectById(Long departmentId) {
        return (Department) MybatisUtils.executeQuery(
                sqlSession -> sqlSession.getMapper(DepartmentDao.class).selectById(departmentId)
        );
    }

    public List<Department> selectAllDepartments() {
        return (List<Department>) MybatisUtils.executeQuery(
                sqlSession -> sqlSession.getMapper(DepartmentDao.class).selectAllDepartments()
                );
    }

    public Department selectByDepartmentName(String departmentName){
        return (Department) MybatisUtils.executeQuery(
                sqlSession -> sqlSession.getMapper(DepartmentDao.class).selectByDepartmentName(departmentName)
        );
    }
}
