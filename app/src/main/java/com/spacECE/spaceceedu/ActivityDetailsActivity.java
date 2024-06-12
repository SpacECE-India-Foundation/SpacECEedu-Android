package com.spacECE.spaceceedu;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityDetailsActivity extends AppCompatActivity {

    TextView textViewStatus,textViewId,textViewLevel,textViewdevDomain,textViewObjectives,textViewKeyDev,
            textViewMaterial,textViewAssesment,textViewProcess,textViewInstruction,
            textViewResult,textViewActivityHeader,textViewName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setTitle("Daily Activities");
        bindComponents();
        displayDetails();

    }
    private void bindComponents(){
        this.textViewActivityHeader = findViewById(R.id.text_actvity_details_head);
        this.textViewId = findViewById(R.id.act_details_no);
        this.textViewStatus = findViewById(R.id.act_details_objective);
        this.textViewName = findViewById(R.id.act_details_title);
        this.textViewResult = findViewById(R.id.act_details_key_dev);
        this.textViewLevel = findViewById(R.id.act_details_level);
        this.textViewdevDomain = findViewById(R.id.act_details_domain);
        this.textViewObjectives = findViewById(R.id.act_details_objective);
        this.textViewKeyDev = findViewById(R.id.act_details_key_dev);
        this.textViewMaterial = findViewById(R.id.act_details_material);
        this.textViewAssesment = findViewById(R.id.act_details_assesment);
        this.textViewProcess = findViewById(R.id.act_details_process);
        this.textViewInstruction = findViewById(R.id.act_details_instruction);
        this.textViewResult = findViewById(R.id.act_details_result);
    }

    private void displayDetails(){

        ActivityData activityData = (ActivityData) getIntent().getSerializableExtra("EXTRA_ACTIVITY");
        this.textViewActivityHeader.setText("Activity "+activityData.getData().get(0).getActivityNo());
        this.textViewStatus.setText(activityData.getStatus());
        this.textViewId.setText(activityData.getData().get(0).getActivityNo());
        this.textViewLevel.setText(activityData.getData().get(0).getActivityLevel());
        this.textViewdevDomain.setText(activityData.getData().get(0).getActivityDevDomain());
        this.textViewObjectives.setText(activityData.getData().get(0).getActivityObjectives());
        this.textViewKeyDev.setText(activityData.getData().get(0).getActivityKeyDev());
        this.textViewName.setText(activityData.getData().get(0).getActivityName());
        this.textViewMaterial.setText(activityData.getData().get(0).getActivityMaterial());
        this.textViewAssesment.setText(activityData.getData().get(0).getActivityAssessment());
        this.textViewProcess.setText(activityData.getData().get(0).getActivityProcess());
        this.textViewInstruction.setText(activityData.getData().get(0).getActivityInstructions());
        this.textViewResult.setText(activityData.getResult());

    }
}