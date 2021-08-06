package com.example.easychef.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.easychef.R;
import com.example.easychef.activities.LoginActivity;
import com.example.easychef.databinding.FragmentProfileBinding;
import com.example.easychef.utils.LogoGifRequestListener;
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

        Glide.with(this)
                .asGif()
                .load(R.drawable.logo_gif)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .listener(new LogoGifRequestListener())
                .into(binding.ivLogo);

        binding.currentUser.setText(String.format("Username: %s", ParseUser.getCurrentUser().getUsername()));
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