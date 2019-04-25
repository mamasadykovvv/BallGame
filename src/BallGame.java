import javax.swing.*;
import java.awt.*;

public class BallGame implements GameConstants {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame ballFrame = new BallGameFrame();
            ballFrame.setVisible(true);
            ballFrame.setResizable(false);
        });
    }}
