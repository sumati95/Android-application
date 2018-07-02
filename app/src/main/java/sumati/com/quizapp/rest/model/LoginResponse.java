package sumati.com.quizapp.rest.model;

import lombok.Data;

@Data
public class LoginResponse {
    private boolean result;
    private String token;
}
