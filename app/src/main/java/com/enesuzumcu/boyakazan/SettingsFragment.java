package com.enesuzumcu.boyakazan;

import static com.enesuzumcu.boyakazan.ViewExtensions.updateBackgroundResource;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.enesuzumcu.boyakazan.databinding.FragmentSettingsBinding;
import com.enesuzumcu.boyakazan.model.Player;

public class SettingsFragment extends Fragment {
    private FragmentSettingsBinding binding;
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tanimlamalar();
        binding.btnSave.setOnClickListener(v -> saveChanges());
    }

    private void tanimlamalar() {
        navController = NavHostFragment.findNavController(this);
        //varsayilan olarak oyuncu1 black, oyuncu2 red renkle basliyor
        updateBackgroundResource(Player.player1, binding.ivSecilenOyuncu1Rengi, true);
        updateBackgroundResource(Player.player2, binding.ivSecilenOyuncu2Rengi, false);
        //her butona ait renk tag olarak eklendi
        setTagColorButtons();
        //renk secmesi icin imageviewlere onClick atamasi yapiliyor
        setOnClickPlayer1Buttons(binding.btnOyuncu1Renk1);
        setOnClickPlayer1Buttons(binding.btnOyuncu1Renk2);
        setOnClickPlayer1Buttons(binding.btnOyuncu1Renk3);
        setOnClickPlayer1Buttons(binding.btnOyuncu1Renk4);
        setOnClickPlayer1Buttons(binding.btnOyuncu1Renk5);
        setOnClickPlayer1Buttons(binding.btnOyuncu1Renk6);
        setOnClickPlayer1Buttons(binding.btnOyuncu1Renk7);
        setOnClickPlayer1Buttons(binding.btnOyuncu1Renk8);
        setOnClickPlayer1Buttons(binding.btnOyuncu1Renk9);

        setOnClickPlayer2Buttons(binding.btnOyuncu2Renk1);
        setOnClickPlayer2Buttons(binding.btnOyuncu2Renk2);
        setOnClickPlayer2Buttons(binding.btnOyuncu2Renk3);
        setOnClickPlayer2Buttons(binding.btnOyuncu2Renk4);
        setOnClickPlayer2Buttons(binding.btnOyuncu2Renk5);
        setOnClickPlayer2Buttons(binding.btnOyuncu2Renk6);
        setOnClickPlayer2Buttons(binding.btnOyuncu2Renk7);
        setOnClickPlayer2Buttons(binding.btnOyuncu2Renk8);
        setOnClickPlayer2Buttons(binding.btnOyuncu2Renk9);
    }

    private void setOnClickPlayer1Buttons(ImageView imageView) {
        imageView.setOnClickListener(v -> {
            //secilen butonun backgroundu ivSecilenOyuncu1Rengi viewine set ediliyor
            binding.ivSecilenOyuncu1Rengi.setBackground(imageView.getBackground());
            Player.player1.color = (ContextCompat.getColor(
                    requireContext(),
                    Integer.parseInt(imageView.getTag().toString())
            ));
        });
    }

    private void setOnClickPlayer2Buttons(ImageView imageView) {
        imageView.setOnClickListener(v -> {
            //secilen butonun backgroundu ivSecilenOyuncu2Rengi viewine set ediliyor
            binding.ivSecilenOyuncu2Rengi.setBackground(imageView.getBackground());
            Player.player2.color = (ContextCompat.getColor(
                    requireContext(),
                    Integer.parseInt(imageView.getTag().toString())
            ));
        });
    }

    private void setTagColorButtons() {
        binding.btnOyuncu1Renk1.setTag(R.color.lime);
        binding.btnOyuncu1Renk2.setTag(R.color.gray);
        binding.btnOyuncu1Renk3.setTag(R.color.black);
        binding.btnOyuncu1Renk4.setTag(R.color.green);
        binding.btnOyuncu1Renk5.setTag(R.color.orange);
        binding.btnOyuncu1Renk6.setTag(R.color.red);
        binding.btnOyuncu1Renk7.setTag(R.color.blue);
        binding.btnOyuncu1Renk8.setTag(R.color.brown);
        binding.btnOyuncu1Renk9.setTag(R.color.yellow);

        binding.btnOyuncu2Renk1.setTag(R.color.lime);
        binding.btnOyuncu2Renk2.setTag(R.color.gray);
        binding.btnOyuncu2Renk3.setTag(R.color.black);
        binding.btnOyuncu2Renk4.setTag(R.color.green);
        binding.btnOyuncu2Renk5.setTag(R.color.orange);
        binding.btnOyuncu2Renk6.setTag(R.color.red);
        binding.btnOyuncu2Renk7.setTag(R.color.blue);
        binding.btnOyuncu2Renk8.setTag(R.color.brown);
        binding.btnOyuncu2Renk9.setTag(R.color.yellow);
    }

    private void setPlayer1Name() {
        String player1Name = binding.etOyuncu1adi.getText().toString().trim();
        if (!player1Name.isEmpty()) {
            Player.player1.name = (player1Name);
        } else {
            Player.player1.name = (getString(R.string.player1DefaultName));
            binding.oyuncu1adiEditText.setError(null);
        }
    }

    private void setPlayer2Name() {
        String player2Name = binding.etOyuncu2adi.getText().toString().trim();
        if (!player2Name.isEmpty()) {
            Player.player2.name =(player2Name);
        } else {
            Player.player2.name =(getString(R.string.player2DefaultName));
            binding.oyuncu2adiEditText.setError(null);
        }
    }

    //iki oyuncu adi ayni mi diye kontrol ediliyor
    private Boolean checkPlayersNames() {
        if (Player.player1.name.equals(Player.player2.name)) {
            binding.oyuncu1adiEditText.setError("Oyuncu adları aynı olamaz!");
            binding.oyuncu2adiEditText.setError("Oyuncu adları aynı olamaz!");
            return false;
        } else {
            binding.oyuncu1adiEditText.setError(null);
            binding.oyuncu2adiEditText.setError(null);
            return true;
        }
    }

    //oyuncu secilen renkler kontrol ediliyor
    private Boolean checkPlayersColors() {
        if (Player.player1.color.equals(Player.player2.color)) {
            Toast.makeText(getContext(), "İki renk aynı olamaz.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void saveChanges() {
        setPlayer1Name();
        setPlayer2Name();
        if (checkPlayersColors() && checkPlayersNames()) {
            navController.navigate(R.id.action_settingsFragment_to_gameFragment);
        }
    }
}
