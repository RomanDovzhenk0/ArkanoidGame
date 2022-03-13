package ArkanoidGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class GameField extends JPanel implements ActionListener {
    private final int SIZE = 500;
    private int platformX = 230;
    private final int PLATFORM_Y = 480;
    private Image platform;
    private Image ballImage;
    private int platformLenght = 40;
    private final int PLATFORM_HEIGHT = 10;
    private java.util.List<Ball> balls = new ArrayList<>();
    private Image background;
    private Image brickImage;
    private Image elogationBoostImage;
    private Image multiplyBoostImage;
    private Brick[] bricks = new Brick[432];
    private int bricksCount;
    private Timer timer;
    private Boost boost;
    private boolean inGame = true;
    private int taps = -1;

    public GameField() {
        setBackground(Color.BLACK);
        initGame();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                int key = e.getKeyCode();
                switch (key) {
                    case KeyEvent.VK_D -> { if(platformX < SIZE - platformLenght){ platformX += 10; } }
                    case KeyEvent.VK_A -> { if(platformX > 0){ platformX -= 10; } }
                    case KeyEvent.VK_SPACE -> {if(!inGame){inGame = true; fillBricks(); repaint(); createFirstBall();}}
                }
            }
        });
        setFocusable(true);
    }
    public void initGame() {
        loadImage();
        fillBricks();
        timer = new Timer(10, this);
        timer.start();
    }
    public void createFirstBall() {
        balls.add(new Ball(new Random().nextInt(0, 500), 400, new int[]{45, 315}[new Random().nextInt(0, 2)], ballImage));
    }
    public void loadImage() {
        ImageIcon iibg = new ImageIcon("src\\ArkanoidGame\\background.png");
        ImageIcon iip = new ImageIcon("src\\ArkanoidGame\\platform.png");
        ImageIcon iib = new ImageIcon("src\\ArkanoidGame\\ball.png");
        ImageIcon iibr = new ImageIcon("src\\ArkanoidGame\\brick4.png");
        ImageIcon iieb = new ImageIcon("src\\ArkanoidGame\\elongationboost.png");
        ImageIcon iimb = new ImageIcon("src\\ArkanoidGame\\multiplyboost.png");
        brickImage = iibr.getImage();
        ballImage = iib.getImage();
        platform = iip.getImage();
        background = iibg.getImage();
        elogationBoostImage = iieb.getImage();
        multiplyBoostImage = iimb.getImage();
        createFirstBall();
    }
    public void fillBricks() {
        int rows = 0;
        int columns = 0;
        int count = 0;
        for (int i = 1; i < 25; i++) {
            for (int j = 1; j < 19; j++) {
                bricks[count] = new Brick(j * 25, i * 10, brickImage);
                count++;
            }
        }
        bricksCount = 432;
    }
    @Override
    protected void paintComponent(Graphics g) {
        if(inGame) {
            super.paintComponent(g);
            g.drawImage(background, 0, 0, this);
            g.drawImage(platform, platformX, PLATFORM_Y, this);
            for (Brick brick : bricks) {
                g.drawImage(brick.image, brick.x, brick.y, this);
            }
            try { g.drawImage(boost.image, boost.x, boost.y, this); } catch(Exception ex) {}
            for (Ball ball : balls) {
                g.drawImage(ball.image, ball.x, ball.y, this);
            }

        }
        else {
            g.clearRect(0,0, 500,500);
            String str = "GAME OVER";
            String str1 = "press SPACE to play again...";
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.drawString(str, 210, 220);
            g.setFont(new Font("Arial", Font.ITALIC, 12));
            g.drawString(str1, 180, 240);
        }
    }
    public void isWall(Ball ball) {
        if(ball.x >= SIZE - ball.SIZE) {
            if(ball.direction == 45) {
                ball.direction = 315;
            }
            else {
                ball.direction = 225;
            }
        }
        else if(ball.y <= 0) {
            if(ball.direction == 45) {
                ball.direction = 135;
            }
            else {
                ball.direction = 225;
            }
        }
        else if(ball.x <= 0) {
            if(ball.direction == 225) {
                ball.direction = 135;
            }
            else {
                ball.direction = 45;
            }
        }
        else if(ball.y == SIZE) {
            balls.remove(ball);
        }
    }
    public void isPlatform(Ball ball) {
        if(ball.y == PLATFORM_Y - PLATFORM_HEIGHT) {
            if (ball.x >= platformX - 5 && ball.x <= platformX + platformLenght + 5) {
                if (ball.direction == 135) {
                    ball.direction = 45;
                } else {
                    ball.direction = 315;
                }
                tapPlatform();
            }
        }
        try{if(boost.y + 30 == PLATFORM_Y) {
            if(boost.x >= platformX - 20 && boost.x <= platformX + 50)
            {
                if(boost.type == "multiply"){
                    multiplyBalls();
                    boost.image = null;
                }
                else{
                    taps = 15;
                    tapPlatform();
                    boost.image = null;
                }
            }
        }} catch(Exception ex){}
    }
    public void tapPlatform() {
        if(taps > 0)
        {
            platform = new ImageIcon("src\\ArkanoidGame\\longplatform.png").getImage();
            platformLenght = 80;
        }
        else if(taps == 0)
        {
            platform = new ImageIcon("src\\ArkanoidGame\\platform.png").getImage();
            platformLenght = 40;
        }
        taps--;
    }
    public void destroyBrick (Brick brick) {
        bricksCount--;
        if(bricksCount % 24 == 0){
            boost = new Boost(brick.x - 2, brick.y + 10, elogationBoostImage, "elong");
        }
        else if(bricksCount % 19 == 0){
            boost = new Boost(brick.x - 2, brick.y + 10, multiplyBoostImage, "multiply");
        }
        brick.x = -1;
        brick.y = -1;
        brick.image = null;
    }
    public void isBrick(Ball ball) {
        int ballUpX = ball.x + ball.SIZE / 2;
        int ballUpY = ball.y;
        int ballLeftX = ball.x;
        int ballLeftY = ball.y + ball.SIZE / 2;
        int ballRightX = ball.x + ball.SIZE;
        int ballRightY = ball.y + ball.SIZE / 2;
        int ballDownX = ball.x + ball.SIZE / 2;
        int ballDownY = ball.y + ball.SIZE;
        int lenght = bricks.length;
        for (int i = 0; i < lenght; i++) {
            if((ballUpX >= bricks[i].x && ballUpX <= bricks[i].x + 25) && ballUpY == bricks[i].y + 10)
            {
                if(ball.direction == 45) {
                    ball.direction = 135;
                }
                else {
                    ball.direction = 225;
                }
                destroyBrick(bricks[i]);
            }
            else if((ballDownX >= bricks[i].x && ballDownX <= bricks[i].x + 25) && ballDownY == bricks[i].y)
            {
                if(ball.direction == 135) {
                    ball.direction = 45;
                }
                else {
                    ball.direction = 315;
                }
                destroyBrick(bricks[i]);
            }
            else if((ballLeftY >= bricks[i].y && ballLeftY <= bricks[i].y + 10) && ballLeftX == bricks[i].x + 25)
            {
                if(ball.direction == 225) {
                    ball.direction = 135;
                }
                else {
                    ball.direction = 45;
                }
                destroyBrick(bricks[i]);
            }
            else if((ballRightY >= bricks[i].y && ballRightY <= bricks[i].y + 10) && ballRightX == bricks[i].x)
            {
                if(ball.direction == 45) {
                    ball.direction = 315;
                }
                else {
                    ball.direction = 225;
                }
                destroyBrick(bricks[i]);
            }
        }
    }
    public void multiplyBalls() {
        if(inGame) {
            java.util.List<Ball> newlist = new ArrayList<>();
            for (Ball ball : balls) {
                int dir = ball.direction;
                switch (dir) {
                    case 45, 225 -> {
                        newlist.add(ball);
                        newlist.add(new Ball(ball.x, ball.y, 135, ballImage));
                        newlist.add(new Ball(ball.x, ball.y, 315, ballImage));
                    }
                    case 135, 315 -> {
                        newlist.add(ball);
                        newlist.add(new Ball(ball.x, ball.y, 45, ballImage));
                        newlist.add(new Ball(ball.x, ball.y, 225, ballImage));
                    }
                }
            }
            balls = newlist;
        }
    }
    public void checkInGame() {
        inGame = balls.size() > 0 && bricksCount > 0;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(inGame) {
            try {
                for (Ball ball : balls) {
                    isWall(ball);
                    isPlatform(ball);
                    isBrick(ball);
                    ball.move();
                    boost.move();
                }
            } catch (Exception ex) {}
            checkInGame();
        }
        repaint();
    }
}
