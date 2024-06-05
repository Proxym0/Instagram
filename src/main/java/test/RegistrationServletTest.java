import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class RegistrationServletTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private UserService userService;

    private RegistrationServlet registrationServlet;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        registrationServlet = new RegistrationServlet();
        registrationServlet.userService = userService;
    }

    @Test
    public void doGet_shouldForwardToRegisterPage() throws ServletException, IOException {
        when(request.getServletContext().getRequestDispatcher(RegistrationServlet.REG_PATH)).thenReturn(requestDispatcher);

        registrationServlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void doPost_shouldRedirectToAuthPage_whenUserIsValidAndDoesNotExist() throws ServletException, IOException {
        when(request.getParameter(RegistrationServlet.FULL_NAME)).thenReturn("John Doe");
        when(request.getParameter(RegistrationServlet.USERNAME)).thenReturn("johndoe");
        when(request.getParameter(RegistrationServlet.EMAIL)).thenReturn("johndoe@example.com");
        when(request.getParameter(RegistrationServlet.PASSWORD)).thenReturn("password");
        when(request.getParameter(RegistrationServlet.AVATAR)).thenReturn("avatar.png");
        when(userService.findByUserName(anyString())).thenReturn(Optional.empty());

        registrationServlet.doPost(request, response);

        verify(userService).createAccount(any(User.class));
        verify(response).sendRedirect(RegistrationServlet.AUTH_PATH);
    }

    @Test
    public void doPost_shouldAddAttributeToRequestAndForwardToRegisterPage_whenUserDoesNotExistButValidationFails() throws ServletException, IOException {
        when(request.getParameter(RegistrationServlet.FULL_NAME)).thenReturn("John Doe");
        when(request.getParameter(RegistrationServlet.USERNAME)).thenReturn("johndoe");
        when(request.getParameter(RegistrationServlet.EMAIL)).thenReturn("johndoe");
        when(request.getParameter(RegistrationServlet.PASSWORD)).thenReturn("password");
        when(request.getParameter(RegistrationServlet.AVATAR)).thenReturn("avatar.png");
        when(userService.findByUserName(anyString())).thenReturn(Optional.empty());
        when(UserValidator.isValid(any(User.class))).thenReturn(false);

        registrationServlet.doPost(request, response);

        verify(request).setAttribute(RegistrationServlet.MESSAGE, RegistrationServlet.REGISTRATION_FAILED);
        verify(request.getServletContext().getRequestDispatcher(RegistrationServlet.REG_PATH)).forward(request, response);
    }

    @Test
    public void doPost_shouldAddAttributeToRequestAndForwardToRegisterPage_whenUserAlreadyExists() throws ServletException, IOException {
        when(request.getParameter(RegistrationServlet.FULL_NAME)).thenReturn("John Doe");
        when(request.getParameter(RegistrationServlet.USERNAME)).thenReturn("johndoe");
        when(request.getParameter(RegistrationServlet.EMAIL)).thenReturn("johndoe");
        when(request.getParameter(RegistrationServlet.PASSWORD)).thenReturn("password");
        when(request.getParameter(RegistrationServlet.AVATAR)).thenReturn("avatar.png");
        when(userService.findByUserName(anyString())).thenReturn(Optional.of(new User(1L, "johndoe", "password", "johndoe@example.com", "John Doe", LocalDateTime.now(), LocalDateTime.now(), "avatar.png")));
        registrationServlet.doPost(request, response);

        verify(request).setAttribute(RegistrationServlet.MESSAGE, RegistrationServlet.USER_ALREADY_EXISTS);
        verify(request.getServletContext().getRequestDispatcher(RegistrationServlet.REG_PATH)).forward(request, response);
    }
}