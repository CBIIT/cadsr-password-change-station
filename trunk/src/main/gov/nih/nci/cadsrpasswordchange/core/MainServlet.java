package gov.nih.nci.cadsrpasswordchange.core;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class MainServlet extends HttpServlet {

    private Logger _logger = Logger.getLogger(MainServlet.class);

    private void debug(String msg) {
//    	_logger.debug(msg);
//		log(msg);  	
    }
    
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		debug("entered MainServlet.doPost");
		
		try {
			UserBean user = new UserBean();
			user.setUsername(req.getParameter("userid"));
			user.setPassword(req.getParameter("pswd"));
debug(user.getUsername());
debug(user.getPassword());
//			user = UserDAO.login(user);
boolean valid = true;
DAO loginDAO = new DAO();
//debug("before changePassword");
//loginDAO.changePassword(user.getUsername(), user.getPassword(), user.getPassword());
//debug ("after changePassword");
valid = loginDAO.checkValidUser(user.getUsername(), user.getPassword());

//			if (user.isValid()) {
if (valid) {
				HttpSession session = req.getSession(true);
				session.setAttribute(UserBean.USERBEAN_SESSION_ATTRIBUTE,user);
//				session.putValue("currentSessionUser",user);
// need to store password in session?				
				resp.sendRedirect("./jsp/validLoginPageForTest.jsp"); //logged-in page
				} else resp.sendRedirect("./jsp/invalidLogin.jsp"); //error page
			}
		catch (Throwable theException) {
			System.out.println(theException);
			debug(theException.getMessage());
			}
		}

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		debug("MainServlet.init");
		
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
		debug("MainServlet.init(ServletConfig config)");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		debug("MainServlet.doGet");
		super.doGet(req, resp);
	}

}
