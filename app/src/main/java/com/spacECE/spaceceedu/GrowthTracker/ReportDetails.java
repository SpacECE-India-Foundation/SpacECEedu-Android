package com.spacECE.spaceceedu.GrowthTracker;

public class ReportDetails {
    private String title;
    private int[] data;

    public ReportDetails(String title, int[] data) {
        this.title = title;
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public int[] getData() {
        return data;
    }
}
