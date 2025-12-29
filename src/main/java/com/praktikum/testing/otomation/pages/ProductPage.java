package com.praktikum.testing.otomation.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page Object untuk halaman Detail Produk
 */
public class ProductPage extends BasePage {

    // --- Locators (Hapus semua 'static') ---
    @FindBy(className = "product-name")
    private WebElement productName;

    @FindBy(className = "product-price")
    private WebElement productPrice;

    @FindBy(className = "short-description")
    private WebElement productDescription;

    @FindBy(className = "picture")
    private WebElement productImage;

    @FindBy(css = ".add-to-cart-button")
    private WebElement addToCartButton;

    @FindBy(id = "product_enteredQuantity")
    private WebElement quantityInput;

    @FindBy(className = "content")
    private WebElement notificationMessage;

    @FindBy(linkText = "shopping cart")
    private WebElement cartLink;

    // --- Constructor ---
    public ProductPage(WebDriver driver) {
        super(driver);
    }

    // --- Page Actions ---

    public String getName() {
        return getText(productName);
    }

    public String getPrice() {
        return getText(productPrice);
    }

    public String getDescription() {
        return getText(productDescription);
    }

    public boolean isImageDisplayed() {
        return isDisplayed(productImage);
    }

    // Metode AddToCart yang sudah diperbaiki (Hanya satu versi saja)
    public void addToCart() {
        // Gunakan method click dari BasePage yang sudah include wait
        click(addToCartButton);

        // Tunggu bar notifikasi sukses muncul
        try {
            waitForVisible(notificationMessage);
        } catch (Exception e) {
            System.out.println("Notifikasi sukses tidak muncul tepat waktu.");
        }
    }

    public void setQuantity(int quantity) {
        enterText(quantityInput, String.valueOf(quantity));
    }

    public String getNotification() {
        try {
            return getText(notificationMessage);
        } catch (Exception e) {
            return "";
        }
    }

    public void goToCart() {
        try {
            // Tunggu sampai link 'shopping cart' di bar hijau bisa diklik
            click(cartLink);
        } catch (Exception e) {
            System.out.println("Gagal klik link notifikasi, mencoba navigasi manual.");
            driver.get("https://demo.nopcommerce.com/cart");
        }
    }

    public boolean isAddedToCart() {
        String message = getNotification().toLowerCase();
        return message.contains("added") && message.contains("cart");
    }
}