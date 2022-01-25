package kr.study.mywork04_gamebasic;

public class MyMissile {

    int x, y;   // 미사일 위치
    int missileSpeed = 35;  // 미사일 속도

    MyMissile(int x, int y){
        this.x = x;
        this.y = y; // 미사일 클래스 생성자를 통해 미사일 위치 초기화
    }

    public void move(){
        y -= missileSpeed;  // 미사일 이동
    }
}
