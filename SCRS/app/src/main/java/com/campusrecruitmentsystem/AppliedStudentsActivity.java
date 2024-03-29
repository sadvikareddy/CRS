package com.campusrecruitmentsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.campusrecruitmentsystem.adapters.AppliedStudentsAdapter;
import com.campusrecruitmentsystem.databinding.ActivityAppliedStudentsBinding;
import com.campusrecruitmentsystem.pojo.StudentDetails;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AppliedStudentsActivity extends AppCompatActivity {

    private ActivityAppliedStudentsBinding binding;
    private ArrayList<String> appliedStudentsList = new ArrayList<>();
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mRootRef;
    private AppliedStudentsAdapter appliedStudentsAdapter;
    private String JobUid;
    private ArrayList<StudentDetails> studentDetailsArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAppliedStudentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        JobUid = getIntent().getStringExtra("job_uid");

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mRootRef = FirebaseDatabase.getInstance().getReference().child("Student");

        appliedStudentsAdapter = new AppliedStudentsAdapter(getApplicationContext(),studentDetailsArrayList, new AppliedStudentsAdapter.OnClickListener() {
            @Override
            public void onJobApplicationStatusListener(int position, boolean isAccepted) {
                DatabaseReference reference = mRootRef.child(studentDetailsArrayList.get(position).getUid()).child("appliedJobs");
                binding.loadingSection.getRoot().setVisibility(View.VISIBLE);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        String tempUid = null;
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            if(dataSnapshot.child("job_uid").getValue().equals(JobUid)){
                                tempUid = dataSnapshot.getKey();
                                break;
                            }
                        }
                        reference.child(tempUid).child("status").setValue((isAccepted) ? 1 : 2);
                        binding.loadingSection.getRoot().setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        binding.loadingSection.getRoot().setVisibility(View.GONE);
                    }
                });
            }
        });
        binding.appliedStudentsRecyclerview.setAdapter(appliedStudentsAdapter);

        appliedStudentsList = getIntent().getStringArrayListExtra("applied_students");
        System.out.println(appliedStudentsList.toString());

        if(Utils.isNetworkConnected(this)) {
            mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    for(int i=0; i<appliedStudentsList.size(); i++){
                        String uid = appliedStudentsList.get(i);
                        if(snapshot.hasChild(uid)) {
                            DataSnapshot reference = snapshot.child(uid).child("Profile");
                            studentDetailsArrayList.add(new StudentDetails(uid,
                                    String.valueOf(reference.child("full_name").getValue()),
                                    String.valueOf(reference.child("email_id").getValue()),
                                    String.valueOf(reference.child("contact_no").getValue()),
                                    String.valueOf(reference.child("roll_no").getValue()),
                                    String.valueOf(reference.child("resume").child("profile_summary").getValue()),
                                    String.valueOf(reference.child("resume").child("key_skills").getValue()),
                                    (Long) reference.child("resume").child("class_tenth").child("percentage").getValue(),
                                    String.valueOf(reference.child("resume").child("class_tenth").child("board").getValue()),
                                    (Long) reference.child("resume").child("class_twelfth").child("percentage").getValue(),
                                    String.valueOf(reference.child("resume").child("class_twelfth").child("board").getValue()),
                                    (Long) reference.child("resume").child("graduation").child("percentage").getValue(),
                                    String.valueOf(reference.child("resume").child("graduation").child("stream").getValue())));
                        }
                    }
                    appliedStudentsAdapter.setNotifyDatasetChanged(studentDetailsArrayList);
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                    Snackbar.make(binding.getRoot(), "Error: " + error.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            });
        } else {
            Snackbar.make(binding.getRoot(), "Internet not connected!", Snackbar.LENGTH_LONG).show();
        }
    }
}