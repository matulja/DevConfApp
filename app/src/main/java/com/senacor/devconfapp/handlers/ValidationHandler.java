package com.senacor.devconfapp.handlers;


import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

/**
 * Created by saba on 30.12.16.
 */

public class ValidationHandler {


    public ValidationHandler() {

    }


    public boolean isNotInRightOrder(LocalTime startTime, LocalTime endTime) {

        return !startTime.isBefore(endTime);
    }

    public boolean isNotInFuture(LocalDate date) {
        return date.isBefore(LocalDate.now());
    }

    public boolean isNotFilled(String input) {
        return input.isEmpty();
    }

}
