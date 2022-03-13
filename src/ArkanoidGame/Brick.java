package ArkanoidGame;

import java.awt.*;

public class Brick {
    public int x;
    public int y;
    public Image image;
    public Brick(int x, int y, Image image) {
        this.x = x;
        this.y = y;
        this.image = image;
    }
}
