package com.enesuzumcu.boyakazan;

import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.enesuzumcu.boyakazan.model.Player;

public class ViewExtensions {
    public static void updateBackgroundResource(Player player, ImageView imageView, Boolean isPlayer1) {
        if (player == null) {
            if (isPlayer1) {
                imageView.setBackgroundResource(R.drawable.custom_button_black);
            } else {
                imageView.setBackgroundResource(R.drawable.custom_button_red);
            }
        } else {
            int color = player.color;
            if (color == ContextCompat.getColor(imageView.getContext(), R.color.lime)) {
                imageView.setBackgroundResource(R.drawable.custom_button_lime);
            } else if (color == ContextCompat.getColor(imageView.getContext(), R.color.gray)) {
                imageView.setBackgroundResource(R.drawable.custom_button_gray);
            } else if (color == ContextCompat.getColor(imageView.getContext(), R.color.black)) {
                imageView.setBackgroundResource(R.drawable.custom_button_black);
            } else if (color == ContextCompat.getColor(imageView.getContext(), R.color.green)) {
                imageView.setBackgroundResource(R.drawable.custom_button_green);
            } else if (color == ContextCompat.getColor(imageView.getContext(), R.color.orange)) {
                imageView.setBackgroundResource(R.drawable.custom_button_orange);
            } else if (color == ContextCompat.getColor(imageView.getContext(), R.color.red)) {
                imageView.setBackgroundResource(R.drawable.custom_button_red);
            } else if (color == ContextCompat.getColor(imageView.getContext(), R.color.blue)) {
                imageView.setBackgroundResource(R.drawable.custom_button_blue);
            } else if (color == ContextCompat.getColor(imageView.getContext(), R.color.brown)) {
                imageView.setBackgroundResource(R.drawable.custom_button_brown);
            } else if (color == ContextCompat.getColor(imageView.getContext(), R.color.yellow)) {
                imageView.setBackgroundResource(R.drawable.custom_button_yellow);
            }
        }
    }
}