package iful.exams.poll.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class QuestionRating {
    private String question;
    private String rating;
}
