package com.campusrecruitmentsystem.fragments.company;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.campusrecruitmentsystem.AddNewJobActivity;
import com.campusrecruitmentsystem.JobDescriptionActivity;
import com.campusrecruitmentsystem.Utils;
import com.campusrecruitmentsystem.adapters.JobListingAdapter;
import com.campusrecruitmentsystem.databinding.FragmentPostJobsBinding;
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

public class PostJobsFragment extends Fragment {

    private FragmentPostJobsBinding binding;
    private JobListingAdapter jobListingAdapter;
    private ArrayList<JobDetails> jobs = new ArrayList<>();
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mRootRef;
    private ArrayList<String> jobKeys = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPostJobsBinding.inflate(inflater, container, false);
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

        binding.extendedFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddNewJobActivity.class));
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
        if (Utils.isNetworkConnected(getContext())) {
            binding.swipeContainer.setRefreshing(true);
            binding.jobListingRecyclerView.setVisibility(View.VISIBLE);
            binding.internetNotConnected.setVisibility(View.GONE);

            mAuth = FirebaseAuth.getInstance();
            mCurrentUser = mAuth.getCurrentUser();
            mRootRef = FirebaseDatabase.getInstance().getReference();

            mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    jobs.clear();
                    jobKeys.clear();
                    for (DataSnapshot dataSnapshot : snapshot.child("Jobs").getChildren()) {
                        JobDetails job = dataSnapshot.getValue(JobDetails.class);
                        if (job.getCompanyUid().equals(mCurrentUser.getUid())) {
                            jobKeys.add(dataSnapshot.getKey());
                            jobs.add(job);
                        }
                    }
                    binding.swipeContainer.setRefreshing(false);
                    jobListingAdapter.setNotifyDatasetChanged(jobs);
                    if (jobs.isEmpty()){
                        binding.jobListingRecyclerView.setVisibility(View.GONE);
                        binding.llEmpty.setVisibility(View.VISIBLE);
                    }else{
                        binding.jobListingRecyclerView.setVisibility(View.VISIBLE);
                        binding.llEmpty.setVisibility(View.GONE);
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