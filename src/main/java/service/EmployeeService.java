package service;

import dao.EmployeeDao;
import entity.Employee;
import utils.MybatisUtils;

public class EmployeeService {

    public Employee selectById(Long employeeId) {
        return (Employee) MybatisUtils.executeQuery(sqlSession -> {
            // 根据接口自动创建实现类
            EmployeeDao employeeDao = sqlSession.getMapper(EmployeeDao.class);
            return employeeDao.selectById(employeeId);
        });
    }
}
