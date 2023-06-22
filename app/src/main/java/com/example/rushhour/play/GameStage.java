package com.example.rushhour.play;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rushhour.DB.DBController;
import com.example.rushhour.DB.Player;
import com.example.rushhour.MainActivity;
import com.example.rushhour.R;
import com.example.rushhour.sound.SoundEffect;

import java.util.Timer;
import java.util.TimerTask;

public class GameStage extends AppCompatActivity {

    DBController dbController;

    GamePlay gamePlay;

    LinearLayout stage;

    MediaPlayer player;

    Button start;
    TextView time;

    TextView showLevel;

    TimerTask timerTask;
    Timer timer;

    int level;

    int second = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_play);

        // DB 컨트롤러 객체
        dbController = new DBController(this);

        start = (Button) findViewById(R.id.start);
        time = (TextView) findViewById(R.id.time);
        stage = (LinearLayout) findViewById(R.id.satge);

        // 플래이할 레벨
        Intent getIntent = getIntent();
        level = getIntent.getIntExtra("level", 1);

        // 레벨 표시
        showLevel = (TextView) findViewById(R.id.level);
        showLevel.setText("Level "+level);

        // 게임 화면 그리는 GamePlay 객체 생성
        gamePlay = new GamePlay(this, level);
        stage.addView(gamePlay);

        // 타이머 객체 생성
        timer = new Timer();

        // 성공 효과음
        player = MediaPlayer.create(this, R.raw.complete);
        // 반복재생
        player.setLooping(false);
    }

    // 스타트 버튼 메소드
    public void start(View view) {

        if (Player.isSoundEffect()){
            // 효과음 재생
            startService(new Intent(this, SoundEffect.class));
        }

        String state = gamePlay.toggle();
        if (state.equals("PAUSE")) {
            // 게임 시작
            start.setText(state);
            startTimer();
        }
        else {
            // 게임 일시정지
            start.setText(state);
            timerTask.cancel();
        }
    }

    // 타이머 메소드
    public void startTimer() {
        if (timerTask != null) {
            timerTask.cancel();
        }
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override // 타이머 동작 처리
                    public void run() {
                        time.setText(showTime(second));
                        if (gamePlay.gameEnd) {
                            gameOver();
                            timerTask.cancel();
                        }
                    }
                });
                second += 1;
            }
        };
        timer.schedule(timerTask, 0, 10);
    }

    // 타이머 시간 표시 메소드
    public static String showTime(int second){
        Integer min, sec, mil;
        min = second / 6000;
        sec = (second % 6000) / 100;
        mil = (second % 6000) % 100;
        String timeStr = setTime(min) + ":" + setTime(sec) + ":" + setTime(mil);
        return timeStr;
    }

    // 타이머 시간 자리수 설정 메소드
    public static String setTime(Integer num) {
        return num > 9 ? num+"" : "0"+num;
    }

    // 리셋 메소드
    public void reset() {
        if (timerTask != null) {
            timerTask.cancel();
        }
        second = 0;
        time.setText("00:00:00");
        gamePlay.reset();
        start.setText("START");
    }
    // 리셋버튼 메소드
    public void onClickReset(View view) {
        if (Player.isSoundEffect()){
            // 효과음 재생
            startService(new Intent(this, SoundEffect.class));
        }
        reset();
    }

    // 게임 오버 처리
    public void gameOver() {
        // 축하음 출력
        if (Player.isSoundEffect()) {
            player.start();
        }
        String msg;
        // 기록 처리
        msg = saveRecord(level, second);

        // 대화상자 출력
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Level Complete!!");
        builder.setMessage(msg);

        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        gamePlay.thread.setRunning(false);
                        dialogInterface.dismiss();
                        finish();
                    }
                });


        // 레벨 클리어시 다음 레벨 오픈
        if (Player.getLevel() == level) {
            Player.setLevel(level+1);
            dbController.levelUp(Player.getLevel());
        }

        // 리셋
        reset();

        // 다이아로그 표시
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // 메뉴로 돌아가기 버튼 클릭
    public void back(View view) {
        if (Player.isSoundEffect()){
            // 효과음 재생
            startService(new Intent(this, SoundEffect.class));
        }
        gamePlay.surfaceDestroyed(gamePlay.getHolder());
        finish();
    }

    // 기록을 DB에 저장하는 메소드
    public String saveRecord(int level, int time) {
        int oldRecord = dbController.getOldRecord(level);
        if (oldRecord == 0) { // 첫 기록
            dbController.saveNewRecord(level, time);
            return "record  >  " + showTime(time);
        }
        if (time > oldRecord) { // 갱신 실패
            return "record  >  " + showTime(time) + "\nBest Records  >  " + showTime(oldRecord);
        }
        else { // 기록 갱신
            dbController.saveBestRecord(level, time);
            return "New Record!!\nrecord  >  " + showTime(time);
        }
    }

}
