package com.example.skripsi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.skripsi.databinding.ActivityHomeBinding;
import com.example.skripsi.manager.SharedPrefManager;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.Blogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                SharedPrefManager.getInstance(getApplicationContext()).logout();
            }
        });
    }
}