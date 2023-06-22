package com.example.rushhour;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.rushhour.DB.DBController;
import com.example.rushhour.DB.Player;
import com.example.rushhour.sound.Bgm;
import com.example.rushhour.sound.SoundEffect;

public class MainActivity extends AppCompatActivity {

    DBController dbController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // DB 컨트롤러 객체
        dbController = new DBController(this);


    }

    @Override
    public void onResume(){
        super.onResume();

        // 최초 정보 메소드 호출
        firstInfo();

        // DB에 저장된 결과를 Player 객체에 반영
        getSettingInfo();

        // 배경음 재생
        if (Player.isBgm()) {
            startService(new Intent(this, Bgm.class));
        }
        else {
            stopService(new Intent(this, Bgm.class));
        }
    }

    // 버튼 클릭
    public void onClick(View view) {
        // 효과음
        if (Player.isSoundEffect()) {
            startService(new Intent(this, SoundEffect.class));
        }
        Intent intent;
        switch (view.getId()){
            case R.id.play:
                intent = new Intent(getApplicationContext(), LevelSelect.class);
                startActivity(intent);
                break;
            case R.id.help:
                intent = new Intent(getApplicationContext(), Help.class);
                startActivity(intent);
                break;
            case R.id.setting:
                intent = new Intent(getApplicationContext(), Setting.class);
                startActivity(intent);
                break;
            case R.id.records:
                intent = new Intent(getApplicationContext(), ShowRecords.class);
                startActivity(intent);
                break;
        }
    }

    // 초기에 정보 입력 메소드
    public void firstInfo() {
        if (!dbController.checkFirstValue()) {
            dbController.insert(1, 1, 1, 1, 0);
        }
    }
    // 설정 정보 받아와서 Player객체 변경
    public void getSettingInfo() {
        int[] result = dbController.selectPlayer();

        Player.setLevel(result[0]);
        Player.setSoundEffect(result[1]==1);
        Player.setBgm(result[2]==1);
        Player.setColor(result[3]);
    }
}