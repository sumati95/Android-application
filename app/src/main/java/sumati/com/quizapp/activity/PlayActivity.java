package sumati.com.quizapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sumati.com.quizapp.R;
import sumati.com.quizapp.rest.QuizoidInterface;
import sumati.com.quizapp.rest.model.TopicResponse;

public class PlayActivity extends AppCompatActivity {

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create())
            .build();

    QuizoidInterface quizoidService = retrofit.create(QuizoidInterface.class);

    private long topicId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        final Spinner selectTopicSpinner = findViewById(R.id.spnSelectTopic);
        final EditText txtNumQuestions = findViewById(R.id.editQuestionno);
        Button btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int numQuestions = Integer.parseInt(txtNumQuestions.getText().toString());
                Intent questionIntent = new Intent(PlayActivity.this, QuestionActivity.class);
                questionIntent.putExtra("numQuestions", numQuestions);
                questionIntent.putExtra("topicId", topicId);

                startActivity(questionIntent);
            }
        });
        Call<List<TopicResponse>> topicCall = quizoidService.getTopics();
        topicCall.enqueue(new Callback<List<TopicResponse>>() {

            @Override
            public void onResponse(Call<List<TopicResponse>> call, Response<List<TopicResponse>> response) {
                final List<TopicResponse> topicResponseList = response.body();
                List<String> spinnerItems = new ArrayList<>();
                for (TopicResponse topicResponse : topicResponseList) {
                    spinnerItems.add(topicResponse.getTopicName());
                }

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, spinnerItems);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                selectTopicSpinner.setAdapter(dataAdapter);
                selectTopicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        topicId = topicResponseList.get(i).getId();
                        Log.d(getClass().getName(), "Selected topic id " + topicId);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }

            @Override
            public void onFailure(Call<List<TopicResponse>> call, Throwable t) {

            }
        });
    }
}
