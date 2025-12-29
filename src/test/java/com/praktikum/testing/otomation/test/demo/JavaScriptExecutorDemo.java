package com.praktikum.testing.otomation.test.demo;

import com.praktikum.testing.otomation.utils.ScreenshotUtil;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class JavaScriptExecutorDemo {
    private WebDriver driver;
    private JavascriptExecutor js;
    private WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        js = (JavascriptExecutor) driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void demonstrateJavaScriptExecution() {
        System.out.println("\n=== JAVASCRIPT EXECUTOR DEMO ===");
        driver.get("https://the-internet.herokuapp.com/login");

        // 1. Execute simple JavaScript
        System.out.println("\nn1. Getting page title via JavaScript:");
        String title = (String) js.executeScript("return document.title;");
        System.out.println(" Title: " + title);

        // 2. Scroll to element
        System.out.println("\nn2. Scrolling to element:");
        WebElement footer = driver.findElement(By.cssSelector("#page-footer"));
        js.executeScript("arguments[0].scrollIntoView(true);", footer);
        System.out.println(" Scrolled to footer");

        try { Thread.sleep(1000); } catch (InterruptedException e) {}

        // 3. Scroll to top
        System.out.println("\nn3. Scrolling to top:");
        js.executeScript("window.scrollTo(0, 0);");
        System.out.println(" Scrolled to top");

        try { Thread.sleep(1000); } catch (InterruptedException e) {}

        // 4. Highlight element
        System.out.println("\nn4. Highlighting element:");
        WebElement usernameField = driver.findElement(By.id("username"));
        js.executeScript("arguments[0].style.border='3px solid red'", usernameField);
        System.out.println(" Username field highlighted");

        try { Thread.sleep(1000); } catch (InterruptedException e) {}

        // 5. Click element using JavaScript
        System.out.println("\nn5. Clicking via JavaScript:");
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));

        // CAPTURE INNERHTML SEBELUM KLIK
        String innerHTMLBeforeClick = (String) js.executeScript("return arguments[0].innerHTML;", loginButton);
        System.out.println(" Button innerHTML before click: " + innerHTMLBeforeClick);

        js.executeScript("arguments[0].click();", loginButton);
        System.out.println(" Login button clicked");

        // 6. Enter text using JavaScript - SETELAH KLIK, PAGE BERUBAH
        System.out.println("\nn6. Entering text via JavaScript:");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("flash")));

        js.executeScript("document.getElementById('username').value='testuser';");
        System.out.println(" Username entered via JS");

        // 7. Get element properties
        System.out.println("\nn7. Getting element properties:");
        WebElement newLoginButton = driver.findElement(By.cssSelector("button[type='submit']"));
        String innerHTMLAfter = (String) js.executeScript("return arguments[0].innerHTML;", newLoginButton);
        System.out.println(" Button innerHTML after click: " + innerHTMLAfter);

        // 8. Additional JavaScript examples
        System.out.println("\nn8. Additional JavaScript Operations:");
        String currentUrl = (String) js.executeScript("return window.location.href;");
        System.out.println(" Current URL: " + currentUrl);

        long viewportWidth = (Long) js.executeScript("return window.innerWidth;");
        long viewportHeight = (Long) js.executeScript("return window.innerHeight;");
        System.out.println(" Viewport size: " + viewportWidth + "x" + viewportHeight);

        js.executeScript("document.title = 'Modified by Selenium';");
        String modifiedTitle = (String) js.executeScript("return document.title;");
        System.out.println(" Modified title: " + modifiedTitle);
    }

    @Test
    public void demonstrateAdvancedJavaScriptOperations() {
        System.out.println("\n=== ADVANCED JAVASCRIPT OPERATIONS ===");
        driver.get("https://the-internet.herokuapp.com/login");

        // 1. Create and add new element via JavaScript
        System.out.println("\nn1. Creating new element via JavaScript:");
        js.executeScript(
                "var newDiv = document.createElement('div');" +
                        "newDiv.innerHTML = '<h3>Added by Selenium JavaScript</h3>';" +
                        "newDiv.style.backgroundColor = 'yellow';" +
                        "newDiv.style.padding = '10px';" +
                        "newDiv.style.margin = '10px';" +
                        "document.body.prepend(newDiv);"
        );
        System.out.println(" Added new div element via JS");
        try { Thread.sleep(1000); } catch (InterruptedException e) {}

        // 2. Remove element via JavaScript
        System.out.println("\nn2. Removing element via JavaScript:");
        js.executeScript(
                "var element = document.querySelector('h2');" +
                        "if(element) { element.remove(); }"
        );
        System.out.println(" Removed h2 element via JS");
        try { Thread.sleep(1000); } catch (InterruptedException e) {}

        // 3. Modify element styles
        System.out.println("\nn3. Modifying styles via JavaScript:");
        js.executeScript(
                "document.querySelector('.example').style.backgroundColor = 'lightblue';" +
                        "document.querySelector('.example').style.padding = '20px';"
        );
        System.out.println(" Modified styles via JS");

        // 4. Execute async JavaScript
        System.out.println("\nn4. Executing async JavaScript:");
        js.executeAsyncScript(
                "var callback = arguments[arguments.length - 1];" +
                        "setTimeout(function() {" +
                        " document.title = 'Async Title Change';" +
                        " callback('Async operation completed');" +
                        "}, 2000);"
        );
        System.out.println(" Async JavaScript executed");
        System.out.println("\n Advanced JavaScript operations test PASSED\n");
    }

    @Test
    public void demonstrateScreenshotCapture() {
        System.out.println("\n=== SCREENSHOT CAPTURE DEMO ===");
        driver.get("https://the-internet.herokuapp.com/login");
        System.out.println(" Navigated to login page");

        // Capture screenshot 1 - Initial page
        ScreenshotUtil.captureScreenshot(driver, "login_page_initial");

        // Perform login with proper credentials
        driver.findElement(By.id("username")).sendKeys("tomsmith");
        driver.findElement(By.id("password")).sendKeys("SuperSecretPassword!");

        // Capture screenshot 2 - Form filled
        ScreenshotUtil.captureScreenshot(driver, "login_page_filled");

        // Click login
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Wait for successful login
        wait.until(ExpectedConditions.urlContains("/secure"));

        // Capture screenshot 3 - After login
        ScreenshotUtil.captureScreenshot(driver, "secure_page_success");

        // Additional: Capture page with JavaScript modifications
        js.executeScript("document.body.style.backgroundColor = 'lightgreen'");
        ScreenshotUtil.captureScreenshot(driver, "page_with_js_modifications");

        System.out.println("\n Screenshot capture test PASSED\n");
        System.out.println("Check folder: src/test/resources/screenshots/\n");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            driver.quit();
        }
    }
}