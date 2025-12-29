package com.praktikum.testing.otomation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {

    @FindBy(id = "Email")
    private WebElement emailInput;

    @FindBy(id = "Password")
    private WebElement passwordInput;

    @FindBy(css = "input[value='Log in']")
    private WebElement loginButton;

    @FindBy(id = "RememberMe")
    private WebElement rememberMeCheckbox;

    @FindBy(className = "validation-summary-errors")
    private WebElement loginError;

    @FindBy(css = ".field-validation-error")
    private WebElement emailError;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void goToLoginPage() {
        driver.get("http://demowebshop.tricentis.com/login");
        wait.until(ExpectedConditions.visibilityOf(emailInput));
    }

    public void login(String email, String password) {
        waitForVisible(emailInput);
        emailInput.clear();
        emailInput.sendKeys(email);
        passwordInput.clear();
        passwordInput.sendKeys(password);

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", loginButton);

        // Jeda 3 detik agar session benar-benar tertanam
        try { Thread.sleep(3000); } catch (InterruptedException e) {}
    }

    public void loginWithRememberMe(String email, String password) {
        waitForVisible(emailInput);
        emailInput.clear();
        emailInput.sendKeys(email);
        passwordInput.clear();
        passwordInput.sendKeys(password);
        if (!rememberMeCheckbox.isSelected()) {
            rememberMeCheckbox.click();
        }
        loginButton.click();
        try { Thread.sleep(3000); } catch (InterruptedException e) {}
    }

    public boolean isLoginSuccess() {
        try {
            return wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText("Log out"))).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getLoginError() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(loginError)).getText();
        } catch (Exception e) {
            return "";
        }
    }

    public String getEmailError() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(emailError)).getText();
        } catch (Exception e) {
            return "";
        }
    }

    public void logout() {
        try {
            driver.get("http://demowebshop.tricentis.com/logout");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText("Log in")));
        } catch (Exception e) {}
    }

    public boolean isLogoutSuccess() {
        try {
            return wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText("Log in"))).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}