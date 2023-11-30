import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Tetris {
    Board gameBoard = new Board(5, 18);
    public static UI ui;

    Tetris() {
        ui = new UI(gameBoard.WIDTH, gameBoard.HEIGHT, 45);
    }
    public static void main(String[] args) throws InterruptedException {

        Tetris tetris = new Tetris();
        Board gameBoard = tetris.gameBoard;
        long MOVE_TIMER = 2 * 1000;
        boolean gameEnded = false;
        int currentPiece = 0;

        int startX = 1, startY = 2;

        while(!gameEnded) {
            if(!gameBoard.validPlacement(new Cords(startX, startY), gameBoard.pieces.get(currentPiece % 12))) {
                JOptionPane.showMessageDialog(null, "You Lost");
                System.exit(0);
                gameEnded = true;
                break;
            }
            gameBoard.addPiece(new Cords(startX, startY), gameBoard.pieces.get(currentPiece % 12));
            updateDisplay(gameBoard);
            while(gameBoard.applyGravity(gameBoard.pieces.get(currentPiece % 12))) {
                //Read inputs from keyboard
                getJFrame(gameBoard, currentPiece % 12);

                TimeUnit.MILLISECONDS.sleep(MOVE_TIMER);
                MOVE_TIMER *= 0.99;

                updateDisplay(gameBoard);
            }

            gameBoard.updateScore();
            updateDisplay(gameBoard);
            currentPiece++;
            if(currentPiece == 144) {
//                System.out.println("You Won!");
                JOptionPane.showMessageDialog(null, "You Won");
                System.exit(0);
//                currentPiece = 0;
//                gameBoard.randomShuffle(gameBoard.permutation);
            }
        }
    }

    private static void getJFrame(Board gameBoard, int id) {
        JFrame tempFrame = new JFrame();
        tempFrame.setFocusable(true);
        tempFrame.setVisible(true);
        tempFrame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                switch (keyCode){
                    case KeyEvent.VK_A:
                    case KeyEvent.VK_LEFT:
                        if (gameBoard.validMove(new Cords(0, -1), gameBoard.pieces.get(id))) {
                            gameBoard.movePiece(new Cords(0, -1), gameBoard.pieces.get(id));
                        }
                        updateDisplay(gameBoard);
                        break;
                    case KeyEvent.VK_D:
                    case KeyEvent.VK_RIGHT:
                        if (gameBoard.validMove(new Cords(0, 1), gameBoard.pieces.get(id))) {
                            gameBoard.movePiece(new Cords(0, 1), gameBoard.pieces.get(id));
                        }
                        updateDisplay(gameBoard);
                        break;
                    case KeyEvent.VK_R:
                        if (gameBoard.validRotationClockW(gameBoard.pieces.get(id))) {
                            gameBoard.rotatePieceClockW(gameBoard.pieces.get(id));
                        }
                        updateDisplay(gameBoard);
                        break;
                    case KeyEvent.VK_Q:
                        if (gameBoard.validRotationAntiClockW(gameBoard.pieces.get(id))) {
                            gameBoard.rotatePieceAntiClockW(gameBoard.pieces.get(id));
                        }
                        updateDisplay(gameBoard);
                        break;
                    case KeyEvent.VK_S:
                    case KeyEvent.VK_DOWN:
                        if (gameBoard.validMove(new Cords(1, 0), gameBoard.pieces.get(id))) {
                            gameBoard.movePiece(new Cords(1, 0), gameBoard.pieces.get(id));
                        }
                        updateDisplay(gameBoard);
                        break;
                    case KeyEvent.VK_SPACE:
                        while (gameBoard.applyGravity(gameBoard.pieces.get(id)))
                            updateDisplay(gameBoard);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public static void updateDisplay(Board gameBoard) {
        ui.setState(transpose(gameBoard.grid));
    }

    public static int[][] transpose(int[][] curGrid) {
        int n = curGrid[0].length, m = curGrid.length;
        int[][] res = new int[n][m];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                res[i][j] = curGrid[j][i];
        return res;
    }
}
