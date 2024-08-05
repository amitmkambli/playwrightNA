package test.playwright;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

import org.testng.annotations.Test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.microsoft.playwright.Download;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Tracing;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.FilePayload;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

class AppTest{
	
	@Test(enabled = true)
	public void intro() throws Exception {
		try (Playwright playwright = Playwright.create()) {
			  //by default playwright will run browser in headless mode, below code is to set headless to false
		      //Browser browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
			  //will open safari browser on windows
		      Browser browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false));
		      Page page = browser.newPage();
		      page.navigate("https://playwright.dev/");
		      System.out.println(page.title());
		      page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("example.png")));
		      browser.close();
		      playwright.close();
		    }
	}

	@Test(enabled = true)
	public void usingChrome() throws Exception {
		try (Playwright playwright = Playwright.create()) {
			LaunchOptions lp = new LaunchOptions();
			//lp.setChannel("chrome");
			lp.setChannel("msedge");
			lp.setHeadless(false);
			
			Browser browser = playwright.chromium().launch(lp);
			Page page = browser.newPage();
			page.navigate("https://playwright.dev/");
			System.out.println(page.title());
			browser.close();
			playwright.close();
		}
	}
	
	@Test(enabled = true)
	public void recordDemo1() throws Exception {
		//Refer : https://playwright.dev/java/docs/codegen-intro
		try (Playwright playwright = Playwright.create()) {
		      Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
		        .setHeadless(false));
		      BrowserContext context = browser.newContext();
		      Page page = context.newPage();
		      page.navigate("https://blazedemo.com/");
		      page.locator("select[name=\"fromPort\"]").selectOption("Philadelphia");
		      page.locator("select[name=\"toPort\"]").selectOption("Dublin");
		      page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Find Flights")).click();
		      page.locator("html").click();
		      
		      //we can pause the test and debug it if required
		      //page.pause();
		      
		      page.getByRole(AriaRole.ROW, new Page.GetByRoleOptions().setName("Choose This Flight 43 Virgin")).getByRole(AriaRole.BUTTON).click();
		      assertThat(page.getByPlaceholder("First Last")).isVisible();
		      assertThat(page.getByRole(AriaRole.HEADING)).containsText("Your flight from TLV to SFO has been reserved.");
		      page.getByText("Price:").click();
		      page.getByText("Your flight from TLV to SFO has been reserved. Airline: United Flight Number:").click();
		    }
	}
	
	@Test(enabled = true)
	public void tracingDemo() throws Exception {
		//Refer : https://playwright.dev/java/docs/trace-viewer-intro
		try (Playwright playwright = Playwright.create()) {
			Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
					.setHeadless(false));
			BrowserContext context = browser.newContext();
			
			// Start tracing before creating / navigating a page.
			context.tracing().start(new Tracing.StartOptions()
			  .setScreenshots(true)
			  .setSnapshots(true)
			  .setSources(true));
			
			Page page = context.newPage();
			page.navigate("https://blazedemo.com/");
			page.locator("select[name=\"fromPort\"]").selectOption("Philadelphia");
			page.locator("select[name=\"toPort\"]").selectOption("Dublin");
			page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Find Flights")).click();
			page.locator("html").click();
			page.getByRole(AriaRole.ROW, new Page.GetByRoleOptions().setName("Choose This Flight 43 Virgin")).getByRole(AriaRole.BUTTON).click();
			
			// Stop tracing and export it into a zip archive.
			context.tracing().stop(new Tracing.StopOptions()
			  .setPath(Paths.get("trace.zip")));
		}
	}
	
	@Test(enabled = true)
	public void multiBrowserContext() throws Exception {
		//we can use the same browser to access 2 different applications , can be useful to check chats , 
		//admin setting and corresponding changes in application
		//note that browser opens in incognito mode
		try(Playwright playwright = Playwright.create()){
			Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
			
			BrowserContext context1 = browser.newContext();
			Page p1 = context1.newPage();
			p1.navigate("https://blazedemo.com/");
			System.out.println(p1.title());
			
			BrowserContext context2 = browser.newContext();
			Page p2 = context2.newPage();
			p2.navigate("https://demo.guru99.com/test/newtours/");
			System.out.println(p2.title());
			
			p1.locator("select[name=\"fromPort\"]").selectOption("Philadelphia");
			p1.locator("select[name=\"toPort\"]").selectOption("Dublin");		
		}
	}
	
	@Test(enabled = true)
	public void locatorConcept() throws Exception {
		try (Playwright playwright = Playwright.create()) {
			Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
			BrowserContext context = browser.newContext();
			Page page = context.newPage();
			page.navigate("https://academy.naveenautomationlabs.com/");
			
			Locator loginBtn = page.locator("text=Login");
			int totalLoginBtn = loginBtn.count();
			System.out.println("login btn count: " + totalLoginBtn);
			loginBtn.first().click();
		    }
	}
	
	@Test(enabled = true)
	public void locatorConcept2() throws Exception {
		try (Playwright playwright = Playwright.create()) {
			Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
			BrowserContext context = browser.newContext();
			Page page = context.newPage();
			page.navigate("https://blazedemo.com/");
			
			Locator fromCity = page.locator("select[name='fromPort']>option");
			int totalfromCity = fromCity.count();
			System.out.println("login btn count: " + totalfromCity);
			for (int i = 0; i < totalfromCity; i++) {
				System.out.println(fromCity.nth(i).innerText()+"::"+fromCity.nth(i).textContent())	;
			}
			
			List<String> fromCityOptions = fromCity.allInnerTexts();
			fromCityOptions.forEach(o -> System.out.println(o));
		}
	}
	
	@Test(enabled = true)
	public void locatorConcept3() throws Exception {
		try (Playwright playwright = Playwright.create()) {
			Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
			BrowserContext context = browser.newContext();
			Page page = context.newPage();
			page.navigate("https://www.orangehrm.com/en/book-a-free-demo/");
			
			//Locator privacyPolicy = page.locator("text=Privacy Policy");
			//since there are many links with the same text we will get error, we can use for loop
			//privacyPolicy.click();
			
			//tagname:has-text() : work for partial text also
			Locator header = page.locator("h4:has-text('We Just Need a')");
			System.out.println(header.innerText());
			
			//we can use text='abc' or "'abc'"
			System.out.println(page.locator("'Book a Free Demo'").first().innerText());
			
			//we can give reference of parent tag if there are multiple elements being returned by css
			//System.out.println(page.locator("div#navbarSupportedContent button:has-text('Book a Free Demo')").innerText());
		}
	}
	
	@Test(enabled = true)
	public void frames1() throws Exception {
		try (Playwright playwright = Playwright.create()) {
			Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
			BrowserContext context = browser.newContext();
			Page page = context.newPage();
			page.navigate("https://the-internet.herokuapp.com/nested_frames");			
			//below line is not working
			//System.out.println(page.frameLocator("frame[name='frame-middle']").locator("div#content").innerText());
			//String frameContent = page.frameLocator("//frame[@name='frame-left']").locator("div#content").textContent();
			//System.out.println(frameContent);
			//if frame name is available then we can use frame() else we need to use frameLocator() with xpath / css selector
			System.out.println(page.frame("frame-middle").locator("div#content").textContent());
		}
	}
	
	@Test(enabled = true)
	public void iframes1() throws Exception {
		try (Playwright playwright = Playwright.create()) {
			Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
			BrowserContext context = browser.newContext();
			Page page = context.newPage();
			page.navigate("https://rahulshettyacademy.com/AutomationPractice/");
			//if frame name is available then we can use frame() else we need to use frameLocator() with xpath / css selector
			System.out.println(page.frameLocator("//iframe[@name='iframe-name']").locator("//a[@href='lifetime-access']").first().textContent());
		}
	}
	
	@Test(enabled = true)
	public void shadowDom1() throws Exception {
		try (Playwright playwright = Playwright.create()) {
			Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
			BrowserContext context = browser.newContext();
			Page page = context.newPage();
			page.navigate("https://books-pwakit.appspot.com/");
			page.locator("book-app[apptitle='BOOKS'] #input").fill("Testing Books");
			String text = page.locator("book-app[apptitle='BOOKS'] .books-desc").textContent();
			System.out.println(text);
		}
	}
	
	@Test(enabled = true)
	public void shadowDom2() throws Exception {
		try (Playwright playwright = Playwright.create()) {
			Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
			BrowserContext context = browser.newContext();
			Page page = context.newPage();
			page.navigate("https://selectorshub.com/shadow-dom-in-iframe/");
			page.frameLocator("iframe#pact").locator("div#snacktime input#tea").fill("Assam Tea");
		}
	}
	
	@Test(enabled = true)
	public void onlyVisibleElements() throws Exception {
		try (Playwright playwright = Playwright.create()) {
			Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
			BrowserContext context = browser.newContext();
			Page page = context.newPage();
			page.navigate("https://omayo.blogspot.com/");
			//both work fine
			//List<Locator> buttonValues = page.locator("input[type='button']:visible").all();
			List<Locator> buttonValues = page.locator("input[type='button'] >> visible=true").all();
			buttonValues.forEach(s -> System.out.println(s.getAttribute("value")));
			
			System.out.println("all buttons->" + page.locator("input[type='button']").count());
			//System.out.println("visible buttons->" + page.locator("xpath=//input[@type='button'] >> visible=true").count());
			System.out.println("visible buttons->" + page.locator("//input[@type='button'] >> visible=true").count());
		}
	}
	
	@Test(enabled = true)
	public void findParentHavingChild() throws Exception {
		try (Playwright playwright = Playwright.create()) {
			Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
			BrowserContext context = browser.newContext();
			Page page = context.newPage();
			page.navigate("https://www.orangehrm.com/en/book-a-free-demo/");
			//here we are selecting a parent locator which has an option value 'India'
			//if we have multiple drop downs we can use this example to select the appropriate select
			//select#Form_getForm_Country -> css of parent Select
			//option[value='India'] -> css of the child element
			//has -> filter
			Locator country = page.locator("select#Form_getForm_Country:has(option[value='India'])");
			country.allInnerTexts().forEach(s -> System.out.println(s));
			
			//print all footer text of section which has 'testimonials' 
			//Locator footerSection = page.locator("div.footer-main:has(a[href='/en/why-orangehrm/our-customers/testimonials/'])");
			Locator footerSection = page.locator("div.footer-main:has(a[href*='testimonials'])");
			footerSection.allInnerTexts().forEach(s -> System.out.println(s));
		}
	}
	
	@Test(enabled = true)
	public void usingMultipleCSSXpath() throws Exception {
		try (Playwright playwright = Playwright.create()) {
			Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
			BrowserContext context = browser.newContext();
			Page page = context.newPage();
			page.navigate("https://omayo.blogspot.com/");
			//we can pass 2 css values in case the current value changes in future/ based on environments
			page.locator("input[value='blue'], input[value='Blue']").click();
			System.out.println(page.locator("input[value='blue'], input[value='Blue']").isChecked());
			
			//using multiple xpath : this is called xpath union and is xpath feature and not playwright feature
			System.out.println(page.locator("//input[@value='blue'] | //input[@value='Blue']").isChecked());
		}
	}
	
	@Test(enabled = true)
	public void relativeLocator1() throws Exception {
		try (Playwright playwright = Playwright.create()) {
			Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
			BrowserContext context = browser.newContext();
			Page page = context.newPage();
			page.navigate("https://selectorshub.com/xpath-practice-page/");
			//we are clicking the check box to the left of 'Joe.Root', we may get multiple check boxes hence .first()
			page.locator("input[type='checkbox']:left-of(:text('Joe.Root'))").first().click();
			
			String role = page.locator("td:right-of(:text('Joe.Root'))").first().textContent();
			System.out.println("role-> " + role);
			
			String aboveUser = page.locator("a:above(:text('Joe.Root'))").first().textContent();
			System.out.println("aboveUser->"+aboveUser);
			
			String belowUser = page.locator("a:above(:text('Joe.Root'))").first().textContent();
			System.out.println("belowUser->"+belowUser);
			
			Locator nearByElements = page.locator("td:near(:text('Joe.Root'),100)");
			nearByElements.all().forEach(s->System.out.println(s.textContent()));
		}
	}
		
	@Test(enabled = true)
	public void nthLocator() throws Exception {
		try (Playwright playwright = Playwright.create()) {
			Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
			BrowserContext context = browser.newContext();
			Page page = context.newPage();
			page.navigate("https://selectorshub.com/xpath-practice-page/");
			page.locator("div.userform input >> nth=2").fill("tester inc");
		}
	}
	
	@Test(enabled = true)
	public void reactLocator() throws Exception {
		//we can use react components in locators to identify elements
		//react developer tools : indicates if site has any react components, we need to install this chrome extension
		//in dev tools we will get Components, Profile. We need to click on components to view react components
		try (Playwright playwright = Playwright.create()) {
			Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
			BrowserContext context = browser.newContext();
			Page page = context.newPage();
			page.navigate("https://www.netflix.com/ae-en/");
			Locator email = page.locator("_react=p[name='email'] >> input").first();
			email.click();
			email.fill("naveen@gmail.com");

			page.locator("_react=UISelect[data-uia='language-picker']").click();

			Locator footer = page.locator("_react=UIMarkup[data-uia='data-uia-footer-label']");
			List<String> footerList = footer.allInnerTexts();

			 for(String e : footerList) {
			     System.out.println(e);
			 }
			
		}
	}
	
	@Test(enabled = true)
	public void webTable1() throws Exception {
		try (Playwright playwright = Playwright.create()) {
			Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
			BrowserContext context = browser.newContext();
			Page page = context.newPage();
			page.navigate("https://selectorshub.com/xpath-practice-page/");
			
			//Locator row = page.locator("table#resultTable tbody tr");
			Locator row = page.locator("table#resultTable tr");
			
			//"Joe.Root" : cell value in the table 
			Locator finalRow = row.locator(":scope", new Locator.LocatorOptions().setHasText("Joe.Root"));
			finalRow.locator("input").click();
			finalRow.locator("td").allTextContents().forEach(s->System.out.println(s));
			
			//will print enter text of the web table
			row.locator(":scope").allInnerTexts().forEach(s->System.out.println(s));
		}
	}
	
	@Test(enabled = true)
	public void automaticLogin1() throws Exception {
		try (Playwright playwright = Playwright.create()) {
			Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
			BrowserContext context = browser.newContext();
			Page page = context.newPage();
			page.navigate("https://www.saucedemo.com/");
			
			page.locator("input#user-name").fill("standard_user");
			page.fill("input#password","secret_sauce");
			page.click("input#login-button");
			
			context.storageState(new BrowserContext.StorageStateOptions().setPath(Paths.get("state.json")));
			//context.storageState(new BrowserContext.StorageStateOptions().setPath(Paths.get("applogin.json")));
			//System.out.println(page.url());
		}
	}
	
	@Test(enabled = true)
	public void automaticLogin2() throws Exception {
		try (Playwright playwright = Playwright.create()) {
			Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
			
			BrowserContext context = browser.newContext(
					  new Browser.NewContextOptions().setStorageStatePath(Paths.get("state.json")));
			Page page = context.newPage();
			page.navigate("https://www.saucedemo.com/");
			Thread.sleep(Duration.ofSeconds(5));
			System.out.println(page.url());
			//expected : https://www.saucedemo.com/inventory.html
			
		}
	}
	
	@Test(enabled = true)
	public void jsAlerts() throws Exception {
		try (Playwright playwright = Playwright.create()) {
			Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
			BrowserContext context = browser.newContext();
			Page page = context.newPage();
			
			//we are adding code for onDialog listner to handle pop-up
			//we need this only if we want to dismiss or enter some text, by default playwright handles alerts
			page.onDialog(dialog -> {
				String text = dialog.message();
				System.out.println(text);
				//dialog.accept();
				//use below when we need to enter text
				dialog.accept("hello world, for js prompt");
				//to dismiss alert
				//dialog.dismiss();
			});
			
			page.navigate("https://the-internet.herokuapp.com/javascript_alerts");
			//normal js alerts can be handled without any listners
			page.click("//button[text()='Click for JS Alert']");
			System.out.println(page.locator("p#result").textContent());
			
			//JS prompt
			page.click("//button[text()='Click for JS Prompt']");
			System.out.println(page.locator("p#result").textContent());
			
		}
	}
	
	@Test(enabled = true)
	public void fileUpload() throws Exception {
		try (Playwright playwright = Playwright.create()) {
			//Note : input field for file upload should have attribute type="file"
			Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
			BrowserContext context = browser.newContext();
			Page page = context.newPage();
			page.navigate("https://davidwalsh.name/demo/multiple-file-upload.php");
			
			//to upload a file
			page.setInputFiles("input#filesToUpload", Paths.get("applogin.json"));
			Thread.sleep(Duration.ofSeconds(3));
			//in case we want to undo the upload
			page.setInputFiles("input#filesToUpload", new Path[0]);
			Thread.sleep(Duration.ofSeconds(3));
			//to upload multiple files in case the application allows
			page.setInputFiles("input#filesToUpload", new Path[]{
					Paths.get("applogin.json"),
					Paths.get("example.png")					
			});
			Thread.sleep(Duration.ofSeconds(3));
			page.setInputFiles("input#filesToUpload", new Path[0]);
			Thread.sleep(Duration.ofSeconds(3));
			
			//create file at runtime and upload it
			//in the below site we get the file preview hence using it
			page.navigate("https://cgi-lib.berkeley.edu/ex/fup.html");

			page.setInputFiles("input[name='upfile']", new FilePayload(
					"uploadtest.txt", 
					"text/plain", 
					"hello world".getBytes(StandardCharsets.UTF_16))
					);
			page.click("input[value='Press']");
			Thread.sleep(Duration.ofSeconds(3));
			//page.setInputFiles("input[name='upfile']", new Path[0]);
		}
	}
	
	@Test(enabled = true)
	public void filedownload() throws Exception {
		try (Playwright playwright = Playwright.create()) {
			Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
			BrowserContext context = browser.newContext();
			Page page = context.newPage();
			page.navigate("https://the-internet.herokuapp.com/download");
			Download download = page.waitForDownload(() ->{
				page.click("//a[text()='TestFile.txt']");
			});
			//to candle download
			//download.cancel();
			System.out.println("download failure reason ->"+download.failure());
			System.out.println(download.url());
			System.out.println(download.page().title());
			//file will be downloaded in temp folder, below line will give its location
			String path = download.path().toString();
			System.out.println(path);
			
			download.saveAs(Paths.get(System.getProperty("user.dir")+"/files", download.suggestedFilename()));
		}
	}
	
	@Test(enabled = true)
	public void maximizeWindow() throws Exception {
		try (Playwright playwright = Playwright.create()) {
			
			Dimension dims = Toolkit.getDefaultToolkit().getScreenSize();
			//dims will return width, height in double but Browser.NewContextOptions().setViewportSize accepts integer
			//hence we need to type cast to int
			int width = (int)dims.getWidth();
			int height = (int)dims.getHeight();
			
			Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
			//BrowserContext context = browser.newContext(new Browser.NewContextOptions().setViewportSize(1920, 1080));
			BrowserContext context = browser.newContext(new Browser.NewContextOptions().setViewportSize(width, height));
			Page page = context.newPage();
			page.navigate("https://the-internet.herokuapp.com/download");
		}
	}
	
	@Test(enabled = true)
	public void videoRecording() throws Exception {
		//video is not getting created
		try (Playwright playwright = Playwright.create()) {
		      Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
		      BrowserContext context = browser.newContext(new Browser.NewContextOptions()
		    		  .setRecordVideoDir(Paths.get(System.getProperty("user.dir")+"/myvideos"))
		    		  .setRecordVideoSize(600, 400)); 
		      Page page = browser.newPage();
		      page.navigate("https://playwright.dev/");
		      System.out.println(page.title());
		      page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("example.png")));
		      
		      //if we do not close context video will not be recorded
		      context.close();
		      browser.close();
		      page.close();
		      playwright.close();
		    }
	}
	
	@Test(enabled = true)
	public void handlingPopUps1() throws Exception {
		//video is not getting created
		try (Playwright playwright = Playwright.create()) {
			Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
			BrowserContext context = browser.newContext(); 
			Page page = browser.newPage();
			page.navigate("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
			
			Page twitterPage = page.waitForPopup(()->{
				page.click("a[href='https://twitter.com/orangehrm?lang=en']");
			});
			System.out.println("twitterPage.url -> "+twitterPage.url());
			twitterPage.close();
			System.out.println("page.title -> "+page.title());
			
			page.close();
			context.close();
			browser.close();
			playwright.close();
		}
	}
	
	@Test(enabled = true)
	public void handlingPopUps2() throws Exception {
		//video is not getting created
		try (Playwright playwright = Playwright.create()) {
			Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
			BrowserContext context = browser.newContext(); 
			Page page = browser.newPage();
			page.navigate("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
			
			//will open a new blank tab window 
			Page newPage = page.waitForPopup(()->{
				page.click("a[target='_blank']");
			});
			newPage.waitForLoadState();
			newPage.navigate("https://playwright.dev/");
			System.out.println("newPage.title -> "+newPage.title());
			newPage.close();
			System.out.println("page.title -> "+page.title());
			
			page.close();
			context.close();
			browser.close();
			playwright.close();
		}
	}
}