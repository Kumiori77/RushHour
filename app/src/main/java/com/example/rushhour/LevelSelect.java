package com.example.rushhour;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rushhour.DB.Player;
import com.example.rushhour.play.GameStage;
import com.example.rushhour.sound.SoundEffect;

public class LevelSelect extends AppCompatActivity {

    GridLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_select);

        layout = (GridLayout) findViewById(R.id.gv);

    }

    @Override
    public void onResume() {

        super.onResume();

        // 그리드 요소 체우기
        for (int i = 0 ; i < 40; i++) {
            GridLayout.LayoutParams params;
            params = new GridLayout.LayoutParams();

            params.rowSpec = GridLayout.spec(i/5);
            params.columnSpec = GridLayout.spec(i%5);
            params.setMargins(15, 20, 15, 20);
            params.width = 180;
            params.height = 160;

            Button btn = mkBtn(this, i);
            btn.setLayoutParams(params);
            layout.addView(btn);
        }

    }

    // 버튼 생성 메소드
    public Button mkBtn(Context context, int id) {
        Button btn = new Button(context);

        btn.setId(id);
        btn.setText((id+1)+"");
        btn.setTextSize(20);
        btn.setTypeface(Typeface.SERIF, Typeface.BOLD);
        btn.setTextColor(Color.WHITE);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Player.isSoundEffect()){
                    // 효과음 재생
                    startService(new Intent(context, SoundEffect.class));
                }
                // 게임 플레이로 넘어가기
                Intent intent = new Intent(getApplicationContext(), GameStage.class);
                intent.putExtra("level", id+1);
                startActivity(intent);

            }
        });

        // 아직 못깬 래밸에면 회색으로 표시
        if (Player.getLevel() < id+1) {
            btn.setBackgroundColor(Color.DKGRAY);
            btn.setEnabled(false);
        }
        // 레벨별로 색 다르게 표시
        else {
            String[] btnColors = {"#22741C", "#F2CB61", "#0054FF", "#FF0000"};
            btn.setBackgroundColor(Color.parseColor(btnColors[id/10]));
        }

        return btn;
    }

    // 메뉴로 돌아가기
    public void gotoMenu(View view) {
        if (Player.isSoundEffect()){
            // 효과음 재생
            startService(new Intent(this, SoundEffect.class));
        }
        finish();
    }


}
