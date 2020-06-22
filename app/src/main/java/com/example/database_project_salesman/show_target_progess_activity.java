package com.example.database_project_salesman;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class show_target_progess_activity extends AppCompatActivity {
   // to show progress bar things
   private ProgressBar circularProgressbar_overAll,circularProgressbar_specific;
   private TextView textView_overAll,textview_specific;
   private Button show_target_button;

    //database reference
    private DatabaseReference skuReference, shopReference, orderReference;

    //splash screen relative layout
    private RelativeLayout rellay1, rally3, rellay2;

    //array adapters for the dropdown lists
    private ArrayAdapter<Sku> skuArrayAdapter;
    //dropdowns spinners
    private Spinner skuSpinner;


    //handler for the splash screen
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable()
    {
        @Override
        public void run()
        {

            rellay1.setVisibility(View.VISIBLE);
            rally3.setVisibility(View.VISIBLE);

        }
    };

    //handler for the progress bar
    private ProgressBar progressBar;
    private Handler progressBars = new Handler();
    private Runnable runnable1 = new Runnable()
    {
        @Override
        public void run()
        {
            rellay2.setVisibility(View.VISIBLE);
        }
    };

    //array lists for the array adapters
    private List<Sku> skuList;
    //to get the email id of the salesman
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_target_progess_activity);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        //initializing lists
        skuList = new ArrayList<>();
        //initializing databasee reference for downloading and uploading the data the data
        skuReference = FirebaseDatabase.getInstance().getReference("SKU");
        //synchronizing the database for the offline use
        skuReference.keepSynced(true);

        //relative layouts
        rellay1 = findViewById(R.id.target_show_layout);
        rellay2 = findViewById(R.id.show_progress_bar_LinearLayout);
        rally3 = findViewById(R.id.adding_sku_bottom_rally2);

        //splash screen
        handler.postDelayed(runnable, 500);

        //text view
        textView_overAll = findViewById(R.id.textView_overAll);
        textview_specific = findViewById(R.id.textview_specific);
        //progress bar
        circularProgressbar_overAll= findViewById(R.id.circularProgressbar_overAll);
        circularProgressbar_specific=findViewById(R.id.circularProgressbar_specific);

        //splash screen
        handler.postDelayed(runnable, 500);
        //initializing the spinners
        skuSpinner = findViewById(R.id.target_Sku_spinner);
        //setting the array adapters
        skuArrayAdapter = new ArrayAdapter(this, R.layout.spinner_text, skuList);
        skuArrayAdapter.setDropDownViewResource(R.layout.spinner_text_dropdown);
        //how Target button initialization and on click listener
        show_target_button=findViewById(R.id.show_target_button);
        show_target_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Sku skU=(Sku)skuSpinner.getSelectedItem();



                progressBars.postDelayed(runnable1,500);
            }
        });

    }
    @Override
    protected void onStart()
    {
        super.onStart();
        skuReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                skuList.clear();
                for(DataSnapshot sku:dataSnapshot.getChildren())
                {
                    skuList.add(sku.getValue(Sku.class));
                }
                skuSpinner.setAdapter(skuArrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(show_target_progess_activity.this, "Error in download ing the data", Toast.LENGTH_SHORT).show();
            }
        });




    }

    @Override
    protected void onStop()
    {
        super.onStop();

    }


}