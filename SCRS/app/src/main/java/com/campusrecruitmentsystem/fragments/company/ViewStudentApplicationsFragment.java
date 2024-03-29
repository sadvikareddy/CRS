package com.campusrecruitmentsystem.fragments.company;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.campusrecruitmentsystem.AppliedStudentsActivity;
import com.campusrecruitmentsystem.Utils;
import com.campusrecruitmentsystem.adapters.JobListingAdapter;
import com.campusrecruitmentsystem.databinding.FragmentPostJobsBinding;
import com.campusrecruitmentsystem.databinding.FragmentViewJobStatusBinding;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ViewStudentApplicationsFragment extends Fragment {

    private FragmentViewJobStatusBinding binding;
    private JobListingAdapter jobListingAdapter;
    private ArrayList<JobDetails> jobs = new ArrayList<>();
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mRootRef;
    private ArrayList<String> jobKeys = new ArrayList<>();
    private HashMap<String, ArrayList<String>> appliedStudentsList = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentViewJobStatusBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        jobListingAdapter = new JobListingAdapter(getContext(), jobs,
                new JobListingAdapter.OnJobClickLitener() {
                    @Override
                    public void onClick(int position) {
                        Intent intent = new Intent(getActivity(), AppliedStudentsActivity.class);
                        if (appliedStudentsList.containsKey(jobKeys.get(position))) {
                            intent.putExtra("applied_students", appliedStudentsList.get(jobKeys.get(position)));
                            intent.putExtra("job_uid", jobKeys.get(position));
                            startActivity(intent);
                        } else {
                            Snackbar.make(binding.getRoot(), "No Students Applied!", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
        binding.jobListingRecyclerView.setAdapter(jobListingAdapter);

        binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        getData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void getData() {

        binding.lottie.setAnimation("no_application.json");
        binding.lottie.playAnimation();

        if (Utils.isNetworkConnected(getContext())) {

            appliedStudentsList.clear();
            binding.swipeContainer.setRefreshing(true);
            binding.jobListingRecyclerView.setVisibility(View.VISIBLE);
            binding.internetNotConnected.setVisibility(View.GONE);

            mAuth = FirebaseAuth.getInstance();
            mCurrentUser = mAuth.getCurrentUser();
            mRootRef = FirebaseDatabase.getInstance().getReference().child("Jobs");

            mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    jobs.clear();
                    jobKeys.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        JobDetails job = dataSnapshot.getValue(JobDetails.class);
                        if (job.getCompanyUid().equals(mCurrentUser.getUid())) {
                            jobKeys.add(dataSnapshot.getKey());
                            jobs.add(job);
                            if (dataSnapshot.hasChild("appliedStudents")) {
                                for (DataSnapshot appliedStudentUid : dataSnapshot.child("appliedStudents").getChildren()) {
                                    if (appliedStudentsList.get(dataSnapshot.getKey()) == null) {
                                        appliedStudentsList.put(dataSnapshot.getKey(), new ArrayList<>(Arrays.asList(appliedStudentUid.getValue().toString())));
                                    } else {
                                        appliedStudentsList.get(dataSnapshot.getKey()).add(appliedStudentUid.getValue().toString());
                                    }
                                }
                            }
                        }
                    }
                    binding.swipeContainer.setRefreshing(false);
                    jobListingAdapter.setNotifyDatasetChanged(jobs);
                    if (jobs.isEmpty()) {
                        binding.llEmpty.setVisibility(View.VISIBLE);
                        binding.jobListingRecyclerView.setVisibility(View.GONE);
                    } else {
                        binding.llEmpty.setVisibility(View.GONE);
                        binding.jobListingRecyclerView.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                    binding.swipeContainer.setRefreshing(false);
                }
            });
        } else {
            binding.swipeContainer.setRefreshing(false);
            binding.jobListingRecyclerView.setVisibility(View.INVISIBLE);
            binding.internetNotConnected.setVisibility(View.VISIBLE);
        }
    }
}