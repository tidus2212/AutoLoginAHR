package mx.ahorra.auto_login_ahr.models;

import lombok.Data;

@Data
public class Result {

    public int htmlCode;
    public String htmlBody;
    public Boolean isOk;
    public String msg;
    
}
