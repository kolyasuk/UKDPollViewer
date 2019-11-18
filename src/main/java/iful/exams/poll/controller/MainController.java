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

public class MainController {

    @FXML
    private Button button;

    @FXML
    private ListView filelist;

    public void initialize() {
        filelist.setItems(FXCollections.observableArrayList(new File("C:/Users/dziob/Desktop/test.xlsx")));
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        Stage stage = (Stage) button.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Microsoft Excel Files", "*.xlsx")
        );
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(stage);
        if (selectedFiles != null) {
            filelist.setItems(FXCollections.observableArrayList(selectedFiles));
        }
    }

    @FXML
    private void handleFileListAction(MouseEvent mouseEvent) throws IOException {
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
            int listIndex = filelist.getSelectionModel().getSelectedIndex();
            if(mouseEvent.getClickCount() == 2 && listIndex != -1) {
                List<File> files = filelist.getItems();

                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/fxml/OpenedFileDialog.fxml"));
                    Parent root = fxmlLoader.load();

                    FileDialogController openedFileDilogController = fxmlLoader.getController();
                    //transfer file to FileDialogController controller
                    openedFileDilogController.transferFile(files.get(listIndex));

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
