package com.example.learningproject.newChanges.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.example.learningproject.R;
import com.example.learningproject.databinding.LayoutRegisterBinding;
import com.example.learningproject.newChanges.model.CustomerModel;
import com.example.learningproject.newChanges.model.DriverModel;
import com.example.learningproject.newChanges.utils.Constants;
import com.example.learningproject.newChanges.utils.SharedPrefUtils;
import com.example.learningproject.newChanges.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class SignupActivity extends AppCompatActivity {
    private LayoutRegisterBinding binding;
    CustomerModel customerModel;
    List<CustomerModel> customerModelList;
    DriverModel driverModel;
    List<DriverModel> driverModelList;
    Intent intent;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_register);
        Bundle bundle = getIntent().getExtras();
        role = bundle.getString(Constants.IntentTypes.ROLE, null);

        showUI();
        setOnClickListener();
    }


    private void setOnClickListener() {
        binding.btnSignup.setOnClickListener(view -> {
           // Utils.showProgressDialog(getApplicationContext(), true);
            CallingFirebase();

        });

        binding.tvCreateOne.setOnClickListener(view -> {
            if (role.equals("Customers") ) {
                intent = new Intent(SignupActivity.this, SignUpCustomer.class);
            } else {
                intent = new Intent(SignupActivity.this, SignUpDriver.class);
            }
            startActivity(intent);
        });
    }


    private void showUI() {

    }





    private void CallingFirebase() {
        if (role.equals("Customers")) {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("SignupUsers");

            mDatabase.orderByChild("username").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  //  Utils.dismissProgressDialog();
                    customerModelList = new ArrayList<>();

                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                        customerModel = noteDataSnapshot.getValue(CustomerModel.class);
                        customerModelList.add(customerModel);
                    }

                    if (UserExist()) {
                        Intent intent = new Intent(SignupActivity.this, CustomerMapActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(SignupActivity.this, "Invalid Username/Password", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                  //  Utils.dismissProgressDialog();
                }
            });
        } else {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("SignupOwners");
            mDatabase.orderByChild("username").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                   // Utils.dismissProgressDialog();
                    driverModelList = new ArrayList<>();

                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                        driverModel = noteDataSnapshot.getValue(DriverModel.class);
                        driverModelList.add(driverModel);
                    }
                    if (driverExists()) {
                        Intent intent = new Intent(SignupActivity.this, DriverMapActivity.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(SignupActivity.this, "Invalid Username/Password", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                 //   Utils.dismissProgressDialog();
                }
            });
        }
    }

    private boolean UserExist() {
        boolean isUserExist = false;
        for (int i = 0; i < customerModelList.size(); i++) {
            customerModel = customerModelList.get(i);
            if (binding.etUsername.getText().toString().equalsIgnoreCase(customerModel.username) && binding.etPassword.getText().toString().equalsIgnoreCase(customerModel.password)) {
                isUserExist = true;
                SharedPrefUtils.setStringPreference(this, Constants.USER_ID, customerModel.userId);
                break;
            } else {
                isUserExist = false;
            }
        }

        return isUserExist;
    }

    private boolean driverExists() {
        boolean isDriverExist = false;
        for (int i = 0; i < driverModelList.size(); i++) {
            driverModel = driverModelList.get(i);
            if (binding.etUsername.getText().toString().equalsIgnoreCase(driverModel.username) && binding.etPassword.getText().toString().equalsIgnoreCase(driverModel.password)) {
                isDriverExist = true;
                SharedPrefUtils.setStringPreference(this, Constants.USER_ID, driverModel.userId);
                break;
            } else {
                isDriverExist = false;
            }
        }
        return isDriverExist;
    }


}