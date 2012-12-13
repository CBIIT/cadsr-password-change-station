package gov.nih.nci.cadsr.cadsrpasswordchange.test;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class PasswordChangeWebTest {
	private WebDriver driver;
	private String baseUrl;
	private StringBuffer verificationErrors = new StringBuffer();
	@Before
	public void setUp() throws Exception {
		driver = new FirefoxDriver();
		baseUrl = "localhost:8080";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void sanityTest() throws Exception {
		driver.get(baseUrl + "/cadsrpasswordchange/jsp/login.jsp");
		driver.findElement(By.linkText("Change Password")).click();
		driver.findElement(By.id("LoginID")).clear();
		driver.findElement(By.id("LoginID")).sendKeys("tanj");
		driver.findElement(By.name("changePassword")).click();
		driver.findElement(By.id("OldPassword")).clear();
		driver.findElement(By.id("OldPassword")).sendKeys("tanj1");
		driver.findElement(By.id("OldPassword")).clear();
		driver.findElement(By.id("OldPassword")).sendKeys("tanj");
		driver.findElement(By.id("NewPassword")).clear();
		driver.findElement(By.id("NewPassword")).sendKeys("tanj1");
		driver.findElement(By.id("NewPasswordRepeat")).clear();
		driver.findElement(By.id("NewPasswordRepeat")).sendKeys("tanj1");
		driver.findElement(By.name("changePassword")).click();
		driver.findElement(By.name("cancel")).click();
		driver.findElement(By.linkText("Setup Security Questions")).click();
		driver.findElement(By.id("LoginID")).clear();
		driver.findElement(By.id("LoginID")).sendKeys("tanj");
		driver.findElement(By.id("OldPassword")).clear();
		driver.findElement(By.id("OldPassword")).sendKeys("tanj");
		new Select(driver.findElement(By.id("Question1"))).selectByVisibleText("What is the name of your favorite sports team?");
		driver.findElement(By.id("Answer1")).clear();
		driver.findElement(By.id("Answer1")).sendKeys("a");
		driver.findElement(By.name("changePassword")).click();
		new Select(driver.findElement(By.id("Question2"))).selectByVisibleText("What is your favorite brand of candy?");
		driver.findElement(By.id("Answer2")).clear();
		driver.findElement(By.id("Answer2")).sendKeys("b");
		new Select(driver.findElement(By.id("Question3"))).selectByVisibleText("What is the name of your favorite singer or band?");
		new Select(driver.findElement(By.id("Question3"))).selectByVisibleText("-- Please Select a Question --");
		driver.findElement(By.id("Answer3")).clear();
		driver.findElement(By.id("Answer3")).sendKeys("c");
		driver.findElement(By.name("changePassword")).click();
		new Select(driver.findElement(By.id("Question3"))).selectByVisibleText("What is your least favorite food?");
		driver.findElement(By.name("changePassword")).click();
		driver.findElement(By.id("OldPassword")).clear();
		driver.findElement(By.id("OldPassword")).sendKeys("tanj");
		driver.findElement(By.name("changePassword")).click();
		driver.findElement(By.linkText("Back to password change logon")).click();
		driver.findElement(By.linkText("Forgot My Password")).click();
		driver.findElement(By.id("LoginID")).clear();
		driver.findElement(By.id("LoginID")).sendKeys("tanj");
		driver.findElement(By.name("changePassword")).click();
		driver.findElement(By.id("answer")).clear();
		driver.findElement(By.id("answer")).sendKeys("a");
		driver.findElement(By.name("changePassword")).click();
		driver.findElement(By.id("answer")).clear();
		driver.findElement(By.id("answer")).sendKeys("b");
		driver.findElement(By.name("changePassword")).click();
		driver.findElement(By.id("answer")).clear();
		driver.findElement(By.id("answer")).sendKeys("c");
		driver.findElement(By.name("changePassword")).click();
		driver.findElement(By.id("NewPassword")).clear();
		driver.findElement(By.id("NewPassword")).sendKeys("http://localhost:8080/cadsrpasswordchange/jsp/login.jsp");
		driver.findElement(By.id("NewPasswordRepeat")).clear();
		driver.findElement(By.id("NewPasswordRepeat")).sendKeys("http://localhost:8080/cadsrpasswordchange/jsp/login.jsp");
		driver.findElement(By.name("changePassword")).click();
		driver.findElement(By.name("cancel")).click();
		driver.findElement(By.linkText("Unlock My Account")).click();
		driver.findElement(By.id("LoginID")).clear();
		driver.findElement(By.id("LoginID")).sendKeys("tanj");
		driver.findElement(By.name("changePassword")).click();
		driver.findElement(By.name("cancel")).click();
	}
	
	@Test
	public void xssTest() throws Exception {
		driver.get(baseUrl + "/cadsrpasswordchange/jsp/login.jsp");
		driver.findElement(By.linkText("Setup Security Questions")).click();
		driver.findElement(By.id("Answer1")).clear();
		driver.findElement(By.id("Answer1")).sendKeys("answer 1 - should not be changed");
		driver.findElement(By.id("Answer2")).clear();
		driver.findElement(By.id("Answer2")).sendKeys("answer 2 - contains script, should be filtered ><script>alert('1')</script><");
		driver.findElement(By.name("changePassword")).click();
//		driver.findElement(By.linkText("Setup Security Questions")).click();
		driver.findElement(By.id("LoginID")).clear();
		driver.findElement(By.id("LoginID")).sendKeys("tanj");
		driver.findElement(By.id("OldPassword")).clear();
		driver.findElement(By.id("OldPassword")).sendKeys("tanj");
		new Select(driver.findElement(By.id("Question1"))).selectByVisibleText("What is the name of your favorite sports team?");
		driver.findElement(By.id("Answer1")).clear();
		driver.findElement(By.id("Answer1")).sendKeys("answer 1 - should stay the same, 2 should be xss and 3 blank");
		driver.findElement(By.id("Answer2")).clear();
		driver.findElement(By.id("Answer2")).sendKeys("><script>alert('1')</script><");
		new Select(driver.findElement(By.id("Question2"))).selectByVisibleText("What is your least favorite food?");
		new Select(driver.findElement(By.id("Question3"))).selectByVisibleText("What is your least favorite food?");
		driver.findElement(By.name("changePassword")).click();
		Assert.assertNotSame("Text Should Not Be Found", "<script>alert('1')</script>", driver.findElement(By.id("Answer2")).getAttribute("value"));
		
		driver.findElement(By.id("Answer2")).clear();
		driver.findElement(By.id("Answer2")).sendKeys("answer 2 - good");
		driver.findElement(By.id("Answer3")).clear();
		driver.findElement(By.id("Answer3")).sendKeys("answer 3 - good done");
		driver.findElement(By.name("changePassword")).click();
		Assert.assertEquals("Text Should Be Found", "answer 2 - good", driver.findElement(By.id("Answer2")).getAttribute("value"));
	}	
	
//TBD - the following is only cdecurate	
//	@Test
/*	
	public void testCDECurateWeb() throws Exception {
		driver.get(baseUrl + "/cdecurate/NCICurationServlet?reqType=homePage");
		driver.findElement(By.name("keyword")).click();
		driver.findElement(By.name("keyword")).clear();
		driver.findElement(By.name("keyword")).sendKeys("blood*");
		driver.findElement(By.name("keyword")).click();
		driver.findElement(By.name("keyword")).click();
		driver.findElement(By.name("keyword")).clear();
		driver.findElement(By.name("keyword")).sendKeys("blood");
		driver.findElement(By.linkText("Login")).click();
		driver.findElement(By.name("login")).click();
		driver.findElement(By.name("Username")).click();
		driver.findElement(By.name("Username")).sendKeys("tanj");
		driver.findElement(By.name("Password")).click();
		driver.findElement(By.name("Password")).sendKeys("tanj");
		driver.findElement(By.name("login")).click();
		driver.findElement(By.name("keyword")).clear();
		driver.findElement(By.name("keyword")).sendKeys("blood*\n");
		driver.findElement(By.name("listSearchIn")).click();
		driver.findElement(By.name("listSearchFor")).click();
		new Select(driver.findElement(By.name("listSearchFor"))).selectByVisibleText("Value Meaning");
		driver.findElement(By.cssSelector("option[value=\"ValueMeaning\"]")).click();
		driver.findElement(By.name("keyword")).clear();
		driver.findElement(By.name("keyword")).sendKeys("blood*");
//		driver.findElement(By.cssSelector("img.white")).click();
//		driver.findElement(By.cssSelector("td.cell > img")).click();
//		driver.findElement(By.name("btnValidate")).click();
//		driver.findElement(By.name("btnBack")).click();
//		driver.findElement(By.name("btnBack")).click();
		driver.findElement(By.linkText("Logout")).click();
	}
*/

	@After
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}
}
