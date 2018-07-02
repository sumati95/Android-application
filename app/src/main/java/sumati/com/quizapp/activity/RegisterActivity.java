package sumati.com.quizapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sumati.com.quizapp.R;
import sumati.com.quizapp.rest.QuizoidInterface;

public class RegisterActivity extends AppCompatActivity {

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create())
            .build();

    QuizoidInterface quizoidService = retrofit.create(QuizoidInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText txtUsername = findViewById(R.id.txtUsername);
        final EditText txtPassword = findViewById(R.id.txtEmail);
        final Button btnRegister = findViewById(R.id.btnRegister);
        final TextView txtMessage = findViewById(R.id.txtMessage);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();
                        Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(loginIntent);
            }
        });
    }
}

