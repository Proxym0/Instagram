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

@WebServlet("/auth")
public class AuthServlet extends HttpServlet {
    private static final String USER = "user";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String MESSAGE = "message";
    private static final String AUTH_PATH = "/pages/auth.jsp";
    private static final String ACCOUNT_PATH = "/pages/account.jsp";
    private static final String WRONG_PASSWORD_MESSAGE = "Wrong password!";
    private static final String USER_NOT_FOUND_MESSAGE = "User not found!";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        getServletContext().getRequestDispatcher(AUTH_PATH).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String username = req.getParameter(USERNAME);
        String password = req.getParameter(PASSWORD);

        Optional<User> byUsername = UserService.getInstance().findByUserName(username);
        if (byUsername.isPresent()) {
            User user = byUsername.get();
            if (user.getPassword().equals(password)) {
                req.getSession().setAttribute(USER, user);
                getServletContext().getRequestDispatcher(ACCOUNT_PATH).forward(req,resp);
                return;
            } else {
                req.setAttribute(MESSAGE, WRONG_PASSWORD_MESSAGE);
            }
        } else {
            req.setAttribute(MESSAGE, USER_NOT_FOUND_MESSAGE);
        }
        getServletContext().getRequestDispatcher(AUTH_PATH).forward(req, resp);
    }
}
