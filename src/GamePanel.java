//import javax.imageio.ImageIO;
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.*;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//import java.util.concurrent.CopyOnWriteArrayList;
//
//public class GamePanel extends JPanel implements KeyListener, Runnable {
//    private BufferedImage image;
//    private int birdY = 250;
//    private int birdX = 100;
//    private int birdVelocity = 0;
//    private boolean gameRunning = false;
//    private CopyOnWriteArrayList<Rectangle> pipes; // Use CopyOnWriteArrayList
//    private int score = 0;
//    private JButton restartButton;
//    private JButton resumeButton;
//
//    public GamePanel(BufferedImage image){
//        this.image = image;
//        pipes = new CopyOnWriteArrayList<>(); // Initialize pipes
//        setFocusable(true);
//        requestFocusInWindow();
//        addKeyListener(this);
//    }
//
//    @Override
//    public void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        g.setColor(Color.WHITE);
//        g.fillRect(0, 0, getWidth(), getHeight());
//
//        try {
//            BufferedImage background = ImageIO.read(new File("Images/חלל.jpg")); // להחליף את הרקע לתמונה בתוך המשחק
//            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        g.setColor(Color.YELLOW);
//        g.fillRect(birdX, birdY, 20, 20);
//
//        g.setColor(Color.GREEN);
//        for (Rectangle pipe : pipes) {
//            g.fillRect(pipe.x, pipe.y, pipe.width, pipe.height);
//        }
//
//        g.setColor(Color.RED);
//        Font currentFont = g.getFont();
//        Font newFont = currentFont.deriveFont(currentFont.getSize() * 2.0F); // Increase font size
//        g.setFont(newFont);
//        g.drawString("Score: " + score, 50, 50); // Adjust position as needed
//    }
//
//    public void startGame() {
//        gameRunning = true;
//        Thread gameThread = new Thread(this);
//        gameThread.start();
//        generatePipes();
//    }
//
//    private void generatePipes() {
//        for (int i = 0; i < 5; i++) {
//            addPipe(800 + i * 200);
//        }
//    }
//
//    private void addPipe(int x) {
//        int gap = 200;
//        int pipeHeight = (int) (Math.random() * 200 + 100);
//        pipes.add(new Rectangle(x, 0, 50, pipeHeight));
//        pipes.add(new Rectangle(x, pipeHeight + gap, 50, 600 - pipeHeight - gap));
//    }
//
//    public void run() {
//        while (gameRunning) {
//            updateGame();
//            repaint();
//            try {
//                Thread.sleep(20);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private synchronized void updateGame() { // Synchronize access to pipes
//        birdY += birdVelocity;
//        birdVelocity += 1;
//
//        for (Rectangle pipe : pipes) {
//            pipe.x -= 5;
//        }
//
//        if (!pipes.isEmpty() && pipes.get(0).x <= -50) {
//            for (int i = 0; i < 2; i++) {
//                pipes.remove(0);
//            }
//            addPipe(800);
//            score++;
//        }
//
//        for (Rectangle pipe : pipes) {
//            if (pipe.intersects(new Rectangle(birdX, birdY, 20, 20))) {
//                gameRunning = false;
//                showRestartButton();
//                return;
//            }
//        }
//
//        if (birdY > 580 || birdY < 0) {
//            gameRunning = false;
//            showRestartButton();
//            return;
//        }
//    }
//
//    public void keyPressed(KeyEvent e) {
//        if (e.getKeyCode() == KeyEvent.VK_UP) {
//            birdVelocity = -10;
//        }
//        if (e.getKeyCode() == KeyEvent.VK_P) {
//            gameRunning = false;
//            showResumeButton();
//        }
//    }
//
//    public void keyReleased(KeyEvent e) {}
//
//    public void keyTyped(KeyEvent e) {}
//
//    private void showRestartButton() {
//        restartButton = new JButton("Restart");
//        restartButton.setFont(new Font("Arial", Font.BOLD, 20));
//        restartButton.setForeground(Color.WHITE);
//        restartButton.setBackground(Color.RED);
//        restartButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
//        restartButton.setFocusPainted(false);
//        restartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
//        restartButton.setPreferredSize(new Dimension(200, 50)); // Set preferred size for button
//        restartButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                restartGame();
//            }
//        });
//        add(restartButton);
//        revalidate();
//    }
//
//    private void showResumeButton() {
//        resumeButton = new JButton("Resume");
//        resumeButton.setFont(new Font("Arial", Font.BOLD, 20));
//        resumeButton.setForeground(Color.WHITE);
//        resumeButton.setBackground(Color.BLUE);
//        resumeButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
//        resumeButton.setFocusPainted(false);
//        resumeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
//        resumeButton.setPreferredSize(new Dimension(200, 50)); // Set preferred size for button
//        resumeButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                gameRunning = true;
//                remove(resumeButton);
//                revalidate();
//                requestFocusInWindow();
//                Thread resumeThread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        resumeGame();
//                    }
//                });
//                resumeThread.start();
//            }
//        });
//        add(resumeButton);
//        revalidate();
//    }
//
//    private void resumeGame() {
//        while (gameRunning) {
//            updateGame();
//            repaint();
//            try {
//                Thread.sleep(20);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public void restartGame() {
//        score = 0;
//        birdY = 250;
//        birdVelocity = 0;
//        pipes.clear();
//        generatePipes();
//        gameRunning = true;
//        removeIfInstanceOf(JButton.class);
//        Thread gameThread = new Thread(this);
//        gameThread.start();
//        requestFocusInWindow();
//    }
//
//    private void removeIfInstanceOf(Class<?> clazz) {
//        Component[] components = getComponents();
//        for (Component component : components) {
//            if (clazz.isInstance(component)) {
//                remove(component);
//            }
//        }
//        revalidate();
//    }
//}
//
//import javax.imageio.ImageIO;
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.*;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//import java.util.concurrent.CopyOnWriteArrayList;
//
//public class GamePanel extends JPanel implements KeyListener, Runnable {
//    private BufferedImage image;
//    private int birdY = 250;
//    private int birdX = 100;
//    private int birdVelocity = 0;
//    private boolean gameRunning = false;
//    private CopyOnWriteArrayList<Rectangle> pipes; // Use CopyOnWriteArrayList
//    private int score = 0;
//    private JButton restartButton;
//    private JButton resumeButton;
//
//    public GamePanel(BufferedImage image){
//        this.image = image;
//        pipes = new CopyOnWriteArrayList<>(); // Initialize pipes
//        setFocusable(true);
//        requestFocusInWindow();
//        addKeyListener(this);
//    }
//
//    @Override
//    public void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        g.setColor(Color.WHITE);
//        g.fillRect(0, 0, getWidth(), getHeight());
//
//        try {
//            BufferedImage background = ImageIO.read(new File("Images/חלל.jpg")); // Replace with your image path
//            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        g.setColor(Color.YELLOW);
//        g.fillRect(birdX, birdY, 20, 20);
//
//        g.setColor(Color.GREEN);
//        for (Rectangle pipe : pipes) {
//            g.fillRect(pipe.x, pipe.y, pipe.width, pipe.height);
//        }
//
//        g.setColor(Color.RED);
//        Font currentFont = g.getFont();
//        Font newFont = currentFont.deriveFont(currentFont.getSize() * 2.0F); // Increase font size
//        g.setFont(newFont);
//        g.drawString("Score: " + score, 50, 50); // Adjust position as needed
//    }
//
//    public void startGame() {
//        gameRunning = true;
//        Thread gameThread = new Thread(this);
//        gameThread.start();
//        generatePipes();
//    }
//
//    private void generatePipes() {
//        for (int i = 0; i < 5; i++) {
//            addPipe(800 + i * 200);
//        }
//    }
//
//    private void addPipe(int x) {
//        int gap = 200;
//        int pipeHeight = (int) (Math.random() * 200 + 100);
//        pipes.add(new Rectangle(x, 0, 50, pipeHeight));
//        pipes.add(new Rectangle(x, pipeHeight + gap, 50, 600 - pipeHeight - gap));
//    }
//
//    public void run() {
//        while (gameRunning) {
//            updateGame();
//            repaint();
//            try {
//                Thread.sleep(20);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private synchronized void updateGame() { // Synchronize access to pipes
//        birdY += birdVelocity;
//        birdVelocity += 1;
//
//        for (Rectangle pipe : pipes) {
//            pipe.x -= 5;
//        }
//
//        if (!pipes.isEmpty() && pipes.get(0).x <= -50) {
//            for (int i = 0; i < 2; i++) {
//                pipes.remove(0);
//            }
//            addPipe(800);
//            score++;
//        }
//
//        for (Rectangle pipe : pipes) {
//            if (pipe.intersects(new Rectangle(birdX, birdY, 20, 20))) {
//                gameRunning = false;
//                showRestartButton();
//                return;
//            }
//        }
//
//        if (birdY > 580 || birdY < 0) {
//            gameRunning = false;
//            showRestartButton();
//            return;
//        }
//    }
//
//    public void keyPressed(KeyEvent e) {
//        if (e.getKeyCode() == KeyEvent.VK_UP) {
//            birdVelocity = -10;
//        }
//        if (e.getKeyCode() == KeyEvent.VK_P) {
//            gameRunning = false;
//            if (resumeButton == null) { // Only show resume button if it doesn't exist
//                showResumeButton();
//            }
//        }
//    }
//
//    public void keyReleased(KeyEvent e) {}
//
//    public void keyTyped(KeyEvent e) {}
//
//    private void showRestartButton() {
//        restartButton = new JButton("Restart");
//        restartButton.setFont(new Font("Arial", Font.BOLD, 20));
//        restartButton.setForeground(Color.WHITE);
//        restartButton.setBackground(Color.RED);
//        restartButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
//        restartButton.setFocusPainted(false);
//        restartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
//        restartButton.setPreferredSize(new Dimension(200, 50)); // Set preferred size for button
//        restartButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                restartGame();
//            }
//        });
//        add(restartButton);
//        revalidate();
//    }
//
//    private void showResumeButton() {
//        resumeButton = new JButton("Resume");
//        resumeButton.setFont(new Font("Arial", Font.BOLD, 20));
//        resumeButton.setForeground(Color.WHITE);
//        resumeButton.setBackground(Color.BLUE);
//        resumeButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
//        resumeButton.setFocusPainted(false);
//        resumeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
//        resumeButton.setPreferredSize(new Dimension(200, 50)); // Set preferred size for button
//        resumeButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                gameRunning = true;
//                remove(resumeButton);
//                resumeButton = null; // Set resumeButton to null after removing
//                revalidate();
//                requestFocusInWindow();
//                Thread resumeThread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        resumeGame();
//                    }
//                });
//                resumeThread.start();
//            }
//        });
//        add(resumeButton);
//        revalidate();
//    }
//
//    private void resumeGame() {
//        while (gameRunning) {
//            updateGame();
//            repaint();
//            try {
//                Thread.sleep(20);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public void restartGame() {
//        score = 0;
//        birdY = 250;
//        birdVelocity = 0;
//        pipes.clear();
//        generatePipes();
//        gameRunning = true;
//        removeIfInstanceOf(JButton.class);
//        Thread gameThread = new Thread(this);
//        gameThread.start();
//        requestFocusInWindow();
//    }
//
//    private void removeIfInstanceOf(Class<?> clazz) {
//        Component[] components = getComponents();
//        for (Component component : components) {
//            if (clazz.isInstance(component)) {
//                remove(component);
//            }
//        }
//        revalidate();
//    }
//}


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

public class GamePanel extends JPanel implements KeyListener, Runnable {
    private BufferedImage image;
    private int birdY = 250;
    private int birdX = 100;
    private int birdVelocity = 0;
    private boolean gameRunning = false;
    private boolean gameOver = false; // Add a flag to check if the game is over
    private CopyOnWriteArrayList<Rectangle> pipes; // Use CopyOnWriteArrayList
    private int score = 0;
    private JButton restartButton;
    private JButton resumeButton;

    public GamePanel(BufferedImage image) {
        this.image = image;
        pipes = new CopyOnWriteArrayList<>(); // Initialize pipes
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        try {
            BufferedImage background = ImageIO.read(new File("Images/חלל.jpg")); // Replace with your image path
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        g.setColor(Color.YELLOW);
        g.fillRect(birdX, birdY, 20, 20);

        g.setColor(Color.GREEN);
        for (Rectangle pipe : pipes) {
            g.fillRect(pipe.x, pipe.y, pipe.width, pipe.height);
        }

        g.setColor(Color.RED);
        Font currentFont = g.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() * 2.0F); // Increase font size
        g.setFont(newFont);
        g.drawString("Score: " + score, 50, 50); // Adjust position as needed
    }

    public void startGame() {
        gameRunning = true;
        gameOver = false; // Reset game over flag
        Thread gameThread = new Thread(this);
        gameThread.start();
        generatePipes();
    }

    private void generatePipes() {
        for (int i = 0; i < 5; i++) {
            addPipe(800 + i * 200);
        }
    }

    private void addPipe(int x) {
        int gap = 200;
        int pipeHeight = (int) (Math.random() * 200 + 100);
        pipes.add(new Rectangle(x, 0, 50, pipeHeight));
        pipes.add(new Rectangle(x, pipeHeight + gap, 50, 600 - pipeHeight - gap));
    }

    public void run() {
        while (gameRunning) {
            updateGame();
            repaint();
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void updateGame() { // Synchronize access to pipes
        birdY += birdVelocity;
        birdVelocity += 1;

        for (Rectangle pipe : pipes) {
            pipe.x -= 5;
        }

        if (!pipes.isEmpty() && pipes.get(0).x <= -50) {
            for (int i = 0; i < 2; i++) {
                pipes.remove(0);
            }
            addPipe(800);
            score++;
        }

        for (Rectangle pipe : pipes) {
            if (pipe.intersects(new Rectangle(birdX, birdY, 20, 20))) {
                gameOver = true; // Set game over flag to true
                gameRunning = false;
                showRestartButton();
                return;
            }
        }

        if (birdY > 580 || birdY < 0) {
            gameOver = true; // Set game over flag to true
            gameRunning = false;
            showRestartButton();
            return;
        }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && !gameOver) { // Only allow jump if game is not over
            birdVelocity = -10;
        }
        if (e.getKeyCode() == KeyEvent.VK_P && !gameOver) { // Only allow pause if game is not over
            gameRunning = false;
            if (resumeButton == null) { // Only show resume button if it doesn't exist
                showResumeButton();
            }
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    private void showRestartButton() {
        restartButton = new JButton("Restart");
        restartButton.setFont(new Font("Arial", Font.BOLD, 20));
        restartButton.setForeground(Color.WHITE);
        restartButton.setBackground(Color.RED);
        restartButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        restartButton.setFocusPainted(false);
        restartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        restartButton.setPreferredSize(new Dimension(200, 50)); // Set preferred size for button
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });
        add(restartButton);
        revalidate();
    }

    private void showResumeButton() {
        resumeButton = new JButton("Resume");
        resumeButton.setFont(new Font("Arial", Font.BOLD, 20));
        resumeButton.setForeground(Color.WHITE);
        resumeButton.setBackground(Color.BLUE);
        resumeButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        resumeButton.setFocusPainted(false);
        resumeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resumeButton.setPreferredSize(new Dimension(200, 50)); // Set preferred size for button
        resumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameRunning = true;
                remove(resumeButton);
                resumeButton = null; // Set resumeButton to null after removing
                revalidate();
                requestFocusInWindow();
                Thread resumeThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        resumeGame();
                    }
                });
                resumeThread.start();
            }
        });
        add(resumeButton);
        revalidate();
    }

    private void resumeGame() {
        while (gameRunning) {
            updateGame();
            repaint();
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void restartGame() {
        score = 0;
        birdY = 250;
        birdVelocity = 0;
        pipes.clear();
        generatePipes();
        gameRunning = true;
        gameOver = false; // Reset game over flag
        removeIfInstanceOf(JButton.class);
        Thread gameThread = new Thread(this);
        gameThread.start();
        requestFocusInWindow();
    }

    private void removeIfInstanceOf(Class<?> clazz) {
        Component[] components = getComponents();
        for (Component component : components) {
            if (clazz.isInstance(component)) {
                remove(component);
            }
        }
        revalidate();
    }
}