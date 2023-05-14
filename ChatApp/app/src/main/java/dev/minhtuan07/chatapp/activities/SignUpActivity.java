package dev.minhtuan07.chatapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import dev.minhtuan07.chatapp.R;
import dev.minhtuan07.chatapp.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_sign_up);
        setListeners();
    }

    private void setListeners(){
        binding.textSignIn.setOnClickListener(v-> onBackPressed());
    }
}