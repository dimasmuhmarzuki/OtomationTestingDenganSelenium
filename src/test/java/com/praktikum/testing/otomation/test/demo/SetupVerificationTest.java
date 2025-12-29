package com.praktikum.testing.otomation.test.demo;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SetupVerificationTest {
    private WebDriver driver;

    @BeforeMethod
    public void setUp() {
        // Setup ChromeDriver menggunakan WebDriverManager
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void verifySeleniumSetup() {
        // Navigasi ke Google
        driver.get("https://www.google.com");

        // Verifikasi judul halaman
        String title = driver.getTitle();
        System.out.println("Page Title: " + title);

        // Assert bahwa judul berisi kata "Google"
        Assert.assertTrue(title.contains("Google"),
                "Page title should contain 'Google'");

        System.out.println("Selenium setup verification successful!");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}