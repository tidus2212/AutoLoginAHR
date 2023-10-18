# AutoLoginAHR
Codigo que permite autologearse en HumanSite

Para poder ejecutarlo se debe tener instalado el navegador chrome o firefox y se debe verificar la integracion de selenium en base a la guia en la siguiente url:
        https://www.selenium.dev/documentation/webdriver/browsers/

Se deben modificar las variables en el archivo properties para personalizar el logeo:

ahr.flavor: (selenium | post)
        Acepta los valores 'selenium' para logeo por medio de un bot y 'post' para un logueo mas simple sin mensajes de confirmacion
ahr.force: (true | false)
        indica si se tratara de usar el sabor post en caso del que el selenium falle por algun problema no controlado
ahr.url: (url de la pantalla de logeo)
        por defecto se proporciona esta ->https://ahr.humansite.com.mx/hasis.aspx
ahr.enterprise: (nombre de la empresa)
        por defecto se proporciona esta -> 'ahr'
ahr.user:   (nombre de usuario HS)
ahr.password: (contraseña de usuario HS)

ahr.sendmail: (true | false)
        por defecto se proporciona este -> false
ahr.messagemail: (subject del correo de error)
         por defecto se proporciona este -> <h4>Hubo un error en el envio de asitencia automatizada</h4>
spring.mail.host: (host smtp)
        por defecto se proporciona este -> smtp.gmail.com
spring.mail.username: (nombre de usuario smtp)
spring.mail.password: (password para smtp)
spring.mail.port: (puerto smtp)


Cualquier otra variables son de control y no deben modificarse.

