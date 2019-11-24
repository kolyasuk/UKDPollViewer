package iful.exams.poll.controller;

import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.*;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

import static iful.exams.poll.util.Constants.FILE_FILTER;

public class MainController {

    @FXML
    private Button newFileButton;

    @FXML
    private ListView fileList;

    public void initialize() {
        fileList.setItems(FXCollections.observableArrayList(new File("C:/Users/dziob/Desktop/test.xlsx")));
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        Stage stage = (Stage) newFileButton.getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(FILE_FILTER);
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(stage);
        if (selectedFiles != null) {
            fileList.setItems(FXCollections.observableArrayList(selectedFiles));
        }
    }

    @FXML
    private void handleFileListAction(MouseEvent mouseEvent) throws IOException {
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
            int listIndex = fileList.getSelectionModel().getSelectedIndex();
            if(mouseEvent.getClickCount() == 2 && listIndex != -1) {
                List<File> files = fileList.getItems();

                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/fxml/OpenedFileDialog.fxml"));
                    Parent root = fxmlLoader.load();

                    FileDialogController fileDilogController = fxmlLoader.getController();
                    //transfer file to FileDialogController controller
                    fileDilogController.transferFile(files.get(listIndex));

                    Stage newStage = new Stage();
                    Scene scene = new Scene(root);
                    newStage.setTitle("New Window");
                    newStage.setScene(scene);
                    newStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
