package dev.minhtuan07.chatapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import dev.minhtuan07.chatapp.R;
import dev.minhtuan07.chatapp.databinding.ActivitySignInBinding;
import dev.minhtuan07.chatapp.untilities.Constants;
import dev.minhtuan07.chatapp.untilities.PreferenceManager;

public class SignInActivity extends AppCompatActivity {
    private ActivitySignInBinding binding;
    private PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager =new PreferenceManager(getApplicationContext());
        if(preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)){ //Kiểm tra đã đăng nhập chưa
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }
        binding=ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
    }

    private void setListeners(){
        binding.textCreateNewAccount.setOnClickListener(v->
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class)));
        ((Button)findViewById(R.id.buttonSignIn)).setOnClickListener(v->{
            if(isValidSignInDetails()){ //Nếu form nhập hợp lệ thì tiến hành đăng nhập
                signIn();
            }


        });
    }
    private void signIn(){
        loading(true);
        String email =  ((EditText)findViewById(R.id.inputEmail)).getText().toString();
        String password = ((EditText)findViewById(R.id.inputPassword)).getText().toString();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        Log.w("SignIn","Đúng đến đây rồi");
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_EMAIL, email)
                .whereEqualTo(Constants.KEY_PASSWORD, password)
                .get()
                .addOnCompleteListener(task -> {

                    if(task.isSuccessful()  &&  task.getResult() != null
                     && task.getResult().getDocuments().size() >0){
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN,true);
                        preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                        preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
                        preferenceManager.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    else {
                        loading(false);
                        showToast("Unable to sign in !");
                    }
                });
    }
    private  void showToast(String message){
        Toast.makeText(getApplicationContext(), message,Toast.LENGTH_LONG).show();
    }
    private Boolean isValidSignInDetails(){
        if(( (EditText) findViewById(R.id.inputEmail)).getText().toString().isEmpty()){
            showToast("Enter email");
            return false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(((EditText) findViewById(R.id.inputEmail)).getText().toString()).matches()) {
            showToast("Enter valid email");
            return false;
        }else if (((EditText) findViewById(R.id.inputPassword)).getText().toString().trim().isEmpty()) {
            showToast("Enter password");
            return false;
        }else return true;
    }
    private void loading(Boolean isLoading){
        if(isLoading){
            findViewById(R.id.buttonSignIn).setVisibility(View.INVISIBLE);
            findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        }else {
            findViewById(R.id.buttonSignIn).setVisibility(View.VISIBLE);
            findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
        }
    }

}