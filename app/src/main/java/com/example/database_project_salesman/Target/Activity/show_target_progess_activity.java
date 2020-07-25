package com.example.database_project_salesman.Target.Activity;

import android.content.SharedPreferences;
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
    private DatabaseReference skuReference;

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
    private DatabaseReference  targetReference, targetSalesmanreference;
    //array lists for the array adapters
    private List<Sku> skuList;
    List<Target> target_list_from_dataBase;
    List<Target> target_list_filter;

    List<Target_SalesMen> target_salesMan_list_from_dataBase;
    List<Target_SalesMen> target_salesMan_list_filter;


    //to get the email id of the salesman
    private FirebaseAuth auth;
    private FirebaseUser user;
    SharedPreferences prefreences ;
    String salesmanEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_target_progess_activity);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        prefreences= getSharedPreferences(getResources().getString(R.string.SharedPreferences_FileName),MODE_PRIVATE);
        salesmanEmail=prefreences.getString(getString(R.string.SharedPreferences_SALESMEN),"");
        //initializing lists
        skuList = new ArrayList<>();
        target_list_from_dataBase=new ArrayList<>();
        target_list_filter=new ArrayList<>();
        target_salesMan_list_filter=new ArrayList<>();
        target_salesMan_list_from_dataBase=new ArrayList<>();
        //initializing databasee reference for downloading and uploading the data the data
        skuReference = FirebaseDatabase.getInstance().getReference("SKU");
        targetReference= FirebaseDatabase.getInstance().getReference("TARGET");
        targetSalesmanreference=FirebaseDatabase.getInstance().getReference("TargetSalesMan");
        //synchronizing the database for the offline use
        skuReference.keepSynced(true);
        targetSalesmanreference.keepSynced(true);
        targetReference.keepSynced(true);
        //relative layouts
        rellay1 = findViewById(R.id.target_show_layout);
        rellay2 = findViewById(R.id.show_progress_bar_LinearLayout);
        rally3 = findViewById(R.id.adding_sku_bottom_rally2);

        //splash screen
        handler.postDelayed(runnable, 500);

        //text view
        textView_overAll = findViewById(R.id.textView_overAll);

        //progress bar
        circularProgressbar_overAll= findViewById(R.id.circularProgressbar_overAll);


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
                String skuID= skuSelected.getId();
                for(int t=0; t<target_list_from_dataBase.size();t++)
                {
                    if(target_list_from_dataBase.get(t).getSkuID().equals(skuID) &&
                            target_list_from_dataBase.get(t).getSalesmenEmail().equals(salesmanEmail))
                    {
                        target_list_filter.add(target_list_from_dataBase.get(t));
                    }
                }
                for (int ts=0; ts<target_salesMan_list_from_dataBase.size(); ts++)
                {
                    if(target_salesMan_list_from_dataBase.get(ts).getSKU_ID().equals(skuID)
                        && target_salesMan_list_from_dataBase.get(ts).getSaleMenEmail().equals(salesmanEmail))
                    {
                        target_salesMan_list_filter.add(target_salesMan_list_from_dataBase.get(ts));
                    }
                }

        if(target_list_filter.isEmpty())
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
        {int progres = 0;
            for(int pts=0;pts<target_salesMan_list_filter.size();pts++)
            {
                progres+=target_salesMan_list_filter.get(pts).getAchieved();
            }
            int total=0;
            for(int tt=0;tt<target_list_filter.size();tt++)
            {
                total+=target_list_filter.get(tt).getTARGET();
            }
            double  actualPercentage;
            actualPercentage=(double) progres/(double) total;
            actualPercentage=actualPercentage*100;
            int actual=(int)Math.round(actualPercentage);
            circularProgressbar_overAll.setProgress(actual);
            textView_overAll.setText(actual+"%");
            progressBars.postDelayed(runnable1,500);
            return;

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
        targetReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                target_list_from_dataBase.clear();
                for (DataSnapshot target : dataSnapshot.getChildren())
                {
                    if(target.getValue(Target.class).getTargetStatus().equals("Active")
                            && target.getValue(Target.class).getSalesmenEmail().equals(salesmanEmail))
                    {
                        target_list_from_dataBase.add(target.getValue(Target.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        targetSalesmanreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                target_salesMan_list_from_dataBase.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getValue(Target_SalesMen.class).getStatus().equals("Active")
                    && snapshot.getValue(Target_SalesMen.class).getSaleMenEmail().equals(salesmanEmail)) {
                        target_salesMan_list_from_dataBase.add(snapshot.getValue(Target_SalesMen.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void onStop()
    {
        super.onStop();

    }


}