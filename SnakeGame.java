import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class SnakeGame extends JFrame {
    private static final int TILE_SIZE = 20;
    private static final int WIDTH = 20;
    private static final int HEIGHT = 20;
    private static final int INITIAL_LENGTH = 3;

    private int length;  // длина змейки
    private int[] x, y; // координаты сегментов змейки
    private int foodX, foodY; // координаты еды
    private char direction; // направление движения
    private boolean running;

    public SnakeGame() {
        setTitle("Змейка");
        setSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        x = new int[WIDTH * HEIGHT];
        y = new int[WIDTH * HEIGHT];
        length = INITIAL_LENGTH;
        direction = 'R';
        running = true;

        placeFood();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        if (direction != 'D') direction = 'U';
                        break;
                    case KeyEvent.VK_DOWN:
                        if (direction != 'U') direction = 'D';
                        break;
                    case KeyEvent.VK_LEFT:
                        if (direction != 'R') direction = 'L';
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (direction != 'L') direction = 'R';
                        break;
                }
            }
        });

        Timer timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (running) {
                    move();
                    checkCollision();
                }
                repaint();
            }
        });
        timer.start();

        setVisible(true);
    }

    private void placeFood() {
        foodX = new Random().nextInt(WIDTH);
        foodY = new Random().nextInt(HEIGHT);
    }

    private void move() {
        for (int i = length; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case 'U':
                y[0]--;
                break;
            case 'D':
                y[0]++;
                break;
            case 'L':
                x[0]--;
                break;
            case 'R':
                x[0]++;
                break;
        }

        // Проверка на поедание еды
        if (x[0] == foodX && y[0] == foodY) {
            length++;
            placeFood();
        }
    }

    private void checkCollision() {
        // Проверка столкновения со стенами
        if (x[0] < 0 || x[0] >= WIDTH || y[0] < 0 || y[0] >= HEIGHT) {
            running = false;
        }

        // Проверка столкновения с собой
        for (int i = length; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);

        // Отрисовка еды
        g.setColor(Color.RED);
        g.fillRect(foodX * TILE_SIZE, foodY * TILE_SIZE, TILE_SIZE, TILE_SIZE);

        // Отрисовка змейки
        g.setColor(Color.GREEN);
        for (int i = 0; i < length; i++) {
            g.fillRect(x[i] * TILE_SIZE, y[i] * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        // Проверка состояния игры
        if (!running) {
            showGameOver(g);
        }
    }

    private void showGameOver(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Helvetica", Font.BOLD, 30));
        g.drawString("Game Over", WIDTH * TILE_SIZE / 4, HEIGHT * TILE_SIZE / 2);
    }

    public static void main(String[] args) {
        new SnakeGame();
    }
}
