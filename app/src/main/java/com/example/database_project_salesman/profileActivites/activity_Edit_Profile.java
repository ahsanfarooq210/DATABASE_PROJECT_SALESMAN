package com.example.database_project_salesman.profileActivites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.database_project_salesman.R;

public class activity_Edit_Profile extends AppCompatActivity {
    EditText Name_EDITTEXT,Cnic_EDITTEXT,Email_EDITTEXT,dob_EDITTEXT,cell_no_EDITTEXT,education_EDITTEXT; //splash screen relative layout
    private RelativeLayout rellay1, rally3, rellay2;
    //handler for the splash screen
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable()
    {
        @Override
        public void run()
        {

            rellay1.setVisibility(View.VISIBLE);
            rally3.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);

        }
    };

    //handler for the progress bar
    private ProgressBar progressBar;
    private Handler progressBarh = new Handler();
    private Runnable runnable1 = new Runnable()
    {
        @Override
        public void run()
        {

            progressBar.setVisibility(View.GONE);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__edit__profile);
        Intent recIntent=getIntent();
        Name_EDITTEXT=findViewById(R.id.Name_EDITTEXT);
        Cnic_EDITTEXT=findViewById(R.id.Cnic_EDITTEXT);
        Email_EDITTEXT=findViewById(R.id.Email_EDITTEXT);
        dob_EDITTEXT=findViewById(R.id.dob_EDITTEXT);
        cell_no_EDITTEXT=findViewById(R.id.cell_no_EDITTEXT);
        education_EDITTEXT=findViewById(R.id.education_EDITTEXT);


        Name_EDITTEXT.setText("Hazeem Hassan ");

        Cnic_EDITTEXT.setText("33100-7915978-9");

        Email_EDITTEXT.setText("hhazimhassan@gmail.com");

        dob_EDITTEXT.setText("18/10/1997");

        education_EDITTEXT.setText("BSCS");
        //relative layouts
        rellay1 = findViewById(R.id.adding_sku_rellay1);
        rellay2 = findViewById(R.id.adding_sku_rellay2);
        rally3 = findViewById(R.id.adding_sku_bottom_rally2);

        //splash screen
        handler.postDelayed(runnable, 500);

        //progress bar
        progressBar = findViewById(R.id.my_progress_bar);
        progressBarh.postDelayed(runnable1, 0);
    }
}