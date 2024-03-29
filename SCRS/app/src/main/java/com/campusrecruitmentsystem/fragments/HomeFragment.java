package com.campusrecruitmentsystem.fragments;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.campusrecruitmentsystem.R;
import com.campusrecruitmentsystem.Utils;
import com.campusrecruitmentsystem.databinding.FragmentHomeBinding;
import com.campusrecruitmentsystem.pojo.JobDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import static android.view.View.GONE;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference rootRef;
    private DatabaseReference userRef;
    private Boolean isBlocked = false;
    private int accountType;
    private long totalStudents = 0, totalJobs = 0, totalCompanies = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();

        accountType = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getInt(getString(R.string.account_type), 1);
        isBlocked = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getBoolean("isBlocked", false);

        if (isBlocked) {
            binding.contentSection.setVisibility(View.GONE);
            binding.accountBlocked.setVisibility(View.VISIBLE);
            return;
        }

        switch (PreferenceManager.getDefaultSharedPreferences(getContext()).getInt(getContext().getResources().getString(R.string.account_type), 1)) {
            case 0:
                userRef = FirebaseDatabase.getInstance().getReference().child("Admin").child(currentUser.getUid());
                break;
            case 1:
                userRef = FirebaseDatabase.getInstance().getReference().child("Student").child(currentUser.getUid());
                break;
            case 2:
                userRef = FirebaseDatabase.getInstance().getReference().child("Company").child(currentUser.getUid());
                binding.property3Card.setVisibility(View.INVISIBLE);
                break;
            case 3:
                userRef = FirebaseDatabase.getInstance().getReference().child("SuperAdmin").child(currentUser.getUid());
                binding.property4Card.setVisibility(View.VISIBLE);
                break;
        }

        if (Utils.isNetworkConnected(getContext())) {
            binding.internetNotConnected.setVisibility(GONE);
            binding.contentSection.setVisibility(View.VISIBLE);
            binding.loadingSection.getRoot().setVisibility(View.VISIBLE);

            totalCompanies = 0;
            totalJobs = 0;
            totalStudents = 0;

            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    switch (accountType) {
                        case 0:  // ADMIN

                            binding.userName.setText(snapshot.child("Admin").
                                    child(currentUser.getUid()).
                                    child("Profile").
                                    child("full_name").getValue().toString());

                            binding.userDetail.setText(snapshot.child("Admin").
                                    child(currentUser.getUid()).
                                    child("Profile").
                                    child("contact_no").getValue().toString());

                            binding.property1Heading.setText("Total Number of Students");
                            totalStudents = snapshot.child("Student").getChildrenCount();
                            binding.property1Value.setText((totalStudents < 10)
                                    ? "0" + String.valueOf(totalStudents) : String.valueOf(totalStudents));

                            binding.property2Heading.setText("Total Number of Companies");
                            totalCompanies = snapshot.child("Company").getChildrenCount();
                            binding.property2Value.setText((totalCompanies < 10)
                                    ? "0" + String.valueOf(totalCompanies) : String.valueOf(totalCompanies));

                            binding.property3Heading.setText("Total Number of Jobs");
                            totalJobs = snapshot.child("Jobs").getChildrenCount();
                            binding.property3Value.setText((totalJobs < 10)
                                    ? "0" + String.valueOf(totalJobs) : String.valueOf(totalJobs));

                            break;
                        case 3:  // SUPER ADMIN

                            binding.userName.setText(snapshot.child("SuperAdmin").
                                    child(currentUser.getUid()).
                                    child("Profile").
                                    child("full_name").getValue().toString());

                            binding.userDetail.setText(snapshot.child("SuperAdmin").
                                    child(currentUser.getUid()).
                                    child("Profile").
                                    child("contact_no").getValue().toString());

                            binding.property1Heading.setText("Total Number of Students");
                            totalStudents = snapshot.child("Student").getChildrenCount();
                            binding.property1Value.setText((totalStudents < 10)
                                    ? "0" + String.valueOf(totalStudents) : String.valueOf(totalStudents));

                            binding.property2Heading.setText("Total Number of Companies");
                            totalCompanies = snapshot.child("Company").getChildrenCount();
                            binding.property2Value.setText((totalCompanies < 10)
                                    ? "0" + String.valueOf(totalCompanies) : String.valueOf(totalCompanies));

                            binding.property3Heading.setText("Total Number of Jobs");
                            totalJobs = snapshot.child("Jobs").getChildrenCount();
                            binding.property3Value.setText((totalJobs < 10)
                                    ? "0" + String.valueOf(totalJobs) : String.valueOf(totalJobs));

                            binding.property4Heading.setText("Total Number of Admins");
                            totalCompanies = snapshot.child("Admin").getChildrenCount();
                            binding.property4Value.setText((totalCompanies < 10)
                                    ? "0" + String.valueOf(totalCompanies) : String.valueOf(totalCompanies));

                            break;
                        case 1:  // STUDENT
                            binding.userName.setText(snapshot.child("Student").
                                    child(currentUser.getUid()).
                                    child("Profile").
                                    child("full_name").getValue().toString());

                            binding.userDetail.setText(snapshot.child("Student").
                                    child(currentUser.getUid()).
                                    child("Profile").
                                    child("contact_no").getValue().toString());

                            totalJobs = snapshot.child("Jobs").getChildrenCount();

                            binding.property1Value.setText((totalJobs < 10) ? "0" + String.valueOf(totalJobs) : String.valueOf(totalJobs));
                            binding.property1Heading.setText("Total Jobs Available");
                            binding.property2Heading.setText("Number of Jobs Applied");
                            if (!snapshot.child("Student").child(currentUser.getUid()).hasChild("appliedJobs")) {
                                binding.property2Value.setText("00");
                                binding.property3Card.setVisibility(GONE);
                            } else {
                                long appliedJobs = snapshot.child("Student")
                                        .child(currentUser.getUid()).child("appliedJobs")
                                        .getChildrenCount();
                                binding.property2Value.setText((appliedJobs < 10)
                                        ? "0" + String.valueOf(appliedJobs) : String.valueOf(appliedJobs));

                                int respondedJobs = 0;
                                for (DataSnapshot snapshot1 : snapshot.child("Student")
                                        .child(currentUser.getUid()).child("appliedJobs").getChildren()) {
                                    if (Integer.parseInt(snapshot1.child("status").getValue().toString()) != 0)
                                        respondedJobs++;
                                }
                                binding.property3Value.setText((respondedJobs < 10)
                                        ? "0" + String.valueOf(respondedJobs) : String.valueOf(respondedJobs));
                                binding.property3Heading.setText("Number of Jobs Responded");
                                binding.property3Card.setVisibility(View.VISIBLE);

                            }
                            break;
                        case 2: // COMPANY
                            binding.userName.setText(snapshot.child("Company").
                                    child(currentUser.getUid()).
                                    child("Profile").
                                    child("full_name").getValue().toString());

                            binding.userDetail.setText(snapshot.child("Company").
                                    child(currentUser.getUid()).
                                    child("Profile").
                                    child("contact_no").getValue().toString());

                            int appliedStudents = 0;
                            for (DataSnapshot dataSnapshot : snapshot.child("Jobs").getChildren()) {
                                JobDetails job = dataSnapshot.getValue(JobDetails.class);
                                if (job.getCompanyUid().equals(currentUser.getUid())) {
                                    totalJobs++;
                                    if (dataSnapshot.hasChild("appliedStudents")) {
                                        appliedStudents += dataSnapshot.child("appliedStudents").getChildrenCount();
                                    }
                                }
                            }
                            binding.property1Value.setText((totalJobs < 10)
                                    ? "0" + String.valueOf(totalJobs) : String.valueOf(totalJobs));
                            binding.property1Heading.setText("Number of Jobs Posted");

                            binding.property2Value.setText((appliedStudents < 10)
                                    ? "0" + String.valueOf(appliedStudents) : String.valueOf(appliedStudents));
                            binding.property2Heading.setText("Number of Students Applied");
                    }
                    binding.loadingSection.getRoot().setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                    binding.loadingSection.getRoot().setVisibility(View.GONE);
                }
            });
        } else {
            binding.internetNotConnected.setVisibility(View.VISIBLE);
            binding.contentSection.setVisibility(GONE);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}