package com.enesuzumcu.boyakazan;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.enesuzumcu.boyakazan.databinding.FragmentHowtoplayBinding;

import java.util.ArrayList;

public class HowtoplayFragment extends Fragment {
    private FragmentHowtoplayBinding binding;
    private NavController navController;
    ArrayList<Integer> resimler;
    ArrayList<String> aciklamalar;
    int i =0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHowtoplayBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        baslangic();
        binding.ileriButon.setOnClickListener(v -> {
            if(i == resimler.size()-1){
                i=-1;
                navController.navigate(R.id.action_howtoplayFragment_to_homeFragment);
            }
            i++;
            setGravity(i);
            binding.resimImageView.setImageResource(resimler.get(i));
            binding.aciklamaTextview.setText(aciklamalar.get(i));
        });
        binding.geriButon.setOnClickListener(v -> {
            if(i== 0){
                i = resimler.size();
            }
            i--;
            setGravity(i);
            binding.resimImageView.setImageResource(resimler.get(i));
            binding.aciklamaTextview.setText(aciklamalar.get(i));
        });
    }

    public void baslangic(){
        navController = NavHostFragment.findNavController(this);
        resimler = new ArrayList<>();
        resimler.add(R.drawable.telefonresim1);
        resimler.add(R.drawable.telefonresim2);
        resimler.add(R.drawable.telefonresim3);
        resimler.add(R.drawable.telefonresim4);
        resimler.add(R.drawable.telefonresim5);
        resimler.add(R.drawable.telefonresim6);
        resimler.add(R.drawable.telefonresim7);
        resimler.add(R.drawable.telefonresim8);
        resimler.add(R.drawable.telefonresim9);
        //0-255 arasi deger alir
        binding.resimImageView.setImageAlpha(150);
        aciklamalar = new ArrayList<>();
        aciklamalar.add(getString(R.string.aciklama1));
        aciklamalar.add(getString(R.string.aciklama2));
        aciklamalar.add(getString(R.string.aciklama3));
        aciklamalar.add(getString(R.string.aciklama4));
        aciklamalar.add(getString(R.string.aciklama5));
        aciklamalar.add(getString(R.string.aciklama6));
        aciklamalar.add(getString(R.string.aciklama7));
        aciklamalar.add(getString(R.string.aciklama8));
        aciklamalar.add(getString(R.string.aciklama9));

        binding.aciklamaTextview.setText(aciklamalar.get(0));
        binding.resimImageView.setImageResource(resimler.get(0));
    }

    public void setGravity(int i){
        if(i ==resimler.size()-1){
            binding.aciklamaTextview.setGravity(Gravity.NO_GRAVITY);
        }
        else{
            binding.aciklamaTextview.setGravity(Gravity.CENTER);
        }
    }
}
