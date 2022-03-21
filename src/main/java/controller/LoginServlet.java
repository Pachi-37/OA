package controller;

import com.alibaba.fastjson.JSON;
import entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import service.exception.BusinessException;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "LoginServlet", value = "/check_login")
public class LoginServlet extends HttpServlet {
    private UserService userService = new UserService();
    Logger logger = LoggerFactory.getLogger(LoginServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        // 接收用户输入
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        Map<String, Object> result = new HashMap<>();
        // 调用业务逻辑
        try {
            User user = userService.checkLogin(username, password);
            result.put("code", "0");
            result.put("message", "success");
        } catch (BusinessException e) {
            logger.error(e.getMessage(), e);
            result.put("code", e.getCode());
            result.put("message", e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.put("code", e.getClass().getName());
            result.put("message", e.getMessage());
        }
        // 返回调用结果
        String json = JSON.toJSONString(result);
        response.getWriter().println(json);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
