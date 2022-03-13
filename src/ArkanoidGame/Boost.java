package ArkanoidGame;

import java.awt.*;

public class Boost {
    public int x;
    public int y;
    public Image image;
    public String type;
    public Boost(int x, int y, Image image, String type) {
        this.x = x;
        this.y = y;
        this.image = image;
        this.type = type;
    }
    public void move() {
        y += 1;
    }
}
