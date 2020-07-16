package com.example.database_project_salesman.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.database_project_salesman.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity
{


    private FirebaseAuth mAuth;
    private RelativeLayout rellay1,rally2,rellay2;
    private TextView bottomTextView;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            rellay1.setVisibility(View.VISIBLE);
            rally2.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);

        }
    };
    private ProgressBar progressBar;
    private Handler progressBarh=new Handler();
    private Runnable runnable1=new Runnable()
    {
        @Override
        public void run()
        {

            progressBar.setVisibility(View.GONE);
        }
    };
    private EditText usernameTf,passeordTf;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rellay1 = findViewById(R.id.rellay1);
        rally2=findViewById(R.id.bottom_rally2);
        rellay2=findViewById(R.id.rellay2);

        handler.postDelayed(runnable, 2000); //2000 is the timeout for the splash

        bottomTextView=findViewById(R.id.message_text_view);

        usernameTf=findViewById(R.id.username_tf);
        passeordTf=findViewById(R.id.password_tf);

        progressBar=findViewById(R.id.my_progress_bar);
        progressBarh.postDelayed(runnable1,100);

        mAuth = FirebaseAuth.getInstance();

        /*if(mAuth.currentUser == null){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            Toast.makeText(this, "Already logged in", Toast.LENGTH_LONG).show()
        }*/
    }

    public void loginButton(View v)
    { if (getCurrentFocus() != null) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }
        progressBar.setVisibility(View.VISIBLE);
        if(usernameTf.getText().toString().trim().length()==0)
        {
            usernameTf.setError("Enter an email");
            progressBarh.postDelayed(runnable1,100);
            return;
        }
        if(passeordTf.getText().toString().trim().length()==0)
        {
            passeordTf.setError("Enter passeord");
            progressBarh.postDelayed(runnable1,100);
            return;
        }

        String username,passsword;
        username=usernameTf.getText().toString().trim();
        passsword=passeordTf.getText().toString().trim();
        signIn(username,passsword);
    }

    public void signIn(String email,String password)
    {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent intent=new Intent(MainActivity.this, salesname_main_dashboard.class);
                            progressBarh.postDelayed(runnable1,100);
                            startActivity(intent);



                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            progressBarh.postDelayed(runnable1,100);

                        }
///

                    }
                });
    }

    @Override
    public void onBackPressed() {

    }
}
