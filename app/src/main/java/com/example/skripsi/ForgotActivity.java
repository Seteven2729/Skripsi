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

import com.example.skripsi.databinding.ActivityForgotBinding;
import com.example.skripsi.manager.RequestHandler;
import com.example.skripsi.manager.SharedPrefManager;
import com.example.skripsi.manager.URLs;
import com.example.skripsi.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ForgotActivity extends AppCompatActivity {
    private ActivityForgotBinding binding;
    boolean passvisible =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.TvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLoginActivity();
            }
        });

        binding.BTNs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPass();
            }
        });
        binding.PasswordEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (binding.PasswordEditText.getRight() - binding.PasswordEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        int selection= binding.PasswordEditText.getSelectionEnd();
                        if(passvisible)
                        {
                            binding.PasswordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.eye_off,0);
                            binding.PasswordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passvisible= false ;
                        }
                        else{
                            binding.PasswordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.eye,0);
                            binding.PasswordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passvisible= true ;
                        }
                        binding.PasswordEditText.setSelection(selection);
                        return true;
                    }
                }

                return false;
            }
        });
    }

    private void resetPass() {
            final String email = binding.EmaiEditText.getText().toString().trim();
            final String phone = binding.editTextPhone.getText().toString().trim();
            final String password = binding.PasswordEditText.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            binding.EmaiEditText.setError("Please enter your email");
            binding.EmaiEditText.requestFocus();
            return;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.EmaiEditText.setError("Invalid Email Address");
            binding.EmaiEditText.requestFocus();
            return;
        }
        else if(TextUtils.isEmpty(password)){
            binding.PasswordEditText.setError("Please enter your password");
            binding.PasswordEditText.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(phone)){
            binding.editTextPhone.setError("Please enter your phone number");
            binding.editTextPhone.requestFocus();
            return;
        }
        else{
            class ResetPass extends AsyncTask<Void, Void, String>{
                @Override
                protected String doInBackground(Void... voids) {
                    //creating request handler object
                    RequestHandler requestHandler = new RequestHandler();

                    //creating request parameters
                    HashMap<String, String> params = new HashMap<>();
                    params.put("email", email);
                    params.put("password", password);
                    params.put("phone",phone);

                    //returing the response
                    return requestHandler.sendPostRequest(URLs.URL_FORGOT,params);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    try {
                        //converting response to json object
                        JSONObject obj = new JSONObject(s);

                        //if no error in response
                        if (!obj.getBoolean("error")) {
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                            //getting the user from the response
                            JSONObject userJson = obj.getJSONObject("user");

                            //creating a new user object
                            User user = new User(
                                    userJson.getInt("id"),
                                    userJson.getString("name"),
                                    userJson.getString("email"),
                                    userJson.getString("gender"),
                                    userJson.getString("phone")
                            );

                            //storing the user in shared preferences
                           // SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                            //starting the profile activity
                            finish();
                            startActivity(new Intent(getApplicationContext(), Lactivity.class));

                        } else {
                            Toast.makeText(getApplicationContext(),obj.getString("message") , Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            ResetPass rp = new ResetPass();
            rp.execute();

        }
    }

    private void openLoginActivity() {
        Intent intent = new Intent(this,Lactivity.class);
        startActivity(intent);
        finish();
    }
}