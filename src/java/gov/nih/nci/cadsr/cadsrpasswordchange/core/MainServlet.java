package gov.nih.nci.cadsr.cadsrpasswordchange.core;

import gov.nih.nci.cadsr.cadsrpasswordchange.core.ConnectionUtil;
import gov.nih.nci.cadsr.cadsrpasswordchange.core.Constants;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.Context;

import javax.naming.Reference;
import javax.naming.StringRefAddr;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.sun.org.apache.xerces.internal.impl.dv.ValidatedInfo;

import gov.nih.nci.cadsr.cadsrpasswordchange.core.UserSecurityQuestion;
//=== dev only
//import com.test.DAO;
//import com.test.oracle.dao.oracle.UserSecurityQuestionDaoImpl;
//import com.test.oracle.dto.UserSecurityQuestion;
//import com.test.mysql.dao.mysql.UserSecurityQuestionDaoImpl;
//import com.test.mysql.dto.UserSecurityQuestion;

public class MainServlet extends HttpServlet {

    private Logger logger = Logger.getLogger(MainServlet.class.getName());
    private static String _jndiUser = "java:/jdbc/caDSR";
    private static String _jndiSystem = "java:/jdbc/caDSRPasswordChange";
    
	private static Connection connection = null;
	private static DataSource datasource = null;
	private static AbstractDao dao;
//	private static DAO dao;

    private void connectDB() {
		boolean isConnectionException = true;  // use to modify returned messages when exceptions are system issues instead of password change issues  
    	
		Result result = new Result(ResultCode.UNKNOWN_ERROR);  // (should get replaced)
        try {
    		if(connection == null) {
        	datasource = ConnectionUtil.getDS(_jndiUser); connection = datasource.getConnection("@systemAccountName@", "@systemAccountPassword@");

//        	datasource = ConnectionUtil.getTestDS("root", "root"); connection = datasource.getConnection();
    		dao = new DAO(connection);
        	
    	    System.out.println("Connected to database [" + _jndiUser + " " + _jndiSystem + "]");
        	
    		}
        	isConnectionException = false;
		} catch (Exception e) {
			e.printStackTrace();
			if (isConnectionException)
				result = new Result(ResultCode.UNKNOWN_ERROR);  // error not related to user, provide a generic error 
			else
				result = ConnectionUtil.decode(e);			
		}
	}

    private void connect() {
    	connectDB();
    }
    
    private void disconnect() {
//    	datasource = null;
//		try {
//			connection.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
    }
    
    private static final int TOTAL_QUESTIONS = 3;
    private static final String ERROR_MESSAGE_SESSION_ATTRIBUTE = "ErrorMessage"; 
    private static final String USER_MESSAGE_SESSION_ATTRIBUTE = "UserMessage";
	private Map userStoredQna;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		logger.debug("doGet");
		doPost(req, resp);
	}
	
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
				if(req.getParameter("cancel") != null) {
					resp.sendRedirect(Constants.LANDING_URL);
				} else {
					doChangePassword(req, resp);
				}
			} else if (servletPath.equals(Constants.SERVLET_URI + "/saveQuestions")) {
				if(req.getParameter("cancel") != null) {
					resp.sendRedirect(Constants.CHANGE_PASSWORD_URL);
				} else {
					doSaveQuestions(req, resp);
				}
			} else if (servletPath.equals(Constants.SERVLET_URI + "/setupPassword")) {
				doSetupPassword(req, resp);
			} else if (servletPath.equals(Constants.SERVLET_URI + "/promptUserQuestions")) {
				if(req.getParameter("cancel") != null) {
					resp.sendRedirect(Constants.LANDING_URL);
				} else {				
				doRequestUserQuestions(req, resp);
				}
			} else if (servletPath.equals(Constants.SERVLET_URI + "/promptQuestion1")) {
				doQuestion1(req, resp);
			} else if (servletPath.equals(Constants.SERVLET_URI + "/promptQuestion2")) {
				doQuestion2(req, resp);
			} else if (servletPath.equals(Constants.SERVLET_URI + "/promptQuestion3")) {
				doQuestion3(req, resp);
			} else if (servletPath.equals(Constants.SERVLET_URI + "/validateQuestion1")) {
				if(req.getParameter("cancel") != null) {
					resp.sendRedirect(Constants.LANDING_URL);
				} else {
				doValidateQuestion1(req, resp);
				}
			} else if (servletPath.equals(Constants.SERVLET_URI + "/validateQuestion2")) {
				if(req.getParameter("cancel") != null) {
					resp.sendRedirect(Constants.LANDING_URL);
				} else {
				doValidateQuestion2(req, resp);
				}
			} else if (servletPath.equals(Constants.SERVLET_URI + "/validateQuestion3")) {
				if(req.getParameter("cancel") != null) {
					resp.sendRedirect(Constants.LANDING_URL);
				} else {
				doValidateQuestion3(req, resp);
				}
			} else if (servletPath.equals(Constants.SERVLET_URI + "/resetPassword")) {
				if(req.getParameter("cancel") != null) {
					resp.sendRedirect(Constants.LANDING_URL);
				} else {
					doChangePassword2(req, resp);
				}
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

	private void doQuestion3(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.sendRedirect(Constants.RESET_URL);
	}

	private void doQuestion2(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		doValidateQuestion2(req, resp);
	}

	private void doQuestion1(HttpServletRequest req, HttpServletResponse resp) {

		logger.info("doQuestion1");
		
		try {
			HttpSession session = req.getSession(false);
			if (session == null) {
				logger.debug("null session");
				// this shouldn't happen, make the user start over
				resp.sendRedirect("./jsp/loggedOut.jsp");
				return;
			}

			String username = req.getParameter("userid");
			
			logger.debug("username " + username);			
			
			// Security enhancement
			Map userQuestions = new HashMap();
			Map userAnswers =  new HashMap();
			
			//pull all questions related to this user
			loadUserStoredQna(username, userQuestions, userAnswers);
			
			//TBD - retrieve all questions related to the users from dao and set them into sessions
			session.setAttribute(Constants.USERNAME, username);
			session.setAttribute(Constants.Q1, userQuestions.get(Constants.Q1));
//			session.setAttribute(Constants.Q2, userQuestions.get(Constants.Q2));
//			session.setAttribute(Constants.Q3, userQuestions.get(Constants.Q3));			

			session.setAttribute(Constants.ALL_QUESTIONS, userQuestions);
			session.setAttribute(Constants.ALL_ANSWERS, userAnswers);
			
			if(userQuestions.size() == 0) {
				logger.info("no security question found");
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.140"));
				resp.sendRedirect(Constants.ASK_USERID_URL);
			} else {
				//resp.sendRedirect(Constants.Q1_URL);
				req.getRequestDispatcher("./jsp/askQuestion1.jsp").forward(req, resp);
			}
		}
		catch (Throwable theException) {
			logger.error(theException);
		}		
			
			
			
	}

	private void doSetupPassword(HttpServletRequest req,
			HttpServletResponse resp) {
		//TBD - populate users questions/answers
		
		
		try {
			resp.sendRedirect("./jsp/setupPassword.jsp");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	private void saveUserStoredQna(String username, Map userQuestions, Map userAnswers) {
		UserSecurityQuestion qna = new UserSecurityQuestion();
		qna.setUaName(username);
		qna.setQuestion1((String)userQuestions.get(Constants.Q1));
		qna.setAnswer1((String)userAnswers.get(Constants.A1));
		qna.setQuestion2((String)userQuestions.get(Constants.Q2));
		qna.setAnswer2((String)userAnswers.get(Constants.A2));
		qna.setQuestion3((String)userQuestions.get(Constants.Q3));
		qna.setAnswer3((String)userAnswers.get(Constants.A3));

		try {
			connect();
			UserSecurityQuestion oldQna = dao.findByUaName(username);
			if(oldQna == null) {
				dao.insert(qna);
			} else {
				dao.update(username, qna);
			}
//			showUserSecurityQuestionList();	//just for debug
			disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showUserSecurityQuestionList() {
		UserSecurityQuestion[] results;
		try {
			connect();			
			results = dao.findAll();
			if (results.length > 0) {
				for (UserSecurityQuestion e : results) {
					System.out.println("User [" + e.getUaName() + "] updated ["
							+ new Date() + "] question [" + e.getQuestion1()
							+ "] answer [" + e.getAnswer1() + "]");
				}
			} else {
				System.out.println("no question");
			}
			disconnect();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	protected void doLogin(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		init();
		
		logger.info("doLogin");

		UserBean userBean = null;
		
		try {	
			HttpSession session = req.getSession(false);
			if (session == null) {
				logger.debug("null session");
				// this shouldn't happen, make the user start over
				resp.sendRedirect("./jsp/loggedOut.jsp");
				return;
			}
			
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

			connect();
			DAO loginDAO = new DAO(datasource);
			userBean = loginDAO.checkValidUser(username, password);
			disconnect();
			session.setAttribute(UserBean.USERBEAN_SESSION_ATTRIBUTE, userBean);		
			logger.debug ("validUser " + userBean.isLoggedIn());
			logger.debug ("resultCode " + userBean.getResult().getResultCode().toString());
			if (userBean.isLoggedIn()) {
				// Provide a user message that notes the "expired" status
				String userMessage = userBean.getResult().getMessage();
				logger.debug ("userMessage " + userMessage);
				session.setAttribute(USER_MESSAGE_SESSION_ATTRIBUTE, userMessage);
				session.setAttribute("username", username);
				resp.sendRedirect("./jsp/changePassword.jsp"); //logged-in page
			} else {
				String errorMessage1 = userBean.getResult().getMessage();
				logger.debug ("errorMessage " + errorMessage1);
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, errorMessage1);
				resp.sendRedirect("./jsp/login.jsp");				
			}
		}
		catch (Throwable e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	
	}

	protected void doSaveQuestions(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {

		logger.info("doSaveQuestions");
		
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
			
			String username = req.getParameter("userid");
			String password = req.getParameter("password");
			
			logger.debug("username " + username);
			
			connect();
			DAO loginDAO = new DAO(datasource);
			userBean = loginDAO.checkValidUser(username, password);
			disconnect();
			session.setAttribute(UserBean.USERBEAN_SESSION_ATTRIBUTE, userBean);		
			logger.debug ("validUser" + userBean.isLoggedIn());
			logger.debug ("resultCode " + userBean.getResult().getResultCode().toString());
			if (!userBean.isLoggedIn()) {
				logger.debug("auth failed during questions/answers save");
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.102"));
//				req.getRequestDispatcher(Constants.SETUP_QUESTIONS_URL).forward(req, resp);		//didn't work for jboss 4.0.5
				req.getRequestDispatcher("./jsp/setupPassword.jsp").forward(req, resp);
				return;
			}
			
			// Security enhancement
			int paramCount = 0;
			String question1 = req.getParameter("question1");
			String answer1 = req.getParameter("answer1");
			String question2 = req.getParameter("question2");
			String answer2 = req.getParameter("answer2");
			String question3 = req.getParameter("question3");
			String answer3 = req.getParameter("answer3");
		    Map userQuestions = new HashMap();	
		    userQuestions.put(question1,"");
		    userQuestions.put(question2,"");
		    userQuestions.put(question3,"");
		    if(question1 != null && !question1.equals("")) paramCount++;
		    if(question2 != null && !question2.equals("")) paramCount++;
		    if(question3 != null && !question3.equals("")) paramCount++;
		    if(userQuestions.size() < TOTAL_QUESTIONS && paramCount == TOTAL_QUESTIONS) {
				logger.debug("security Q&A validation failed");
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.135"));
//				req.getRequestDispatcher(Constants.SETUP_QUESTIONS_URL).forward(req, resp);		//didn't work for jboss 4.0.5
				req.getRequestDispatcher("./jsp/setupPassword.jsp").forward(req, resp);
				return;
			}
		    userQuestions = new HashMap();
		    Map userAnswers = new HashMap();	
		    if(question1 != null && !question1.equals("") && answer1 != null && !answer1.equals("")) userQuestions.put(Constants.Q1, question1); userAnswers.put(Constants.A1, answer1);
		    if(question2 != null && !question2.equals("") && answer2 != null && !answer2.equals("")) userQuestions.put(Constants.Q2, question2); userAnswers.put(Constants.A2, answer2);
		    if(question3 != null && !question3.equals("") && answer3 != null && !answer3.equals("")) userQuestions.put(Constants.Q3, question3); userAnswers.put(Constants.A3, answer3);
			logger.debug("saving request: " + question1 + "=" + answer1 + " " +question2 + "=" + answer2 + " " +question3 + "=" + answer3);
			if(Messages.getString("PasswordChangeHelper.125").equals(PasswordChangeHelper.validateSecurityQandA(TOTAL_QUESTIONS, username, userQuestions, userAnswers))) {
				logger.debug("security Q&A validation failed");
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.125"));
//				req.getRequestDispatcher(Constants.SETUP_QUESTIONS_URL).forward(req, resp);		//didn't work for jboss 4.0.5
				req.getRequestDispatcher("./jsp/setupPassword.jsp").forward(req, resp);
				return;
			}
			if(!PasswordChangeHelper.validateQuestionsLength(TOTAL_QUESTIONS, userQuestions, userAnswers)) {
				logger.debug("security Q&A validation failed");
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.150"));
//				req.getRequestDispatcher(Constants.SETUP_QUESTIONS_URL).forward(req, resp);		//didn't work for jboss 4.0.5
				req.getRequestDispatcher("./jsp/setupPassword.jsp").forward(req, resp);
				return;
			}
			
			logger.info("saving request: user provided " +  userQuestions + " " + userAnswers);
		    saveUserStoredQna(username, userQuestions, userAnswers);
			
			connect();			
			DAO userDAO = new DAO(connection);
			try {
				if(!userDAO.checkValidUser(username)) {
					session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.101"));
					resp.sendRedirect(Constants.ASK_USERID_URL);
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				disconnect();
			}
			
			//TBD - retrieve all questions related to the users from dao and set them into sessions
			session.setAttribute(Constants.USERNAME, username);
			
			session.invalidate();
			resp.sendRedirect(Constants.SETUP_SAVED_URL);
		}
		catch (Throwable theException) {
			logger.error(theException);
		}		
	}

	protected void doRequestUserQuestions(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {

		logger.info("doRequestUserQuestions");
		
		try {
			HttpSession session = req.getSession(false);
			if (session == null) {
				logger.debug("null session");
				// this shouldn't happen, make the user start over
				resp.sendRedirect("./jsp/loggedOut.jsp");
				return;
			}

			String username = req.getParameter("userid");
			
			logger.debug("username " + username);
			
			connect();		
			DAO userDAO = new DAO(connection);
			try {
				if(!userDAO.checkValidUser(username)) {
					session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.101"));
					resp.sendRedirect(Constants.ASK_USERID_URL);
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				disconnect();
			}
			
			// Security enhancement
			Map userQuestions = new HashMap();
			Map userAnswers =  new HashMap();
			
			//pull all questions related to this user
			loadUserStoredQna(username, userQuestions, userAnswers);
			
			//TBD - retrieve all questions related to the users from dao and set them into sessions
			session.setAttribute(Constants.USERNAME, username);
			session.setAttribute(Constants.Q1, userQuestions.get(Constants.Q1));
			session.setAttribute(Constants.Q2, userQuestions.get(Constants.Q2));
			session.setAttribute(Constants.Q3, userQuestions.get(Constants.Q3));			

			session.setAttribute(Constants.ALL_QUESTIONS, userQuestions);
			session.setAttribute(Constants.ALL_ANSWERS, userAnswers);
			
			if(userQuestions == null || userQuestions.size() == 0) {
				logger.info("no security question found");
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.140"));
				resp.sendRedirect(Constants.ASK_USERID_URL);
			} else {
				//resp.sendRedirect(Constants.Q1_URL);
				req.getRequestDispatcher("./jsp/askQuestion1.jsp").forward(req, resp);
			}
		}
		catch (Throwable theException) {
			logger.error(theException);
		}		
	}

	protected void doValidateQuestion1(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		logger.info("doValidateQuestion 1");
		
		HttpSession session = req.getSession(false);
		if (session == null) {
			logger.debug("null session");
			// this shouldn't happen, make the user start over
			resp.sendRedirect("./jsp/loggedOut.jsp");
			return;
		}		

		try {
			if (validateQuestions(req, resp)) {
				logger.info("answer is correct");
				resp.sendRedirect("./jsp/askQuestion2.jsp");				
			} else {
				logger.info("security question answered wrongly");
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.130"));
				resp.sendRedirect("./jsp/askQuestion1.jsp");		
			}
		}
		catch (Throwable theException) {
			logger.error(CommonUtil.toString(theException));
		}		
	}
	
	protected void doValidateQuestion2(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		logger.info("doValidateQuestion 2");
		
		HttpSession session = req.getSession(false);
		if (session == null) {
			logger.debug("null session");
			// this shouldn't happen, make the user start over
			resp.sendRedirect("./jsp/loggedOut.jsp");
			return;
		}		

		try {
			if (validateQuestions(req, resp)) {
				logger.info("answer is correct");
				resp.sendRedirect("./jsp/askQuestion3.jsp");				
			} else {
				logger.info("security question answered wrongly");
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.130"));
				resp.sendRedirect("./jsp/askQuestion2.jsp");		
			}
		}
		catch (Throwable theException) {
			logger.error(CommonUtil.toString(theException));
		}		
	}

	protected void doValidateQuestion3(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		logger.info("doValidateQuestion 3");
		
		HttpSession session = req.getSession(false);
		if (session == null) {
			logger.debug("null session");
			// this shouldn't happen, make the user start over
			resp.sendRedirect("./jsp/loggedOut.jsp");
			return;
		}		

		try {
			if (validateQuestions(req, resp)) {
				logger.info("answer is correct");
				resp.sendRedirect("./jsp/resetPassword.jsp");				
			} else {
				logger.info("security question answered wrongly");
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.130"));
				resp.sendRedirect("./jsp/askQuestion3.jsp");		
			}
		}
		catch (Throwable theException) {
			logger.error(CommonUtil.toString(theException));
		}		
	}

	protected boolean validateQuestions(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {

		HttpSession session = req.getSession(false);
		Map userQuestions = (HashMap) session.getAttribute(Constants.ALL_QUESTIONS);
		Map userAnswers = (HashMap) session.getAttribute(Constants.ALL_ANSWERS);
		logger.info("questions " + userQuestions.size() + " answers " + userAnswers.size());

		String question1 = req.getParameter("question");
		String answer1 = req.getParameter("answer");
		String answerIndex =  req.getParameter("answerIndex");
		logger.debug("doValidateQuestions: (" + question1 + ")=" + answer1);
		
		boolean validated = false;
		//get user's stored answer related to the question selected
		String correctAnswer = (String)userAnswers.get(answerIndex);
		if(correctAnswer != null && correctAnswer.equals(answer1)) {
			validated = true;
		}
		
		return validated;
	}

	protected void doChangePassword2(HttpServletRequest req, HttpServletResponse resp)
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

			String username = req.getParameter("userid");
			String newPassword = req.getParameter("newpswd1");

			// Security enhancement
			String question1 = (String)req.getParameter("question1");
			String answer1 = (String)req.getParameter("answer1");
			String question2 = (String)req.getParameter("question2");
			String answer2 = (String)req.getParameter("answer2");
			String question3 = (String)req.getParameter("question3");
			String answer3 = (String)req.getParameter("answer3");
			logger.debug("changing request: " + question1 + "=" + answer1 + " " +question2 + "=" + answer2 + " " +question3 + "=" + answer3);
		
			logger.debug("username " + username);
			connect();
			DAO changeDAO = new DAO(connection);
			Result passwordChangeResult = changeDAO.resetPassword(username, newPassword);
			disconnect();

//			if (passwordChangeResult.getResultCode() == ResultCode.PASSWORD_CHANGED) {
				logger.info("password changed");
				session.invalidate();  // they are done, log them out
				resp.sendRedirect("./jsp/passwordChanged.jsp");				
//			} else {
//				logger.info("password change failed");
//				String errorMessage = passwordChangeResult.getMessage();
//				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, errorMessage);
//				resp.sendRedirect("./jsp/resetPassword.jsp");		
//			}
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

			logger.debug("username " + username);
			connect();
			DAO changeDAO = new DAO(connection);
			Result passwordChangeResult = changeDAO.changePassword(username, oldPassword, newPassword);
			disconnect();

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

	private boolean loadUserStoredQna(String username, Map userQuestions, Map userAnswers) {
		UserSecurityQuestion qna = new UserSecurityQuestion();
		boolean retVal = false;
		try {
			connect();
			qna = dao.findByUaName(username);
			if(qna != null) {
				userQuestions.put(Constants.Q1, qna.getQuestion1());
				userQuestions.put(Constants.Q2, qna.getQuestion2());
				userQuestions.put(Constants.Q3, qna.getQuestion3());
				userAnswers.put(Constants.A1, qna.getAnswer1());
				userAnswers.put(Constants.A2, qna.getAnswer2());
				userAnswers.put(Constants.A3, qna.getAnswer3());
				retVal = true;
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return retVal;
		
	}

	@Override
	public void init() throws ServletException {
		super.init();
		logger.debug("init");
		connect();
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		logger.debug("init(ServletConfig config)");
		connect();
	}
}