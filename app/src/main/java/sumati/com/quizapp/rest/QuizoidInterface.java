package sumati.com.quizapp.rest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import sumati.com.quizapp.rest.model.LoginResponse;
import sumati.com.quizapp.rest.model.QuestionResponse;
import sumati.com.quizapp.rest.model.TopicResponse;
import sumati.com.quizapp.rest.model.UserRequest;

/**
 * Created by sumati on 3/4/18.
 */

public interface QuizoidInterface {
    @POST("login")
    Call<LoginResponse> login(@Body UserRequest user);


    @GET("topics")
    Call<List<TopicResponse>> getTopics();

    @GET("questions/{topic_id}/{count}")
    Call<List<QuestionResponse>> getQuestions(@Path("topic_id") long topicId, @Path("count") int count);
}
