package com.praktikum.testing.otomation.test.demo;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.openqa.selenium.support.ui.Select;

import java.time.Duration;

public class FormInteractionDemo {
    private WebDriver driver;
    private WebDriverWait wait; // Tambahkan wait global untuk class ini

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        // Inisialisasi wait dengan durasi 10 detik
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void testSuccessfulLogin() {
        System.out.println("\n=== TEST: SUCCESSFUL LOGIN ===");
        driver.get("https://the-internet.herokuapp.com/login");

        driver.findElement(By.id("username")).sendKeys("tomsmith");
        driver.findElement(By.id("password")).sendKeys("SuperSecretPassword!");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Gunakan explicit wait untuk pesan sukses
        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("flash")));
        String messageText = successMessage.getText();

        Assert.assertTrue(messageText.contains("You logged into a secure area!"));
        System.out.println("Login test PASSED");
    }

    @Test
    public void testInvalidLogin() {
        System.out.println("\n=== TEST: INVALID LOGIN ===");
        driver.get("https://the-internet.herokuapp.com/login");

        driver.findElement(By.id("username")).sendKeys("invaliduser");
        driver.findElement(By.id("password")).sendKeys("wrongpassword");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // PERBAIKAN: Tunggu sampai elemen flash muncul
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("flash")));
        String errorText = errorMessage.getText();
        System.out.println("Error message found: " + errorText);

        Assert.assertTrue(errorText.contains("Your username is invalid!"), "Error message should be displayed");
        System.out.println("Invalid login test PASSED");
    }

    @Test
    public void testEmptyFormSubmission() {
        System.out.println("\n=== TEST: EMPTY FORM SUBMISSION ===");
        driver.get("https://the-internet.herokuapp.com/login");

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Tunggu pesan error muncul
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("flash")));
        Assert.assertTrue(errorMessage.isDisplayed());
        System.out.println("Empty form test PASSED");
    }

    @Test
    public void demonstrateFormElements() {
        System.out.println("\n=== DEMONSTRATING FORM ELEMENTS ===");
        driver.get("https://the-internet.herokuapp.com/inputs");

        WebElement inputField = driver.findElement(By.tagName("input"));
        inputField.sendKeys("67890");

        String value = inputField.getAttribute("value");
        Assert.assertEquals(value, "67890");
        System.out.println("Form elements test PASSED");
    }

    @Test
    public void demonstrateCheckboxes() {
        System.out.println("\n=== DEMONSTRATING CHECKBOXES ===");
        driver.get("https://the-internet.herokuapp.com/checkboxes");

        WebElement checkbox1 = driver.findElement(By.xpath("(//input[@type='checkbox'])[1]"));
        if (!checkbox1.isSelected()) checkbox1.click();

        Assert.assertTrue(checkbox1.isSelected());
        System.out.println("Checkbox test PASSED");
    }

    @Test
    public void demonstrateDropdown() {
        System.out.println("\n=== DEMONSTRATING DROPDOWN ===");
        driver.get("https://the-internet.herokuapp.com/dropdown");

        Select dropdown = new Select(driver.findElement(By.id("dropdown")));
        dropdown.selectByVisibleText("Option 1");

        Assert.assertEquals(dropdown.getFirstSelectedOption().getText(), "Option 1");
        System.out.println("Dropdown test PASSED");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}