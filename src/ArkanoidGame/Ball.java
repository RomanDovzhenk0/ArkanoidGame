package ArkanoidGame;

import java.awt.*;

public class Ball {
    public final int VELOCITY = 2;
    public int x = 230;
    public int y = 480;
    public final int SIZE = 10;
    public int direction;
    public Image image;
    public Ball(int x, int y, int direction, Image image) {
        this.x = x;
        this.y = y;
        this.image = image;
        this.direction = direction;
    }
    public void move() {
        switch (direction) {
            case 45 -> {x += VELOCITY; y -= VELOCITY;}
            case 135 -> {x += VELOCITY; y += VELOCITY;}
            case 225 -> {x -= VELOCITY; y += VELOCITY;}
            case 315 -> {x -= VELOCITY; y -= VELOCITY;}
        }
    }
}
