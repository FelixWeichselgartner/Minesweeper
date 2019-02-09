import java.util.Random;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * main class
 */
public class Minesweeper extends JPanel {
    /**
     * maximum of nodes no x and y coordinate.
     */
    private final int WIDTH = 15, HEIGHT = 15;
    /**
     * amount of pixels of one node (width and height).
     */
    private final int amountPixels = 40;
    /**
     * space between 2 nodes.
     */
    private final int space = 3;
    /**
     * the actual gameBoard.
     */
    private Node[][] gameBoard = new Node[WIDTH][HEIGHT];
    /**
     * the amount of bombs on the current gameBoard.
     */
    private int amountBombs;
    /**
     * keeps track of gameOver or not gameOver.
     */
    private Boolean gameOver;
    /**
     * true if the first move has been made - false if not.
     */
    private Boolean initialised = false;

    /**
     * generates a certain amount of random generated bombs.
     */
    public void generateBombs() {
        Random randomNumber = new Random();
        int x, y;
        for (int i = 0; i < amountBombs; i++) {
            do {
                x = randomNumber.nextInt(WIDTH - 1);
                y = randomNumber.nextInt(HEIGHT - 1);

            } while (gameBoard[x][y].getContent() == 'b' || gameBoard[x][y].getOpened());
            gameBoard[x][y].setContent('b');
        }
    }

    /**
     * constructor inits all params
     */
    public Minesweeper() {
        for (int i = 0; i < WIDTH; i++) {
            for (int k = 0; k < HEIGHT; k++) {
                gameBoard[i][k] = new Node();
            }
        }
        this.gameOver = false;
        this.amountBombs = 64;
    }

    /**
     * paints the gameBoard.
     * red squares for bombs.
     * gray squares for not opened nodes.
     * orange squares for marked nodes.
     * light grey for opened nodes with blue number -> amount of bombs around node.
     *
     * @param g
     */
    public void paint(Graphics g) {
        int constantx = 8;
        int constanty = 10;
        g.setColor(Color.GREEN);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 30));
        for (int i = 0; i < WIDTH; i++) {
            for (int k = 0; k < HEIGHT; k++) {
                if (!gameBoard[i][k].getOpened() && !gameOver) {
                    //not opened
                    if (gameBoard[i][k].getMarked()) {
                        g.setColor(Color.orange);
                        g.fillRect(i * amountPixels + i * space, k * amountPixels + k * space, amountPixels, amountPixels);
                    } else {
                        g.setColor(Color.GRAY);
                        g.fillRect(i * amountPixels + i * space, k * amountPixels + k * space, amountPixels, amountPixels);
                    }
                } else {
                    if (gameBoard[i][k].getContent() == 'e') {
                        g.setColor(Color.LIGHT_GRAY);
                        g.fillRect(i * amountPixels + i * space, k * amountPixels + k * space, amountPixels, amountPixels);
                        g.setColor(Color.BLUE);
                        g.drawString(Integer.toString(gameBoard[i][k].getAmountBombsNearby()), i * amountPixels + i * space + amountPixels / 2 - constantx, k * amountPixels + k * space + amountPixels / 2 + constanty);
                    } else if (gameBoard[i][k].getContent() == 'b') {
                        g.setColor(Color.red);
                        g.fillRect(i * amountPixels + i * space, k * amountPixels + k * space, amountPixels, amountPixels);
                    }
                }
            }
        }
    }

    /**
     * updates the current window
     *
     * @param jframe JFrame from gameloop (main)
     */
    public void updateWindow(JFrame jframe) {
        jframe.getContentPane().validate();
        jframe.repaint();
    }

    /**
     * calculates for every node the amount of bombs around it
     */
    public void calculate() {
        int temp;
        //middle
        for (int i = 0; i < WIDTH; i++) {
            for (int k = 0; k < HEIGHT; k++) {
                temp = 0;
                if (i > 0) {
                    if (k > 0) {
                        //above left
                        if (gameBoard[i - 1][k - 1].getContent() == 'b') {
                            temp++;
                        }
                    }
                    if (k < HEIGHT - 1) {
                        //below left
                        if (gameBoard[i - 1][k + 1].getContent() == 'b') {
                            temp++;
                        }
                    }
                    //left
                    if (gameBoard[i - 1][k].getContent() == 'b') {
                        temp++;
                    }
                }
                if (i < WIDTH - 1) {
                    if (k > 0) {
                        //above right
                        if (gameBoard[i + 1][k - 1].getContent() == 'b') {
                            temp++;
                        }
                    }
                    //right
                    if (gameBoard[i + 1][k].getContent() == 'b') {
                        temp++;
                    }
                    if (k < HEIGHT - 1) {
                        //below right
                        if (gameBoard[i + 1][k + 1].getContent() == 'b') {
                            temp++;
                        }
                    }
                }
                if (k > 0) {
                    //above
                    if (gameBoard[i][k - 1].getContent() == 'b') {
                        temp++;
                    }
                }
                if (k < HEIGHT - 1) {
                    //below
                    if (gameBoard[i][k + 1].getContent() == 'b') {
                        temp++;
                    }
                }
                gameBoard[i][k].setAmountBombsNearby(temp);
            }
        }
    }

    /**
     * gameloop with MouseListener
     * breaks out of gameloop if a bomb node is opened
     *
     * @param frame JFrame from main
     */
    public void gameloop(JFrame frame) {
        //implement MouseListener
        frame.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //setOpened setMarked if opened==bomb gameOVer = true
                int mouseX = e.getX();
                int mouseY = e.getY();
                System.out.println("mouseX = " + mouseX + " mouseY = " + mouseY);

                int X = -1, Y = -1;
                for (int i = 0; i < WIDTH; i++) {
                    if (mouseX > i * amountPixels + i * space && mouseX < (i + 1) * amountPixels + i * space) {
                        X = i;
                    }
                }
                for (int i = 0; i < HEIGHT; i++) {
                    if (mouseY > i * amountPixels + i * space + 30 && mouseY < (i + 1) * amountPixels + i * space + 30) {
                        Y = i;
                    }
                }
                if (!initialised) {
                    if (X != -1 && Y != -1) {
                        gameBoard[X][Y].setOpened(true);
                        for (int l = 0; l < 3; l++) {
                            gameBoard[l + X - 1][Y - 1].setOpened(true);
                            gameBoard[l + X - 1][Y + 1].setOpened(true);
                            gameBoard[X - 1][Y].setOpened(true);
                            gameBoard[X + 1][Y].setOpened(true);
                        }
                        initialised = true;
                    }
                } else {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        if (X != -1 && Y != -1) {
                            gameBoard[X][Y].setOpened(true);
                            if (gameBoard[X][Y].getContent() == 'b') {
                                gameOver = true;
                            }
                        }
                        System.out.println("X = " + X + " Y = " + Y);
                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        if (X != -1 && Y != -1) {
                            gameBoard[X][Y].setMarked(!gameBoard[X][Y].getMarked());
                        }
                        System.out.println("X = " + X + " Y = " + Y);
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        while (true) {
            updateWindow(frame);
            if (initialised) {
                generateBombs();
                calculate();
                updateWindow(frame);

                while (true) {
                    updateWindow(frame);
                    if (gameOver) {
                        break;
                    }
                }
            }
            if (gameOver) {
                break;
            }
        }
    }

    /**
     * Dimensions for frame.pack().
     *
     * @return the dimensions for the current frame
     */
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH * amountPixels + (WIDTH - 1) * space,
                HEIGHT * amountPixels + (HEIGHT - 1) * space);
    }

    /**
     * main function
     * setting up JFrame and starting gameloop
     *
     * @param args
     */
    public static void main(String[] args) {
        Minesweeper newGame = new Minesweeper();

        JFrame frame = new JFrame("Minesweeper");
        frame.add(newGame);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setResizable(true);
        frame.setVisible(true);

        newGame.gameloop(frame);
        if (newGame.gameOver) {
            System.out.println("you lost!");
        }
    }
}
