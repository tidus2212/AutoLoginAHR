package mx.ahorra.auto_login_ahr.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestAsisto {

    public String url;
    public String enterprise;
    public String user;
    public String password;

    
}
