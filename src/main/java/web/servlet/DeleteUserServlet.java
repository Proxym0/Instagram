package web.servlet;

import entity.User;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/delete-account")
public class DeleteUserServlet extends HttpServlet {
    private UserService userService = UserService.getInstance();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userid = req.getParameter("userid");
        long parsedUserId = Long.parseLong(userid);
        Optional<User> byId = userService.findById(parsedUserId);
        if (byId.isPresent()) {
            User user = byId.get();
            userService.removeAccount(user);
            req.setAttribute("message", "user deleted");
            getServletContext().getRequestDispatcher("/index.jsp").forward(req, resp);

        }
    }
}
