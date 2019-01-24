import java.util.Random;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Minesweeper extends JPanel {
    private static int WIDTH = 15, HEIGHT = 15;
    private static int amountPixels = 40;
    private static int space = 3;
    private Node[][] gameBoard = new Node[WIDTH][HEIGHT];
    private int amountBombs;
    private Boolean gameOver;

    public void generateBombs() {
        Random randomNumber = new Random();
        int x, y;
        for (int i = 0; i < amountBombs; i++) {
            do {
                x = randomNumber.nextInt(WIDTH - 1);
                y = randomNumber.nextInt(HEIGHT - 1);

            } while(gameBoard[x][y].getContent() == 'b');
            gameBoard[x][y].setContent('b');
        }
    }

    public Minesweeper() {
        for (int i = 0; i < WIDTH; i++) {
            for (int k = 0; k < HEIGHT; k++) {
                gameBoard[i][k] = new Node();
            }
        }
        this.gameOver = false;
        this.amountBombs = 64;
        generateBombs();
    }

    public void paint(Graphics g) {
        int constantx = 8;
        int constanty = 10;
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.GREEN);
        Boolean flag;
        g.setFont(new Font("TimesRoman", Font.PLAIN, 30));
        for (int i = 0; i < WIDTH; i++) {
            for (int k = 0; k < HEIGHT; k++) {
                if (!gameBoard[i][k].getOpened()) {
                    //not opened
                    if (gameBoard[i][k].getMarked()) {
                        g.setColor(Color.orange);
                        g.fillRect(i*amountPixels+i*space, k*amountPixels+k*space, amountPixels, amountPixels);
                    } else {
                        g.setColor(Color.GRAY);
                        g.fillRect(i * amountPixels+i*space, k * amountPixels+k*space, amountPixels, amountPixels);
                    }
                } else {
                    if (gameBoard[i][k].getContent() == 'e') {
                        g.setColor(Color.LIGHT_GRAY);
                        g.fillRect(i*amountPixels+i*space, k*amountPixels+k*space, amountPixels, amountPixels);
                        g.setColor(Color.BLUE);
                        g.drawString(Integer.toString(gameBoard[i][k].getAmountBombsNearby()), i*amountPixels+i*space+amountPixels/2-constantx,k*amountPixels+k*space+amountPixels/2+constanty);
                        //System.out.println(Integer.toString(gameBoard[i][k].getAmountBombsNearby()));
                    } else if (gameBoard[i][k].getContent() == 'b') {
                        g.setColor(Color.red);
                        g.fillRect(i*amountPixels+i*space, k*amountPixels+k*space, amountPixels, amountPixels);
                    }
                }
            }
        }
    }

    public void updateWindow(JFrame jframe) {
        jframe.getContentPane().validate();
        jframe.repaint();
    }

    public void calculate() {
        int temp;
        //middle
        for (int i = 0; i < WIDTH; i++) {
            for (int k = 0; k < HEIGHT; k++) {
                temp = 0;
                if (i>0) {
                    if (k>0) {
                        //above left
                        if (gameBoard[i - 1][k - 1].getContent() == 'b') {
                            temp++;
                        }
                    }
                    if (k<HEIGHT-1) {
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
                if (i<WIDTH-1) {
                    if (k>0) {
                        //above right
                        if (gameBoard[i + 1][k - 1].getContent() == 'b') {
                            temp++;
                        }
                    }
                    //right
                    if (gameBoard[i + 1][k].getContent() == 'b') {
                        temp++;
                    }
                    if (k<HEIGHT-1) {
                        //below right
                        if (gameBoard[i + 1][k + 1].getContent() == 'b') {
                            temp++;
                        }
                    }
                }
                if (k>0) {
                    //above
                    if (gameBoard[i][k - 1].getContent() == 'b') {
                        temp++;
                    }
                }
                if (k<HEIGHT-1) {
                    //below
                    if (gameBoard[i][k + 1].getContent() == 'b') {
                        temp++;
                    }
                }
                gameBoard[i][k].setAmountBombsNearby(temp);
            }
        }
    }

    public Boolean gameloop(JFrame frame) {
        //implement MouseListener
        frame.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //setOpened setMarked if opened==bomb gameOVer = true
                int mouseX = e.getX();
                int mouseY = e.getY();
                System.out.println("mouseX = " + mouseX + " mouseY = " + mouseY);
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

        calculate();
        updateWindow(frame);

        while(true) {
            updateWindow(frame);
            System.out.println("running ");
            break;
        }


        return false;
    }

    public static void main(String[] args) {
        Minesweeper newGame = new Minesweeper();

        JFrame frame = new JFrame("Minesweeper");
        frame.setSize((WIDTH+1)*amountPixels+(WIDTH-1)*space, (HEIGHT+1)*amountPixels+(HEIGHT-1)*space);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(newGame);
        frame.setVisible(true);

        newGame.gameOver = newGame.gameloop(frame);
        if (newGame.gameOver == true) {
            System.out.println("you lost!");
        }
    }
}
