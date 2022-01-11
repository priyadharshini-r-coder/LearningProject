package com.example.learningproject.newChanges.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.learningproject.R;
import com.example.learningproject.databinding.ActivitySignUpDriverBinding;
import com.example.learningproject.newChanges.model.DriverModel;

import java.util.Objects;

public class SignUpDriver extends AppCompatActivity  {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        private ActivitySignUpDriverBinding binding;


@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_sign_up_driver);

        setOnClickListener();
        }

private void setOnClickListener() {
    binding.btnSignup.setOnClickListener(view -> {
        if (checkMandatoryInputFields()) {
            String email = binding.etEmail.getText().toString();
            String username = binding.etUsername.getText().toString();
            String phonenumber = binding.etPhoneno.getText().toString();
            String password = binding.etPassword.getText().toString();
            String idProof = binding.etIdProof.getText().toString();
            String vehicleRegistrationNo = binding.etVehicleRegistrationNo.getText().toString();
            String vehicleType = binding.etVehicleVehicleType.getText().toString();
            String license = binding.etLicense.getText().toString();
            DriverModel.writeDriverDetails(email, username, phonenumber, password, idProof, vehicleRegistrationNo, vehicleType, license);
            Toast.makeText(SignUpDriver.this, "Success", Toast.LENGTH_LONG).show();
        }
    });
    binding.tvSignin.setOnClickListener(view -> {
        Intent intent = new Intent(SignUpDriver.this, SignupActivity.class);
        startActivity(intent);
    });
        }




private boolean checkMandatoryInputFields() {
        if (binding.etEmail.getText().toString().trim().equalsIgnoreCase("")) {
            binding.tilEmail.setError("Enter Email");
            binding.tilEmail.requestFocus();
        return false;
        } else if (!binding.etEmail.getText().toString().trim().matches(emailPattern)) {
            binding.tilEmail.setError("Enter Valid Email");
            binding. tilEmail.requestFocus();
        return false;
        } else if (binding.etUsername.getText().toString().trim().equalsIgnoreCase("")) {
            binding.tilUsername.setError("Enter Username");
            binding.tilUsername.requestFocus();
        return false;
        } else if (binding.etPhoneno.getText().toString().trim().equalsIgnoreCase("")) {
            binding.tilPhoneno.setError("Enter Phoneno");
            binding.tilPhoneno.requestFocus();
        return false;
        } else if (binding.etPassword.getText().toString().trim().equalsIgnoreCase("")) {
            binding.tilPassword.setError("Enter Password");
            binding.tilPassword.requestFocus();
        return false;
        } else if (binding.etConfirmpassword.getText().toString().trim().equalsIgnoreCase("")) {
            binding.tilConfirmpassword.setError("Enter ConfirmPassword");
            binding.tilConfirmpassword.requestFocus();
        return false;
        } else if (!Objects.equals(binding.etPassword.getText().toString(), binding.etConfirmpassword.getText().toString())) {
            binding.tilConfirmpassword.setError("Password do not match");
            binding.tilConfirmpassword.requestFocus();
        return false;
        } else if (binding.etIdProof.getText().toString().trim().equalsIgnoreCase("")) {
            binding.tilIdProof.setError("Enter Id Proof");
            binding.tilIdProof.requestFocus();
        return false;
        } else if (binding.etVehicleRegistrationNo.getText().toString().trim().equalsIgnoreCase("")) {
            binding.tilVehicleRegistrationNo.setError("Enter Vehicle Registration No");
            binding.tilVehicleRegistrationNo.requestFocus();
        return false;
        } else if (binding.etVehicleVehicleType.getText().toString().trim().equalsIgnoreCase("")) {
            binding.tilVehicleType.setError("Enter Vehicle Type");
            binding.tilVehicleType.requestFocus();
        return false;
        } else if (binding.etLicense.getText().toString().trim().equalsIgnoreCase("")) {
            binding.tilLicense.setError("Enter License No");
            binding.tilLicense.requestFocus();
        return false;
        }

        return true;

        }


}