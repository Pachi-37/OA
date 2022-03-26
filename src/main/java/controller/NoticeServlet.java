package controller;

import com.alibaba.fastjson.JSON;
import entity.Notice;
import entity.User;
import service.NoticeService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "NoticeServlet", value = "/notice/list")
public class NoticeServlet extends HttpServlet {

    private NoticeService noticeService = new NoticeService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();

        User user = (User) session.getAttribute("login_user");

        List<Notice> noticeList = noticeService.selectByReceiverId(user.getEmployeeId());

        Map result = new HashMap();
        result.put("code", "0");
        result.put("message", "success");
        result.put("count", result.size());
        result.put("data", noticeList);

        String jsonString = JSON.toJSONString(result);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(jsonString);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
