package com.praktikum.testing.otomation.pages;

import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class CheckoutPage extends BasePage {

    // --- Form Billing ---
    @FindBy(id = "BillingNewAddress_FirstName") private WebElement firstNameInput;
    @FindBy(id = "BillingNewAddress_LastName") private WebElement lastNameInput;
    @FindBy(id = "BillingNewAddress_Email") private WebElement emailInput;
    @FindBy(id = "BillingNewAddress_CountryId") private WebElement countryDropdown;
    @FindBy(id = "BillingNewAddress_City") private WebElement cityInput;
    @FindBy(id = "BillingNewAddress_Address1") private WebElement addressInput;
    @FindBy(id = "BillingNewAddress_ZipPostalCode") private WebElement zipInput;
    @FindBy(id = "BillingNewAddress_PhoneNumber") private WebElement phoneInput;

    // --- Tombol-Tombol Continue (Accordion Steps) ---
    // Selector ini mencakup tombol untuk user baru maupun user lama (existing address)
    @FindBy(css = "#billing-buttons-container .button-1")
    private WebElement continueBillingButton;

    @FindBy(css = "#shipping-buttons-container .button-1")
    private WebElement continueShippingButton;

    @FindBy(css = "#shipping-method-buttons-container .button-1")
    private WebElement continueShippingMethodButton;

    @FindBy(css = "#payment-method-buttons-container .button-1")
    private WebElement continuePaymentMethodButton;

    @FindBy(css = "#payment-info-buttons-container .button-1")
    private WebElement continuePaymentInfoButton;

    // --- Final Step ---
    @FindBy(css = ".confirm-order-next-step-button") private WebElement confirmOrderButton;
    @FindBy(css = ".order-completed .title") private WebElement orderCompleteMessage;
    @FindBy(className = "field-validation-error") private WebElement validationError;

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public void fillBillingAddress(String firstName, String lastName, String email,
                                   String country, String city, String address,
                                   String zip, String phone) {

        // Tunggu kontainer billing muncul
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("checkout-billing-load")));

        // CEK: Apakah muncul dropdown alamat lama?
        List<WebElement> dropdown = driver.findElements(By.id("billing-address-select"));

        if (!dropdown.isEmpty() && dropdown.get(0).isDisplayed()) {
            // JIKA ADA: Pilih alamat pertama yang ada
            new Select(dropdown.get(0)).selectByIndex(0);
            System.out.println("LOG: Memilih alamat yang sudah ada.");
        } else {
            // JIKA TIDAK ADA: Baru isi manual (untuk akun baru)
            wait.until(ExpectedConditions.visibilityOf(firstNameInput)).sendKeys(firstName);
            lastNameInput.sendKeys(lastName);
            if(emailInput.getAttribute("value").isEmpty()) emailInput.sendKeys(email);
            new Select(countryDropdown).selectByVisibleText(country);
            cityInput.sendKeys(city);
            addressInput.sendKeys(address);
            zipInput.sendKeys(zip);
            phoneInput.sendKeys(phone);
        }
    }

    public void continueBilling() {
        // PERBAIKAN: Tunggu tombol benar-benar ada dan bisa diklik
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(continueBillingButton));
        clickViaJS(btn);

        // Tunggu loading overlay muncul lalu hilang
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("billing-please-wait")));
        } catch (Exception e) {
            // Jika koneksi sangat cepat, loading mungkin tidak terlihat, abaikan saja
        }
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("billing-please-wait")));
    }

    private void selectCountry(String country) {
        Select countrySelect = new Select(countryDropdown);
        countrySelect.selectByVisibleText(country);
    }

    /**
     * Method sakti untuk menyelesaikan semua step accordion yang muncul otomatis
     * (Shipping -> Shipping Method -> Payment -> Payment Info)
     */
    public void completeRemainingSteps() {
        // 1. Shipping Address
        clickViaJS(wait.until(ExpectedConditions.elementToBeClickable(continueShippingButton)));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("shipping-please-wait")));

        // 2. Shipping Method
        clickViaJS(wait.until(ExpectedConditions.elementToBeClickable(continueShippingMethodButton)));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("shipping-method-please-wait")));

        // 3. Payment Method
        clickViaJS(wait.until(ExpectedConditions.elementToBeClickable(continuePaymentMethodButton)));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("payment-method-please-wait")));

        // 4. Payment Info
        clickViaJS(wait.until(ExpectedConditions.elementToBeClickable(continuePaymentInfoButton)));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("payment-info-please-wait")));
    }

    public void confirmOrder() {
        wait.until(ExpectedConditions.elementToBeClickable(confirmOrderButton));
        clickViaJS(confirmOrderButton);
        wait.until(ExpectedConditions.visibilityOf(orderCompleteMessage));
    }

    // Helper untuk klik via JavaScript agar lebih stabil
    private void clickViaJS(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    // Ubah dari private menjadi protected supaya cocok dengan BasePage
    @Override
    protected boolean isElementDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    public String getError() {
        try {
            // Tunggu sebentar sampai error muncul (jika ada)
            wait.until(ExpectedConditions.visibilityOf(validationError));
            return getText(validationError);
        } catch (Exception e) {
            return ""; // Kembalikan string kosong jika tidak ada error yang muncul
        }
    }

    public String getOrderCompleteMessage() {
        return getText(orderCompleteMessage);
    }

    public boolean isOrderSuccess() {
        try {
            // Tunggu sebentar sampai pesannya muncul di layar
            wait.until(ExpectedConditions.visibilityOf(orderCompleteMessage));
            return getText(orderCompleteMessage).toLowerCase().contains("successfully processed");
        } catch (Exception e) {
            return false;
        }
    }
}