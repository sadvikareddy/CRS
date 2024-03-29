package com.campusrecruitmentsystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.campusrecruitmentsystem.databinding.ActivityAboutUsBinding;

public class AboutUsFragment extends Fragment {

    private ActivityAboutUsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = ActivityAboutUsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

}