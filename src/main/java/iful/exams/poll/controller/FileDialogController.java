package iful.exams.poll.controller;

import iful.exams.poll.model.Poll;
import iful.exams.poll.model.QuestionRating;
import iful.exams.poll.util.DateUtil;
import iful.exams.poll.util.PopulateTableViewUtil;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.stream.Collectors;

public class FileDialogController {
    @FXML
    private TableView<Poll> tableView;
    @FXML
    private TextField startTime;
    @FXML
    private TextField endTime;
    @FXML
    private TextField studentFullName;
    @FXML
    private Button resetButton;
    @FXML
    private CheckBox onlyByName;
    @FXML
    private DatePicker datePicker;
    private ObservableList<Poll> fileData;
    private File file;
    Locale ukLocale = new Locale("uk", "UA");
    DateFormat pickerDateFormat = new SimpleDateFormat("dd MMMM yyyy hh:mm", ukLocale);
    DateFormat textFieldDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm", ukLocale);
    DateTimeFormatter formatter = DateTimeFormatter.BASIC_ISO_DATE;

    public void transferFile(File file) {
        this.file = file;
    }

    public void initialize(){
        Platform.runLater(() -> {
            try {
                PopulateTableViewUtil populateUtil = new PopulateTableViewUtil(tableView, file);
                fileData = fixRatingValues(populateUtil.populateTableView());
                tableView.setItems(fileData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        datePicker.setValue(LocalDate.now());
        datePicker.setOnShowing(e-> Locale.setDefault(Locale.Category.FORMAT,Locale.ENGLISH));
        Locale locale = new Locale("uk", "UA");
        datePicker.setOnShown(e-> Locale.setDefault(Locale.Category.FORMAT,locale));
    }

    @FXML
    private void search(){
        System.out.println(tableView.getItems().size());
        if(tableView.getItems().size() != fileData.size())
            tableView.setItems(fileData);
        ObservableList<Poll> searchResult = FXCollections.observableArrayList(tableView.getItems().stream().filter(item -> {
                try {
                    Date itemStartDate = pickerDateFormat.parse(item.getStartedAt());
                    Date itemEndDate = pickerDateFormat.parse(item.getStartedAt());
                    Date userPickedStartDate = textFieldDateFormat.parse(datePicker.getValue().toString()+ " " + (startTime.getText().isEmpty() ? "00:00" : startTime.getText()));
                    Date userPickedSEndDate = textFieldDateFormat.parse(datePicker.getValue().toString()+ " " + (endTime.getText().isEmpty() ? "23:59" : endTime.getText()));
                    if(onlyByName.isSelected())
                        return (item.getSecondName().toLowerCase()+' '+item.getFirstName().toLowerCase()).contains(studentFullName.getText());
                    else return !DateUtil.compare(itemStartDate, userPickedStartDate) && DateUtil.compare(itemEndDate, userPickedSEndDate)
                            && (item.getSecondName().toLowerCase()+' '+item.getFirstName().toLowerCase()).contains(studentFullName.getText());
                } catch (Exception e) {
                    //handle exception
                }
                return false;
            }).collect(Collectors.toList()));

        tableView.setItems(searchResult);
        if(searchResult.isEmpty()) tableView.setPlaceholder(new Label("Немає результатів"));
        resetButton.setVisible(true);
    }

    @FXML
    private void resetTableData(){
        tableView.setItems(fileData);
        resetButton.setVisible(false);
        startTime.clear();
        endTime.clear();
        studentFullName.clear();
        onlyByName.setSelected(false);
    }

    private ObservableList<Poll> fixRatingValues(ObservableList<Poll> fileData){
        Iterator<Poll> itr = fileData.iterator();
        while (itr.hasNext()){
            Poll poll = itr.next();
            double realSumRating = 0.0;
            for(QuestionRating qr : poll.getQuestionsRating()){
                if(Double.parseDouble(qr.getRating()) > 0.4){
                    qr.setRating("0.4");
                    realSumRating += 0.4;
                }
                else{
                    realSumRating += Double.parseDouble(qr.getRating());
                }
            }
            String fixedRating = String.valueOf(Math.round(realSumRating * 10.0) / 10.0);
            poll.setRating(fixedRating);
        }
        return fileData;
    }

}
