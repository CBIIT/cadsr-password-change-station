package gov.nih.nci.cadsr.cadsrpasswordchange.core;

import gov.nih.nci.cadsr.cadsrpasswordchange.core.UserBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

//import com.test.DAO;	//just for test

public class MainServlet extends HttpServlet {

    private Logger logger = Logger.getLogger(MainServlet.class.getName());
	private static final int TOTAL_QUESTIONS = 3;
    private static final String ERROR_MESSAGE_SESSION_ATTRIBUTE = "ErrorMessage"; 
    private static final String USER_MESSAGE_SESSION_ATTRIBUTE = "UserMessage";
//	private static final String question1 = "What is the name of your pet?";
//	private static final String question2 = "What is the year of your car?";
//	private static final String question3 = "What is the city that you were born?";
	private Map userStoredQna;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		logger.info("doPost");
				
		try {
			String servletPath = req.getServletPath();			
			logger.debug("getServletPath  |" + servletPath +"|");
			if (servletPath.equals(Constants.SERVLET_URI + "/login")) {
				doLogin(req, resp);
			} else if (servletPath.equals(Constants.SERVLET_URI + "/changePassword")) {
				doChangePassword(req, resp);
			} else if (servletPath.equals(Constants.SERVLET_URI + "/promptUserQuestions")) {
				doRequestUserQuestions(req, resp);
			} else if (servletPath.equals(Constants.SERVLET_URI + "/resetPassword")) {
				doChangePassword2(req, resp);
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
			logger.error(CommonUtil.toString(theException));
		}
	}

	private void saveUserStoredQna(String username, Map userQna) {
		//to save into database
		userStoredQna = userQna;
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

			String errorMessage = "";

			// Limit input to legal characters before attempting any processing
			if(Messages.getString("PasswordChangeHelper.1").equals(PasswordChangeHelper.validateLogin(username, password))) {
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.1"));
				resp.sendRedirect("./jsp/login.jsp");				
				return;
			}

			if(Messages.getString("PasswordChangeHelper.2").equals(PasswordChangeHelper.validateLogin(username, password))) {
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.2"));
				resp.sendRedirect("./jsp/login.jsp");				
				return;
			}

			
			System.out.println("1");
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
System.out.println("2");
			} else {
				String errorMessage1 = userBean.getResult().getMessage();
				logger.debug ("errorMessage " + errorMessage1);
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, errorMessage1);
				resp.sendRedirect("./jsp/login.jsp");				
System.out.println("3");
			}
System.out.println("4");
		}
		catch (Throwable e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	
	}

	protected void doRequestUserQuestions(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {

		logger.info("doRequestUserQuestions");
		
		try {
			HttpSession session = req.getSession(false);

			String username = req.getParameter("userid");
			
			logger.debug("username " + username);
			DAO userDAO = new DAO();
			if(!userDAO.checkValidUser(username)) {
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.101"));
				resp.sendRedirect(Constants.ASK_USERID_URL);
				return;
			}
			
			//TBD - retrieve all questions related to the users from dao and set them into sessions
			session.setAttribute(Constants.USERNAME, username);
			session.setAttribute(Constants.Q1, "q1");
			session.setAttribute(Constants.Q2, "q2");
			session.setAttribute(Constants.Q3, "q3");			
			
			resp.sendRedirect(Constants.RESET_URL);
		}
		catch (Throwable theException) {
			logger.error(theException);
		}		
	}

	protected void doChangePassword2(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {

		logger.info("doChangePassword");
		
		try {
			HttpSession session = req.getSession(false);

			String username = req.getParameter("userid");
			String newPassword = req.getParameter("newpswd1");
			String newPassword2 = req.getParameter("newpswd2");
			
			if(Messages.getString("PasswordChangeHelper.3").equals(PasswordChangeHelper.validateChangePassword2(username, newPassword, newPassword2))) {
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.3"));
				resp.sendRedirect("./jsp/resetPassword.jsp");
				return;
			}

			if(Messages.getString("PasswordChangeHelper.5").equals(PasswordChangeHelper.validateChangePassword2(username, newPassword, newPassword2))) {
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.5"));
				resp.sendRedirect("./jsp/resetPassword.jsp");
				return;
			}

			if(Messages.getString("PasswordChangeHelper.6").equals(PasswordChangeHelper.validateChangePassword2(username, newPassword, newPassword2))) {
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.6"));
				resp.sendRedirect("./jsp/resetPassword.jsp");
				return;
			}

			if(Messages.getString("PasswordChangeHelper.8").equals(PasswordChangeHelper.validateChangePassword2(username, newPassword, newPassword2))) {
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.8"));
				resp.sendRedirect("./jsp/resetPassword.jsp");
				return;
			}

			// Security enhancement
			String question1 = (String)req.getParameter("question1");
			String answer1 = (String)req.getParameter("answer1");
			String question2 = (String)req.getParameter("question2");
			String answer2 = (String)req.getParameter("answer2");
			String question3 = (String)req.getParameter("question3");
			String answer3 = (String)req.getParameter("answer3");
			logger.debug("changing request: " + question1 + "=" + answer1 + " " +question2 + "=" + answer2 + " " +question3 + "=" + answer3);
		    Map userQna = new HashMap();
			userQna.put(question1, answer1);
			userQna.put(question2, answer2);
			userQna.put(question3, answer3);
		    userStoredQna = loadUserStoredQna(username);
			logger.info("changing request: comparing user provided " +  userQna + " against user stored " + userStoredQna);
			if(Messages.getString("PasswordChangeHelper.130").equals(PasswordChangeHelper.validateResetPassword(userQna, userStoredQna))) {
				logger.debug("security q and a setup");
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.130"));
				resp.sendRedirect("./jsp/resetPassword.jsp");
				return;
			}			
		
			logger.debug("username " + username);
			DAO changeDAO = new DAO();
			Result passwordChangeResult = changeDAO.changePassword2(username, newPassword);

			if (passwordChangeResult.getResultCode() == ResultCode.PASSWORD_CHANGED) {
				logger.info("password changed");
				session.invalidate();  // they are done, log them out
				resp.sendRedirect("./jsp/passwordChanged.jsp");				
			} else {
				logger.info("password change failed");
				String errorMessage = passwordChangeResult.getMessage();
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, errorMessage);
				resp.sendRedirect("./jsp/resetPassword.jsp");		
			}
		}
		catch (Throwable theException) {
			logger.error(CommonUtil.toString(theException));
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
			
			if(Messages.getString("PasswordChangeHelper.3").equals(PasswordChangeHelper.validateChangePassword(username, oldPassword, newPassword, newPassword2, userBean.getUsername(), req.getParameter("newpswd2")))) {
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.3"));
				resp.sendRedirect("./jsp/changePassword.jsp");
				return;
			}

			if(Messages.getString("PasswordChangeHelper.4").equals(PasswordChangeHelper.validateChangePassword(username, oldPassword, newPassword, newPassword2, userBean.getUsername(), req.getParameter("newpswd2")))) {
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.4"));
				resp.sendRedirect("./jsp/changePassword.jsp");
				return;
			}
			
			if(Messages.getString("PasswordChangeHelper.5").equals(PasswordChangeHelper.validateChangePassword(username, oldPassword, newPassword, newPassword2, userBean.getUsername(), req.getParameter("newpswd2")))) {
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.5"));
				resp.sendRedirect("./jsp/changePassword.jsp");
				return;
			}

			if(Messages.getString("PasswordChangeHelper.6").equals(PasswordChangeHelper.validateChangePassword(username, oldPassword, newPassword, newPassword2, userBean.getUsername(), req.getParameter("newpswd2")))) {
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.6"));
				resp.sendRedirect("./jsp/changePassword.jsp");
				return;
			}					

			if(Messages.getString("PasswordChangeHelper.7").equals(PasswordChangeHelper.validateChangePassword(username, oldPassword, newPassword, newPassword2, userBean.getUsername(), req.getParameter("newpswd2")))) {
				logger.debug("entered username doesn't match session " + username + " " + req.getParameter("userid"));
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.7"));
				resp.sendRedirect("./jsp/changePassword.jsp");				
				return;
			}

			if(Messages.getString("PasswordChangeHelper.8").equals(PasswordChangeHelper.validateChangePassword(username, oldPassword, newPassword, newPassword2, userBean.getUsername(), req.getParameter("newpswd2")))) {
				logger.debug("new password mis-typed");
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.8"));
				resp.sendRedirect("./jsp/changePassword.jsp");
				return;
			}
						
			// Security enhancement
			String question1 = req.getParameter("question1");
			String answer1 = req.getParameter("answer1");
			String question2 = req.getParameter("question2");
			String answer2 = req.getParameter("answer2");
			String question3 = req.getParameter("question3");
			String answer3 = req.getParameter("answer3");
		    Map userQna = new HashMap();
		    if(question1 != null && !question1.equals("") && answer1 != null && !answer1.equals("")) userQna.put(question1, answer1);
		    if(question2 != null && !question2.equals("") && answer2 != null && !answer2.equals("")) userQna.put(question2, answer2);
		    if(question3 != null && !question3.equals("") && answer3 != null && !answer3.equals("")) userQna.put(question3, answer3);
			logger.debug("saving request: " + question1 + "=" + answer1 + " " +question2 + "=" + answer2 + " " +question3 + "=" + answer3);
			if(Messages.getString("PasswordChangeHelper.125").equals(PasswordChangeHelper.validateSecurityQandA(TOTAL_QUESTIONS, username, newPassword, newPassword2, userQna))) {
				logger.debug("security Q&A validation failed");
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.125"));
				resp.sendRedirect("./jsp/changePassword.jsp");
				return;
			}
			logger.info("saving request: user provided " +  userQna);
		    saveUserStoredQna(userBean.getUsername(), userQna);

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
			logger.error(theException);
		}		
	}

	private Map loadUserStoredQna(String username) {
		DAO qnaDAO = new DAO();
		
		return userStoredQna;
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
