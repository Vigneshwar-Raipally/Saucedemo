package testcases;

// Made change

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import utils.DriverFactory;
import reporting.ExtentReportManager;
import utils.ScreenshotUtil;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

public class LoginTest extends BaseTest {

	private static ExtentReports extent = ExtentReportManager.getReporter();
	private ExtentTest test;

	// 1. Valid login
	@Test(priority = 1)
	public void validLoginTest() {
		test = extent.createTest("Valid Login Test - SauceDemo");
		try {
			LoginPage loginPage = new LoginPage(DriverFactory.getDriver());

			test.info("Navigating to login page");
			test.addScreenCaptureFromPath(ScreenshotUtil.captureScreenshot(DriverFactory.getDriver(), "loginPage"));

			loginPage.enterUsername("standard_user");
			loginPage.enterPassword("secret_sauce");
			loginPage.clickLogin();

			test.info("Entered credentials and clicked login");
			test.addScreenCaptureFromPath(ScreenshotUtil.captureScreenshot(DriverFactory.getDriver(), "afterLogin"));

			String currentUrl = DriverFactory.getDriver().getCurrentUrl();
			Assert.assertTrue(currentUrl.contains("inventory.html"), "Login failed - not navigated to Products page");

			test.pass("Login successful, navigated to Products page");
			test.addScreenCaptureFromPath(ScreenshotUtil.captureScreenshot(DriverFactory.getDriver(), "productsPage"));
		} catch (Exception e) {
			captureFailure(e, "validLogin");
		}
	}

	// 2. Cross-browser test
	@Test(priority = 5)
	public void crossBrowserLoginTest() {
		test = extent.createTest("Cross-Browser Login Test - SauceDemo");
		try {
			// Chrome
			test.info("Running login test on Chrome");
			DriverFactory.setBrowser("chrome");
			DriverFactory.getDriver().get("https://www.saucedemo.com/");
			new LoginPage(DriverFactory.getDriver()).enterUsername("standard_user");
			new LoginPage(DriverFactory.getDriver()).enterPassword("secret_sauce");
			new LoginPage(DriverFactory.getDriver()).clickLogin();
			Assert.assertTrue(DriverFactory.getDriver().getCurrentUrl().contains("inventory.html"));
			test.pass("Login successful on Chrome");
			
			// Edge
			test.info("Running login test on Edge");
			DriverFactory.setBrowser("edge");
			DriverFactory.getDriver().get("https://www.saucedemo.com/");
			new LoginPage(DriverFactory.getDriver()).enterUsername("standard_user");
			new LoginPage(DriverFactory.getDriver()).enterPassword("secret_sauce");
			new LoginPage(DriverFactory.getDriver()).clickLogin();
			Assert.assertTrue(DriverFactory.getDriver().getCurrentUrl().contains("inventory.html"));
			test.pass("Login successful on Edge");

		} catch (Exception e) {
			captureFailure(e, "crossBrowserLogin");
		}
	}

	// Helper method for failures
	private void captureFailure(Exception e, String step) {
		test.fail("Test failed: " + e.getMessage());
		try {
			test.addScreenCaptureFromPath(ScreenshotUtil.captureScreenshot(DriverFactory.getDriver(), step + "_error"));
		} catch (Exception ignored) {
		}
		Assert.fail(e.getMessage());
	}
}
