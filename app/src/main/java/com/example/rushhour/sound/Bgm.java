package com.example.rushhour.sound;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.example.rushhour.R;

public class Bgm extends Service {

    // 음악 재생 객체
    MediaPlayer player;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        // 플래이어 객체 생성
        player = MediaPlayer.create(this, R.raw.bgm);
        // 반복재생
        player.setLooping(true);
    }

    @Override
    public void onDestroy() {
        // 음악 정지
        player.stop();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("LTE", "서비스는 시작함");
        // 음악 재생
        player.start();
        return super.onStartCommand(intent, flags, startId);
    }
}