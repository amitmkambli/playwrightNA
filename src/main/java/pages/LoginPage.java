package pages;

import com.microsoft.playwright.Page;

public class LoginPage {
	
	Page page;
	private String emailId = "input[name='email']";
	private String password = "input[name='password']";
	private String loginBtn = "input[value='Login']";
	private String forgotPwdLink = "div.form-group a";
	private String logoutLink = "//div/child::a[text()='Logout']";
	
	public LoginPage(Page page) {
		this.page = page;
	}
	
	public String getLoginPageTitle() {
		return page.title();
	}
	
	public boolean isForgotPwdLinkExist() {
		return page.isVisible(forgotPwdLink);
	}

	public boolean doLogin(String appUserName, String appPassword) {
		System.out.println("App creds: " + appUserName + ":" + appPassword);
		page.fill(emailId, appUserName);
		page.fill(password, appPassword);
		page.click(loginBtn);
		page.locator(logoutLink).waitFor();
		if(page.locator(logoutLink).isVisible()) {
			System.out.println("user is logged in successfully....");
			return true;
		}else {
			System.out.println("user is not logged in successfully....");
			return false;
		}
	}	
}
