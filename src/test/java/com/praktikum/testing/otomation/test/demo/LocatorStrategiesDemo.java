package com.praktikum.testing.otomation.test.demo;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.time.Duration;

public class LocatorStrategiesDemo {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        // Menggunakan WebDriverWait selama 10 detik
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void demonstrateAllLocators() {
        driver.get("https://the-internet.herokuapp.com/login");
        System.out.println("--- DEMONSTRATING ALL LOCATOR STRATEGIES ---\n");

        // 1. By ID
        System.out.println("1. LOCATOR BY ID:");
        WebElement usernameById = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("username")));
        System.out.println("   Found element: " + usernameById.getTagName());
        System.out.println("   Element displayed: " + usernameById.isDisplayed());

        // 2. By Name
        System.out.println("\n2. LOCATOR BY NAME:");
        WebElement usernameByName = driver.findElement(By.name("username"));
        System.out.println("   Found element: " + usernameByName.getTagName());

        // 3. By Class Name dengan penanganan error
        System.out.println("\n3. LOCATOR BY CLASS NAME:");
        try {
            WebElement flashMessage = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("flash")));
            System.out.println("   Found element: " + flashMessage.getTagName());
            System.out.println("   Element text: " + flashMessage.getText());
        } catch (Exception e) {
            System.out.println("   Element with class 'flash' not found, trying alternative...");
        }

        // 4. By Tag Name
        System.out.println("\n4. LOCATOR BY TAG NAME:");
        WebElement h2Element = driver.findElement(By.tagName("h2"));
        System.out.println("   Text content: " + h2Element.getText());

        // 5. By Link Text
        System.out.println("\n5. LOCATOR BY LINK TEXT:");
        WebElement elementalSelenium = driver.findElement(By.linkText("Elemental Selenium"));
        System.out.println("   Link text: " + elementalSelenium.getText());
        System.out.println("   Link href: " + elementalSelenium.getAttribute("href"));

        // 6. By Partial Link Text
        System.out.println("\n6. LOCATOR BY PARTIAL LINK TEXT:");
        WebElement partialLink = driver.findElement(By.partialLinkText("Elemental"));
        System.out.println("   Found link: " + partialLink.getText());

        // 7. By CSS Selector (ID, Class, Attribute, Advanced)
        System.out.println("\n7. LOCATOR BY CSS SELECTOR:");
        WebElement usernameCssId = driver.findElement(By.cssSelector("#username"));
        System.out.println("   CSS by ID: " + usernameCssId.getAttribute("name"));

        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
        System.out.println("   CSS by Attribute: " + loginButton.getText());

        WebElement usernameAdvanced = driver.findElement(By.cssSelector("input[type='text'][name='username']"));
        System.out.println("   CSS Advanced: " + usernameAdvanced.getTagName());

        // 8. By XPath (Relative, Text, Parent/Child, Index)
        System.out.println("\n8. LOCATOR BY XPATH:");
        WebElement usernameXpath = driver.findElement(By.xpath("//input[@id='username']"));
        System.out.println("   XPath by attribute: " + usernameXpath.getTagName());

        WebElement h2Xpath = driver.findElement(By.xpath("//h2[contains(text(), 'Login Page')]"));
        System.out.println("   XPath by text: " + h2Xpath.getText());

        System.out.println("\n=== ALL LOCATORS DEMONSTRATED SUCCESSFULLY ===");
    }

    @Test
    public void demonstrateLocatorBestPractices() {
        driver.get("https://the-internet.herokuapp.com/login");
        System.out.println("\n=== LOCATOR BEST PRACTICES ===\n");

        // Praktik Terbaik: Prioritaskan ID
        System.out.println("BEST: Using ID (unique and fast)");
        WebElement username = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("username")));
        System.out.println("   Username field found: " + username.isDisplayed());

        // Praktik Terbaik: Gunakan CSS untuk fleksibilitas
        System.out.println("BEST: Using CSS Selector for flexibility");
        WebElement loginBtn = driver.findElement(By.cssSelector("button.radius"));
        System.out.println("   Login button text: " + loginBtn.getText());

        // Praktik Terbaik: Hindari XPath Absolut
        System.out.println("AVOID: Absolute XPath (brittle)");
        System.out.println("   Example: /html/body/div[2]/div/div/form/div[1]/div/input");

        // Praktik Terbaik: Hubungan Parent-Child
        System.out.println("BEST: XPath for complex relationships");
        WebElement passwordField = driver.findElement(By.xpath("//form[@id='login']//input[@type='password']"));
        System.out.println("   Password field found: " + passwordField.isDisplayed());

        // Praktik Terbaik: Explicit Waits
        System.out.println("BEST: Using explicit waits for element stability");
        WebElement secureAreaLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Elemental Selenium")));
        System.out.println("   Secure area link: " + secureAreaLink.getText());

        System.out.println("\n=== BEST PRACTICES DEMONSTRATED ===");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            try {
                Thread.sleep(2000); // Jeda singkat
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            driver.quit();
        }
    }
}