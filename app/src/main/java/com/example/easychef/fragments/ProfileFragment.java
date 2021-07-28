package com.example.easychef.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.easychef.activities.LoginActivity;
import com.example.easychef.databinding.FragmentProfileBinding;
import com.parse.ParseUser;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnLogout.setOnClickListener(new LogoutButtonViewOnClickListener());
    }

    private void goToLoginActivity() {
        startActivity(new Intent(getContext(), LoginActivity.class));
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        getActivity().finish();
    }

    public class LogoutButtonViewOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            ParseUser.logOut();
            goToLoginActivity();
        }
    }
}