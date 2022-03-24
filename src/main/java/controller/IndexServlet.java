package controller;

import entity.Department;
import entity.Employee;
import entity.Node;
import entity.User;
import service.DepartmentService;
import service.EmployeeService;
import service.UserService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "IndexServlet", value = "/index")
public class IndexServlet extends HttpServlet {

    private UserService userService = new UserService();
    private EmployeeService employeeService = new EmployeeService();
    private DepartmentService departmentService = new DepartmentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        User user = (User) session.getAttribute("login_user");
        Employee employee = employeeService.selectById(user.getEmployeeId());
        Department department = departmentService.selectById(employee.getDepartmentId());
        List<Node> nodeList = userService.selectNodeByUserId(user.getUserId());

        session.setAttribute("current_employee", employee);
        session.setAttribute("current_department", department);

        request.setAttribute("node_list", nodeList);
        request.getRequestDispatcher("/index.ftl").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
