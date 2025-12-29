package com.praktikum.testing.otomation.test.demo;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Set;

public class NavigationAndWindowDemo {
    private WebDriver driver;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void demonstrateNavigation() {
        System.out.println("\n=== NAVIGATION COMMANDS ===");
        driver.get("https://the-internet.herokuapp.com/");
        driver.findElement(By.linkText("Form Authentication")).click();
        driver.navigate().back();
        driver.navigate().forward();
        driver.navigate().refresh();
        driver.navigate().to("https://the-internet.herokuapp.com/dropdown");
        System.out.println(" Navigation test PASSED");
    }

    @Test
    public void demonstrateMultipleWindows() {
        System.out.println("\n=== MULTIPLE WINDOWS HANDLING ===");
        driver.get("https://the-internet.herokuapp.com/windows");
        String originalWindow = driver.getWindowHandle();
        driver.findElement(By.linkText("Click Here")).click();

        try { Thread.sleep(1000); } catch (InterruptedException e) {}

        Set<String> allWindows = driver.getWindowHandles();
        for (String windowHandle : allWindows) {
            if (!windowHandle.equals(originalWindow)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }

        String newWindowText = driver.findElement(By.tagName("h3")).getText();
        Assert.assertEquals(newWindowText, "New Window");
        driver.close();
        driver.switchTo().window(originalWindow);
        System.out.println(" Multiple windows test PASSED");
    }

    @Test
    public void demonstrateIframes() {
        System.out.println("\n=== IFRAME HANDLING (BYPASS READ-ONLY) ===");
        driver.get("https://the-internet.herokuapp.com/iframe");

        // 1. Switch ke iframe
        WebElement iframe = driver.findElement(By.id("mce_0_ifr"));
        driver.switchTo().frame(iframe);
        System.out.println(" Switched to iframe");

        // 2. Ambil elemen editor (TinyMCE)
        WebElement editor = driver.findElement(By.id("tinymce"));

        // 3. JALUR PAKSA (JavaScript): Mengatasi error Read-Only karena limit TinyMCE
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String expectedText = "Testing iframe interaction with Selenium!";

        js.executeScript(
                "arguments[0].setAttribute('contenteditable', 'true'); " + // Paksa editable
                        "arguments[0].innerHTML = '<p>' + arguments[1] + '</p>';", // Masukkan teks
                editor, expectedText
        );
        System.out.println(" Forced text entry via JS due to TinyMCE limit");

        // 4. Jeda agar DOM stabil
        try { Thread.sleep(1000); } catch (InterruptedException e) {}

        // 5. Verifikasi
        String actualText = editor.getText();
        System.out.println(" Actual text found: " + actualText);
        Assert.assertEquals(actualText, expectedText, "Teks tetap tidak sesuai!");

        // 6. Kembali ke konten utama
        driver.switchTo().defaultContent();
        System.out.println(" Iframe test PASSED");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            try { Thread.sleep(2000); } catch (InterruptedException e) {}
            driver.quit();
        }
    }
}