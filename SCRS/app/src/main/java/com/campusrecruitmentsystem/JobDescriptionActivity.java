package com.campusrecruitmentsystem;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.campusrecruitmentsystem.databinding.ActivityJobDescriptionBinding;
import com.campusrecruitmentsystem.pojo.JobDetails;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class JobDescriptionActivity extends AppCompatActivity {

    private ActivityJobDescriptionBinding binding;
    private JobDetails jobDetail;
    private String jobUid;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mRootRef;
    private DatabaseReference mCurrentStudentRootRef;
    private DatabaseReference mCurrentJobRootRef;
    private Boolean isAlreadyApplied = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityJobDescriptionBinding.inflate(getLayoutInflater());
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

        jobDetail = (JobDetails) getIntent().getSerializableExtra("jobDetail");
        jobUid = getIntent().getStringExtra("jobUid");

        mCurrentJobRootRef = mRootRef.child("Jobs").child(jobUid);

        if(PreferenceManager.getDefaultSharedPreferences(this).getInt("accountType",1) == 1){
            mCurrentStudentRootRef = mRootRef.child("Student").child(mCurrentUser.getUid());
            binding.applyButton.setVisibility(View.VISIBLE);

            mCurrentStudentRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    if(snapshot.hasChild("appliedJobs")){
                        for(DataSnapshot dataSnapshot : snapshot.child("appliedJobs").getChildren()){
                            if(jobUid.equals(dataSnapshot.child("job_uid").getValue().toString())){
                                isAlreadyApplied = true;
                                binding.applyButton.setText("Already Applied!");
                                binding.applyButton.setEnabled(false);
                                break;
                            }
                        }
                        if(!isAlreadyApplied)
                            setupApplyButton();
                    } else {
                        setupApplyButton();
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

        }

        binding.companyNameValue.setText(jobDetail.getCompanyName());
        binding.companyMarketValue.setText(jobDetail.getCompanyMarket());
        binding.jobDescriptionValue.setText(jobDetail.getJobDescription());

        binding.batchValue.setText(String.valueOf(jobDetail.getBatch()));
        binding.employmentTypeValue.setText(jobDetail.getEmploymentType());
        binding.jobRoleValue.setText(jobDetail.getJobRole());
        binding.jobLocationValue.setText(jobDetail.getJobLocation()); 
        binding.probationValue.setText(jobDetail.getProbationPeriod());
        binding.salaryDuringProbationValue.setText(String.valueOf(jobDetail.getSalaryDuringProbation()));
        binding.salaryAfterProbationValue.setText(String.valueOf(jobDetail.getSalaryAfterProbation()));
        binding.bondValue.setText(String.valueOf(jobDetail.getBond()));
        binding.workingDaysValue.setText(String.valueOf(jobDetail.getWorkingDays()));
        binding.messageValue.setText(String.valueOf(jobDetail.getMessage()));

        binding.twelfthPercentageValue.setText(String.valueOf(jobDetail.getTwelfthPercentage())+"%");
        binding.graduationPercentageValue.setText(String.valueOf(jobDetail.getGraduationPercentage())+"%");
        binding.backlogsValue.setText(String.valueOf(jobDetail.getBacklogs()));
        binding.hiringProcessValue.setText(jobDetail.getHiringProcess());
    }

    private void setupApplyButton() {
        binding.applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.isNetworkConnected(getApplicationContext())) {
                    binding.applyButton.setEnabled(false);
                    binding.applyButton.setText("Applied");
                    mCurrentJobRootRef.child("appliedStudents").push().setValue(mCurrentUser.getUid());
                    String key = mCurrentStudentRootRef.child("appliedJobs").push().getKey();
                    mCurrentStudentRootRef.child("appliedJobs").child(key).child("job_uid").setValue(jobUid);
                    mCurrentStudentRootRef.child("appliedJobs").child(key).child("status").setValue(0);
                    Snackbar.make(binding.getRoot(), "Applied for Job!", Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(binding.getRoot(), "Internet not connected!", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }
}