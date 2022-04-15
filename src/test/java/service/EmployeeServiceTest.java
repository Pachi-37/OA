package service;

import dao.EmployeeDao;
import entity.Employee;
import org.junit.Test;
import utils.MybatisUtils;

import static org.junit.Assert.*;

public class EmployeeServiceTest {

    private EmployeeService employeeService = new EmployeeService();

    @Test
    public void selectById() {
    }

    @Test
    public void onboarding() {
        Employee employee = new Employee();
        employee.setName("test");
        employee.setDepartmentId(0l);
        employee.setTitle("test");
        employee.setLevel(1);

        employeeService.onboarding(employee);
        for (int i = 0; i < 100000; i++) {
            System.out.println(employee.getEmployeeId());
        }
    }
}