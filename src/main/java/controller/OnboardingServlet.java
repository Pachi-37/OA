package controller;

import com.alibaba.fastjson.JSON;
import dao.RbacDao;
import entity.Department;
import entity.Employee;
import entity.RoleUser;
import entity.User;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import service.DepartmentService;
import service.EmployeeService;
import service.UserService;
import utils.MD5Utils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.*;

@WebServlet(name = "OnboardingServlet", urlPatterns = "/onboarding/*")
public class OnboardingServlet extends HttpServlet {

    private Logger logger = LoggerFactory.getLogger(OnboardingServlet.class);
    private DepartmentService departmentService = new DepartmentService();
    private EmployeeService employeeService = new EmployeeService();
    private UserService userService = new UserService();
    private RbacDao rbacDao = new RbacDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");

        String uri = request.getRequestURI();
        String method = uri.substring(uri.lastIndexOf("/") + 1);

        if (method.equals("create")) {
            this.create(request, response);
        }
    }

    private void create(HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession();

        String name = request.getParameter("name");
        String userName = request.getParameter("user_name");
        String level = request.getParameter("level");
        String title = request.getParameter("title");
        String department = request.getParameter("department");
        String password = request.getParameter("password");

        Department onboardDepartment = departmentService.selectByDepartmentName(department);
        Employee employee = new Employee();
        User user = new User();
        RoleUser roleUser = new RoleUser();
        Map result = new HashMap();
        try {
            employee.setName(name);
            employee.setDepartmentId(onboardDepartment.getDepartmentId());
            employee.setTitle(title);
            employee.setLevel(Integer.parseInt(level));

            employeeService.onboarding(employee);

            int salt = generateSalt(1000);
            user.setUserName(userName);
            user.setPassword(MD5Utils.md5Digest(password, salt));
            user.setEmployeeId(employee.getEmployeeId());
            user.setSalt(salt);

            userService.onboarding(user);


            if (Integer.parseInt(level) >= 7) {
                roleUser.setRoleId(2l);
            } else {
                roleUser.setRoleId(1l);
            }
            roleUser.setUserId(user.getUserId());
            rbacDao.onboarding(roleUser);

            result.put("code", "0");
            result.put("message", "success");
        } catch (Exception e) {
            logger.error("员工入职异常", e);
            result.put("code", e.getClass().getName());
            result.put("message", e.getMessage());
        } finally {
            String json = JSON.toJSONString(result);
            response.getWriter().println(json);
        }

    }

    private int generateSalt(int bound) {
        Random random = new Random();
        return random.nextInt(bound);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
