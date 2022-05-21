package com.example.skripsi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.skripsi.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.textViewButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLoginActivity();
            }
        });

        SharedPreferences preferences = getSharedPreferences("PREFERENCE",MODE_PRIVATE);
        String firsttime = preferences.getString("FirstTimeInstall","");

        if(firsttime.equals("Yes")){
            openLoginActivity();
        }
        else{
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("FirstTimeInstall","Yes");
            editor.apply();
        }



    }

    private void openLoginActivity() {

        Intent intent = new Intent(this,Lactivity.class);
        startActivity(intent);
        finish();
    }


}