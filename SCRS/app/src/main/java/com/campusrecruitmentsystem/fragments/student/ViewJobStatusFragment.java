package com.campusrecruitmentsystem.fragments.student;

import android.animation.Animator;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.campusrecruitmentsystem.Utils;
import com.campusrecruitmentsystem.adapters.JobListingAdapter;
import com.campusrecruitmentsystem.databinding.FragmentViewJobStatusBinding;
import com.campusrecruitmentsystem.pojo.JobDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static android.view.View.GONE;

public class ViewJobStatusFragment extends Fragment {

    private FragmentViewJobStatusBinding binding;
    private JobListingAdapter jobListingAdapter;
    private ArrayList<JobDetails> jobs = new ArrayList<>();
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mRootRef;
    private ArrayList<String> jobKeys = new ArrayList<>();
    private ArrayList<Integer> jobStatus = new ArrayList<>();
    private Boolean isStatusVisible = false;

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
                        if(isStatusVisible)
                            return;

                        switch (jobStatus.get(position)){
                            case 0:
                                binding.status.setText("DECISION PENDING");
                                break;
                            case 1:
                                binding.status.setText("ACCEPTED");
                                break;
                            case 2:
                                binding.status.setText("REJECTED");
                        }
                        binding.statusLayout.setVisibility(View.VISIBLE);

                        binding.statusLayout.animate()
                                .alpha(1f)
                                .setDuration(300)
                                .setListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {
                                        isStatusVisible = true;
                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                binding.statusLayout.animate()
                                                        .alpha(0f)
                                                        .setDuration(300)
                                                        .setListener(new Animator.AnimatorListener() {
                                                            @Override
                                                            public void onAnimationStart(Animator animation) { }

                                                            @Override
                                                            public void onAnimationEnd(Animator animation) {
                                                                binding.statusLayout.setVisibility(View.GONE);
                                                                isStatusVisible = false;
                                                            }

                                                            @Override
                                                            public void onAnimationCancel(Animator animation) { }

                                                            @Override
                                                            public void onAnimationRepeat(Animator animation) { }
                                                        });
                                            }
                                        },1000);
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) { }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) { }
                                }).start();
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
            binding.llEmpty.setVisibility(View.GONE);
            binding.internetNotConnected.setVisibility(GONE);

            mAuth = FirebaseAuth.getInstance();
            mCurrentUser = mAuth.getCurrentUser();
            mRootRef = FirebaseDatabase.getInstance().getReference();

            mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    if(!snapshot.child("Student").child(mCurrentUser.getUid()).hasChild("appliedJobs")){
                        binding.message.setText("NO JOBS AVAILABLE!");
                        binding.llEmpty.setVisibility(View.VISIBLE);
                        binding.swipeContainer.setRefreshing(false);
                        return;
                    }
                    jobs.clear();
                    jobKeys.clear();
                    jobStatus.clear();
                    for(DataSnapshot dataSnapshot : snapshot.child("Student").child(
                            mCurrentUser.getUid()).child("appliedJobs").getChildren()){
                        jobKeys.add(dataSnapshot.child("job_uid").getValue().toString());
                        jobs.add(snapshot.child("Jobs").
                                child(dataSnapshot.child("job_uid").getValue().toString())
                                .getValue(JobDetails.class));
                        jobStatus.add(Integer.parseInt(dataSnapshot.child("status").getValue().toString()));
                    }

                    binding.swipeContainer.setRefreshing(false);
                    jobListingAdapter.setNotifyDatasetChanged(jobs);
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
            binding.llEmpty.setVisibility(GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}