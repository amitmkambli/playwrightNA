package factory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Properties;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class PlaywrightFactory {
	
	Playwright playwrght;
	Browser browser;
	BrowserContext browserContext;
	Page page;
	
	private static ThreadLocal<Browser> tlBrowser = new ThreadLocal<Browser>();
	private static ThreadLocal<BrowserContext> tlBrowserContext = new ThreadLocal<BrowserContext>();
	private static ThreadLocal<Page> tlPage = new ThreadLocal<Page>();
	private static ThreadLocal<Playwright> tlPlaywrght = new ThreadLocal<Playwright>();
	
	Properties prop;
	
	public static Browser getBrowser() {
		return tlBrowser.get();
	}
	public static BrowserContext getBrowserContext() {
		return tlBrowserContext.get();
	}
	public static Page getPage() {
		return tlPage.get();
	}
	public static Playwright getPlaywright() {
		return tlPlaywrght.get();
	}
	
	public Page initBrowser(Properties prop) {
		String browserName = prop.getProperty("browser").trim();
		System.out.println("browser name is " + browserName);
		//playwrght = Playwright.create();
		tlPlaywrght.set(Playwright.create());
		switch(browserName.toLowerCase()) {
		case "chromium":
			//browser = playwrght.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
			tlBrowser.set(getPlaywright().chromium().launch(new BrowserType.LaunchOptions().setHeadless(false)));
			break;
		case "firefox":
			//browser = playwrght.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
			tlBrowser.set(getPlaywright().firefox().launch(new BrowserType.LaunchOptions().setHeadless(false)));
			break;
		case "safari":
			//browser = playwrght.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false));
			tlBrowser.set(getPlaywright().webkit().launch(new BrowserType.LaunchOptions().setHeadless(false)));
			break;
		case "chrome":
			//browser = playwrght.chromium().launch(new LaunchOptions().setChannel("chrome").setHeadless(false));
			//BrowserType.LaunchOptions options = new BrowserType.LaunchOptions().setHeadless(false);
			//tlBrowser.set(getPlaywright().chromium().connect("http://localhost:4444"));
			tlBrowser.set(getPlaywright().chromium().launch(new LaunchOptions().setChannel("chrome").setHeadless(false)));
			
			break;
		case "edge":
			//browser = playwrght.chromium().launch(new LaunchOptions().setChannel("msedge").setHeadless(false));
			tlBrowser.set(getPlaywright().chromium().launch(new LaunchOptions().setChannel("msedge").setHeadless(false)));
			break;
		default:
			System.out.println("please enter correct browser name");
			break;
		}
		
		//browserContext = browser.newContext();
		//page = browserContext.newPage();
		//page.navigate("https://naveenautomationlabs.com/opencart/");
		//page.navigate(prop.getProperty("url").trim());
		//return page;
		
		tlBrowserContext.set(getBrowser().newContext());
		tlPage.set(getBrowserContext().newPage());
		getPage().navigate(prop.getProperty("url").trim());
		return getPage();
	}
	
	public Properties init_prop() {
		try {
			FileInputStream fis = new FileInputStream("./src/test/resources/config/config.properties");
			prop = new Properties();
			prop.load(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}
	

	public static String takeScreenshot() {
		String path = System.getProperty("user.dir") + "/screenshot/" + System.currentTimeMillis() + ".png";
		//getPage().screenshot(new Page.ScreenshotOptions().setPath(Paths.get(path)).setFullPage(true));
		
		byte[] buffer = getPage().screenshot(new Page.ScreenshotOptions().setPath(Paths.get(path)).setFullPage(true));
		String base64Path = Base64.getEncoder().encodeToString(buffer);
		
		return base64Path;
	}
}
