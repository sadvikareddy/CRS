package com.campusrecruitmentsystem.fragments.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.campusrecruitmentsystem.JobDescriptionActivity;
import com.campusrecruitmentsystem.Utils;
import com.campusrecruitmentsystem.adapters.JobListingAdapter;
import com.campusrecruitmentsystem.databinding.FragmentApplyForJobBinding;
import com.campusrecruitmentsystem.pojo.JobDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;

import static android.view.View.GONE;

public class ApplyForJobFragment extends Fragment {

    private FragmentApplyForJobBinding binding;
    private JobListingAdapter jobListingAdapter;
    private ArrayList<JobDetails> jobs = new ArrayList<>();
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mRootRef;
    private ArrayList<String> jobKeys = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentApplyForJobBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        jobListingAdapter = new JobListingAdapter(getContext(), jobs,
                new JobListingAdapter.OnJobClickLitener() {
                    @Override
                    public void onClick(int position) {
                        Intent intent = new Intent(getActivity(), JobDescriptionActivity.class);
                        intent.putExtra("jobUid", jobKeys.get(position));
                        intent.putExtra("jobDetail", (Serializable) jobs.get(position));
                        startActivity(intent);
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

    public void getData() {
        if (Utils.isNetworkConnected(getContext())) {
            binding.swipeContainer.setRefreshing(true);
            binding.jobListingRecyclerView.setVisibility(View.VISIBLE);
            binding.internetNotConnected.setVisibility(GONE);

            mAuth = FirebaseAuth.getInstance();
            mCurrentUser = mAuth.getCurrentUser();
            mRootRef = FirebaseDatabase.getInstance().getReference();

            mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    if(!snapshot.child("Student").child(mCurrentUser.getUid()).child("Profile").hasChild("resume")){
                        binding.llEmpty.setVisibility(View.VISIBLE);
                        binding.lottie.setAnimation("empty_profile.json");
                        binding.lottie.playAnimation();
                        binding.message.setText("First fill out your profile details in Update Details Section!");
                        binding.jobListingRecyclerView.setVisibility(GONE);
                        binding.swipeContainer.setRefreshing(false);
                        return;
                    }
                    jobs.clear();
                    jobKeys.clear();
                    for (DataSnapshot dataSnapshot : snapshot.child("Jobs").getChildren()) {
                        jobKeys.add(dataSnapshot.getKey());
                        jobs.add(dataSnapshot.getValue(JobDetails.class));
                    }
                    binding.swipeContainer.setRefreshing(false);
                    jobListingAdapter.setNotifyDatasetChanged(jobs);
                    if(jobs.size() == 0){
                        binding.message.setText("NO JOBS AVAILABLE!");
                        binding.lottie.setAnimation("no_jobs.json");
                        binding.lottie.playAnimation();
                        binding.llEmpty.setVisibility(View.VISIBLE);
                    } else {
                        binding.llEmpty.setVisibility(GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                    binding.swipeContainer.setRefreshing(false);
                }
            });
        } else {
            binding.llEmpty.setVisibility(GONE);
            binding.swipeContainer.setRefreshing(false);
            binding.jobListingRecyclerView.setVisibility(View.INVISIBLE);
            binding.internetNotConnected.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}