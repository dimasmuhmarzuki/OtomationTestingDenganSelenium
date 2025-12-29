package com.praktikum.testing.otomation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class HomePage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    @FindBy(id = "small-searchterms")
    private WebElement searchBox;

    @FindBy(css = "input[type='submit'][value='Search']")
    private WebElement searchButton;

    // TAMBAHAN: Elemen untuk menampung pesan hasil pencarian (Lulus/Gagal)
    @FindBy(css = ".search-results .result, .no-result, .result")
    private WebElement searchResultMessage;

    @FindBy(css = ".product-item")
    private List<WebElement> productItems;

    @FindBy(css = ".product-title")
    private List<WebElement> productTitles;

    @FindBy(css = "a[href='/cart']")
    private WebElement cartLink;

    @FindBy(css = ".cart-qty")
    private WebElement cartQuantity;

    @FindBy(css = ".product-box-add-to-cart-button")
    private List<WebElement> addToCartButtons;

    @FindBy(css = ".header-links a[href='/login']")
    private WebElement loginLink;

    @FindBy(linkText = "Log out")
    private WebElement logoutLink;

    @FindBy(css = ".account")
    private WebElement accountLink;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    // TAMBAHAN: Method untuk mengambil pesan pencarian agar ProductSearchTest PASSED
    public String getSearchMessage() {
        try {
            // Tunggu sebentar sampai pesan muncul di UI
            wait.until(ExpectedConditions.visibilityOf(searchResultMessage));
            return searchResultMessage.getText();
        } catch (Exception e) {
            // Jika tidak ditemukan lewat @FindBy, coba cari langsung via driver
            try {
                return driver.findElement(By.cssSelector(".result")).getText();
            } catch (Exception e2) {
                return "No message found";
            }
        }
    }

    public void goToHomePage() {
        driver.get("https://demowebshop.tricentis.com/");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".header")));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void search(String keyword) {
        wait.until(ExpectedConditions.visibilityOf(searchBox));
        searchBox.clear();
        searchBox.sendKeys(keyword);
        searchButton.click();
    }

    public int getSearchResultCount() {
        try {
            // Beri waktu sinkronisasi agar list terupdate
            Thread.sleep(1000);
            return productItems.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public String getProductTitle(int index) {
        try {
            if (index < productTitles.size()) {
                return productTitles.get(index).getText();
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    public void clickProduct(int index) {
        try {
            if (index < productItems.size()) {
                productItems.get(index).findElement(By.cssSelector(".product-title a")).click();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addToCart(int index) {
        try {
            System.out.println("Attempting to add product " + index + " to cart...");
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".product-box-add-to-cart-button")));

            if (addToCartButtons.size() > index) {
                WebElement button = addToCartButtons.get(index);
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
                Thread.sleep(500);
                wait.until(ExpectedConditions.elementToBeClickable(button));
                button.click();
                Thread.sleep(2000);
            }
        } catch (Exception e) {
            System.out.println("Error adding to cart: " + e.getMessage());
        }
    }

    public void goToCart() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(cartLink));
            cartLink.click();
            wait.until(ExpectedConditions.urlContains("/cart"));
        } catch (Exception e) {
            driver.get("https://demowebshop.tricentis.com/cart");
        }
    }

    public String getCartItemCount() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".cart-qty")));
            String text = cartQuantity.getText().trim();
            return text.replace("(", "").replace(")", "");
        } catch (Exception e) {
            return "0";
        }
    }

    public boolean isUserLoggedIn() {
        try {
            return logoutLink.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isLoginSuccess() {
        try {
            return accountLink.isDisplayed() || logoutLink.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void goToLoginPage() {
        try {
            loginLink.click();
        } catch (Exception e) {
            driver.get("https://demowebshop.tricentis.com/login");
        }
    }
}