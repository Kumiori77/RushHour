package com.example.rushhour.play;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.rushhour.DB.Map;
import com.example.rushhour.DB.Player;
import com.example.rushhour.R;
import com.example.rushhour.sound.SoundEffect;

public class GamePlay extends SurfaceView implements SurfaceHolder.Callback{

    // x, y, w, h, color, direct (v:0, h:1)
    int[][] map;

    int level;


    Block[] blocks = new Block[16];

    Block target;

    MyThread thread;

    Boolean gameStart;
    Boolean gameEnd;

    Context context;

    // 생성자
    public GamePlay(Context context, int level) {
        super(context);
        this.context = context;

        // 레벨 설정
        this.level = level;

        // 맵 받아오기
        map = Map.getMap(level);

        // 서피스 뷰를 다루기 위한 홀더 얻음
        SurfaceHolder holder = getHolder();
        // 콜백 메소드 등록
        holder.addCallback(this);

        // 스레드 생성
        thread = new MyThread(holder);

        // 블록 객체를 생성해서 배열에 넣음
        for (int i = 0; i < map.length; i++) {
            blocks[i] = new Block(i, map[i]);
        }

        gameStart = false;
        gameEnd = false;
    }

    public MyThread getThread() {
        return thread;
    }

    // 서피스뷰가 생성되면 호출되는 콜백 메소드
    public void surfaceCreated(SurfaceHolder holder) {
        // 스레드 시작
        thread.setRunning(true);
        thread.start();
    }
    // 서피스뷰가 소멸되면 호출되는 콜백 메소드
    public void surfaceDestroyed(SurfaceHolder holder) {
        // 스레드 중지
        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                // 메인 스레드와 합침
                thread.join();
                retry = false;
            }
            catch (InterruptedException e) {
            }
        }

        thread.setRunning(false);

    }
    // 서피스뷰에 구조적인 변경이 발생하면 호출되는 콜백 메소드
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!gameStart) { return true;}

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (Player.isSoundEffect()){
                // 효과음 재생
                context.startService(new Intent(context, SoundEffect.class));
            }

            // 어느 그림을 클릭했는지 확인하기
            float x = event.getX();
            float y = event.getY();
            for (Block block : blocks) {
                if (block == null) {
                    break;
                }
                if (x >= block.x && x <= (block.x + block.w) &&
                        y >= block.y && y <= (block.y+block.h)) {
                    target = block;
                    target.onEvent(event, blocks);
                    return true;
                }
            }
            return true;
        }
        else if (event.getAction() == MotionEvent.ACTION_MOVE && target != null) {
            target.onEvent(event, blocks);
        }
        else if (event.getAction() == MotionEvent.ACTION_UP && target != null) {
            target.onEvent(event, blocks);
            target = null;
        }
        return true;
    }

    public String toggle(){
        if (gameStart) {
            gameStart = false;
            return "START";
        }
        else {
            gameStart = true;
            return "PAUSE";
        }
    }

    public void reset() {
        // 블록 객체를 새로 생성
        blocks = new Block[16];
        // 블록 객체를 생성해서 배열에 넣음
        for (int i = 0; i < map.length; i++) {
            blocks[i] = new Block(i, map[i]);
        }
        gameStart = false;
        gameEnd = false;
    }



    public class MyThread extends Thread {
        // 스레드 실행 여부
        private boolean mRun = false;
        // 서피스뷰를 다루기 위한 SurfaceHolder 객체
        private SurfaceHolder mSurfaceholder;


        // 생성자
        public MyThread(SurfaceHolder surfaceHolder) {
            // 서피스 뷰에서 생성한 홀더를 매게변수로 전달받음
            mSurfaceholder = surfaceHolder;
        }

        // 스레드가 사작되면 호출되는 메소드
        @Override
        public void run() {
            while (mRun) {
                // 캔버스
                Canvas canvas = null;
                try {
                    // 서피스뷰 객체로 부터 캔버스를 얻음
                    canvas = mSurfaceholder.lockCanvas(null);
//                    canvas.drawColor(Color.WHITE);
                    // 여러 스레드에서 자원에 동시에 접근하는 것을 막기위해 동기화함
                    synchronized (mSurfaceholder) {
                        Paint paint = new Paint();
                        // 비트맵 읽어오기
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.stage);

                        // 크기를 축소한 비트맵 생성
                        bitmap = Bitmap.createScaledBitmap(bitmap, 1000, 1000, false);

                        // 비트맵 그리기
                        canvas.drawBitmap(bitmap, 0, 0, paint);

                        // 시작했을 때만 화면이 보임
                        if (gameStart) {
                            //  블록 그리기
                            for (Block block : blocks) {
                                if (block == null) {
                                    break;
                                }
                                // 게임 오버 확인
                                if (block.color.equals("#FF0000") && block.x > 619) {
                                    gameEnd = true;
                                    block.x = 770;
                                }
                                block.drawBlocks(canvas);
                            }
                        }
                        invalidate();
                    }
                } finally {
                    // 캔버스 로깅 풀기 (그리기 종료하고 화면에 표시)
                    mSurfaceholder.unlockCanvasAndPost(canvas);
                }
            }
        }

        // 스레드 실행여부 설정 메소드
        public void setRunning(boolean b) {
            mRun = b;
        }
    }

    // 블록 그리는 메소드
    public void drawBlock(Block block, Canvas canvas) {
        Paint paint = new Paint();
        // 블록 색 지정
        paint.setColor(Color.parseColor(block.color));

        // 블록 그리기
        canvas.drawRect(block.x, block.y, block.x+block.w, block.y+block.h, paint);
    }

}
