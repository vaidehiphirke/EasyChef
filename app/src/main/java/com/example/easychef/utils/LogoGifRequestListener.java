package com.example.easychef.utils;

import androidx.annotation.Nullable;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class LogoGifRequestListener implements RequestListener<GifDrawable> {
    private static final int NUMBER_OF_TIMES_TO_LOOP_LOGO_GIF = 1;

    @Override
    public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
        return false;
    }

    @Override
    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
        resource.setLoopCount(NUMBER_OF_TIMES_TO_LOOP_LOGO_GIF);
        return false;
    }
}

