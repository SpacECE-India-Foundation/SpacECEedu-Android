package com.spacECE.spaceceedu.Authentication;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import com.spacECE.spaceceedu.R;

import static java.lang.String.*;

public class ConsultantRegistrationInit extends AppCompatActivity {

    Toolbar toolbar;
    Spinner Type;
    Spinner Language;
    EditText Address;
    EditText Fee;
    EditText Qualification;
    EditText StartTime;
    EditText EndTime;
    ToggleButton Mon,Tue,Wed,Thu,Fri,Sat,Sun;

    String TYPE, LANGUAGE, ADDRESS, FEE,
            QUALIFICATION, START_TIME, END_TIME;


    //9 ME SE 7 HA


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultant_registration);

        Button b_register=findViewById(R.id.ConsultantRegistration_Button_Signup);

        Type = findViewById(R.id.Type);
        Language = findViewById(R.id.Consultant_Language);
        Address = findViewById(R.id.Consultant_Address);
        Fee = findViewById(R.id.Consultant_Fee);
        Qualification = findViewById(R.id.Consultant_Qualification);
        StartTime = findViewById(R.id.TimeStart);
        EndTime = findViewById(R.id.TimeEnd);
        Mon = findViewById(R.id.Mon);
        Tue = findViewById(R.id.Tue);
        Wed = findViewById(R.id.Wed);
        Thu = findViewById(R.id.Thu);
        Fri = findViewById(R.id.Fri);
        Sat = findViewById(R.id.Sat);
        Sun = findViewById(R.id.Sun);

        b_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateAll()) {
                    String selectedDays = getSelectedDays();
                    Intent intent = new Intent(ConsultantRegistrationInit.this, RegistrationFinal.class);
                    intent.putExtra("Type", TYPE);
                    intent.putExtra("Language", LANGUAGE);
                    intent.putExtra("Address", ADDRESS);
                    intent.putExtra("Fee", FEE);
                    intent.putExtra("Qualification", QUALIFICATION);
                    intent.putExtra("StartTime", START_TIME);
                    intent.putExtra("EndTime", END_TIME);
                    intent.putExtra("c_available_days", selectedDays);
                    startActivity(intent);
                } else {
                    Toast.makeText(ConsultantRegistrationInit.this, "Please Check Details!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        StartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(ConsultantRegistrationInit.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                START_TIME = format("%02d:%02d", hourOfDay, minute);
                                StartTime.setText(START_TIME);
                            }
                        }, 12, 0, false);
                timePickerDialog.show();
            }
        });

        EndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(ConsultantRegistrationInit.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                END_TIME = format("%02d:%02d", hourOfDay, minute);
                                EndTime.setText(END_TIME);
                            }
                        }, 12, 0, false);
                timePickerDialog.show();
            }
        });

        String[] type = {"Paediatrician", "Psychiatrist",
                "Physical Health", "Mental Health", "Nutritionist"};
        ArrayAdapter<String> typeAd = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, type);
        Type.setAdapter(typeAd);

        String[] language = {"Hindi", "English", "Marathi"};
        ArrayAdapter<String> languageAd = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, language);
        Language.setAdapter(languageAd);


        Type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TYPE = String.valueOf(position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(ConsultantRegistrationInit.this,
                        "Lol", Toast.LENGTH_SHORT).show();
            }
        });


        Language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LANGUAGE = language[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(ConsultantRegistrationInit.this,
                        "Lol", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean validateAll() {
        return getAddress() && getEndTime() && getFee()
                && getStartTime() && getQualification() ;
    }

    private boolean getStartTime() {
        if(START_TIME.isEmpty())
            return false;
        else return true;
    }

    private boolean getEndTime() {
        if(END_TIME.isEmpty())
            return false;
        else return true;
    }

    private boolean getAddress() {
        ADDRESS = Address.getText().toString().trim();
        if(ADDRESS.isEmpty())
            return false;
        else return true;
    }

    private boolean getFee() {
        FEE = Fee.getText().toString().trim();
        if(FEE.isEmpty())
            return false;
        else {
            try {
                Integer.parseInt(FEE);
                return true;
            } catch(NumberFormatException e){
                return false;
            }
        }
    }

    private boolean getQualification() {
        QUALIFICATION = Qualification.getText().toString().trim();
        if(QUALIFICATION.isEmpty())
            return false;
        else return true;
    }

    private String getSelectedDays() {
        StringBuilder selectedDays = new StringBuilder();
        if (Mon.isChecked()) selectedDays.append("Monday,");
        if (Tue.isChecked()) selectedDays.append("Tuesday,");
        if (Wed.isChecked()) selectedDays.append("Wednesday,");
        if (Thu.isChecked()) selectedDays.append("Thursday,");
        if (Fri.isChecked()) selectedDays.append("Friday,");
        if (Sat.isChecked()) selectedDays.append("Saturday,");
        if (Sun.isChecked()) selectedDays.append("Sunday,");
        if (selectedDays.length() > 0) {
            selectedDays.deleteCharAt(selectedDays.length() - 1); // Remove the last comma
        }
        return selectedDays.toString();
    }


}