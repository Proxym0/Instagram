import entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.UserService;
import validators.UserValidator;
import web.servlet.RegistrationServlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class RegistrationServletTest {
    private static final String AVATAR = "avatar";
    private static final String EMAIL = "email";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String MESSAGE = "message";
    private static final String FULL_NAME = "fullName";
    private static final String REG_PATH = "/pages/register.jsp";
    private static final String AUTH_PATH = "/auth";
    private static final String USER_ALREADY_EXISTS = "The user already exists!!!";
    private static final String REGISTRATION_FAILED = "Registration failed. Check the correctness of the entered data!";
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private UserService userService;
    @Mock
    private UserValidator userValidator;

    private RegistrationServlet registrationServlet;
@BeforeEach
public void setUp() {
    registrationServlet = new RegistrationServlet(userService,userValidator);
}

    @Test
    public void doGet_shouldForwardToRegisterPage() throws ServletException, IOException {
        when(request.getRequestDispatcher(REG_PATH)).thenReturn(requestDispatcher);
        registrationServlet.doGet(request, response);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void doPost_shouldRedirectToAuthPage_whenUserIsValidAndDoesNotExist() throws ServletException, IOException {
        when(request.getParameter(FULL_NAME)).thenReturn("John Doe");
        when(request.getParameter(USERNAME)).thenReturn("johndoe");
        when(request.getParameter(EMAIL)).thenReturn("dimaa@gmail.ru");
        when(request.getParameter(PASSWORD)).thenReturn("123321");
        when(request.getParameter(AVATAR)).thenReturn("avatar");
        when(userService.findByUserName(anyString())).thenReturn(Optional.empty());
        registrationServlet.doPost(request,response);
        verify(userService).createAccount(any(User.class));
        verify(response).sendRedirect(AUTH_PATH);
    }

    @Test
    public void testDoPostWithInvalidInput() throws ServletException, IOException {
        String fullName = null;
        String username = null;
        String email = "not an email";
        String password = "short";
        String avatar = "invalid-avatar.exe";
        when(request.getParameter(FULL_NAME)).thenReturn(fullName);
        when(request.getParameter(USERNAME)).thenReturn(username);
        when(request.getParameter(EMAIL)).thenReturn(email);
        when(request.getParameter(PASSWORD)).thenReturn(password);
        when(request.getParameter(AVATAR)).thenReturn(avatar);
        registrationServlet.doPost(request, response);
        verify(userService, never()).createAccount(any());
        verify(request).setAttribute(MESSAGE,REGISTRATION_FAILED);
        verify(requestDispatcher).forward(request, response);
    }
}