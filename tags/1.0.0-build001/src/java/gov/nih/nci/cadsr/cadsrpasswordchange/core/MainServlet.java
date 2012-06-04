package gov.nih.nci.cadsr.cadsrpasswordchange.core;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class MainServlet extends HttpServlet {

    private Logger logger = Logger.getLogger(MainServlet.class);
    private static final String ERROR_MESSAGE_SESSION_ATTRIBUTE = "ErrorMessage"; 
    private static final String USER_MESSAGE_SESSION_ATTRIBUTE = "UserMessage"; 
    private static final String INPUT_FILTER = "[a-zA-Z0-9$#_]+";  // (this also prevents spaces, i.e. multiple words)
   
    
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		logger.info("doPost");
				
		try {
			String servletPath = req.getServletPath();			
			logger.debug("getServletPath  |" + servletPath +"|");
			if (servletPath.equals("/login")) {
				doLogin(req, resp);
			} else if (servletPath.equals("/changePassword")) {
				doChangePassword(req, resp);
			} else {
				// this also catches the intentional logout with path /logout 
				logger.info("logging out because of invalid servlet path");				
				HttpSession session = req.getSession(false);
				if (session != null) {
					logger.debug("non-null session");					
					session.invalidate();
				}				
				resp.sendRedirect("./jsp/loggedOut.jsp");
			}
		}
		catch (Throwable theException) {
			logger.error(theException.getMessage());
		}
	}

	
	protected void doLogin(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {

		logger.info("doLogin");

		HttpSession session = null;
		UserBean userBean = null;
		
		try {	
			session = req.getSession(false);
			session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, "");

			String username = req.getParameter("userid");
			String password = req.getParameter("pswd");
			logger.info("unvalidated username " + username);

			// Limit input to legal characters before attempting any processing

			if (!username.matches(INPUT_FILTER)) {
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, "Login ID is empty or contains illegal characters");
				resp.sendRedirect("./jsp/login.jsp");				
				return;
			}

			if (!password.matches(INPUT_FILTER)) {
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, "Password is empty or contains illegal characters");
				resp.sendRedirect("./jsp/login.jsp");				
				return;
			}

			
			DAO loginDAO = new DAO();
			userBean = loginDAO.checkValidUser(username, password);
			session.setAttribute(UserBean.USERBEAN_SESSION_ATTRIBUTE, userBean);		
			logger.debug ("validUser" + userBean.isLoggedIn());
			logger.debug ("resultCode " + userBean.getResult().getResultCode().toString());
			if (userBean.isLoggedIn()) {
				// Provide a user message that notes the "expired" status
				String userMessage = userBean.getResult().getMessage();
				logger.debug ("userMessage " + userMessage);
				session.setAttribute(USER_MESSAGE_SESSION_ATTRIBUTE, userMessage);
				resp.sendRedirect("./jsp/changePassword.jsp"); //logged-in page
			} else {
				String errorMessage = userBean.getResult().getMessage();
				logger.debug ("errorMessage " + errorMessage);
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, errorMessage);
				resp.sendRedirect("./jsp/login.jsp");				
			}
		}
		
		catch (Throwable theException) {
			logger.error(theException.getMessage());
		}
	
	}	
	

	
	protected void doChangePassword(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {

		logger.info("doChangePassword");
		
		try {	
			HttpSession session = req.getSession(false);
			if (session == null) {
				logger.debug("null session");
				// this shouldn't happen, make the user start over
				resp.sendRedirect("./jsp/loggedOut.jsp");
				return;
			}

			session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, "");
			
			UserBean userBean = (UserBean) session.getAttribute(UserBean.USERBEAN_SESSION_ATTRIBUTE);

			if (!userBean.isLoggedIn()) {
				logger.info("user not logged in " + userBean.getUsername());
				session.invalidate();
				resp.sendRedirect("./jsp/loggedOut.jsp");
				return;
			}
	
			String username = req.getParameter("userid");
			String oldPassword = req.getParameter("pswd");
			String newPassword = req.getParameter("newpswd1");
			String newPassword2 = req.getParameter("newpswd2");
			
			// Limit input to legal characters before attempting any processing
			
			if (!username.matches(INPUT_FILTER)) {
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, "User ID is empty or contains illegal characters");
				resp.sendRedirect("./jsp/changePassword.jsp");				
				return;
			}

			if (!oldPassword.matches(INPUT_FILTER)) {
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, "Password is empty or contains illegal characters");
				resp.sendRedirect("./jsp/changePassword.jsp");				
				return;
			}
			
			if (!newPassword.matches(INPUT_FILTER)) {
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, "New Password is empty or contains illegal characters");
				resp.sendRedirect("./jsp/changePassword.jsp");				
				return;
			}

			if (!newPassword2.matches(INPUT_FILTER)) {
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, "New Password (repeated) is empty or contains illegal characters");
				resp.sendRedirect("./jsp/changePassword.jsp");				
				return;
			}					

			
			// Basic validation
			
			// make sure username entered is the same as previously authenticated (even though we'll authenticate again)
			if (!username.equalsIgnoreCase(userBean.getUsername())) {  // (Oracle usernames ignore case, we should too)
				logger.debug("entered username doesn't match session " + username + " " + req.getParameter("userid"));
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, "User ID doesn't match previous login.");
				resp.sendRedirect("./jsp/changePassword.jsp");				
				return;
			}

			if (!newPassword.equals(req.getParameter("newpswd2"))) {
				logger.debug("new password mis-typed");
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, "The two entries for New Password did not match. Please try again.");
				resp.sendRedirect("./jsp/changePassword.jsp");
				return;
			}

			
		
			logger.debug("username " + username);
			DAO changeDAO = new DAO();	
			Result passwordChangeResult = changeDAO.changePassword(username, oldPassword, newPassword);

			if (passwordChangeResult.getResultCode() == ResultCode.PASSWORD_CHANGED) {
				logger.info("password changed");
				session.invalidate();  // they are done, log them out
				resp.sendRedirect("./jsp/passwordChanged.jsp");				
			} else {
				logger.info("password change failed");
				String errorMessage = passwordChangeResult.getMessage();
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, errorMessage);
				resp.sendRedirect("./jsp/changePassword.jsp");		
			}			
		}
		catch (Throwable theException) {		
			logger.error(theException.getMessage());
			}		
	}


	
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		logger.debug("init");
		
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
		logger.debug("init(ServletConfig config)");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		logger.debug("doGet");
		super.doGet(req, resp);
	}

}
