package com.example.skripsi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;

import com.example.skripsi.databinding.ActivityLactivityBinding;
import com.example.skripsi.manager.SharedPrefManager;

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
    }

    private void openRegisterActivity() {
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
        finish();
    }
}