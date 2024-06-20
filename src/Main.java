
import javax.swing.JFrame;

public class Main extends JFrame {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Flappy Bird");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        MainMenu mainMenu = new MainMenu(frame);
        frame.add(mainMenu);

        frame.setVisible(true);
    }
}

