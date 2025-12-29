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

public class CartPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    @FindBy(css = ".cart-item-row")
    private List<WebElement> cartItems;

    @FindBy(css = ".remove-from-cart input[type='checkbox']")
    private List<WebElement> removeCheckboxes;

    @FindBy(name = "updatecart")
    private WebElement updateCartButton;

    @FindBy(css = ".product-price")
    private List<WebElement> prices;

    @FindBy(name = "continueshopping")
    private WebElement continueShoppingButton;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    @FindBy(css = ".qty-input")
    private List<WebElement> quantityInputs;

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    public void goToCartPage() {
        driver.get("https://demowebshop.tricentis.com/cart");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".page-title")));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getItemCount() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".page-body")));
            Thread.sleep(1000);

            if (isEmpty()) {
                System.out.println("Cart is empty");
                return 0;
            }

            List<WebElement> items = driver.findElements(By.cssSelector(".cart-item-row"));
            System.out.println("Found " + items.size() + " items in cart");
            return items.size();
        } catch (Exception e) {
            System.out.println("Error getting item count: " + e.getMessage());
            return 0;
        }
    }

    public boolean isEmpty() {
        try {
            // Check for empty cart message
            WebElement pageBody = driver.findElement(By.cssSelector(".page-body"));
            String bodyText = pageBody.getText().toLowerCase();

            if (bodyText.contains("your shopping cart is empty") ||
                    bodyText.contains("cart is empty")) {
                return true;
            }

            // Check if no cart items exist
            List<WebElement> items = driver.findElements(By.cssSelector(".cart-item-row"));
            return items.isEmpty();
        } catch (Exception e) {
            return true;
        }
    }

    public void removeItem(int index) {
        try {
            System.out.println("Attempting to remove item at index " + index);

            // Wait and find remove checkboxes
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".cart-item-row")));
            Thread.sleep(500);

            List<WebElement> checkboxes = driver.findElements(
                    By.cssSelector(".remove-from-cart input[type='checkbox']"));

            System.out.println("Found " + checkboxes.size() + " remove checkboxes");

            if (checkboxes.size() > index) {
                WebElement checkbox = checkboxes.get(index);

                // Scroll to checkbox
                ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].scrollIntoView(true);", checkbox);
                Thread.sleep(300);

                // Click checkbox
                if (!checkbox.isSelected()) {
                    checkbox.click();
                    System.out.println("Clicked remove checkbox");
                }

                // Click update cart button
                wait.until(ExpectedConditions.elementToBeClickable(updateCartButton));
                ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].scrollIntoView(true);", updateCartButton);
                Thread.sleep(300);
                updateCartButton.click();
                System.out.println("Clicked update cart button");

                // Wait for cart to update
                Thread.sleep(2000);
                System.out.println("Item removed successfully");
            } else {
                System.out.println("Index " + index + " out of bounds");
            }
        } catch (Exception e) {
            System.out.println("Error removing item: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void clearCart(int index) {
        removeItem(index);
    }

    public void clearAllItems() {
        try {
            while (!isEmpty()) {
                List<WebElement> checkboxes = driver.findElements(
                        By.cssSelector(".remove-from-cart input[type='checkbox']"));
                if (checkboxes.isEmpty()) {
                    break;
                }
                checkboxes.get(0).click();
                updateCartButton.click();
                Thread.sleep(2000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateQuantity(int index, int quantity) {
        try {
            System.out.println("Attempting to update quantity at index " + index + " to " + quantity);

            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".cart-item-row")));
            Thread.sleep(500);

            List<WebElement> qtyInputs = driver.findElements(By.cssSelector(".qty-input"));
            System.out.println("Found " + qtyInputs.size() + " quantity inputs");

            if (qtyInputs.size() > index) {
                WebElement quantityInput = qtyInputs.get(index);

                // Scroll to input
                ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].scrollIntoView(true);", quantityInput);
                Thread.sleep(300);

                // Clear and enter new quantity
                quantityInput.clear();
                quantityInput.sendKeys(String.valueOf(quantity));
                System.out.println("Set quantity to " + quantity);

                // Click update cart
                wait.until(ExpectedConditions.elementToBeClickable(updateCartButton));
                ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].scrollIntoView(true);", updateCartButton);
                Thread.sleep(300);
                updateCartButton.click();
                System.out.println("Clicked update cart");

                // Wait for cart to update
                Thread.sleep(2000);
                System.out.println("Quantity updated successfully");
            } else {
                System.out.println("Index " + index + " out of bounds");
            }
        } catch (Exception e) {
            System.out.println("Error updating quantity: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String getTotal() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".page-body")));
            Thread.sleep(1000);

            // Try multiple selectors for finding the total
            String[] selectors = {
                    ".order-total .product-price",
                    ".cart-total .product-price",
                    "td.cart-total-right .product-price",
                    ".totals .product-price"
            };

            for (String selector : selectors) {
                try {
                    WebElement totalElement = driver.findElement(By.cssSelector(selector));
                    String total = totalElement.getText().trim();
                    if (!total.isEmpty()) {
                        System.out.println("Found total using selector '" + selector + "': " + total);
                        return total;
                    }
                } catch (Exception e) {
                    // Try next selector
                }
            }

            // Last resort: find any price element in totals area
            try {
                List<WebElement> allPrices = driver.findElements(By.cssSelector(".product-price"));
                if (!allPrices.isEmpty()) {
                    String total = allPrices.get(allPrices.size() - 1).getText().trim();
                    System.out.println("Found total from last price element: " + total);
                    return total;
                }
            } catch (Exception e) {
                // Continue
            }

            System.out.println("Could not find total");
            return "";
        } catch (Exception e) {
            System.out.println("Error getting total: " + e.getMessage());
            return "";
        }
    }

    public void continueShopping() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(continueShoppingButton));
            continueShoppingButton.click();
            Thread.sleep(1000);
        } catch (Exception e) {
            driver.get("https://demowebshop.tricentis.com/");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }

    public void proceedToCheckout() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(checkoutButton));
            checkoutButton.click();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getUnitPrice(int index) {
        try {
            List<WebElement> unitPrices = driver.findElements(By.cssSelector(".product-unit-price"));
            if (index < unitPrices.size()) {
                return unitPrices.get(index).getText();
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    public String getSubtotal(int index) {
        try {
            List<WebElement> subtotals = driver.findElements(By.cssSelector(".product-subtotal"));
            if (index < subtotals.size()) {
                return subtotals.get(index).getText();
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    public int getQuantity(int index) {
        try {
            List<WebElement> qtyInputs = driver.findElements(By.cssSelector(".qty-input"));
            if (index < qtyInputs.size()) {
                String value = qtyInputs.get(index).getAttribute("value");
                return Integer.parseInt(value);
            }
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean isCheckoutEnabled() {
        try {
            return checkoutButton.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    public void waitForCartToLoad() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".page-body")));
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}