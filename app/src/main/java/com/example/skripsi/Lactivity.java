package com.example.skripsi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.skripsi.databinding.ActivityLactivityBinding;
import com.example.skripsi.manager.RequestHandler;
import com.example.skripsi.manager.SharedPrefManager;
import com.example.skripsi.manager.URLs;
import com.example.skripsi.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Lactivity extends AppCompatActivity {

    private ActivityLactivityBinding binding;
    boolean passvisible =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this,HomeActivity.class));
            return;
        }

        binding.Bsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });

        binding.RegisterTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegisterActivity();
            }
        });

        binding.ETpassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (binding.ETpassword.getRight() - binding.ETpassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                            int selection= binding.ETpassword.getSelectionEnd();
                            if(passvisible)
                            {
                                binding.ETpassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.eye_off,0);
                                binding.ETpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                passvisible= false ;
                            }
                            else{
                                binding.ETpassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.eye,0);
                                binding.ETpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                passvisible= true ;
                            }
                            binding.ETpassword.setSelection(selection);
                            return true;
                    }
                }

                        return false;
            }
        });

        binding.TvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openForgotActivity();
            }
        });
    }

    private void openForgotActivity() {
        Intent intent = new Intent(this,ForgotActivity.class);
        startActivity(intent);
        finish();
    }

    private void userLogin() {
        final String email = binding.ETemail.getText().toString();
        final String password = binding.ETpassword.getText().toString();

        if(TextUtils.isEmpty(email)){
            binding.ETemail.setError("Please enter your email");
            binding.ETemail.requestFocus();
            return;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.ETemail.setError("Invalid Email Address");
            binding.ETemail.requestFocus();
            return;
        }
        else if(TextUtils.isEmpty(password)){
            binding.ETpassword.setError("Please enter your password");
            binding.ETpassword.requestFocus();
            return;
        }

        class UserLogin extends AsyncTask<Void,Void,String>{

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);
                    //if no error in response
                    if(!obj.getBoolean("error")){
                        Toast.makeText(getApplicationContext(),obj.getString("message"),Toast.LENGTH_SHORT).show();
                        //getting the user from the response
                        JSONObject userJson = obj.getJSONObject("user");
                        //creating a new user object
                        User user  = new User(
                                userJson.getInt("id"),
                                userJson.getString("name"),
                                userJson.getString("email"),
                                userJson.getString("gender"),
                                userJson.getString("phone")
                        );

                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                        //starting the home activity
                        finish();
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_LOGIN, params);
            }
        }
        UserLogin ul = new UserLogin();
        ul.execute();

    }

    private void openRegisterActivity() {
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
        finish();
    }
}