package com.campusrecruitmentsystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.campusrecruitmentsystem.dashboard.AdminDashboardActivity;
import com.campusrecruitmentsystem.dashboard.CompanyDashboardActivity;
import com.campusrecruitmentsystem.dashboard.StudentDashboardActivity;
import com.campusrecruitmentsystem.dashboard.SuperAdminDashboardActivity;
import com.campusrecruitmentsystem.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;
    private DatabaseReference userRef;
    private String email;
    private String password;
    private String confirmPassword;
    private String s;
    private Utils.AccountType accountType = Utils.AccountType.STUDENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        
        binding.studentRadioButton.toggle();
        signUpClickAction();

        binding.loadingSection.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });


        binding.accountTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.studentRadioButton:
                        accountType = Utils.AccountType.STUDENT;
                        binding.userFullName.setHint("Full Name");
                        binding.companyLocation.setVisibility(View.GONE);
                        binding.userCollegeRollNo.setVisibility(View.VISIBLE);
                        break;
                    case R.id.adminRadioButton:
                        accountType = Utils.AccountType.ADMIN;
                        binding.userFullName.setHint("Full Name");
                        binding.companyLocation.setVisibility(View.GONE);
                        binding.userCollegeRollNo.setVisibility(View.GONE);
                        break;
                    case R.id.superAdminRadioButton:
                        accountType = Utils.AccountType.SUPERADMIN;
                        binding.userFullName.setHint("Full Name");
                        binding.companyLocation.setVisibility(View.GONE);
                        binding.userCollegeRollNo.setVisibility(View.GONE);
                        break;
                    case R.id.companyRadioButton:
                        accountType = Utils.AccountType.COMPANY;
                        binding.userFullName.setHint("Company Name");
                        binding.companyLocation.setVisibility(View.VISIBLE);
                        binding.userCollegeRollNo.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }

    private void signUpClickAction() {
        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.userFullName.getText().toString().isEmpty()) {
                    binding.userFullName.requestFocus();
                    if(accountType == Utils.AccountType.COMPANY)
                        Toast.makeText(SignUpActivity.this, "Company Name is required", Toast.LENGTH_SHORT).show();
                    else
                    Toast.makeText(SignUpActivity.this, "Full Name is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(accountType == Utils.AccountType.STUDENT && binding.userCollegeRollNo.getText().toString().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Roll No. is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                email = binding.userEmailId.getText().toString();
                password = binding.userPassword.getText().toString();
                confirmPassword = binding.userConfirmPassword.getText().toString();

                if(email.isEmpty()){
                    Toast.makeText(SignUpActivity.this, "Email id is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(SignUpActivity.this, "Enter a valid email id", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(binding.userContactNo.getText().toString().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "User Contact No is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(binding.userContactNo.getText().toString().length() != 10) { // !Patterns.PHONE.matcher(binding.userContactNo.getEditText().getText().toString()).matches()
                    Toast.makeText(SignUpActivity.this, "Enter a valid phone number", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(password.isEmpty()){
                    Toast.makeText(SignUpActivity.this, "Password is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(confirmPassword.length()<6) {
                    Toast.makeText(SignUpActivity.this, "Password length must be minimum 6", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(password.equals(confirmPassword)) {
                    if(Utils.isNetworkConnected(getApplicationContext())) {
                        binding.loadingSection.getRoot().setVisibility(View.VISIBLE);
                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                binding.loadingSection.getRoot().setVisibility(View.INVISIBLE);
                                if (task.isSuccessful()) {
                                    Intent i = null;
                                    switch (accountType) {
                                        case STUDENT:
                                            i = new Intent(SignUpActivity.this, StudentDashboardActivity.class);
                                            userRef = FirebaseDatabase.getInstance().getReference().child("Student")
                                                    .child(mAuth.getCurrentUser().getUid()).child("Profile");
                                            userRef.child("roll_no").setValue(binding.userCollegeRollNo.getText().toString());
                                            break;
                                        case COMPANY:
                                            i = new Intent(SignUpActivity.this, CompanyDashboardActivity.class);
                                            userRef = FirebaseDatabase.getInstance().getReference().child("Company")
                                                    .child(mAuth.getCurrentUser().getUid()).child("Profile");
                                            userRef.child("company_location").setValue(binding.companyLocation.getText().toString());
                                            break;
                                        case ADMIN:
                                            i = new Intent(SignUpActivity.this, AdminDashboardActivity.class);
                                            userRef = FirebaseDatabase.getInstance().getReference().child("Admin")
                                                    .child(mAuth.getCurrentUser().getUid()).child("Profile");
                                            break;
                                        case SUPERADMIN:
                                            i = new Intent(SignUpActivity.this, SuperAdminDashboardActivity.class);
                                            userRef = FirebaseDatabase.getInstance().getReference().child("SuperAdmin")
                                                    .child(mAuth.getCurrentUser().getUid()).child("Profile");
                                            break;
                                    }

                                    userRef.child("full_name").setValue(binding.userFullName.getText().toString());
                                    userRef.child("email_id").setValue(binding.userEmailId.getText().toString());
                                    userRef.child("contact_no").setValue(binding.userContactNo.getText().toString());

                                    i.putExtra("accountType", accountType);
                                    startActivity(i);
                                    finishAffinity();
                                } else {
                                    if (task.getException() instanceof FirebaseAuthEmailException) {
                                        Toast.makeText(getApplicationContext(), "User already registered", Toast.LENGTH_SHORT).show();
                                        return;
                                    } else {
                                        String errorMessage = task.getException().getMessage();
                                        Toast.makeText(SignUpActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG);
                                        return;
                                    }
                                }
                            }
                        });
                    } else {
                        Snackbar.make(binding.getRoot(), "Internet not connected!",Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Snackbar.make(binding.getRoot(), "Confirm Password & Password doesn't match",Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }
}