package com.example.database_project_salesman.Target.Activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
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

import com.example.database_project_salesman.R;
import com.example.database_project_salesman.SKU.Sku;
import com.example.database_project_salesman.Target.Enity.Target;
import com.example.database_project_salesman.Target.Enity.Target_SalesMen;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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
    private List<Target_SalesMen> overAll_Selected_sku_progress;
    private List<Target> selected_sku_list;
    private List<Target_SalesMen> Target_saleMen_list;
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
        overAll_Selected_sku_progress=new ArrayList<>();
        selected_sku_list=new ArrayList<>();
        Target_saleMen_list=new ArrayList<>();
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

                Sku skuSelected =(Sku)skuSpinner.getSelectedItem();
                Query q =FirebaseDatabase.getInstance().getReference("TARGET").orderByChild("SKU_ID").equalTo(skuSelected.getId());
                q.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        selected_sku_list.clear();
                        for(DataSnapshot targets:dataSnapshot.getChildren()  )
                        {
                            selected_sku_list.add(targets.getValue(Target.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Query q1 =FirebaseDatabase.getInstance().getReference("TARGET").orderByChild("SKU_ID");
                q1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        overAll_Selected_sku_progress.clear();
                        for(DataSnapshot targets:dataSnapshot.getChildren()  )
                        {
                            overAll_Selected_sku_progress.add(targets.getValue(Target_SalesMen.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Query q2 =FirebaseDatabase.getInstance().getReference("Target_SalesMen").orderByChild("SKU_ID").equalTo(skuSelected.getId());
                q2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Target_saleMen_list.clear();
                        for(DataSnapshot targets:dataSnapshot.getChildren()  )
                        {
                            Target_saleMen_list.add(targets.getValue(Target_SalesMen.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        if(overAll_Selected_sku_progress.isEmpty()&&Target_saleMen_list.isEmpty())
        {

            View contextView = findViewById(android.R.id.content);
            Snackbar snackbar = Snackbar.make(contextView,"Target for Selected Sku is not set by admin Yet",Snackbar.LENGTH_LONG);

            View snackbarView = snackbar.getView();
            TextView snackbarText = (TextView) snackbarView.findViewById(R.id.snackbar_text);
            snackbarText.setTextColor(Color.RED);
            snackbarText.setGravity(Gravity.CENTER_HORIZONTAL);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                snackbarText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            else
                snackbarText.setGravity(Gravity.CENTER_HORIZONTAL);
            snackbar.show();
        }
        else
        {
            int progres = 0;
            for (int i=0; i<Target_saleMen_list.size(); i++) {
                progres += Target_saleMen_list.get(i).getAchieved();
            }
            int total=0;
            for (int i=0; i<selected_sku_list.size(); i++) {
                total += selected_sku_list.get(i).getTARGET();
            }
            int  actualPercentage=(progres/total)*100;
            circularProgressbar_specific.setProgress(actualPercentage);
            textview_specific.setText(actualPercentage);
            int overall_progress=0;
            for (int i=0; i<overAll_Selected_sku_progress.size(); i++) {
                overall_progress += overAll_Selected_sku_progress.get(i).getAchieved();
            }
            int target=0;
            target=selected_sku_list.get(0).getTARGET();
            int actual_overall_progress=(overall_progress/target)*100;
            circularProgressbar_overAll.setProgress(actual_overall_progress);
            textView_overAll.setText(actual_overall_progress);
            progressBars.postDelayed(runnable1,500);
        }


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