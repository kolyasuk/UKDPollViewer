package iful.exams.poll.util;

import iful.exams.poll.model.Poll;
import iful.exams.poll.model.QuestionRating;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.Iterator;

import static iful.exams.poll.util.Constants.*;

public class ValuesUtil {

    public static String fixRatingValue(String dirtyRatingValue){
        return dirtyRatingValue.replace(",", ".").replace("-", "0");
    }

    public static ObservableList<Poll> fixRatingValues(ObservableList<Poll> fileData){
        Iterator<Poll> itr = fileData.iterator();
        while (itr.hasNext()){
            Poll poll = itr.next();
            double realSumRating = 0.0;
            for(QuestionRating qr : poll.getQuestionsRating()){
                if(Double.parseDouble(qr.getRating()) > MAX_QUESTION_RATING_DOUBLE){
                    qr.setRating(MAX_QUESTION_RATING_TEXT);
                    realSumRating += MAX_QUESTION_RATING_DOUBLE;
                }
                else{
                    realSumRating += Double.parseDouble(qr.getRating());
                }
            }
            String fixedRating = String.valueOf(DECIMAL_FORMAT.format(realSumRating));
            poll.setRating(fixedRating);
        }
        return fileData;
    }

    public static void calculateGeneralRating(TableView tableView, ObservableList<Poll> fileData){
        Poll poll = new Poll();
        poll.setSecondName(RESULT_SECOND_NAME);
        ArrayList<QuestionRating> questionRating = new ArrayList<>();
        ObservableList<TableColumn> columns = tableView.getColumns();

        for(int i = RATING_COLUMNS_START_INDEX; i < RATING_COLUMNS_LAST_INDEX; i++){
            double columnAvg = 0;
            for (Object row : tableView.getItems()) {
                columnAvg += Double.parseDouble(columns.get(i).getCellObservableValue(row).getValue().toString());
            }

            String fixedRating = String.valueOf(DECIMAL_FORMAT.format(columnAvg/tableView.getItems().size()));
            if(i == RATING_COLUMNS_START_INDEX)
                poll.setRating(fixedRating);
            else
                questionRating.add(new QuestionRating(columns.get(i).getText(), fixedRating));
        }
        //get the last child of table
        Poll lastTableChild = fileData.get(fileData.size()-1);

        poll.setQuestionsRating(questionRating);
        //if the last child is result - replace it
        if(lastTableChild.getEmail().isEmpty() && lastTableChild.getStartedAt().isEmpty() && lastTableChild.getFinishedAt().isEmpty() && lastTableChild.getTimeSpent().isEmpty())
            lastTableChild = poll;
        else fileData.add(poll);
    }
}
