package com.senacor.devconfapp.handlers;


import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

/**
 * Created by saba on 30.12.16.
 */

public class ValidationHandler {


    public ValidationHandler(){

    }


    public boolean isInRightOrder(LocalTime startTime, LocalTime endTime) {

        return startTime.isBefore(endTime);
    }

    public boolean isInFuture(LocalDate date) {
        System.out.println(LocalDate.now());
        return date.isAfter(LocalDate.now());
    }
}
