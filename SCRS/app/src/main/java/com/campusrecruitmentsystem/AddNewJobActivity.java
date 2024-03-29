package com.campusrecruitmentsystem;

import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.campusrecruitmentsystem.databinding.ActivityAddNewJobBinding;
import com.campusrecruitmentsystem.pojo.JobDetails;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AddNewJobActivity extends AppCompatActivity {

    private ActivityAddNewJobBinding binding;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference mRootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNewJobBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mRootRef = FirebaseDatabase.getInstance().getReference();

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void onAddJobClick(View v){
        if(Utils.isNetworkConnected(this)) {
            int batch = 0;
            if(binding.batchTextField.getEditText().getText().toString().isEmpty())
                batch = Calendar.getInstance().get(Calendar.YEAR);
            else
                batch = Integer.parseInt(binding.batchTextField.getEditText().getText().toString());

            if(binding.companyMarketTextField.getEditText().getText().toString().isEmpty()){
                binding.companyMarketTextField.requestFocus();
                binding.companyMarketTextField.getEditText().setError("This field is required!");
                return;
            }
            if(binding.jobDescriptionTextField.getEditText().getText().toString().isEmpty()){
                binding.jobDescriptionTextField.requestFocus();
                binding.jobDescriptionTextField.getEditText().setError("Job description is required!");
                return;
            }
            if(binding.employmentTypeTextField.getEditText().getText().toString().isEmpty()){
                binding.employmentTypeTextField.requestFocus();
                binding.employmentTypeTextField.getEditText().setError("Job type is required!");
                return;
            }
            if(binding.jobRoleTextField.getEditText().getText().toString().isEmpty()){
                binding.jobRoleTextField.requestFocus();
                binding.jobRoleTextField.getEditText().setError("Job role is required!");
                return;
            }
            if(binding.salaryDuringProbationTextField.getEditText().getText().toString().isEmpty()){
                binding.salaryDuringProbationTextField.requestFocus();
                binding.salaryDuringProbationTextField.getEditText().setError("Salary is required!");
                return;
            }
            if(binding.salaryAfterProbationTextField.getEditText().getText().toString().isEmpty()){
                binding.salaryAfterProbationTextField.requestFocus();
                binding.salaryAfterProbationTextField.getEditText().setError("Salary is required!");
                return;
            }
            if(binding.bondTextField.getEditText().getText().toString().isEmpty()){
                binding.bondTextField.requestFocus();
                binding.bondTextField.getEditText().setError("Bond is required!");
                return;
            }
            if(binding.workingDaysTextField.getEditText().getText().toString().isEmpty()){
                binding.workingDaysTextField.requestFocus();
                binding.workingDaysTextField.getEditText().setError("Working days is required!");
                return;
            }

            if(binding.twelfthPercentageTextField.getEditText().getText().toString().isEmpty()){
                binding.twelfthPercentageTextField.requestFocus();
                binding.twelfthPercentageTextField.getEditText().setError("Percentage is required!");
                return;
            }

            if(binding.graduationPercentageTextField.getEditText().getText().toString().isEmpty()){
                binding.graduationPercentageTextField.requestFocus();
                binding.graduationPercentageTextField.getEditText().setError("Percentage is required!");
                return;
            }
            if(binding.numberOfBacklogsTextField.getEditText().getText().toString().isEmpty()){
                binding.numberOfBacklogsTextField.requestFocus();
                binding.numberOfBacklogsTextField.getEditText().setError("Number of backlogs is required!");
                return;
            }

            DatabaseReference detailsRef = mRootRef.child("Jobs").push();
            detailsRef.setValue(new JobDetails(mCurrentUser.getUid(),
                    PreferenceManager.getDefaultSharedPreferences(this).getString("full_name", "Company Name"),
                    batch,
                    binding.jobDescriptionTextField.getEditText().getText().toString(),
                    binding.companyMarketTextField.getEditText().getText().toString(),
                    binding.employmentTypeTextField.getEditText().getText().toString(),
                    binding.jobRoleTextField.getEditText().getText().toString(),
                    binding.jobLocationTextField.getEditText().getText().toString(),
                    binding.hiringProcessTextField.getEditText().getText().toString(),
                    binding.probationPeriodTextField.getEditText().getText().toString(),
                    Integer.parseInt(binding.salaryDuringProbationTextField.getEditText().getText().toString()),
                    Integer.parseInt(binding.salaryAfterProbationTextField.getEditText().getText().toString()),
                    Integer.parseInt(binding.bondTextField.getEditText().getText().toString()),
                    Integer.parseInt(binding.workingDaysTextField.getEditText().getText().toString()),
                    binding.messageTextField.getEditText().getText().toString(),
                    Integer.parseInt(binding.twelfthPercentageTextField.getEditText().getText().toString()),
                    Integer.parseInt(binding.graduationPercentageTextField.getEditText().getText().toString()),
                    Integer.parseInt(binding.numberOfBacklogsTextField.getEditText().getText().toString())));

            Snackbar.make(binding.getRoot(), "Job Posted", Snackbar.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onBackPressed();
                }
            },500);

        } else {
            Snackbar.make(binding.getRoot(), "Internet not available!", Snackbar.LENGTH_LONG).show();
        }
    }
}