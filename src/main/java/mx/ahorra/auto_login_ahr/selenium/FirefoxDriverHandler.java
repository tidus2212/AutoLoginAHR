package mx.ahorra.auto_login_ahr.selenium;

import java.time.Duration;
import java.util.NoSuchElementException;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import mx.ahorra.auto_login_ahr.models.AsistoKeys;
import mx.ahorra.auto_login_ahr.models.RequestAsisto;
import mx.ahorra.auto_login_ahr.models.Result;

@Slf4j
public class FirefoxDriverHandler {

    
    public static String MSG_OK ="exitoso";
    public static String MSG_FAIL= "no corresponden";

    WebDriver driver;
    RequestAsisto reqData;
    AsistoKeys reqKeys;

    public FirefoxDriverHandler(RequestAsisto reqData, AsistoKeys reqKeys){

        this.reqData= reqData;
        this.reqKeys = reqKeys;

        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();

          // this i added  this and it worked, Thanks a ton  xinchao zhang !! 
        driver = new FirefoxDriver(options);
    }


    public Result getResfromAlertAhr(){
        log.info(String.format("basic data: %s,%s,%s,%s", reqData.url,reqData.enterprise,reqData.user,reqData.password));

        Result result = new Result();
        result.isOk = false;


        try {
            log.trace("URL Probada en Driver:"+reqData.url);
            driver.get(reqData.url);
           
            System.out.println("Begin Wait.");
            FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver);
            wait.withTimeout(Duration.ofSeconds(10))
            .pollingEvery(Duration.ofSeconds(1))
            .ignoring(NoSuchElementException.class);
       
            wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(By.id("MAINFORM"))
            ));
            log.trace("MainForm is Visible.");


            //Get Elements
            WebElement entTxtBox = driver.findElement(By.id(reqKeys.idEntElement));
            WebElement userTxtBox = driver.findElement(By.id(reqKeys.idUserElement));
            WebElement passTxtBox = driver.findElement(By.id(reqKeys.idPassElement));
            WebElement btnSubmit = driver.findElement(By.id(reqKeys.idBtnElement));


            //populate Texts
            entTxtBox.sendKeys(reqData.enterprise);
            userTxtBox.sendKeys(reqData.user);
            passTxtBox.sendKeys(reqData.password);
            log.trace("BPopulate Texts.");


            btnSubmit.click();
            log.trace("Submit.");


            wait.withTimeout(Duration.ofSeconds(10))
            .pollingEvery(Duration.ofSeconds(1))
            .ignoring(NoSuchElementException.class);
       
            wait.until(ExpectedConditions.or(ExpectedConditions.alertIsPresent()
            ));


            Alert alert = driver.switchTo().alert();
            result.msg =  alert.getText();
            alert.accept();

            result.htmlBody = driver.getPageSource();
            
            
            if(result.msg.contains(MSG_OK))
            {
                result.isOk = true;
                result.htmlCode = 200;
            }
            else{
                result.htmlCode = 300;
            }
           
            log.trace("end test!!");
           


           


        } catch (Exception e) {
           result.msg = e.getMessage();
           result.htmlBody = driver.getPageSource();
           result.htmlCode = 400;
        }
       
        return result;

    }

    public void Dispose(){
        driver.close();
        driver = null;

        WebDriverManager.firefoxdriver().quit();
    }
    
}
