package kr.study.mywork04_gamebasic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int Width, Height; // 사용자 기기 해상도
    Bitmap spaceship;    // Bitmap 객체 spaceship 생성
    int spaceship_x, spaceship_y;   // 우주선 위치
    int spaceship_width;    // 우주선 너비

    Bitmap missile; // Bitmap 객체 missile 생성
    int missile_x, missile_y;   // 미사일 위치
    int missile_width;  // 미사일 너비
    int missile_middle; // 미사일 너비/2

    Bitmap leftKey, rightKey;      // 좌우 키 이미지
    int leftKey_x, leftKey_y;  // 왼쪽 키 위치
    int rightKey_x, rightKey_y;     // 오른쪽 키 위치
    int button_width;   // 좌우 키 크기

    Bitmap screen;  // 배경

    ArrayList<Planet> planet;   // planet ArrayList 선언
    ArrayList<MyMissile> myM;   // myMissiel ArrayList 선언

    Bitmap missileButton;   // Bitmap 객체 missileButton 생성
    int missileButton_x, missileButton_y;    // 미사일 버튼 위치

    Bitmap planetimg;   // Bitmap 객체 planetimg 생성

    int score;  // 점수
    int count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));

        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        Width = metrics.widthPixels;    // 사용중인 기기의 해상도(가로) 크기 저장
        Height = metrics.heightPixels;  // 사용중인 기기의 해상도(세로) 크기 저장

        // ArrayList planet, myM 객체 생성
        planet = new ArrayList<Planet>();
        myM = new ArrayList<MyMissile>();

        // spaceship에 우주선 사진 등록
        spaceship = BitmapFactory.decodeResource(getResources(), R.drawable.battleship);
        int x = Width/8;
        int y = Height/11;
        // spaceship Bitmap의 가로,세로 비율 재조정
        spaceship = Bitmap.createScaledBitmap(spaceship, x, y, true);

        // spaceship의 위치 저장
        spaceship_x = Width*1/9;
        spaceship_y = Height*6/9;
        // 우주선 너비 저장
        spaceship_width = spaceship.getWidth();

        // 버튼 너비 저장
        button_width = Width/6;

        // 왼쪽 키 이미지 Bitmap으로 저장
        leftKey = BitmapFactory.decodeResource(getResources(), R.drawable.left);
        // 왼쪽 키 위치 저장
        leftKey_x = Width*5/9;
        leftKey_y = Height*7/9;
        // 왼쪽 키 비율 재조정
        leftKey = Bitmap.createScaledBitmap(leftKey, button_width, button_width, true);

        // 오른쪽 키 이미지 Bitmap으로 저장
        rightKey = BitmapFactory.decodeResource(getResources(), R.drawable.right);
        // 오른쪽 키 위치 저장
        rightKey_x = Width*7/9;
        rightKey_y = Height*7/9;
        // 오른쪽 키 비율 재조정
        rightKey = Bitmap.createScaledBitmap(rightKey, button_width, button_width, true);

        // 미사일 버튼 이미지 Bitmap으로 저장
        missileButton = BitmapFactory.decodeResource(getResources(), R.drawable.missilebutton);
        // 미사일 버튼 비율 재조정
        missileButton = Bitmap.createScaledBitmap(missileButton, button_width, button_width, true);
        // 미사일 버튼 위치 저장
        missileButton_x = Width*1/11;
        missileButton_y = Height*7/9;

        // 미사일 이미지 Bitmap으로 저장
        missile = BitmapFactory.decodeResource(getResources(), R.drawable.missile);
        // 미사일 이미지 비율 재조정
        missile = Bitmap.createScaledBitmap(missile, button_width/4, button_width/4, true);
        // 미사일 너비 저장
        missile_width = missile.getWidth();

        // 행성 이미지 Bitmap으로 저장
        planetimg = BitmapFactory.decodeResource(getResources(), R.drawable.planet);
        planetimg = Bitmap.createScaledBitmap(planetimg, button_width, button_width, true);

        // 배경 Bitmap으로 저장
        screen = BitmapFactory.decodeResource(getResources(), R.drawable.screen);
        // 배경 비율 재조정
        screen = Bitmap.createScaledBitmap(screen, Width, Height, true);

    }


    class MyView extends View {
        MyView(Context context){    // MyView 생성자
            super(context);
            setBackgroundColor(Color.BLUE); // View 배경색 설정(파랑)
            // handler 설정
            gHandler.sendEmptyMessageDelayed(0, 1000);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            // 랜덤위치에서 행성 생성
            Random r1 = new Random();
            int x = r1.nextInt(Width);  // 0~(Width-1) 사이의 랜덤값 저장
            int dir = r1.nextInt(2);
            if(planet.size() < 5)   // 행성의 수가 5개 미만이면 행성 추가
                planet.add(new Planet(x, -100, dir));


            // 화면에 나타날 그림이나 문자를 처리
            Paint p1 = new Paint();
            p1.setColor(Color.RED);
            p1.setTextSize(50);

            canvas.drawText(Integer.toString(count), 0, 200, p1);
            canvas.drawText("점수 : " + Integer.toString(score), 0, 200, p1);
            canvas.drawBitmap(screen, 0,0,p1);
            canvas.drawBitmap(spaceship, spaceship_x, spaceship_y, p1);
            canvas.drawBitmap(leftKey, leftKey_x, leftKey_y, p1);
            canvas.drawBitmap(rightKey, rightKey_x, rightKey_y, p1);
            canvas.drawBitmap(missileButton, missileButton_x, missileButton_y, p1);

            // 확장 for 이용해서 미사일 그리기
            for (MyMissile tmp : myM)
                canvas.drawBitmap(missile, tmp.x, tmp.y, p1);

            // 확장 for 이용해서 행성 그리기
            for (Planet tmp : planet)
                canvas.drawBitmap(planetimg, tmp.x, tmp.y, p1);

            moveMissile();
            movePlanet();
            checkCollision();
            count++;

        }

        public void moveMissile() { // 미사일 이동 메소드
                                    // (모든 미사일에 대해 move()처리)
            for (int i = myM.size()-1; i>=0; i--){
                myM.get(i).move();
            }
            for (int i = myM.size()-1; i>=0; i--){
                // 미사일이 화면을 벗어나면 사라짐
                if (myM.get(i).y < 0) myM.remove(i);
            }
        }

        public void movePlanet() {  // 행성 이동 메소드
                                    // (모든 행성에 대해 move()처리)
            for (int i=planet.size()-1; i>=0; i--){
                planet.get(i).move();
            }
            for (int i=planet.size()-1; i>=0; i--){
                if(planet.get(i).x>Width) planet.remove(i);
            }
        }

        public void checkCollision() {  // 행성 미사일 충돌 메소드
            for (int i=planet.size()-1; i>=0; i--){
                for (int j=myM.size()-1; j>=0; j--){
                    if ((myM.get(j).x+missile_middle > planet.get(i).x)
                            && (myM.get(j).x+missile_middle < planet.get(i).x+button_width)
                            && (myM.get(j).y > planet.get(i).y)
                            && (myM.get(j).y < planet.get(i).y+button_width)){
                        planet.remove(i);   // 행성 제거
                        myM.get(j).y =- 30; // 미사일 이동
                        score += 10;    // 행성 격추시 점수 +10
                    }

                }
            }
        }

        Handler gHandler = new Handler();

        public class myHandler extends Handler {
            int c = 0;

            @Override
            public void handleMessage(Message msg) {
                Log.d("handler", "start : " + c++);
                //invalidate();

                this.sendEmptyMessageDelayed(0, 30);
            }
        }

        /*public void handleMessage(Message msg) {
            invalidate();
            gHandler.sendEmptyMessageDelayed(0,30); // 1초당 약 30번 반복
        }*/

        public boolean onTouchEvent(MotionEvent event){
            // 화면을 터치했을 경우 처리
            int x = 0, y = 0;   // 사용자가 터치한 좌표

            if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE){
                // 화면을 터치하거나 터치한채로 움직일 경우 처리
                x = (int) event.getX();
                y = (int) event.getY(); // 사용자가 터치한 좌표 저장
            }

            if ((x>leftKey_x) && (x<leftKey_x+button_width) && (y>leftKey_y) && (y<leftKey_y+button_width)) {
                // 왼쪽 키 터치할 경우 처리
                spaceship_x -= 20;  // 우주선을 왼쪽으로 20만큼 이동
                return true;
            }

            if ((x>rightKey_x) && (x<rightKey_x+button_width) && (y>rightKey_y) && (y<rightKey_y+button_width)) {
                // 오른쪽 키 터치할 경우 처리
                spaceship_x += 20;  // 우주선을 오른쪽으로 20만큼 이동
                return true;
            }
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // 화면 터치시 처리
                if((x>missileButton_x) && (x<missileButton_x+button_width)
                        && (y>missileButton_y) && (y<missileButton_y+button_width)){
                    // 미사일 버튼 터치시 처리
                    if(myM.size()<1) {  // 미사일 1개 미만일 경우 미사일 추가가
                       myM.add(new MyMissile(spaceship_x+spaceship_width/2-missile_width/2, spaceship_y));
                    }
                }
            }

            invalidate();   // 화면 터치시마다 onDraw() 재호출 -> Handler로 대체
            return true;    // 제대로 처리되었다는 의미
        }

    }

}