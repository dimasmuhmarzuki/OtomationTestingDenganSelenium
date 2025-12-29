package com.praktikum.testing.otomation.test;
import com.praktikum.testing.otomation.test.BaseTest;
import com.praktikum.testing.otomation.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class UserLoginTest extends BaseTest {
    private LoginPage loginPage;

    @BeforeMethod
    public void setupTest() {
        loginPage = new LoginPage(driver);
        loginPage.goToLoginPage();
    }

    @Test(priority = 1)
    public void testLoginWithInvalidPassword() {
        // Menggunakan email sembarang untuk testing pesan kesalahan
        loginPage.login("existing_user@gmail.com", "WrongPassword123!");
        String errorMessage = loginPage.getLoginError();
        Assert.assertTrue(errorMessage.contains("Login was unsuccessful"),
                "Seharusnya muncul pesan error login gagal");
    }

    @Test(priority = 2)
    public void testLoginWithEmptyCredentials() {
        loginPage.login("", "");
        String errorMessage = loginPage.getLoginError();
        Assert.assertTrue(errorMessage.contains("Login was unsuccessful"),
                "Seharusnya muncul pesan error karena input kosong");
    }

    @Test(priority = 3)
    public void testLoginWithInvalidEmailFormat() {
        loginPage.login("format-email-salah", "password123");
        String emailError = loginPage.getEmailError();
        Assert.assertEquals(emailError, "Please enter a valid email address.",
                "Seharusnya muncul pesan validasi format email");
    }

    @Test(priority = 4)
    public void testLoginWithUnregisteredEmail() {
        loginPage.login("akun_tidak_terdaftar_999@gmail.com", "Sembarang123");
        String errorMessage = loginPage.getLoginError();
        Assert.assertTrue(errorMessage.contains("No customer account found"),
                "Seharusnya muncul pesan akun tidak ditemukan");
    }
}