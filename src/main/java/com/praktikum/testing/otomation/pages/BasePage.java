package com.praktikum.testing.otomation.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/**
 * Base class untuk semua Page Objects
 */
public class BasePage {
    protected WebDriver driver;
    public static WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        // Inisialisasi wait dengan timeout 15 detik
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    // --- Reusable Wait Methods ---
    public static void waitForVisible(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }
    
    public void waitForVisibility(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    // --- Method untuk mengisi text field ---
    protected void enterText(WebElement element, String text) {
        waitForVisible(element);
        element.clear();
        element.sendKeys(text);
    }

    // --- Method untuk klik elemen (Hapus 'static' agar bisa akses 'wait') ---
    protected static void click(WebElement element) {
        waitForClickable(element);
        element.click();
    }

    private static void waitForClickable(WebElement element) {
    }

    // --- Method untuk mendapatkan text dari elemen ---
    protected String getText(WebElement element) {
        waitForVisible(element);
        return element.getText();
    }

    // --- Method untuk cek elemen ditampilkan ---
    protected boolean isDisplayed(WebElement element) {
        try {
            waitForVisible(element);
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // --- Method untuk navigasi ke URL ---
    protected void navigateTo(String url) {
        driver.get(url);
    }

    // --- Alias methods untuk konsistensi ---
    protected void clickElement(WebElement element) {
        click(element);
    }

    protected String getElementText(WebElement element) {
        return getText(element);
    }

    protected boolean isElementDisplayed(WebElement element) {
        return isDisplayed(element);
    }
}