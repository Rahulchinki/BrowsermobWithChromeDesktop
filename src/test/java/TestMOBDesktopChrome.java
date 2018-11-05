import java.io.File;
import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.proxy.CaptureType;

public class TestMOBDesktopChrome {

	public String ChromeDriverPath=System.getProperty("user.dir")+"\\Jars\\";
	
	public WebDriver driver ;
	
	public String sFileName =System.getProperty("user.dir")+"\\harfile.har";

	
	public BrowserMobProxyServer proxy;
	
	@BeforeTest
	public void setUp(){
		
		proxy = new BrowserMobProxyServer();
		proxy.start(0);
		
		Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
		
		DesiredCapabilities capabilities = new DesiredCapabilities();
	    capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);
		
	    //set chromedriver system property
		System.setProperty("webdriver.chrome.driver", ChromeDriverPath+"chromedriver.exe");
		driver = new ChromeDriver(capabilities);
		
	    // enable more detailed HAR capture, if desired (see CaptureType for the complete list)
	    proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);

	    
	    proxy.newHar("Westpac.com");

	    // open seleniumeasy.com
	    driver.get("https://www.westpac.com.au/");
        
		
	}
	
	@Test
	public void TestCaseOne() {
		try {
		Thread.sleep(4000);
	 	driver.findElement(By.xpath("//button[@id='search']")).click();
	System.out.println("edecdecd");
	//eSearch.click();
	
	
	driver.findElement(By.id("headersearch")).sendKeys("Credit Cards");
		
	
	//driver.findElement(By.xpath("//a/span[./text()='credit cards']")).click();
	
//	Thread.sleep(8000);

	}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@AfterTest
	public void TearDown() {
		
		Har har = proxy.getHar();

		
		List<HarEntry> entries = har.getLog().getEntries();
		
		
		for (HarEntry entry : entries) {
		System.out.println(entry.getRequest().getBodySize());
		
		
		// Write HAR Data in a File
		File harFile = new File(sFileName);
		try {
			har.writeTo(harFile);
		} catch (IOException ex) {
			 System.out.println (ex.toString());
		     System.out.println("Could not find file " + sFileName);
		}
		
		if (driver != null) {
			//proxy.stop();
			driver.quit();
		}
	}
}
}

