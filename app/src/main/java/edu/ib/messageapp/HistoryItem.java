package edu.ib.messageapp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HistoryItem {
    private String message;
    private LocalDateTime localDateTime;

    public HistoryItem(String message, String localDateTime){
        this.message=message;
        this.localDateTime=LocalDateTime.parse(localDateTime, DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }
    public HistoryItem(String message, LocalDateTime localDateTime){
        this.message=message;
        this.localDateTime=localDateTime;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    @Override
    public String toString() {
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))+"\n"+message;
    }

}