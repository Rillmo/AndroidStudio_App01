package kr.study.mywork04_gamebasic;

public class Planet {

    int x, y;   // 행성 위치
    int planetSpeed = 15;   // 행성 이동속도
    int dir;    // 0이면 좌측으로 이동, 1이면 우측으로 이동

    Planet(int x, int y, int dir){
        this.x = x;
        this.y = y;
        this.dir = dir;
    }

    public void move(){
        if (dir == 0)
            x -= planetSpeed;
        else
            x += planetSpeed;
    }
}
