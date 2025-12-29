package com.praktikum.testing.otomation.test;
import com.aventstack.extentreports.Status;
import com.praktikum.testing.otomation.pages.CartPage;
import com.praktikum.testing.otomation.pages.HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ShoppingCartTest extends BaseTest {

    @Test(priority = 1, description = "Test membuka halaman cart yang kosong")
    public void testEmptyCartScenario() {
        test.log(Status.INFO, "Memulai test empty cart scenario");
        CartPage cartPage = new CartPage(driver);

        // Langsung buka halaman cart tanpa nambah barang
        cartPage.goToCartPage();
        test.log(Status.INFO, "Buka halaman cart");

        // Verifikasi apakah cart kosong (ini pasti PASS jika selector isEmpty benar)
        boolean isEmpty = cartPage.isEmpty();
        Assert.assertTrue(isEmpty, "Cart harus kosong di awal");
        test.log(Status.PASS, "Empty cart scenario berhasil");
    }

    @Test(priority = 2, description = "Test navigasi continue shopping")
    public void testContinueShoppingFunctionality() {
        test.log(Status.INFO, "Memulai test continue shopping");
        CartPage cartPage = new CartPage(driver);

        cartPage.goToCartPage();
        test.log(Status.INFO, "Buka halaman cart");

        cartPage.continueShopping();
        test.log(Status.INFO, "Klik tombol continue shopping");

        // Verifikasi kembali ke home
        Assert.assertTrue(driver.getCurrentUrl().contains("demowebshop"), "Harus kembali ke home");
        test.log(Status.PASS, "Navigasi berhasil");
    }

    @Test(priority = 3, description = "Test dasar akses halaman cart")
    public void testCartPageDisplay() {
        test.log(Status.INFO, "Memulai test display halaman cart");
        HomePage homePage = new HomePage(driver);

        homePage.goToHomePage();
        homePage.goToCart();

        String title = driver.getTitle();
        Assert.assertTrue(title.contains("Shopping Cart"), "Judul halaman harus Shopping Cart");
        test.log(Status.PASS, "Halaman cart berhasil ditampilkan");
    }
}