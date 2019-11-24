package iful.exams.poll.util;

import javafx.stage.FileChooser;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public final class Constants {
    private Constants(){}

    public static final Locale UKRAINE_LOCALE = new Locale("uk", "UA");
    public static final DateFormat DATEPICKER_DATE_FORMAT = new SimpleDateFormat("dd MMMM yyyy hh:mm", UKRAINE_LOCALE);
    public static final DateFormat SEARCH_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm", UKRAINE_LOCALE);
    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("##.##");
    public static final FileChooser.ExtensionFilter FILE_FILTER = new FileChooser.ExtensionFilter("Microsoft Excel Files", "*.xlsx");


    public static final int RESPONDENT_DATA_PARAMS_COUNT = 10;
    public static final String NO_SEARCH_RESULTS = "Немає результатів";
    public static final String MAX_QUESTION_RATING_TEXT = "0.4";
    public static final double MAX_QUESTION_RATING_DOUBLE = 0.4;
    public static final String RESULT_SECOND_NAME = "Результат";
    public static final int RATING_COLUMNS_START_INDEX = 9;
    public static final int FILE_COLUMNS_NAME_INDEX = 0;
    public static final int RATING_COLUMNS_LAST_INDEX = 30;

    public static final String SECOND_NAME_COLUMN = "прізвище";
    public static final String FIRST_NAME_COLUMN = "ім'я";
    public static final String INSTITUTION_COLUMN = "заклад";
    public static final String DEPARTMENT_COLUMN = "відділ";
    public static final String EMAIL_COLUMN = "електронна пошта";
    public static final String STATE_COLUMN = "стан";
    public static final String STARTED_AT_COLUMN = "розпочато";
    public static final String FINISHED_AT_COLUMN = "завершено";
    public static final String TIME_SPENT_COLUMN = "витрачений час";
    public static final String RATING_COLUMN = "оцінка/20,00";


}
