package com.example.rushhour;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rushhour.DB.DBController;
import com.example.rushhour.DB.Player;
import com.example.rushhour.play.GameStage;
import com.example.rushhour.sound.SoundEffect;

public class ShowRecords extends AppCompatActivity implements RecordsAdapter.ItemClickListener{

    RecordsAdapter adapter;

    DBController dbController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_records);

        dbController = new DBController(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        // 버튼에 표시할 문자열 배열 생성
        String[] data = new String[40];
        for (int i = 1; i <= 40; i++) {
            data[i-1] = "" + i;
        }
        // xml 파일의 RecyclerView 의 객체 생성
        RecyclerView recyclerView = findViewById(R.id.rv);

        // GridLayoutManager 객체를 생성해서 RecyclerView의 매니저 객체로 지정
        // 격자 형식으로 아이템 배지
        int columns = 5; // 가로 아이템 개수
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 어댑터 객체 생성
        adapter = new RecordsAdapter(this, data);
        // MyAdapter의 ItemClickListener 인터패이스를 구현한 자기 자신을 넘겨줌
        // 이벤트가 발생하면 이 객체에 구현된onItemClick() 메소드가 호출드
        adapter.setClickListener(this);
        // RecycleerView에 어댑터를 연결
        recyclerView.setAdapter(adapter);
    }

    // 인터페이스의 메소드 구현
    // 이벤트 발생시 호출
    @Override
    public void onItemClick(View view, int position){
        if (Player.isSoundEffect()) {
            startService(new Intent(this, SoundEffect.class));
        } // 효과음 재생

        // 해당 레벨 플레이하러 이동
        Intent intent = new Intent(this, GameStage.class);
        intent.putExtra("level", position+1);
        startActivity(intent);

    }

    public void gotoMenu(View view) {
        if (Player.isSoundEffect()){
            // 효과음 재생
            startService(new Intent(this, SoundEffect.class));
        }
        finish();
    }

    // 리셋 버튼
    public void reset(View view) {
        if (Player.isSoundEffect()){
            // 효과음 재생
            startService(new Intent(this, SoundEffect.class));
        }
        // 대화상자 출력
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("정말로 초기화 하시겠습니까??");
        builder.setMessage("모든 기록이 삭제됩니다.");

        builder.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (Player.isSoundEffect()){
                            // 효과음 재생
                            startService(new Intent(getApplicationContext(), SoundEffect.class));
                        }
                        dialogInterface.dismiss();
                        dbController.deleteAll(); // 기록 삭제
                        finish();
                  }
                });

        builder.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (Player.isSoundEffect()){
                            // 효과음 재생
                            startService(new Intent(getApplicationContext(), SoundEffect.class));
                        }
                        dialogInterface.dismiss();
                    }
                });


        // 다이아로그 표시
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
