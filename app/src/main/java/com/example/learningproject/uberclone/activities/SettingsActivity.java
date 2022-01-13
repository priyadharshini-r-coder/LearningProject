package com.example.learningproject.uberclone.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.example.learningproject.R;
import com.example.learningproject.databinding.ActivitySettingsBinding;
import com.example.learningproject.uberclone.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding binding;
    private String role;
    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;

    private StorageReference UserProfileImageRef;
    String currentUserID;
    private  String myUrl="";
    private Uri uri;
    private StorageTask task;
    final static int Gallery_Pick = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        UsersRef= FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        Bundle bundle=getIntent().getExtras();
        role= bundle.getString(Constants.IntentRole.role,null);
        setOnclicks();

    }

    private void setOnclicks() {
        binding.CloseButton.setOnClickListener(view -> {
            if( role!=null && role.equals("Drivers")){
              Intent intent =new Intent(SettingsActivity.this,DriverMapsActivity.class);
              startActivity(intent);
            }
            else{
                Intent intent =new Intent(SettingsActivity.this,CustomerMapsActivity.class);
                startActivity(intent);
            }
        });

        binding.SuccessButton.setOnClickListener(view -> {

        });

        binding.ProfilePictures.setOnClickListener(view -> {


//            CropImage.activity()
//                    .setGuidelines(CropImageView.Guidelines.ON)
//                    .setAspectRatio(1, 1)
//                    .start(this);

            UsersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        if (dataSnapshot.hasChild("profileimage")) {
                            String image = dataSnapshot.child("profileimage").getValue().toString();
                            Glide.with(SettingsActivity.this).load(image).placeholder(R.drawable.profile).into(binding.ProfilePictures);
                        } else {
                            Toast.makeText(SettingsActivity.this, "Please select profile image first.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);


//        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            uri = result.getUri();
//            binding.ProfilePictures.setImageURI(uri);
//
//        }
//            else
//            {
//                Toast.makeText(this, "Error Occured: Image can not be cropped. Try Again.", Toast.LENGTH_SHORT).show();
//
//            }
        }

}