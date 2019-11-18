package iful.exams.poll.util;

import java.util.Date;

public class DateUtil {
    /**
     * This method compare two dates
     *
     * @param originDate the date you want to compare with
     * @param newDate the date you want to compare
     * @return returns true if origin date is before yours; false if yours date is after origin date
     */
    public static boolean compare(Date originDate, Date newDate){
        return originDate.getTime() <= newDate.getTime();
    }
}
