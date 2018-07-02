package sumati.com.quizapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sumati.com.quizapp.R;
import sumati.com.quizapp.rest.QuizoidInterface;
import sumati.com.quizapp.rest.model.QuestionResponse;

public class QuestionActivity extends AppCompatActivity {

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create())
            .build();

    QuizoidInterface quizoidService = retrofit.create(QuizoidInterface.class);
    int currentQuestion = 0;

    private Button btnGetNext;
    private Button btnGetPrev;
    private Button btnCalculateScore;
    private Button btnHome;
    private TextView txtQuestion;
    private RadioButton radOption1;
    private RadioButton radOption2;
    private RadioButton radOption3;
    private RadioButton radOption4;


    private Map<Integer, Integer> answerMap = new HashMap<>();
    private List<QuestionResponse> questionResponseList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        btnGetNext = findViewById(R.id.btnGetNext);
        btnGetPrev = findViewById(R.id.btnGetPrev);
        txtQuestion = findViewById(R.id.txtQuestion);
        radOption1 = findViewById(R.id.rbtnChoice1);
        radOption2 = findViewById(R.id.rbtnChoice2);
        radOption3 = findViewById(R.id.rbtnChoice3);
        radOption4 = findViewById(R.id.rbtnChoice4);
        btnCalculateScore = findViewById(R.id.btnOk);

        View.OnClickListener radioClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answerMap.put(currentQuestion, view.getId() - R.id.rbtnChoice1 + 1);
                Log.d("Score update", "Answer map now has " + answerMap.size() + " entries");
            }
        };
        radOption1.setOnClickListener(radioClickListener);
        radOption2.setOnClickListener(radioClickListener);
        radOption3.setOnClickListener(radioClickListener);
        radOption4.setOnClickListener(radioClickListener);

        final long topicId = getIntent().getLongExtra("topicId", 1);
        int numQuestions = getIntent().getIntExtra("numQuestions", 10);
        final Call<List<QuestionResponse>> questionCall = quizoidService.getQuestions(topicId, numQuestions);
        questionCall.enqueue(new Callback<List<QuestionResponse>>() {
            @Override
            public void onResponse(Call<List<QuestionResponse>> call, Response<List<QuestionResponse>> response) {
                questionResponseList = response.body();
                currentQuestion = 0;
                showQuestion(currentQuestion);
                btnCalculateScore.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<List<QuestionResponse>> call, Throwable t) {
            }
        });

        btnGetNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentQuestion++;
                showQuestion(currentQuestion);
            }
        });

        btnGetPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentQuestion--;
                showQuestion(currentQuestion);
            }
        });

        btnCalculateScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int correct = 0;
                for (Map.Entry<Integer, Integer> answerEntry : answerMap.entrySet()) {
                    Log.d("Score", "Key " + answerEntry.getKey() + " Value " + answerEntry.getValue());
                    Log.d("Score", "Correct answer is: " + questionResponseList.get(answerEntry.getKey()).getCorrectAnswer());
                    if (questionResponseList.get(answerEntry.getKey()).getCorrectAnswer() == answerEntry.getValue()) {
                        correct++;
                    }
                }
                int incorrect = answerMap.size() - correct;
                int unattempted = questionResponseList.size() - (correct + incorrect);
                Toast.makeText(getApplicationContext(), "Total " + correct + " correct and " + incorrect + " incorrect and not attempted " + unattempted, Toast.LENGTH_LONG).show();
            }
        });
    }

    void showQuestion(int questionId) {
        Log.d("Question", "Showing question " + questionId);
        btnGetPrev.setVisibility(View.VISIBLE);
        btnGetNext.setVisibility(View.VISIBLE);
        if (questionId >= questionResponseList.size() - 1) {
            btnGetNext.setVisibility(View.INVISIBLE);
        }
        if (questionId <= 0) {
            btnGetPrev.setVisibility(View.INVISIBLE);
        }
        if (questionId < questionResponseList.size() && questionId >= 0) {
            populateQuestion(questionResponseList.get(questionId));
        } else {
            txtQuestion.setText("No more questions. Click the below button to see other questions.");
            radOption1.setVisibility(View.INVISIBLE);
            radOption2.setVisibility(View.INVISIBLE);
            radOption3.setVisibility(View.INVISIBLE);
            radOption4.setVisibility(View.INVISIBLE);
        }
    }

    void populateQuestion(QuestionResponse questionResponse) {
        Log.d("", "Populating question " + questionResponse.toString());
        txtQuestion.setText(questionResponse.getQuestionText());
        radOption1.setVisibility(View.VISIBLE);
        radOption2.setVisibility(View.VISIBLE);
        radOption3.setVisibility(View.VISIBLE);
        radOption4.setVisibility(View.VISIBLE);
        radOption1.setText(questionResponse.getOption1());
        radOption2.setText(questionResponse.getOption2());
        radOption3.setText(questionResponse.getOption3());
        radOption4.setText(questionResponse.getOption4());
        radOption1.setChecked(false);
        radOption2.setChecked(false);
        radOption3.setChecked(false);
        radOption4.setChecked(false);
    }
}

