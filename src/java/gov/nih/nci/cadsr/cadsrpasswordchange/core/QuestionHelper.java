package gov.nih.nci.cadsr.cadsrpasswordchange.core;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class QuestionHelper {
	public static void initQuestionsOptions(HttpServletRequest req) {
		List<String> l = new ArrayList<String>();
		l.add("What is the last name of your favorite school teacher?");
		l.add("What is the name of your favorite sports team?");
		l.add("What is the name of your favorite singer or band?");
		l.add("What is the name of your favorite television series?");
		l.add("What is the last name of your favorite school teacher?");
		l.add("What is the name of your favorite restaurant?");
		l.add("What is the name of your favorite movie?");
		l.add("What is the name of your favorite song?");
		l.add("What is the furthest place to which you have traveled?");
		l.add("What is the name of your favorite actor or actress?");
		l.add("Who is your personal hero?");
		l.add("What is your favorite hobby?");
		l.add("Your mother's first name?");
		l.add("The city name or town name of your birth?");
		l.add("A four digit PIN (personal identification number)? ");
		l.add("What is your least favorite sports team?");
		l.add("What is your mother's occupation?");
		l.add("What was your SAT score?");
		l.add("What is your favorite brand of candy?");
		l.add("What is your least favorite food? ");
		l.add("What is your least favorite beverage?");
		l.add("What was your first pet's name?");
		req.getSession().setAttribute("questionsList", l);
	}
}
