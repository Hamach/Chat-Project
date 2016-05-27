package by.bsu.tyupa.chat.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/login")
public class DoPostServlet extends HttpServlet {
    private Map<String, String> keys = new HashMap<>();

    @Override
    public void init() throws ServletException {
        keys.put("Tyupa", "admin");
        super.init();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String username = request.getParameter("username");
        String password = request.getParameter("pass");
        if((keys.containsKey(username)) && (keys.get(username).equals(password))) {
            response.sendRedirect("/html/NewChat.html");
        }
        else {
            response.sendRedirect("/html/Error.html");
        }
    }
}
