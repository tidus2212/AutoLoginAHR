package mx.ahorra.auto_login_ahr.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import mx.ahorra.auto_login_ahr.models.AsistoKeys;
import mx.ahorra.auto_login_ahr.models.RequestAsisto;
import mx.ahorra.auto_login_ahr.models.Result;
import mx.ahorra.auto_login_ahr.selenium.FirefoxDriverHandler;
import mx.ahorra.auto_login_ahr.services.MailSenderService;

@Slf4j
@Component
public class CommandLineComponent implements CommandLineRunner{

     @Autowired
    private ConfigurableApplicationContext context;
    
    @Autowired
    private MailSenderService mailSenderService;

    @Value("${ahr.url}")
    String url;
    @Value("${ahr.flavor}")
    String flavor;
    @Value("${ahr.force}")
    Boolean isForce;
    @Value("${ahr.enterprise}")
    String enterprise;
    @Value("${ahr.user}")
    String user;
    @Value("${ahr.password}")
    String password;

    @Value("${ahr.keys.btnsubmit}")
    String keyBtnSubmit;
    @Value("${ahr.keys.enterprise}")
    String keyTxtEnterprise;
    @Value("${ahr.keys.user}")
    String keyTxtUser;
    @Value("${ahr.keys.password}")
    String keyTxtPassword;

    @Value("${ahr.sendmail}")
    Boolean isSendMail;
    @Value("${spring.mail.username}")
    String userMail;
    


    @Override
    public void run(String... args) throws Exception {
        
        Result result;
        switch (flavor) {
            case "selenium":
                result = seleniumFlavor();
                break;
            case "post":
            default:
                result = postFlavor();
                break;
        }

        log.debug("resultado:"+result.htmlBody);
        if (result.getIsOk()) {
            log.info("Asistencia Exitosa!!!!!");
        }
        else{
            
            if(isForce == true && flavor == "selenium"){

                result = postFlavor();

                if (result.getIsOk()) {
                    log.info("Asistencia Exitosa Por excepcion por post!!!!!");
                }
                else{
                    log.info("ERROR al Enviar asistencia. incluso por post");
                    if(isSendMail){
                        log.info("ERROR al Enviar asistencia incluso por post, se envio por correo.");
                        mailSenderService.simpleSendMail(userMail,result.getMsg());
                    }
                }

            }
            else{
                if(isSendMail){
                    log.info("ERROR al Enviar asistencia, se envio por correo.");
                    mailSenderService.simpleSendMail(userMail,result.getMsg());
                    
                }
            }

            
        
            
            

        }
        log.info(result.msg);
        System.exit(SpringApplication.exit(context));
    }


    public Result postFlavor(){

        RestTemplate restTemplate = new RestTemplate();
        RequestAsisto asistoReq = new RequestAsisto(url, enterprise,user,password);
        HttpEntity<RequestAsisto> resEntity = new HttpEntity<RequestAsisto>(asistoReq);
        ResponseEntity<String> response = restTemplate.postForEntity(asistoReq.url,resEntity,String.class);
        Result result = new Result();
        result.htmlBody = response.getBody();
        result.htmlCode = response.getStatusCode().value();
        result.isOk = !response.getStatusCode().isError();
        result.msg = "Realizado";

        return result;
    }
    
    public Result seleniumFlavor(){
        
        FirefoxDriverHandler  handler = null;
        Result result = new Result();
        result.isOk = false;

        try{
            RequestAsisto req= new RequestAsisto(url, enterprise, user, password);
            AsistoKeys keys = new AsistoKeys(keyTxtEnterprise, keyTxtUser, keyTxtPassword, keyBtnSubmit);

            handler = new FirefoxDriverHandler(req,keys);

            result = handler.getResfromAlertAhr();
        }
        catch(Exception ex){
            
            log.info(ex.getMessage());
        }
        
        if(handler!=null)
        handler.Dispose();
        
        return result;
    }

    
}
