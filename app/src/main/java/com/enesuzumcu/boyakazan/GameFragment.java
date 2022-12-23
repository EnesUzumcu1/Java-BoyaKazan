package com.enesuzumcu.boyakazan;

import static com.enesuzumcu.boyakazan.ViewExtensions.updateBackgroundResource;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.enesuzumcu.boyakazan.databinding.FragmentGameBinding;
import com.enesuzumcu.boyakazan.model.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class GameFragment extends Fragment {
    private FragmentGameBinding binding;
    private NavController navController;

    private final Integer maxValueYAxis = 5;
    private Integer yAxis1 = 0;
    private Integer yAxis2 = 0;
    private Integer yAxis3 = 0;
    private Integer yAxis4 = 0;
    private Integer yAxis5 = 0;
    private Integer yAxis6 = 0;
    private Integer yAxis7 = 0;
    private Integer yAxis8 = 0;
    private Integer yAxis9 = 0;
    private Integer yAxis10 = 0;
    //5
    //4                         maxValueYAxis max yuksekligi belirliyor
    //3                         koordinatlar arrayi icindeki viewler bu numaralandirmaya gore cagiriliyor
    //2                         en ustteki view en yuksek maxValueYAxis sahip
    //1                         //<-koordinatlar y ekseni sirasi
    //0
    //0 1 2 3 4 5 6 7 8 9       <-koordinatlar x ekseni sirasi

    //true - Player1 / false - Player2
    private Boolean currentPlayer = true;

    private Long backPressTime = 0L;
    private ImageView[][] koordinatlar;
    private ArrayList<ArrayList<Integer>> moveRecorderArray;
    private Boolean isBuilderExisting = false;

    //max tiklanma sayisi ulasmasina ragmen oyun bitmediyse beraberlik vermek icin sayici olusturuldu
    private Integer clickCount = 0;
    private final Integer maxClickCount = 60;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentGameBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        baslangic();
        atamalar();
        setBtnClick();

        //backpress basildiginda gerekli durum icin callback cagirildi
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                onBackPressed();
            }
        });
    }

    private void atamalar() {
        //2 boyutlu bir array olusturuluyor ve viewler sirasina gore ataniyor (10 genislik 6 yukseklik)
        koordinatlar = new ImageView[][]{
                {binding.iv60, binding.iv61, binding.iv62, binding.iv63, binding.iv64, binding.iv65, binding.iv66, binding.iv67, binding.iv68, binding.iv69},
                {binding.iv50, binding.iv51, binding.iv52, binding.iv53, binding.iv54, binding.iv55, binding.iv56, binding.iv57, binding.iv58, binding.iv59},
                {binding.iv40, binding.iv41, binding.iv42, binding.iv43, binding.iv44, binding.iv45, binding.iv46, binding.iv47, binding.iv48, binding.iv49},
                {binding.iv30, binding.iv31, binding.iv32, binding.iv33, binding.iv34, binding.iv35, binding.iv36, binding.iv37, binding.iv38, binding.iv39},
                {binding.iv20, binding.iv21, binding.iv22, binding.iv23, binding.iv24, binding.iv25, binding.iv26, binding.iv27, binding.iv28, binding.iv29},
                {binding.iv10, binding.iv11, binding.iv12, binding.iv13, binding.iv14, binding.iv15, binding.iv16, binding.iv17, binding.iv18, binding.iv19}
        };
        //butun viewlerin background degistiliyor
        for (int i = 0; i <= 5; i++) {
            for (int j = 0; j <= 9; j++) {
                koordinatlar[i][j].setBackgroundResource(R.drawable.buton_border_custom);
            }
        }
        //yapilan hamlelerin geri alinmasi icin her hamle kaydediliyor
        moveRecorderArray = new ArrayList<>();
    }

    private void baslangic() {
        navController = NavHostFragment.findNavController(this);

        binding.tvPlayer1Name.setText(Player.player1.name);
        binding.tvPlayer2Name.setText(Player.player2.name);

        updateBackgroundResource(Player.player1, binding.ivOyuncu1Rengi, true);
        updateBackgroundResource(Player.player2, binding.ivOyuncu2Rengi, false);

        //oyun oyuncu1'in baslamasi icin oyuncu2'nin viewi invisible yapiliyor
        binding.ivPlayer2Turn.setVisibility(View.INVISIBLE);
        binding.clOyuncu1.setBackgroundColor(Color.argb(40, 0, 255, 255));
        binding.clOyuncu2.setBackgroundColor(Color.TRANSPARENT);
    }

    private Boolean checkMoves(Integer yAxis, Integer xAxis) {
        //yAxis yuksekligi maxValueYAxis kucukse mevcut sutun yerindeki viewin backgroundunu degistirir.
        if (yAxis <= maxValueYAxis) {
            //hamle kaydedildi.
            moveRecorderArray.add(new ArrayList<>(Arrays.asList(yAxis, xAxis)));
            //renkDegisdeki degere gore oyuncu1 veya oyuncu2 nin rengi backgrounda set edilir.
            if (currentPlayer) {
                //player1
                koordinatlar[yAxis][xAxis].setBackgroundColor(Player.player1.color);
            } else {
                //player2
                koordinatlar[yAxis][xAxis].setBackgroundColor(Player.player2.color);
            }
            showNextPlayer(currentPlayer);
            allControls(yAxis, xAxis);
            //currentPlayer degistirildi bu sayede diger oyuncuya sira gecmis oldu
            currentPlayer = !currentPlayer;

            clickCount++;
            if (clickCount.equals(maxClickCount)) showAlertDialog(getString(R.string.beraberlik));
            return true;
        } else return false;
    }

    private void setBtnClick() {
        binding.buttonGeriAl.setOnClickListener(v -> {
            //geri alma islemi icin sirayi eski oyuncuya ceviriyor, eski koordinattaki view eski background olarak setleniyor
            if (moveRecorderArray.size() > 0) {
                showNextPlayer(currentPlayer);
                currentPlayer = !currentPlayer;
                koordinatlar[moveRecorderArray.get(moveRecorderArray.size() - 1).get(0)][moveRecorderArray.get(moveRecorderArray.size() - 1).get(1)]
                        .setBackgroundResource(R.drawable.buton_border_custom);
                //geri alindigi zaman sutun sayisi arttiriliyor. sutun sayisi en alttan satirdan baslayip 5 ile baslar
                //oyuncu bir hamle yaptigi zaman sutun sayisi azalir, o yuzden burada arttiliyor.
                switch (moveRecorderArray.get(moveRecorderArray.size() - 1).get(1)) {
                    case 0: {
                        yAxis1--;
                        break;
                    }
                    case 1:
                        yAxis2--;
                        break;
                    case 2:
                        yAxis3--;
                        break;
                    case 3:
                        yAxis4--;
                        break;
                    case 4:
                        yAxis5--;
                        break;
                    case 5:
                        yAxis6--;
                        break;
                    case 6:
                        yAxis7--;
                        break;
                    case 7:
                        yAxis8--;
                        break;
                    case 8:
                        yAxis9--;
                        break;
                    case 9:
                        yAxis10--;
                        break;
                }
                //hamle geri alindiktan sonra arrayden silindi
                moveRecorderArray.remove(moveRecorderArray.size() - 1);
                //hamel geri alininca tiklama sayisida azaltiliyor
                clickCount--;
            }
        });
        binding.xAxis1.setOnClickListener(view -> {
            if (checkMoves(yAxis1, 0)) {
                //bu sutundaki deger azaltildi bu sayede bir ust view'a gecildi
                yAxis1++;
            }
        });
        binding.xAxis2.setOnClickListener(view -> {
            if (checkMoves(yAxis2, 1)) {
                yAxis2++;
            }
        });
        binding.xAxis3.setOnClickListener(view -> {
            if (checkMoves(yAxis3, 2)) {
                yAxis3++;
            }
        });
        binding.xAxis4.setOnClickListener(view -> {
            if (checkMoves(yAxis4, 3)) {
                yAxis4++;
            }
        });
        binding.xAxis5.setOnClickListener(view -> {
            if (checkMoves(yAxis5, 4)) {
                yAxis5++;
            }
        });
        binding.xAxis6.setOnClickListener(view -> {
            if (checkMoves(yAxis6, 5)) {
                yAxis6++;
            }
        });
        binding.xAxis7.setOnClickListener(view -> {
            if (checkMoves(yAxis7, 6)) {
                yAxis7++;
            }
        });
        binding.xAxis8.setOnClickListener(view -> {
            if (checkMoves(yAxis8, 7)) {
                yAxis8++;
            }
        });
        binding.xAxis9.setOnClickListener(view -> {
            if (checkMoves(yAxis9, 8)) {
                yAxis9++;
            }
        });
        binding.xAxis10.setOnClickListener(view -> {
            if (checkMoves(yAxis10, 9)) {
                yAxis10++;
            }
        });

    }

    private void showNextPlayer(Boolean currentPlayer) {
        //sira oyuncu1deyken oyuncu2, oyuncu2deyken oyuncu1'in alanlari gizlendi ve background renkleri degistirildi
        //true - Player1 / false - Player2
        if (currentPlayer) {
            //ilk tıklamada burası calisiyor
            binding.ivPlayer1Turn.setVisibility(View.INVISIBLE);
            binding.ivPlayer2Turn.setVisibility(View.VISIBLE);
            binding.clOyuncu2.setBackgroundColor(
                    Color.argb(
                            40,
                            0,
                            255,
                            255
                    )
            );
            binding.clOyuncu1.setBackgroundColor(Color.TRANSPARENT);
        } else {
            binding.ivPlayer1Turn.setVisibility(View.VISIBLE);
            binding.ivPlayer2Turn.setVisibility(View.INVISIBLE);
            binding.clOyuncu1.setBackgroundColor(
                    Color.argb(
                            40,
                            0,
                            255,
                            255
                    )
            );
            binding.clOyuncu2.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    private void checkVertical(Integer yAxis, Integer xAxis) {
        //en son tiklanilan buton (x olarak gosterilen)baz alinarak arraydeki belirtilen sekilde kontrol ediliyor
        //x
        //o
        //o
        //o
        baseCheck(
                yAxis,
                xAxis,
                -1,
                -2,
                -3,
                0,
                0,
                0
        );
    }

    private void checkLeftTopRightBottom1(Integer yAxis, Integer xAxis) {
        //x---
        //-o--
        //--o-
        //---o
        baseCheck(
                yAxis,
                xAxis,
                -1,
                -2,
                -3,
                +1,
                +2,
                +3
        );
    }

    private void checkLeftTopRightBottom2(Integer yAxis, Integer xAxis) {
        //o---
        //-x--
        //--o-
        //---o
        baseCheck(
                yAxis,
                xAxis,
                +1,
                -1,
                -2,
                -1,
                +1,
                +2
        );
    }

    private void checkLeftTopRightBottom3(Integer yAxis, Integer xAxis) {
        //o---
        //-o--
        //--x-
        //---o
        baseCheck(
                yAxis,
                xAxis,
                +1,
                +2,
                -1,
                -1,
                -2,
                +1
        );
    }

    private void checkLeftTopRightBottom4(Integer yAxis, Integer xAxis) {
        //o---
        //-o--
        //--o-
        //---x
        baseCheck(
                yAxis,
                xAxis,
                +1,
                +2,
                +3,
                -1,
                -2,
                -3
        );
    }

    private void checkRightTopLeftBottom1(Integer yAxis, Integer xAxis) {
        //---x
        //--o-
        //-o--
        //o---
        baseCheck(
                yAxis,
                xAxis,
                -1,
                -2,
                -3,
                -1,
                -2,
                -3
        );
    }

    private void checkRightTopLeftBottom2(Integer yAxis, Integer xAxis) {
        //---o
        //--x-
        //-o--
        //o---
        baseCheck(
                yAxis,
                xAxis,
                +1,
                -1,
                -2,
                +1,
                -1,
                -2
        );
    }

    private void checkRightTopLeftBottom3(Integer yAxis, Integer xAxis) {
        //---o
        //--o-
        //-x--
        //o---
        baseCheck(
                yAxis,
                xAxis,
                +1,
                +2,
                -1,
                +1,
                +2,
                -1
        );
    }

    private void checkRightTopLeftBottom4(Integer yAxis, Integer xAxis) {
        //---o
        //--o-
        //-o--
        //x---
        baseCheck(
                yAxis,
                xAxis,
                +1,
                +2,
                +3,
                +1,
                +2,
                +3
        );
    }

    private void checkHorizontal1(Integer yAxis, Integer xAxis) {
        //xooo
        baseCheck(
                yAxis,
                xAxis,
                0,
                0,
                0,
                +1,
                +2,
                +3
        );
    }

    private void checkHorizontal2(Integer yAxis, Integer xAxis) {
        //oxoo
        baseCheck(
                yAxis,
                xAxis,
                0,
                0,
                0,
                -1,
                +1,
                +2
        );
    }

    private void checkHorizontal3(Integer yAxis, Integer xAxis) {
        //ooxo
        baseCheck(
                yAxis,
                xAxis,
                0,
                0,
                0,
                -1,
                -2,
                +1
        );
    }

    private void checkHorizontal4(Integer yAxis, Integer xAxis) {
        //ooox
        baseCheck(
                yAxis,
                xAxis,
                0,
                0,
                0,
                -1,
                -2,
                -3
        );
    }

    private void baseCheck(
            Integer yAxis,
            Integer xAxis,
            Integer distanceFromYAxis1,
            Integer distanceFromYAxis2,
            Integer distanceFromYAxis3,
            Integer distanceFromXAxis1,
            Integer distanceFromXAxis2,
            Integer distanceFromXAxis3
    ) {
        //en son tiklanilan buton baz alinarak arraydeki belirtilen sekilde kontrol ediliyor
        //toplamda 4 view backgroundlari ayni mi diye kontrol ediliyor
        //eger hepsi ayni ise oyun sonlandirma asamasi basliyor
        //kontroller array disina gidebilecegi icin try-catch icinde yapiliyor

        //----
        //----              saga dogru gitmek icin deger arttirmak gerekiyor, sola dogru azalmak gerekiyor
        //----              yukari cikmak icin deger azalmak gerekiyor, asagı dogru arttırmak gerekiyor
        //----
        //satirDegeri yukardan asagi dogru hangi satirda oldugunun degeri
        //sutun sagdan sola hangi sutun oldugunun degeri
        try {
            ColorDrawable a = (ColorDrawable) koordinatlar[yAxis][xAxis].getBackground();
            ColorDrawable b = (ColorDrawable) koordinatlar[yAxis + (distanceFromYAxis1)][xAxis + (distanceFromXAxis1)].getBackground();
            ColorDrawable c = (ColorDrawable) koordinatlar[yAxis + (distanceFromYAxis2)][xAxis + (distanceFromXAxis2)].getBackground();
            ColorDrawable d = (ColorDrawable) koordinatlar[yAxis + (distanceFromYAxis3)][xAxis + (distanceFromXAxis3)].getBackground();
            if (a.getColor() == b.getColor() && a.getColor() == c.getColor() && a.getColor() == d.getColor()) {
                int[][] dogruKoordinatlar = new int[][]{
                        {yAxis, xAxis},
                        {yAxis + (distanceFromYAxis1), xAxis + (distanceFromXAxis1)},
                        {yAxis + (distanceFromYAxis2), xAxis + (distanceFromXAxis2)},
                        {yAxis + (distanceFromYAxis3), xAxis + (distanceFromXAxis3)}
                };
                result(dogruKoordinatlar);
            }
        } catch (Exception ignored) {

        }
    }

    private void allControls(Integer yAxis, Integer xAxis) {
        //tum kontroller her hamleden sonra buradan cagiriliyor
        checkVertical(yAxis, xAxis);
        checkLeftTopRightBottom1(yAxis, xAxis);
        checkLeftTopRightBottom2(yAxis, xAxis);
        checkLeftTopRightBottom3(yAxis, xAxis);
        checkLeftTopRightBottom4(yAxis, xAxis);
        checkRightTopLeftBottom1(yAxis, xAxis);
        checkRightTopLeftBottom2(yAxis, xAxis);
        checkRightTopLeftBottom3(yAxis, xAxis);
        checkRightTopLeftBottom4(yAxis, xAxis);
        checkHorizontal1(yAxis, xAxis);
        checkHorizontal2(yAxis, xAxis);
        checkHorizontal3(yAxis, xAxis);
        checkHorizontal4(yAxis, xAxis);
    }

    private void result(int[][] coordinates) {
        //bir oyuncu kazandigi zaman 10 adet viewin bulundugu viewler gizleniyor
        binding.butonlarSatiri.setVisibility(View.INVISIBLE);
        binding.buttonGeriAl.setVisibility(View.INVISIBLE);
        showNextPlayer(!currentPlayer);
        winEffect(coordinates);
    }

    private void winEffect(int[][] coordinate) {
        //kazanan oyuncunun nasil kazandigini gostermek icin kazandigi kosullardaki viewler yanip sonme efekti ile gosteriliyor
        int winnerColor;
        if (currentPlayer) {
            winnerColor = Player.player1.color;
        } else {
            winnerColor = Player.player2.color;
        }
        final int[] controlCount = {0};
        ArrayList<Integer> colors = new ArrayList<>();
        //bu renkler sırayla yanıp sönerek kazananın kazandigi koordinatlari belli ediyor
        colors.add(winnerColor);
        colors.add(Color.TRANSPARENT);
        new CountDownTimer(3000, 500) {
            @Override
            public void onTick(long millisUntilFinished) {
                koordinatlar[coordinate[0][0]][coordinate[0][1]].setBackgroundColor(colors.get(controlCount[0] % 2));
                koordinatlar[coordinate[1][0]][coordinate[1][1]].setBackgroundColor(colors.get(controlCount[0] % 2));
                koordinatlar[coordinate[2][0]][coordinate[2][1]].setBackgroundColor(colors.get(controlCount[0] % 2));
                koordinatlar[coordinate[3][0]][coordinate[3][1]].setBackgroundColor(colors.get(controlCount[0] % 2));
                controlCount[0]++;
            }

            @Override
            public void onFinish() {
                koordinatlar[coordinate[0][0]][coordinate[0][1]].setBackgroundColor(winnerColor);
                koordinatlar[coordinate[1][0]][coordinate[1][1]].setBackgroundColor(winnerColor);
                koordinatlar[coordinate[2][0]][coordinate[2][1]].setBackgroundColor(winnerColor);
                koordinatlar[coordinate[3][0]][coordinate[3][1]].setBackgroundColor(winnerColor);
                //eğer aynı anda birden fazla şekilde kazanma durumu varsa birden fazla builder oluşturmamak için kontrol
                if (!isBuilderExisting) showAlertDialog(getString(R.string.galibiyet));
            }
        }.start();
    }

    private void showAlertDialog(String durum) {
        //oyunun bittigini gostermek icin alertdialog ile kullanici bilgilendiriliyor
        //ayni anda birden fazla kontrol dogru cikmasi ihtimaline karsi isBuilderExisting ile yalnizca 1 kez olusturulmasi saglaniyor
        isBuilderExisting = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Tebrikler!");
        String winner;
        if (!currentPlayer) {
            winner = Player.player1.name;
        } else {
            winner = Player.player2.name;
        }
        builder.setMessage(winner+" kazandı.");
        if (durum.equals(getString(R.string.beraberlik))) {
            builder.setTitle("Beraberlik!");
            builder.setMessage("Kazanan çıkmadı.");
        }
        builder.setCancelable(false);
        builder.setNeutralButton("Girişe Git", ((dialogInterface, i) -> navController.navigate(R.id.action_gameFragment_to_homeFragment)));
        builder.setNegativeButton("Tekrar Oyna", (dialogInterface, i) -> {
            //tekrar oynanmasi icin oyuncu adlari ve renkleri tekrar gonderiliyor
            navController.navigate(R.id.action_gameFragment_self);
        });
        builder.show();
    }

    private void onBackPressed() {
        //kullanici oyun ekranindayken backpress durumunda toplamda 2 saniyeden fazla basarsa geri yonlerdirme yapiliyor
        Toast toast= Toast.makeText(getContext(), "Giriş sayfasına gitmek için bir daha tıklayın.", Toast.LENGTH_SHORT);
        if (backPressTime + 2000 > System.currentTimeMillis()) {
            toast.cancel();
            navController.navigate(R.id.action_gameFragment_to_homeFragment);
        } else {
            toast.show();
        }
        backPressTime = System.currentTimeMillis();
    }
}
