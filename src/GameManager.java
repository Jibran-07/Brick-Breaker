import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameManager extends JPanel implements ActionListener, KeyListener {
    private boolean play = false;
    private boolean showStartScreen = true;
    private boolean showEndScreen = false;
    private boolean showWinScreen = false;

    private int score = 0;
    private int totalBricks = 12;  // Total bricks per level
    private int level = 1;  // Current level
    private int maxLevels = 3;  // Maximum number of levels

    private Timer timer;
    private int delay = 6;  // Base delay for ball speed

    private int playerX = 310;
    private int ballposX = 120;
    private int ballposY = 350;
    private int ballXdir = -2;
    private int ballYdir = -3;

    private JButton startButton, restartButton, endButton;
    private BrickManager map;

    public GameManager() {
        map = new BrickManager(2, 6);
        setLayout(null);
        addKeyListener(this);
        setFocusable(true);

        // Start Button
        startButton = new JButton("Start Game");
        startButton.setBounds(250, 250, 200, 50);
        startButton.setFont(new Font("Arial", Font.BOLD, 18));
        startButton.setFocusPainted(false);
        startButton.addActionListener(e -> startGame());
        add(startButton);

        // Restart Button
        restartButton = new JButton("Restart Game");
        restartButton.setBounds(200, 250, 150, 50);
        restartButton.setFont(new Font("Arial", Font.BOLD, 14));
        restartButton.setFocusPainted(false);
        restartButton.addActionListener(e -> restartGame());
        restartButton.setVisible(false);
        add(restartButton);

        // End Button
        endButton = new JButton("End Game");
        endButton.setBounds(380, 250, 150, 50);
        endButton.setFont(new Font("Arial", Font.BOLD, 14));
        endButton.setFocusPainted(false);
        endButton.addActionListener(e -> endGame());
        endButton.setVisible(false);
        add(endButton);

        timer = new Timer(delay, this);
        timer.start();
    }

    private void startGame() {
        play = true;
        showStartScreen = false;
        startButton.setVisible(false);
        requestFocus();
        repaint();
    }

    private void restartGame() {
        play = true;
        showEndScreen = false;
        showWinScreen = false;
        ballposX = 120;
        ballposY = 350;
        ballXdir = -2;
        ballYdir = -3;
        playerX = 310;
        score = 0;
        level = 1;  // Reset level
        totalBricks = 12;  // Reset bricks count
        map = new BrickManager(2, 6);  // Reset bricks
        restartButton.setVisible(false);
        endButton.setVisible(false);
        repaint();
    }

    private void endGame() {
        System.exit(0);
    }

    private void nextLevel() {
        if (level < maxLevels) {
            level++;
            totalBricks = 12;
            map = new BrickManager(2, 6);
            map.hitBricks.clear();

            ballposX = 120;
            ballposY = 350;

            // Increase ball speed noticeably (1.5x faster)
            ballXdir = (int) Math.signum(ballXdir) * Math.max(2, (int) Math.ceil(Math.abs(ballXdir) * 1.5));
            ballYdir = (int) -Math.max(3, Math.ceil(Math.abs(ballYdir) * 1.5));

            System.out.println("Level " + level + " started. Ball speed: (" + ballXdir + ", " + ballYdir + ")");  // Debug

            play = true;
            repaint();
        } else {
            play = false;
            showWinScreen = true;
            repaint();
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);

        if (showStartScreen) {
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Welcome to Brick Breaker", 160, 200);
            return;
        }

        map.draw((Graphics2D) g);

        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 20));
        g.drawString("Score: " + score, 580, 30);
        g.drawString("Level: " + level, 480, 30);

        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100, 8);

        g.setColor(Color.yellow);
        g.fillOval(ballposX, ballposY, 20, 20);

        if (totalBricks <= 0) {
            if (level < maxLevels) {
                nextLevel();
            } else {
                play = false;
                showWinScreen = true;
                g.setColor(Color.GREEN);
                g.setFont(new Font("serif", Font.BOLD, 30));
                g.drawString("You Won! Score: " + score, 190, 200);

                restartButton.setVisible(true);
                endButton.setVisible(true);
            }
        }

        if (showEndScreen) {
            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Game Over, Score: " + score, 190, 200);

            g.setColor(Color.white);
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Choose an option below:", 230, 230);

            restartButton.setVisible(true);
            endButton.setVisible(true);
        }

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();

        if (play) {
            if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
                ballYdir = -ballYdir;
            }

            for (int i = 0; i < map.map.size(); i++) {
                for (int j = 0; j < map.map.get(i).size(); j++) {
                    if (map.map.get(i).get(j) > 0) {
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);

                        if (ballRect.intersects(rect)) {
                            map.setBrickValue(0, i, j);
                            score += 5;
                            totalBricks--;

                            if (ballposX + 19 <= rect.x || ballposX + 1 >= rect.x + rect.width) {
                                ballXdir = -ballXdir;
                            } else {
                                ballYdir = -ballYdir;
                            }
                        }
                    }
                }
            }

            ballposX += ballXdir;
            ballposY += ballYdir;

            if (ballposX < 0 || ballposX > 670) {
                ballXdir = -ballXdir;
            }
            if (ballposY < 0) {
                ballYdir = -ballYdir;
            }
            if (ballposY > 570) {
                play = false;
                showEndScreen = true;
            }

            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT && playerX > 10) {
            playerX -= 20;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && playerX < 590) {
            playerX += 20;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}