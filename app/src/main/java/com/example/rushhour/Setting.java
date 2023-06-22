package com.example.rushhour;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rushhour.DB.DBController;
import com.example.rushhour.DB.Player;
import com.example.rushhour.sound.SoundEffect;

public class Setting extends AppCompatActivity {

    Switch bgm, effect;
    RadioButton type1, type2, type3;
    RadioGroup group;
    ImageView img;


    int colorType;
    DBController dbController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        bgm = (Switch) findViewById(R.id.bgm);
        effect = (Switch) findViewById(R.id.effect);
        group = (RadioGroup) findViewById(R.id.group);
        type1 = (RadioButton) findViewById(R.id.type1);
        type2 = (RadioButton) findViewById(R.id.type2);
        type3 = (RadioButton) findViewById(R.id.type3);
        img = (ImageView) findViewById(R.id.img);

        dbController = new DBController(this);
        set();
        colorTypeChanged();
    }

    // 세팅 상태를 Player와 일치 시키는 메소드
    public void set(){
        effect.setChecked(Player.isSoundEffect());
        bgm.setChecked(Player.isBgm());

        switch (Player.getColor()) {
            case 0:
                type1.setChecked(true);
                break;
            case 1:
                type2.setChecked(true);
                break;
            case 2:
                type3.setChecked(true);
                break;
        }
    }

    // 버튼 클릭시 호출
    public void onClick(View view) {
        if (Player.isSoundEffect()) {
            startService(new Intent(this, SoundEffect.class));
        } // 효과음 재생
        int id = view.getId();
        if (id == R.id.type1 || id == R.id.type2 || id == R.id.type3) {
            colorTypeChanged();
        }
    }

    // 선택된 테마 표시
    public void colorTypeChanged() {
        switch (group.getCheckedRadioButtonId()) {
            case R.id.type1:
                img.setImageResource(R.drawable.type1);
                colorType = 0;
                break;
            case R.id.type2:
                img.setImageResource(R.drawable.type2);
                colorType = 1;
                break;
            case R.id.type3:
                img.setImageResource(R.drawable.type3);
                colorType = 2;
                break;
        }
    }

    // 홈으로 이동 버튼 클릭시 호출
    public void gotoMenu(View view) {
        if (Player.isSoundEffect()) {
            startService(new Intent(this, SoundEffect.class));
         } // 효과음

        // DB에 세팅값 저장
        int effectV = effect.isChecked() ? 1: 0;
        int bgmV = bgm.isChecked() ? 1: 0;
        Log.d("effect", ""+effectV);
        Log.d("bgm", ""+bgmV);
        Log.d("color", ""+colorType);

        dbController.updateSetting(effectV, bgmV, colorType);

        // main으로 이동
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
