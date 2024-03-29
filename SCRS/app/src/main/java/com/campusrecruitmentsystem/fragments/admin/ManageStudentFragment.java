package com.campusrecruitmentsystem.fragments.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.campusrecruitmentsystem.Utils;
import com.campusrecruitmentsystem.adapters.ManageStudentsAdapter;
import com.campusrecruitmentsystem.databinding.FragmentManageStudentBinding;
import com.campusrecruitmentsystem.pojo.StudentDetails;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ManageStudentFragment extends Fragment {

    private FragmentManageStudentBinding binding;
    private FirebaseAuth mAuth;
    private DatabaseReference mRootRef;
    private ManageStudentsAdapter appliedStudentsAdapter;
    private ArrayList<StudentDetails> studentDetailsArrayList = new ArrayList<>();
    private ArrayList<Boolean> isBlockedList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentManageStudentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();
        mRootRef = FirebaseDatabase.getInstance().getReference().child("Student");

        appliedStudentsAdapter = new ManageStudentsAdapter(getContext(), studentDetailsArrayList,
                isBlockedList,
                new ManageStudentsAdapter.OnClickListener() {
                    @Override
                    public void onBlockStatusChanged(int position, boolean isBlocked) {
                        mRootRef.child(studentDetailsArrayList.get(position).getUid()).child("is_blocked").setValue(isBlocked);
                        isBlockedList.set(position, isBlocked);
                    }
                });
        binding.studentsRecyclerview.setAdapter(appliedStudentsAdapter);

        if (Utils.isNetworkConnected(getContext())) {
            binding.loadingSection.getRoot().setVisibility(View.VISIBLE);
            mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    studentDetailsArrayList.clear();
                    isBlockedList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        if (!dataSnapshot.hasChild("is_blocked"))
                            isBlockedList.add(false);
                        else
                            isBlockedList.add((boolean) dataSnapshot.child("is_blocked").getValue());

                        DataSnapshot reference = dataSnapshot.child("Profile");
                        studentDetailsArrayList.add(new StudentDetails(dataSnapshot.getKey(),
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
                    appliedStudentsAdapter.setNotifyDatasetChanged(studentDetailsArrayList, isBlockedList);
                    binding.loadingSection.getRoot().setVisibility(View.GONE);

                    if (studentDetailsArrayList.isEmpty()) {
                        binding.llEmpty.setVisibility(View.VISIBLE);
                        binding.studentsRecyclerview.setVisibility(View.GONE);
                    } else {
                        binding.llEmpty.setVisibility(View.GONE);
                        binding.studentsRecyclerview.setVisibility(View.VISIBLE);
                    }

                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                    binding.loadingSection.getRoot().setVisibility(View.GONE);
                    Snackbar.make(binding.getRoot(), "Error: " + error.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            });
        } else {
            Snackbar.make(binding.getRoot(), "Internet not connected!", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
