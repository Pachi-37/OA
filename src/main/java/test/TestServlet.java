package test;

import utils.MybatisUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "TestServlet", urlPatterns = "/test")
public class TestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String result = (String) MybatisUtils.executeQuery(sqlSession -> sqlSession.selectOne("test.test"));
        request.setAttribute("result", result);
        request.getRequestDispatcher("/test.ftl").forward(request, response);
    }
}
