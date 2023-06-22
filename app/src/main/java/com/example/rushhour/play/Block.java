package com.example.rushhour.play;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.example.rushhour.DB.Player;

public class Block {

    int id;
    float x, y, w, h;
    int direction;
    String color;
    float point; // 이벤트 발생 좌표

    float[] position = {20, 170, 320, 470, 620, 770};

    float z = (float) (1000.0 / 940.0);


    String[][] colors = {{"#FF0000", "#86E57F", "#FFBB00", "#3DB7CC",
            "#F361DC", "#8041D9", "#22741C", "#353535",
            "#FAECC5", "#FAED7D", "#CC723D", "#476600",
            "#FFE400", "#D1B2FF", "#0100FF", "#008299"
    }, {
        "#FF0000", "#0000FF", "#008000", "#FFFF00",
        "#FFA500", "#FF00FF", "#000080", "#40E0D0",
        "#FFD700", "#8B4513", "#808080", "#F5F5DC",
        "#00FF00", "#00FFFF", "#800080", "#FFFFFF"
    }, {
        "#FF0000", "#87CEEB", "#6B8E23", "#FFDB58",
        "#FFC0CB", "#900020", "#00CED1" ,"#7FFFD4",
        "#964B00", "#CC00CC", "#C3B091", "#4B0082",
        "#9966CC", "#FFFFF0", "#8A2BE2", "#FF7F50"
    }
    };

    public Block(int id, int[] map) {
        this.id = id;

        this.x = (float) map[0] * z;
        this.y = (float) map[1] * z;
        this.w = (float) map[2] * 150 * z;
        this.h = (float) map[3] * 150 * z;

        this.color = colors[Player.getColor()][map[4]];
        this.direction = map[5];

    }

    public void drawBlocks(Canvas canvas) {
        Paint paint = new Paint();
        // 블록 색 지정
        paint.setColor(Color.parseColor(this.color));

        // 블록 그리기
//        canvas.drawRect(this.x, this.y, this.x+this.w, this.y+this.h, paint);
        canvas.drawRoundRect(this.x, this.y, this.x+this.w, this.y+this.h, 15, 15, paint);
    }

    // 이벤트 처리 메소드
    public void onEvent(MotionEvent event, Block[] blocks) {

        if (event.getAction() == MotionEvent.ACTION_DOWN){
            this.point = this.direction == 0 ? event.getY() : event.getX();
        }
        else if (event.getAction() == MotionEvent.ACTION_MOVE) {

            float newPoint;
            if (this.direction == 0) {
                newPoint = event.getY();
                if (!checkWithBlock(blocks, this.y + (newPoint - point))){
                    point = newPoint;
                    return;
                }
                else {
                    this.y += (newPoint - point);
                    point = newPoint;
                }

            }
            else {
                newPoint = event.getX();
                if (!checkWithBlock(blocks, this.x + (newPoint - point))){
                    point = newPoint;
                    return;
                }
                else {
                    this.x += (newPoint - point);
                    point = newPoint;
                }
            }
            checkWithStage();
        }
        else if (event.getAction() == MotionEvent.ACTION_UP) {

            checkPosition(blocks);
        }
    }
    // 정해진 칸에만 있도록 하는 메소드
    public void checkPosition(Block[] blocks) {
        float blockPoint = this.direction == 0 ? this.y : this.x;

        for (int i = 1; i < position.length; i++) {
            if (position[i - 1] * z <= blockPoint && blockPoint <= position[i] * z) {
                blockPoint = (blockPoint - position[i - 1] > position[i] - blockPoint) ? position[i] * z : position[i - 1] * z;
                break;
            }
        }

        if (this.direction == 0) {
            this.y = blockPoint;
        }
        else {
            this.x = blockPoint;
        }
    }

    // 블록 끼리와의 충돌 감지 메소드
    public boolean checkWithBlock(Block[] blocks, float position) {
        float X, Y;
        if (direction == 0) {
            X = this.x;
            Y = position;
        }
        else {
            X = position;
            Y = this.y;
        }
        for (Block block : blocks) {
            if (block == null) {
                continue;
            }
            else if (block.id == this.id) {
                continue;
            }
            if ((X < block.x+block.w) && (X+this.w > block.x)) {
                if ((Y < block.y+block.h) && (Y+this.h > block.y)) {
                    return false;
                }
            }
        }
        return true;
    }

    // 가드라인 충돌 감지 메소드
    public void checkWithStage() {
        if (this.x < 20*z) {
            this.x = 20*z;
        }
        else if (this.x > 920*z - this.w) {
            this.x = 920*z - this.w;
        }
        if (this.y < 20*z) {
            this.y = 20*z;
        }
        else if (this.y > 920*z  - this.h) {
            this.y = 920*z - this.h;
        }
    }

}
