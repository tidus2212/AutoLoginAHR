package mx.ahorra.auto_login_ahr.services;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MailSenderService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${ahr.messagemail}")
    private String messageMail;


    public CompletableFuture<Boolean> simpleSendMail(String recieptMail, String messageBody) {

        Boolean resultado= true;

        try{
            var message = javaMailSender.createMimeMessage();
            var helper = new MimeMessageHelper(message,true,"UTF-8");
            
            helper.setFrom(recieptMail);
            helper.setTo(recieptMail);
            helper.setText(messageMail +"</br><p>"+messageBody+"</p>",true);

            helper.setSubject("ERROR al enviar asistencia automatica, se intenta por post");
            javaMailSender.send(message);

        }catch(Exception e)
        {
            resultado = false;
            log.info(e.getMessage());
        }



        return CompletableFuture.completedFuture(resultado);

    }
    
}
