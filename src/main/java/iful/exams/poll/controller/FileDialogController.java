package iful.exams.poll.controller;

import iful.exams.poll.model.Poll;
import iful.exams.poll.model.QuestionRating;
import iful.exams.poll.util.DateUtil;
import iful.exams.poll.util.FIleUtil;
import iful.exams.poll.util.PopulateTableViewUtil;
import static iful.exams.poll.util.Constants.*;
import static iful.exams.poll.util.ValuesUtil.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
    private Button saveButton;
    @FXML
    private CheckBox onlyByName;
    @FXML
    private DatePicker datePicker;
    private ObservableList<Poll> fileData;
    private File file;

    public void transferFile(File file) {
        this.file = file;
    }

    public void initialize(){
        Platform.runLater(() -> {
            try {
                PopulateTableViewUtil populateUtil = new PopulateTableViewUtil(tableView, file);
                fileData = fixRatingValues(populateUtil.populateTableView());
                tableView.setItems(fileData);
                calculateGeneralRating(tableView, fileData);
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
        if(tableView.getItems().size() != fileData.size())
            tableView.setItems(fileData);
            ObservableList<Poll> searchResult = FXCollections.observableArrayList(tableView.getItems().stream().filter(item -> {
                try {
                    Date itemStartDate = DATEPICKER_DATE_FORMAT.parse(item.getStartedAt());
                    Date itemEndDate = DATEPICKER_DATE_FORMAT.parse(item.getStartedAt());
                    Date userPickedStartDate = SEARCH_DATE_FORMAT.parse(datePicker.getValue().toString()+ " " + (startTime.getText().isEmpty() ? "00:00" : startTime.getText()));
                    Date userPickedSEndDate = SEARCH_DATE_FORMAT.parse(datePicker.getValue().toString()+ " " + (endTime.getText().isEmpty() ? "23:59" : endTime.getText()));
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
        calculateGeneralRating(tableView, searchResult);
        if(searchResult.isEmpty()) tableView.setPlaceholder(new Label(NO_SEARCH_RESULTS));
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

    @FXML
    private void handleSaveAction(){
        Stage stage = (Stage) saveButton.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        fileChooser.getExtensionFilters().add(FILE_FILTER);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(stage);
        if(file != null){
            FIleUtil.save(tableView, file);
        }
    }


}
