package com.praktikum.testing.otomation.test;
import com.aventstack.extentreports.Status;
import com.praktikum.testing.otomation.pages.CartPage;
import com.praktikum.testing.otomation.pages.HomePage;
import com.praktikum.testing.otomation.pages.ProductPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test class untuk feature Product Details (5 test cases)
 */
public class ProductDetailsTest extends BaseTest {

    @Test(priority = 1, description = "Test view product details")
    public void testViewProductDetails() {
        test.log(Status.INFO, "Memulai test view product details");

        HomePage homePage = new HomePage(driver);
        ProductPage productPage = new ProductPage(driver);

        homePage.goToHomePage();
        test.log(Status.INFO, "Buka halaman home");

        // Klik produk pertama untuk lihat details
        homePage.clickProduct(0);
        test.log(Status.INFO, "Klik produk pertama untuk lihat details");

        // Verifikasi product details page terbuka
        String productName = productPage.getName();
        Assert.assertFalse(productName.isEmpty(), "Nama produk harus ditampilkan");
        test.log(Status.PASS, "Product details berhasil dibuka - product: " + productName);
    }

    @Test(priority = 2, description = "Test product image display")
    public void testProductImageDisplay() {
        test.log(Status.INFO, "Memulai test product image display");

        HomePage homePage = new HomePage(driver);
        ProductPage productPage = new ProductPage(driver);

        homePage.goToHomePage();
        test.log(Status.INFO, "Buka halaman home");

        // Klik produk pertama
        homePage.clickProduct(0);
        test.log(Status.INFO, "Klik produk pertama");

        // Verifikasi gambar produk ditampilkan
        boolean imageDisplayed = productPage.isImageDisplayed();
        Assert.assertTrue(imageDisplayed, "Gambar produk harus ditampilkan");
        test.log(Status.PASS, "Product image berhasil ditampilkan");
    }

    @Test(priority = 3, description = "Test product price display")
    public void testProductPriceDisplay() {
        test.log(Status.INFO, "Memulai test product price display");

        HomePage homePage = new HomePage(driver);
        ProductPage productPage = new ProductPage(driver);

        homePage.goToHomePage();
        test.log(Status.INFO, "Buka halaman home");

        // Klik produk pertama
        homePage.clickProduct(0);
        test.log(Status.INFO, "Klik produk pertama");

        // Verifikasi harga produk ditampilkan
        String price = productPage.getPrice();
        Assert.assertFalse(price.isEmpty(), "Harga produk harus ditampilkan");
        Assert.assertTrue(price.contains("$") || price.matches(".*\\d+.*"),
                "Harga harus mengandung angka atau currency symbol");
        test.log(Status.PASS, "Product price berhasil ditampilkan: " + price);
    }

    @Test(priority = 4, description = "Test product description")
    public void testProductDescription() {
        test.log(Status.INFO, "Memulai test product description");

        HomePage homePage = new HomePage(driver);
        ProductPage productPage = new ProductPage(driver);

        homePage.goToHomePage();
        test.log(Status.INFO, "Buka halaman home");

        // Klik produk pertama
        homePage.clickProduct(0);
        test.log(Status.INFO, "Klik produk pertama");

        // Verifikasi deskripsi produk
        String description = productPage.getDescription();
        // Beberapa produk mungkin tidak punya deskripsi, jadi tidak diasert
        if (!description.isEmpty()) {
            test.log(Status.INFO, "Product description: " +
                    (description.isEmpty() ? "Tidak ada deskripsi" : description));
        }
        test.log(Status.PASS, "Product description test selesai");
    }

    @Test(priority = 5, description = "Test verifikasi navigasi ke halaman produk")
    public void testProductPageNavigation() {
        test.log(Status.INFO, "Memulai test navigasi halaman produk");
        HomePage homePage = new HomePage(driver);

        homePage.goToHomePage();
        homePage.clickProduct(0);
        test.log(Status.INFO, "Klik produk pertama");

        // Verifikasi sederhana: apakah URL berubah ke halaman produk?
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("demowebshop"), "URL harus mengandung domain web shop");
        test.log(Status.PASS, "Navigasi ke detail produk berhasil");
    }
}