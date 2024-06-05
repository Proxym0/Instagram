package web.filter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class UserLoginFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (req.getSession().getAttribute("user") == null && !checkAuthPaths(req)) {
            res.sendRedirect("/pages/auth.jsp");
        } else {
            chain.doFilter(req, res);
        }
    }
    private boolean checkAuthPaths(HttpServletRequest request) {
        String pathInfo = request.getRequestURI();
        return pathInfo.equals("/auth") || pathInfo.equals("/register") || pathInfo.equals("/pages/auth.jsp") || pathInfo.equals("/pages/register.jsp");
    }
}
