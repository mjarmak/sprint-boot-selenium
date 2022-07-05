package com.example.demo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;

public class SeleniumDownloader {

    private static final String url = "https://www.gleif.org/en/lei-data/gleif-golden-copy/download-the-golden-copy";
    private static WebDriver driver;
    private static WebDriverWait wait;

    public static void main(String[] args) {
        run();
    }
    public static void run() {

        try {

            ChromeOptions options = new ChromeOptions();

//            options.addArguments("--headless");
            options.addArguments("--start-maximized");
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            options.addArguments("--ignore-certificate-errors");
            options.addArguments("--allow-running-insecure-content");
            options.addArguments("--disable-extensions");
            options.addArguments("--proxy-server='direct://'");
            options.addArguments("--proxy-bypass-list=*");
            options.addArguments("--disable-dev-shm-usage");

            String downloadFilepath = "C:\\dev\\temp";
            HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
            chromePrefs.put("profile.default_content_settings.popups", 0);
            chromePrefs.put("download.default_directory", downloadFilepath);
            options.setExperimentalOption("prefs", chromePrefs);
            driver = new ChromeDriver(options);
            wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            driver.get(url);
            waitAndClick(By.id("CybotCookiebotDialogBodyButtonAccept"));
            waitAndClick(By.id("deltaSelect"));
            waitAndClick(By.xpath("//a[contains(text(), '24 hours')]"));
            waitAndClick(By.id("formatSelect"));
            waitAndClick(By.xpath("//a[contains(text(), 'CSV')]"));
            System.out.println("downloading csv");
            Thread.sleep(10000);
        } catch (Exception e) {
            driver.quit();
            e.printStackTrace();
        }

        driver.quit();
    }

    public static void waitAndClick(By by) throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(by));
        Thread.sleep(500);
        WebElement element = driver.findElement(by);
        element.click();
    }

}
