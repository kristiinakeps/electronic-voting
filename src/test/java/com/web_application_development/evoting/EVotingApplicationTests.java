package com.web_application_development.evoting;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EVotingApplicationTests {

    private WebDriver driver;

    @BeforeClass
    public static void setUpClass() {
        ChromeDriverManager.getInstance().setup();
    }

    @Before
    public void setUp() {
        driver = new ChromeDriver();
        driver.get("http://localhost:8080/");
        logIn();
    }

    @After
    public void tearDown() {
        logOut();
        driver.close();
    }

    @Test
    public void contextLoads() {
    }

    private void logOut() {
        driver.findElement(By.className("dropdown-toggle")).click();
        driver.findElement(By.className("linkButton")).click();
        assertEquals("http://localhost:8080/login", driver.getCurrentUrl());

    }

    private void logIn() {
        driver.get("http://localhost:8080/login");
        driver.findElement(By.id("nationalIdentityNumber")).sendKeys("10101010005");
        driver.findElement(By.className("btn")).click();
        new WebDriverWait(driver, 1000).until(ExpectedConditions.titleContains("Home"));
        assertEquals("http://localhost:8080/", driver.getCurrentUrl());
    }

    private void failLogInWith(String identityCode, String errorMessage) {
        logOut();
        driver.get("http://localhost:8080/login");
        driver.findElement(By.id("nationalIdentityNumber")).sendKeys(identityCode);
        driver.findElement(By.className("btn")).click();
        new WebDriverWait(driver, 1000).until(ExpectedConditions.visibilityOf(driver.findElement(By.id("error-text"))));
        assertEquals(errorMessage, driver.findElement(By.id("error-text")).getText());
        logIn();
    }

    @Test
    public void failLogInWithAccountNotFound() {
        failLogInWith("12345", "Account not found!");
    }

    @Test
    public void failLogInWithUserRefused() {
        failLogInWith("10101010016", "User cancelled Smart-ID request!");
    }

    @Test
    public void vote() {
        try {
            driver.findElement(By.id("take-back-vote-button")).click();
        } catch (NoSuchElementException e) {
            System.out.println("No active vote");
        }
        assertEquals(0, driver.findElements(By.id("take-back-vote-button")).size());
        driver.findElement(By.id("vote-button")).click();
        driver.navigate().refresh();
        assertNotEquals(0, driver.findElements(By.id("take-back-vote-button")).size());
    }

    @Test
    public void candidacy() {
        driver.get("http://localhost:8080/candidacy");
        try {
            driver.findElement(By.id("take-back-candidacy-button")).click();
        } catch (NoSuchElementException e) {
            System.out.println("No active candidacy");
        }
        assertEquals(0, driver.findElements(By.id("take-back-candidacy-button")).size());
        driver.findElement(By.id("submit-candidacy-button")).click();
        assertNotEquals(0, driver.findElements(By.id("take-back-candidacy-button")).size());
    }

    @Test
    public void searchForCandidate() throws InterruptedException {
        driver.findElement(By.id("candidateTable_filter")).findElement(By.className("form-control")).sendKeys("Demo");
        assertTrue("Couldn't find candidate", driver.findElement(By.tagName("body")).getText().contains("Demo"));
    }
}
