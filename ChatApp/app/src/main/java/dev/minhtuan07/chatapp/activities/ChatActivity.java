package dev.minhtuan07.chatapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import dev.minhtuan07.chatapp.R;
import dev.minhtuan07.chatapp.databinding.ActivityChatBinding;
import dev.minhtuan07.chatapp.models.User;
import dev.minhtuan07.chatapp.untilities.Constants;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding binding;
    private User receiverUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        loadReceiverDetails();
    }
    private void loadReceiverDetails(){
        receiverUser =(User) getIntent().getSerializableExtra(Constants.KEY_USER);
        binding.textName.setText(receiverUser.name);
    }
    private  void setListeners(){
        binding.imageBack.setOnClickListener(v->onBackPressed());
    }
}