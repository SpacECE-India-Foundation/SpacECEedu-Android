
package com.spacECE.spaceceedu;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Data implements Serializable {

    @SerializedName("activity_no")
    @Expose
    private String activityNo;
    @SerializedName("activity_name")
    @Expose
    private String activityName;
    @SerializedName("activity_level")
    @Expose
    private String activityLevel;
    @SerializedName("activity_dev_domain")
    @Expose
    private String activityDevDomain;
    @SerializedName("activity_objectives")
    @Expose
    private String activityObjectives;
    @SerializedName("activity_key_dev")
    @Expose
    private String activityKeyDev;
    @SerializedName("activity_material")
    @Expose
    private String activityMaterial;
    @SerializedName("activity_assessment")
    @Expose
    private String activityAssessment;
    @SerializedName("activity_process")
    @Expose
    private String activityProcess;
    @SerializedName("activity_instructions")
    @Expose
    private String activityInstructions;

    public Data(String activityNo, String activityName, String activityLevel, String activityDevDomain, String activityObjectives, String activityKeyDev, String activityMaterial, String activityAssessment, String activityProcess, String activityInstructions) {
            this.activityNo = activityNo;
            this.activityName = activityName;
            this.activityLevel = activityLevel;
            this.activityDevDomain = activityDevDomain;
            this.activityObjectives = activityObjectives;
            this.activityKeyDev = activityKeyDev;
            this.activityMaterial = activityMaterial;
            this.activityAssessment = activityAssessment;
            this.activityProcess = activityProcess;
            this.activityInstructions = activityInstructions;
    }

    public String getActivityNo() {
        return activityNo;
    }

    public void setActivityNo(String activityNo) {
        this.activityNo = activityNo;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(String activityLevel) {
        this.activityLevel = activityLevel;
    }

    public String getActivityDevDomain() {
        return activityDevDomain;
    }

    public void setActivityDevDomain(String activityDevDomain) {
        this.activityDevDomain = activityDevDomain;
    }

    public String getActivityObjectives() {
        return activityObjectives;
    }

    public void setActivityObjectives(String activityObjectives) {
        this.activityObjectives = activityObjectives;
    }

    public String getActivityKeyDev() {
        return activityKeyDev;
    }

    public void setActivityKeyDev(String activityKeyDev) {
        this.activityKeyDev = activityKeyDev;
    }

    public String getActivityMaterial() {
        return activityMaterial;
    }

    public void setActivityMaterial(String activityMaterial) {
        this.activityMaterial = activityMaterial;
    }

    public String getActivityAssessment() {
        return activityAssessment;
    }

    public void setActivityAssessment(String activityAssessment) {
        this.activityAssessment = activityAssessment;
    }

    public String getActivityProcess() {
        return activityProcess;
    }

    public void setActivityProcess(String activityProcess) {
        this.activityProcess = activityProcess;
    }

    public String getActivityInstructions() {
        return activityInstructions;
    }

    public void setActivityInstructions(String activityInstructions) {
        this.activityInstructions = activityInstructions;
    }

}
