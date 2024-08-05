package pages;

import com.microsoft.playwright.Page;

public class HomePage {
	//if its public some one will try to access it and will get null pointer exception
	private Page page;
	
	private String search = "input[name='search']";
	private String searchIcon = "div#search button";
	private String searchPageHeader = "div#content h1";
	private String loginLink = "//a[text()='Login']";
	private String myAccountLink = "//span[text()='My Account']";
	
	public HomePage(Page page) {
		this.page = page;
	}
	
	public String getHomePageTitle() {
		String title =  page.title();
		System.out.println("page title: " + title);
		return title;
	}

	public String getHomePageURL() {
		String url =  page.url();
		System.out.println("page url : " + url);
		return url;
	}

	public String doSearch(String productName) {
		page.fill(search, productName);
		page.click(searchIcon);
		String header =  page.textContent(searchPageHeader);
		System.out.println("search header: " + header);
		return header;
	}
	
	public LoginPage navigateToLoginPage() {
		page.click(myAccountLink);
		page.click(loginLink);
		return new LoginPage(page);
		
	}
}
