package iful.exams.poll.model;

import lombok.Data;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class Poll {
    private String firstName;
    private String secondName;
    private String institution;
    private String department;
    private String email;
    private String state;
    private String startedAt;
    private String finishedAt;
    private String timeSpent;
    private String rating;
    private List<QuestionRating> questionsRating;
}
