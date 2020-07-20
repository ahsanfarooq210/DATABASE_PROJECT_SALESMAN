package com.example.database_project_salesman.ProfileActivities.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.database_project_salesman.Activities.salesname_main_dashboard;
import com.example.database_project_salesman.ProfileActivities.Entity.ProfileData;
import com.example.database_project_salesman.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class activity_View_Profile extends AppCompatActivity {

    EditText name_tf_view_profile,cnic_tf_view_profile,email_tf_view_profile,dob_tf_view_profile,contact_number_tf_view_profile,education_tf_view_profile;

    Button ok_button,edit_Profile_button;
    //splash screen relative layout
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
    String profileDataIntent="",email;
    //profileData
    //database reference
    private DatabaseReference profileDataReference;
    //array lists for the array adapters
    private List<ProfileData> profileDataList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__view__profile);
        Intent recIntent=getIntent();
        email=recIntent.getStringExtra("VIEW_EMAIL");

        name_tf_view_profile=findViewById(R.id.name_tf_view_profile);
        cnic_tf_view_profile=findViewById(R.id.cnic_tf_view_profile);
        email_tf_view_profile=findViewById(R.id.email_tf_view_profile);
        dob_tf_view_profile=findViewById(R.id.dob_tf_view_profile);
        contact_number_tf_view_profile=findViewById(R.id.contact_number_tf_view_profile);
        education_tf_view_profile=findViewById(R.id.education_tf_view_profile);

        //initializing databasee reference for downloading and uploading the data the data
        profileDataReference = FirebaseDatabase.getInstance().getReference("ProfileData");
        profileDataReference.keepSynced(true);
        profileDataList=new ArrayList<>();

        //relative layouts
        rellay1 = findViewById(R.id.adding_view_rellay1);
        rellay2 = findViewById(R.id.adding_view_rellay2);
        rally3 = findViewById(R.id.adding_view_bottom_rally2);

        //splash screen
        handler.postDelayed(runnable, 500);

        //progress bar
        progressBar = findViewById(R.id.my_progress_bar);
        progressBarh.postDelayed(runnable1, 0);
        ok_button=findViewById(R.id.ok_button);
        edit_Profile_button=findViewById(R.id.edit_Profile_button);
         ok_button.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                Intent send =new Intent(activity_View_Profile.this, salesname_main_dashboard.class);
                startActivity(send);
             }
         });
         edit_Profile_button.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent send =new Intent(activity_View_Profile.this, activity_Edit_Profile.class);
                 startActivity(send);
             }
         });
    }

    @Override
    protected void onStart() {
        super.onStart();
        profileDataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                profileDataList.clear();
                for (DataSnapshot profile : snapshot.getChildren())
                {
                    if(profile.getValue(ProfileData.class).getEmail().equals(email))
                    {
                        profileDataList.add(profile.getValue(ProfileData.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}