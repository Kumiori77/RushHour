package com.example.rushhour.sound;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.example.rushhour.R;

public class SoundEffect extends Service {
    // 음악 재생 객체
    MediaPlayer player;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        // 플래이어 객체 생성
        player = MediaPlayer.create(this, R.raw.effect4);
        // 반복재생
        player.setLooping(false);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 음악 재생
        player.start();
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

}
