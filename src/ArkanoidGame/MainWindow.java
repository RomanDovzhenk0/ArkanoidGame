package ArkanoidGame;

import javax.swing.*;

public class MainWindow extends JFrame {
    public MainWindow() {
        setTitle("ARKANOID");
        setSize(517,540);
        setLocation(400,400);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(new GameField());
        setVisible(true);
    }
}
