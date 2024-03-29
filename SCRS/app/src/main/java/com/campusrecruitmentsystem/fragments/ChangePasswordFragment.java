package com.campusrecruitmentsystem.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.campusrecruitmentsystem.databinding.FragmentChangePasswordBinding;
import com.campusrecruitmentsystem.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class ChangePasswordFragment extends Fragment {

    private FragmentChangePasswordBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        binding.loadingSection.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        binding.updatePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.currentPasswordEditText.getText().toString().isEmpty()) {
                    Toast.makeText(requireContext(), "Password is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(binding.currentPasswordEditText.getText().toString().length()<6) {
                    Toast.makeText(requireContext(), "Password length must be minimum 6", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(binding.newPasswordEditText.getText().toString().isEmpty()) {
                    Toast.makeText(requireContext(), "Password is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(binding.newPasswordEditText.getText().toString().length()<6) {
                    Toast.makeText(requireContext(), "Password length must be minimum 6", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(binding.confirmPasswordEditText.getText().toString().isEmpty()) {
                    Toast.makeText(requireContext(), "Password is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(binding.confirmPasswordEditText.getText().toString().length()<6) {
                    Toast.makeText(requireContext(), "Password length must be minimum 6", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(binding.newPasswordEditText.getText().toString().
                        equals(binding.confirmPasswordEditText.getText().toString())) {
                    binding.loadingSection.getRoot().setVisibility(View.VISIBLE);
                    mUser.reauthenticate(EmailAuthProvider.getCredential(mUser.getEmail(),
                            binding.currentPasswordEditText.getText().toString()))
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        mUser.updatePassword(binding.confirmPasswordEditText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()) {
                                                    binding.loadingSection.getRoot().setVisibility(View.INVISIBLE);
                                                    Toast.makeText(getContext(),"Password Updated",Toast.LENGTH_LONG).show();
                                                    binding.currentPasswordEditText.setText("");
                                                    binding.newPasswordEditText.setText("");
                                                    binding.confirmPasswordEditText.setText("");
//                                                    startActivity(new Intent(getContext(),login.class));
//                                                    finish();
                                                } else {
                                                    String errorMessage = task.getException().getMessage();
                                                    binding.loadingSection.getRoot().setVisibility(View.INVISIBLE);
                                                    Toast.makeText(getContext(),"Try again",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        binding.loadingSection.getRoot().setVisibility(View.INVISIBLE);
                                        Toast.makeText(getContext(), "Password Authentication Failure!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(getContext(), "Password didn't match",Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}