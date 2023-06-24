package dev.minhtuan07.chatapp.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.unusedapprestrictions.IUnusedAppRestrictionsBackportCallback;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.regex.Pattern;

import dev.minhtuan07.chatapp.R;
import dev.minhtuan07.chatapp.databinding.ActivitySignUpBinding;
import dev.minhtuan07.chatapp.untilities.Constants;
import dev.minhtuan07.chatapp.untilities.PreferenceManager;

public class SignUpActivity extends AppCompatActivity {
    private TextView txtSignIn;
    private Button buttonSignUp;
    private  FrameLayout layoutImage;
    private ActivitySignUpBinding binding;
    private PreferenceManager preferenceManager;
    private String encodedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_sign_up);
        preferenceManager = new PreferenceManager(getApplicationContext());
        txtSignIn = findViewById(R.id.textSignIn);
        txtSignIn.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                                             startActivity(intent);
                                             ;
                                         }
                                     }
        );
        buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidSignUpDetails()) signUp();
            }
        });

        layoutImage = findViewById(R.id.layoutImage);
        layoutImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                pickImage.launch(intent);

            }
        });


    }



    private  void showToast(String message){
        Toast.makeText(getApplicationContext(), message,Toast.LENGTH_SHORT).show();
    }
    private void signUp() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        String email = ((EditText) findViewById(R.id.inputEmail)).getText().toString();

        // Kiểm tra email đã tồn tại trong Firestore hay chưa
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_EMAIL, email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (!querySnapshot.isEmpty()) {
                            // Email đã được sử dụng, hiển thị thông báo lỗi
                            showToast("Email already exists");
                            loading(false);
                        } else {
                            // Email chưa được sử dụng, thêm vào Firestore
                            HashMap<String, Object> user = new HashMap<>();
                            user.put(Constants.KEY_NAME, ((EditText) findViewById(R.id.inputName)).getText().toString());
                            user.put(Constants.KEY_EMAIL, email);
                            user.put(Constants.KEY_PASSWORD, ((EditText) findViewById(R.id.inputPassword)).getText().toString());
                            user.put(Constants.KEY_IMAGE, encodedImage);
                            database.collection(Constants.KEY_COLLECTION_USERS)
                                    .add(user)
                                    .addOnSuccessListener(documentReference -> {
                                        loading(false);
                                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                                        preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
                                        preferenceManager.putString(Constants.KEY_NAME, ((EditText) findViewById(R.id.inputName)).getText().toString());
                                        preferenceManager.putString(Constants.KEY_IMAGE, encodedImage);
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    })
                                    .addOnFailureListener(exception -> {
                                        loading(false);
                                        showToast(exception.getMessage());
                                    });
                        }
                    } else {
                        loading(false);
                        showToast(task.getException().getMessage());
                    }
                });
    }


    private String encodeImage(Bitmap bitmap){
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight()* previewWidth/bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap,previewWidth,previewHeight,false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
        byte[] bytes=byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes,Base64.DEFAULT);
    }
    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == RESULT_OK ){
                    Uri imageUri = result.getData().getData();
                    try{
                        RoundedImageView imageProfile = findViewById(R.id.imageProfile);
                        InputStream inputStream = getContentResolver().openInputStream(imageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        imageProfile.setImageBitmap(bitmap);//hien thi anh profile
                        findViewById(R.id.textAddImage).setVisibility(View.GONE);
                        encodedImage = encodeImage(bitmap);
                        Log.w("ERROR","set image: "+encodedImage);
                    }
                    catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
            }
    );
    private  Boolean isValidSignUpDetails(){
        if(encodedImage == null){
            showToast("Select profile image");
            return false;
        }
        else if( ((EditText) findViewById(R.id.inputName)).getText().toString().trim().isEmpty()){
            showToast("Empty name");
            Log.d("Lỗi gì đó","..."+binding.inputName.getText().toString());
            return false;
        }
        else if(((EditText) findViewById(R.id.inputEmail)).getText().toString().trim().isEmpty()){
            showToast("Empty email");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(((EditText) findViewById(R.id.inputEmail)).getText().toString()).matches()) {
            showToast("Enter valid email");
            return false;
            } else if (((EditText) findViewById(R.id.inputPassword)).getText().toString().trim().isEmpty()) {
            showToast("Enter password");
            return false;
        }else if (((EditText) findViewById(R.id.inputConfirmPassword)).getText().toString().trim().isEmpty()) {
            showToast("Confirm your password");
            return false;
        } else if (!((EditText) findViewById(R.id.inputPassword)).getText().toString()
                .equals(((EditText) findViewById(R.id.inputConfirmPassword)).getText().toString())) {
        showToast("Password and Confirm password must be same");
        return false;
        }

        else return true;
    }
    private void loading(Boolean isLoading){
        if(isLoading){
            findViewById(R.id.buttonSignUp).setVisibility(View.INVISIBLE);
            findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        }else {
            findViewById(R.id.buttonSignUp).setVisibility(View.VISIBLE);
            findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
        }
    }
}


