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
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.skripsi.databinding.ActivityRegisterBinding;
import com.example.skripsi.manager.RequestHandler;
import com.example.skripsi.manager.SharedPrefManager;
import com.example.skripsi.manager.URLs;
import com.example.skripsi.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class    RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    boolean passvisible =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this,HomeActivity.class));
            return;
        }

        binding.Bregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });


        binding.TVsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLoginActivity();
            }
        });

        binding.ETRpassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (binding.ETRpassword.getRight() - binding.ETRpassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        int selection= binding.ETRpassword.getSelectionEnd();
                        if(passvisible)
                        {
                            binding.ETRpassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.eye_off,0);
                            binding.ETRpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passvisible= false ;
                        }
                        else{
                            binding.ETRpassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.eye,0);
                            binding.ETRpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passvisible= true ;
                        }
                        binding.ETRpassword.setSelection(selection);
                        return true;
                    }
                }

                return false;
            }
        });
    }
//    public boolean isValidPassword(final String password) {
//
//        Pattern pattern;
//        Matcher matcher;
//
//        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
//
//        pattern = Pattern.compile(PASSWORD_PATTERN);
//        matcher = pattern.matcher(password);
//
//        return matcher.matches();
//
//    }

    private void registerUser() {

        final String name = binding.ETRName.getText().toString().trim();
        final String email = binding.ETRemail.getText().toString().trim();
        final String password = binding.ETRpassword.getText().toString().trim();
        int idGender= binding.radioGender.getCheckedRadioButtonId();
        RadioButton rB = binding.getRoot().findViewById(idGender);
        final String gender = rB.getText().toString();

        final String phone = binding.ETphone.getText().toString().trim();

        if (TextUtils.isEmpty(name)){
            binding.ETRName.setError("Please enter your name");
            binding.ETRName.requestFocus();
            return;
        }

        else if (TextUtils.isEmpty(email)){
            binding.ETRemail.setError("Please enter your email");
            binding.ETRemail.requestFocus();
            return;
        }

        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.ETRemail.setError("Enter Valid Email Address");
            binding.ETRemail.requestFocus();
            return;
        }

        else if(password.length()<8){
            binding.ETRpassword.setError("Password length must be greater than 8");
            binding.ETRpassword.requestFocus();
        }

        else if(TextUtils.isEmpty(phone)){
            binding.ETphone.setError("Please enter your phone number");
            binding.ETphone.requestFocus();
        }
        else{
            class RegisterUser extends AsyncTask<Void, Void, String> {



                @Override
                protected String doInBackground(Void... voids) {
                    //creating request handler object
                    RequestHandler requestHandler = new RequestHandler();

                    //creating request parameters
                    HashMap<String, String> params = new HashMap<>();
                    params.put("name", name);
                    params.put("email", email);
                    params.put("password", password);
                    params.put("gender", gender);
                    params.put("phone",phone);

                    //returing the response
                    return requestHandler.sendPostRequest(URLs.URL_REGISTER,params);
                }

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    //displaying the progress bar while user registers on the server
//                progressBar = (ProgressBar) findViewById(R.id.progressBar);
//                progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    //hiding the progressbar after completion
//                progressBar.setVisibility(View.GONE);

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
                            SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                            //starting the profile activity
                            finish();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));

                        } else {
                            Toast.makeText(getApplicationContext(),obj.getString("message") , Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            //executing the async task
            RegisterUser ru = new RegisterUser();
            ru.execute();

        }








    }

    private void openLoginActivity() {
        Intent intent = new Intent(this,Lactivity.class);
        startActivity(intent);
        finish();
    }
}