import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

class MainMenu extends JPanel {
    private JFrame frame;
    private BufferedImage backgroundImage;

    public MainMenu(JFrame frame) {
        this.frame = frame;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        try {
            backgroundImage = ImageIO.read(new File("Images/חלל.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JLabel instructions = new JLabel("Welcome to Flappy Bird! Press Start to play.");
        instructions.setForeground(Color.GREEN);
        instructions.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton startButton = new JButton("Start");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.remove(MainMenu.this);
                BufferedImage birdImage = null; // Replace with your bird image
                GamePanel gamePanel = new GamePanel(birdImage);
                frame.add(gamePanel);
                frame.revalidate();
                gamePanel.requestFocusInWindow();
                gamePanel.startGame();
            }
        });

        JButton instructionsButton = new JButton("Instructions");
        instructionsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        instructionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame,
                        "Press UP arrow to make the bird jump.\nPress P to pause the game.\nAvoid hitting the pipes.\nTry to get the highest score!",
                        "Instructions", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        add(Box.createVerticalGlue());
        add(instructions);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(startButton);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(instructionsButton);
        add(Box.createVerticalGlue());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

    }
}
