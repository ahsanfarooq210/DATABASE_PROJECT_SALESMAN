package com.example.database_project_salesman.Activities;

import android.app.Application;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class Home extends Application
{
    public static boolean flag;
    @Override
    public void onCreate()
    {

        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        FirebaseAuth firebaseAuth =FirebaseAuth.getInstance();
        FirebaseUser firebaseUser =firebaseAuth.getCurrentUser();

        if(firebaseUser!=null)
        {
            flag=true;
            Intent intent=new Intent(Home.this, salesname_main_dashboard.class);

            startActivity(intent);
        }
        else
        {
            flag=false;
            Intent intent=new Intent(Home.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
