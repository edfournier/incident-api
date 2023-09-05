package com.group.api.model;

public class Incident {
    private String date;
    private String[] log;

    public Incident() {}

    public Incident(String date, String[] log) {
        this.date = date;
        this.log = log;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLog(String[] log) {
        this.log = log;
    }

    public String getDate() {
        return date;
    }

    public String[] getLog() {
        return log;
    }
}