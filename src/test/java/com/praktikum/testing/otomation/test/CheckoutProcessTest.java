package com.praktikum.testing.otomation.test;
import com.aventstack.extentreports.Status;
import com.praktikum.testing.otomation.pages.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class CheckoutProcessTest extends BaseTest {

    private void addProductToCartFlow() {
        test.log(Status.INFO, "Menambahkan produk ke keranjang...");

        // 1. Langsung ke halaman kategori produk
        driver.get("https://demowebshop.tricentis.com/computing-and-internet");

        // 2. Tunggu halaman selesai loading
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".product-item")));

        // 3. Klik Add to cart pertama yang ditemukan
        WebElement addToCartBtn = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("input[value='Add to cart']"))
        );
        addToCartBtn.click();

        // 4. Tunggu notifikasi sukses muncul
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".bar-notification.success")
        ));

        // 5. Tunggu sebentar agar server sempat update cart
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 6. Pindah ke halaman keranjang
        driver.get("https://demowebshop.tricentis.com/cart");

        // 7. Pastikan tabel keranjang muncul
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("cart")));

        test.log(Status.PASS, "Produk berhasil ditambahkan ke keranjang");
    }

    @Test(priority = 1, description = "Test full checkout success flow")
    public void testFullCheckoutSuccess() {
        test.log(Status.INFO, "Memulai test full checkout");

        // Tambah produk menggunakan helper method
        addProductToCartFlow();

        // Pastikan sudah di halaman cart
        wait.until(ExpectedConditions.urlContains("/cart"));

        // Centang Terms of Service
        WebElement termsCheckbox = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("termsofservice"))
        );

        // Gunakan JavaScript untuk click jika element tertutup elemen lain
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", termsCheckbox);

        test.log(Status.INFO, "Terms of service dicentang");

        // Klik tombol Checkout
        WebElement checkoutBtn = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("checkout"))
        );
        checkoutBtn.click();

        // Tunggu halaman checkout/login loading
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("/checkout"),
                ExpectedConditions.urlContains("/login")
        ));

        test.log(Status.PASS, "Berhasil masuk ke halaman checkout/login");
    }

    @Test(priority = 2, description = "Test billing address validation")
    public void testBillingAddressValidation() {
        test.log(Status.INFO, "Memulai test billing address validation");

        // Tambah produk ke cart
        addProductToCartFlow();

        // Pastikan di halaman cart
        wait.until(ExpectedConditions.urlContains("/cart"));

        // Pastikan checkbox terms ada sebelum di-click
        WebElement termsCheckbox = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.id("termsofservice"))
        );

        // Gunakan JavaScript untuk click
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", termsCheckbox);

        test.log(Status.INFO, "Terms of service dicentang");

        // Click checkout button
        WebElement checkoutBtn = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("checkout"))
        );
        js.executeScript("arguments[0].click();", checkoutBtn);

        // Tunggu sampai redirect ke login, checkout, atau onepagecheckout
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("/checkout"),
                ExpectedConditions.urlContains("/onepagecheckout"),
                ExpectedConditions.urlContains("/login")
        ));

        // Cek apakah diarahkan ke login page
        if (driver.getCurrentUrl().contains("/login")) {
            test.log(Status.INFO, "Diarahkan ke halaman login (diperlukan untuk checkout)");

            // Pilih opsi "Checkout as Guest"
            WebElement guestCheckoutBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(By.cssSelector("input[value='Checkout as Guest']"))
            );
            guestCheckoutBtn.click();

            // Tunggu sampai masuk ke halaman checkout atau onepagecheckout
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.urlContains("/checkout"),
                    ExpectedConditions.urlContains("/onepagecheckout")
            ));
        }

        test.log(Status.INFO, "Masuk ke halaman checkout");

        // Tunggu form billing address muncul
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("BillingNewAddress_FirstName")));

        // Klik continue tanpa isi form (untuk validasi)
        CheckoutPage checkoutPage = new CheckoutPage(driver);
        checkoutPage.continueBilling();

        // Cek error message
        String error = checkoutPage.getError();
        Assert.assertFalse(error.isEmpty(), "Error message tidak muncul saat form kosong!");

        test.log(Status.PASS, "Validasi billing address berhasil: " + error);
    }

    @Test(priority = 3, description = "Test terms and conditions visibility")
    public void testTermsAndConditions() {
        test.log(Status.INFO, "Memulai test terms and conditions");

        // Tambah produk ke cart
        addProductToCartFlow();

        // Pastikan di halaman cart
        wait.until(ExpectedConditions.urlContains("/cart"));

        // Verifikasi checkbox terms terlihat
        WebElement termsCheckbox = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.id("termsofservice"))
        );

        boolean isTermsVisible = termsCheckbox.isDisplayed();
        Assert.assertTrue(isTermsVisible, "Checkbox terms tidak ditemukan!");

        test.log(Status.PASS, "Terms and conditions checkbox terverifikasi");
    }
}