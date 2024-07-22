package com.spacECE.spaceceedu.GrowthTracker;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class ItemModel {
    @SerializedName("vaccine_id")
    private String vaccineId;

    @SerializedName("vaccine_name")
    private String vaccineName;

    @SerializedName("protects_against")
    private String protectsAgainst;

    @SerializedName("info")
    private String info;

    // Constructors
    public ItemModel() {}

    public ItemModel(String vaccineId, String vaccineName, String protectsAgainst, String info) {
        this.vaccineId = vaccineId;
        this.vaccineName = vaccineName;
        this.protectsAgainst = protectsAgainst;
        this.info = info;
    }

    // Getters and Setters
    public String getVaccineId() {
        return vaccineId;
    }

    public void setVaccineId(String vaccineId) {
        this.vaccineId = vaccineId;
    }

    public String getVaccineName() {
        return vaccineName;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }

    public String getProtectsAgainst() {
        return protectsAgainst;
    }

    public void setProtectsAgainst(String protectsAgainst) {
        this.protectsAgainst = protectsAgainst;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @NonNull
    @Override
    public String toString() {
        return "Vaccine{" +
                "vaccineId='" + vaccineId + '\'' +
                ", vaccineName='" + vaccineName + '\'' +
                ", protectsAgainst='" + protectsAgainst + '\'' +
                ", info='" + info + '\'' +
                '}';
    }

    // by Mohit
}

