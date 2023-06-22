package com.example.rushhour;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rushhour.DB.Player;
import com.example.rushhour.sound.Bgm;
import com.example.rushhour.sound.SoundEffect;

public class Help extends AppCompatActivity {

    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
    }

    public void gotoMenu(View view) {
        if (Player.isSoundEffect()) {
            startService(new Intent(this, SoundEffect.class));
        } // 효과음 재생
        intent = new Intent(this, MainActivity.class);
        startActivity(intent); // 홈으로 이동
    }
}
