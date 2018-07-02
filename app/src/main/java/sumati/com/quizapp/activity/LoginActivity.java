package sumati.com.quizapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sumati.com.quizapp.R;
import sumati.com.quizapp.rest.QuizoidInterface;
import sumati.com.quizapp.rest.model.LoginResponse;
import sumati.com.quizapp.rest.model.UserRequest;

public class LoginActivity extends AppCompatActivity {

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create())
            .build();

    QuizoidInterface quizoidService = retrofit.create(QuizoidInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText txtUsername  = findViewById(R.id.txtUsername);
        final EditText txtPassword  = findViewById(R.id.txtEmail);
        Button btnLogin = findViewById(R.id.btnLogin);
        final TextView txtLoginFailure = findViewById(R.id.txtLoginFailure);
        CheckBox checkView = findViewById(R.id.checkView);

        checkView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (!b) {
                    //TODO show password
                    txtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                else {
                    //TODO  hide password
                    txtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();
                Call<LoginResponse> loginCall = quizoidService.login(UserRequest.builder().username(username).password(password).build());
                loginCall.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        LoginResponse loginResponse = response.body();
                        if (loginResponse.isResult()) {
                            // TODO Move to play activity
                            SharedPreferences.Editor sharedPreferencesEditor = getSharedPreferences("quizoid.prefs", MODE_PRIVATE).edit();
                            sharedPreferencesEditor.putString("token", loginResponse.getToken());
                            Intent playIntent = new Intent(LoginActivity.this, PlayActivity.class);
                            startActivity(playIntent);
                        } else {
                            // TODO Set the result text view with fail error message
                            txtLoginFailure.setText("Invalid username and password combination");
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        // TODO Set the result text view saying internet issue
                        txtLoginFailure.setText("Check your Internet connection");
                    }
                });
            }
        });
    }
}
