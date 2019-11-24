package iful.exams.poll.util;

import iful.exams.poll.model.Poll;
import iful.exams.poll.model.QuestionRating;
import static iful.exams.poll.util.Constants.*;
import static iful.exams.poll.util.ValuesUtil.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PopulateTableViewUtil {
    private TableView tableView;
    private List<TableColumn> tableColumns;
    private File file;

    public PopulateTableViewUtil(TableView tableView, File file) {
        this.tableView = tableView;
        this.file = file;
    }

    public ObservableList<Poll> populateTableView(){
        tableView.setEditable(true);
        return readFile(file);
    }

    private ObservableList<Poll> readFile(File file) {
        ObservableList<Poll> obsListTableData = FXCollections.observableArrayList();
        try (FileInputStream fis = new FileInputStream(file);XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
                XSSFSheet sheet = workbook.getSheetAt(0);
                Iterator<Row> rowIt = sheet.iterator();

                while(rowIt.hasNext()) {
                    Row row = rowIt.next();
                    Iterator<Cell> cellIterator = row.cellIterator();
                    if(row.getRowNum() == FILE_COLUMNS_NAME_INDEX){
                        tableView.getColumns().addAll(defineTableColumns(cellIterator));
                    }
                    else {
                        obsListTableData.add(pollBuilder(cellIterator));
                    }
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obsListTableData;
    }

    private Poll pollBuilder(Iterator<Cell> cellIterator){
        Poll poll = new Poll();
        List<QuestionRating> questions = new ArrayList<>();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            if(cell.getColumnIndex() < RESPONDENT_DATA_PARAMS_COUNT) {
                switch (tableColumns.get(cell.getColumnIndex()).getText().toLowerCase()) {
                    case SECOND_NAME_COLUMN:
                        tableColumns.get(cell.getColumnIndex()).setCellValueFactory(new PropertyValueFactory<Poll, String>("secondName"));
                        poll.setSecondName(cell.getStringCellValue());
                        break;
                    case FIRST_NAME_COLUMN:
                        tableColumns.get(cell.getColumnIndex()).setCellValueFactory(new PropertyValueFactory<Poll, String>("firstName"));
                        poll.setFirstName(cell.getStringCellValue());
                        break;
                    case INSTITUTION_COLUMN:
                        tableColumns.get(cell.getColumnIndex()).setCellValueFactory(new PropertyValueFactory<Poll, String>("institution"));
                        poll.setInstitution(cell.getStringCellValue());
                        break;
                    case DEPARTMENT_COLUMN:
                        tableColumns.get(cell.getColumnIndex()).setCellValueFactory(new PropertyValueFactory<Poll, String>("department"));
                        poll.setDepartment(cell.getStringCellValue());
                        break;
                    case EMAIL_COLUMN:
                        tableColumns.get(cell.getColumnIndex()).setCellValueFactory(new PropertyValueFactory<Poll, String>("email"));
                        poll.setEmail(cell.getStringCellValue());
                        break;
                    case STATE_COLUMN:
                        tableColumns.get(cell.getColumnIndex()).setCellValueFactory(new PropertyValueFactory<Poll, String>("state"));
                        poll.setState(cell.getStringCellValue());
                        break;
                    case STARTED_AT_COLUMN:
                        tableColumns.get(cell.getColumnIndex()).setCellValueFactory(new PropertyValueFactory<Poll, String>("startedAt"));
                        poll.setStartedAt(cell.getStringCellValue());
                        break;
                    case FINISHED_AT_COLUMN:
                        tableColumns.get(cell.getColumnIndex()).setCellValueFactory(new PropertyValueFactory<Poll, String>("finishedAt"));
                        poll.setFinishedAt(cell.getStringCellValue());
                        break;
                    case TIME_SPENT_COLUMN:
                        tableColumns.get(cell.getColumnIndex()).setCellValueFactory(new PropertyValueFactory<Poll, String>("timeSpent"));
                        poll.setTimeSpent(cell.getStringCellValue());
                        break;
                    case RATING_COLUMN:
                        tableColumns.get(cell.getColumnIndex()).setCellValueFactory(new PropertyValueFactory<Poll, String>("rating"));
                        poll.setRating(cell.getStringCellValue());
                        break;
                }
            }else {
                questions.add(new QuestionRating(tableColumns.get(cell.getColumnIndex()).getText(), fixRatingValue(cell.getStringCellValue())));
                tableColumns.get(cell.getColumnIndex()).setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Poll, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Poll, String> p) {
                        if (p.getValue() != null) {
                            String questionRating = p.getValue().getQuestionsRating().get(cell.getColumnIndex() - RESPONDENT_DATA_PARAMS_COUNT).getRating();
                            return new SimpleStringProperty(questionRating);
                        } else {
                            return new SimpleStringProperty("<no data>");
                        }
                    }
                });

            }
            poll.setQuestionsRating(questions);
        }
        return poll;
    }

    private List<TableColumn> defineTableColumns(Iterator<Cell> cellIterator){
        List<TableColumn> tableColumns = new ArrayList<>();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            tableColumns.add(new TableColumn(cell.toString()));
        }
        this.tableColumns = tableColumns;
        return tableColumns;
    }

}
