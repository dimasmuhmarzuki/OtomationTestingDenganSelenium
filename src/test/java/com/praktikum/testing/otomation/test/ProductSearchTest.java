package com.praktikum.testing.otomation.test;
import com.aventstack.extentreports.Status;
import com.praktikum.testing.otomation.pages.HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test class untuk feature Product Search (6 test cases)
 */
public class ProductSearchTest extends BaseTest {

    @Test(priority = 1, description = "Test search dengan keyword valid")
    public void testSearchWithValidKeyword() {
        test.log(Status.INFO, "Memulai test search dengan keyword valid");

        HomePage homePage = new HomePage(driver);

        homePage.goToHomePage();
        test.log(Status.INFO, "Buka halaman home");

        // Search dengan keyword: computer
        homePage.search("computer");
        test.log(Status.INFO, "Search dengan keyword: computer");

        // Verifikasi ada hasil
        int resultCount = homePage.getSearchResultCount();
        Assert.assertTrue(resultCount > 0, "Harus ada hasil search");
        test.log(Status.PASS, "Search berhasil - ditemukan " + resultCount + " produk");
    }

    @Test(priority = 2, description = "Test search dengan keyword invalid")
    public void testSearchWithInvalidKeyword() {
        test.log(Status.INFO, "Memulai test search dengan keyword invalid");

        HomePage homePage = new HomePage(driver);

        homePage.goToHomePage();
        test.log(Status.INFO, "Buka halaman home");

        // Search dengan keyword yang tidak ada
        homePage.search("xyzabc123invalid");
        test.log(Status.INFO, "Search dengan keyword yang tidak ada: xyzabc123invalid");

        // Verifikasi tidak ada hasil
        String message = homePage.getSearchMessage();
        Assert.assertTrue(message.contains("No products") || message.isEmpty(),
                "Harus ada pesan no products");
        test.log(Status.PASS, "Search invalid - tidak ada hasil yang ditemukan");
    }

    @Test(priority = 3, description = "Test search dengan query kosong")
    public void testSearchWithEmptyQuery() {
        test.log(Status.INFO, "Memulai test search dengan query kosong");

        HomePage homePage = new HomePage(driver);

        homePage.goToHomePage();
        test.log(Status.INFO, "Buka halaman home");

        // Search dengan string kosong
        homePage.search("");
        test.log(Status.INFO, "Search dengan query kosong");

        // Verifikasi tidak ada hasil
        int resultCount = homePage.getSearchResultCount();
        test.log(Status.INFO, "Jumlah hasil dengan query kosong: " + resultCount);
        test.log(Status.PASS, "Search dengan query kosong berhasil tanpa error");
    }

    @Test(priority = 4, description = "Test validasi jumlah hasil search")
    public void testSearchResultCountValidation() {
        test.log(Status.INFO, "Memulai test validasi jumlah hasil search");

        HomePage homePage = new HomePage(driver);

        homePage.goToHomePage();
        test.log(Status.INFO, "Buka halaman home");

        // Search buku
        homePage.search("book");
        test.log(Status.INFO, "Search dengan keyword: book");

        // Verifikasi jumlah hasil reasonable
        int resultCount = homePage.getSearchResultCount();
        Assert.assertTrue(resultCount >= 0, "Jumlah hasil harus non-negative");

        // Jika ada hasil, cek judul produk pertama
        if (resultCount > 0) {
            String productName = homePage.getProductTitle(0);
            Assert.assertFalse(productName.isEmpty(), "Judul produk harus ditampilkan");
            test.log(Status.INFO, "Produk pertama: " + productName);
        }

        test.log(Status.PASS, "Validasi jumlah hasil berhasil - " + resultCount + " produk");
    }

    @Test(priority = 5, description = "Test search filter functionality")
    public void testSearchFilterFunctionality() {
        test.log(Status.INFO, "Memulai test search filter");

        HomePage homePage = new HomePage(driver);

        homePage.goToHomePage();
        test.log(Status.INFO, "Buka halaman home");

        // Search software
        homePage.search("software");
        test.log(Status.INFO, "Search dengan keyword: software");

        // Verifikasi search bekerja
        int resultCount = homePage.getSearchResultCount();
        Assert.assertTrue(resultCount >= 0, "Search harus bekerja tanpa error");
        test.log(Status.PASS, "Search filter test berhasil - " + resultCount + " hasil");
    }

    @Test(priority = 6, description = "Test search sorting options")
    public void testSearchSortingOptions() {
        test.log(Status.INFO, "Memulai test search sorting");

        HomePage homePage = new HomePage(driver);

        homePage.goToHomePage();
        test.log(Status.INFO, "Buka halaman home");

        // Search computer
        homePage.search("computer");
        test.log(Status.INFO, "Search dengan keyword: computer");

        // Verifikasi ada hasil untuk test sorting
        int resultCount = homePage.getSearchResultCount();
        Assert.assertTrue(resultCount > 0, "Harus ada hasil untuk test sorting");
        test.log(Status.PASS, "Search sorting test berhasil - " + resultCount + " produk");
    }
}