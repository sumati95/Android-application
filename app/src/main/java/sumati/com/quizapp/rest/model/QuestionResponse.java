package sumati.com.quizapp.rest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionResponse {
    private long id;
    private String questionText;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private int correctAnswer;
    private String topicName;
}
