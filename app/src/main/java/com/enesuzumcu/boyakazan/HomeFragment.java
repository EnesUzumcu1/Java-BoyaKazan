package com.enesuzumcu.boyakazan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.enesuzumcu.boyakazan.databinding.FragmentHomeBinding;
import com.enesuzumcu.boyakazan.model.Player;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private NavController navController;
    private Long backPressTime = 0L;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tanimlamalar();
        binding.btnOyna.setOnClickListener(v -> navController.navigate(R.id.action_homeFragment_to_gameFragment)) ;
        binding.btnNasilOynanir.setOnClickListener( v-> navController.navigate(R.id.action_homeFragment_to_howtoplayFragment)) ;
        binding.btnAyarlar.setOnClickListener( v->navController.navigate(R.id.action_homeFragment_to_settingsFragment) );

        //BackPress basildigi zaman yapilacak islemler icin callback eklendi
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                onBackPressed();
            }
        });
    }

    private void tanimlamalar() {
        binding.enGenisLayout.getBackground().setAlpha(50);
        navController = NavHostFragment.findNavController(this);
        if (Player.player1 == null) {
            Player.player1 = new Player(
                    getString(R.string.player1DefaultName),
                    ContextCompat.getColor(requireContext(), R.color.black)
            );
        }
        if (Player.player2 == null) {
            Player.player2 = new Player(
                    getString(R.string.player2DefaultName),
                    ContextCompat.getColor(requireContext(), R.color.red)
            );
        }
    }

    private void onBackPressed() {
        //backpress toplamda 2 saniyeden uzun sure basilirsa uygulama kapanmasi saglandi
        Toast toast = Toast.makeText(
                getContext(),
        "Uygulamadan çıkmak için bir daha tıklayın.",
                Toast.LENGTH_SHORT
        );
        if (backPressTime + 2000 > System.currentTimeMillis()) {
            toast.cancel();
            requireActivity().finish();
        } else {
            toast.show();
        }
        backPressTime = System.currentTimeMillis();
    }
}
