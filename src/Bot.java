import java.util.concurrent.TimeUnit;

public class Bot {
    Board gameBoard = new Board(5, 18);
    public static UI ui;

    static final char[] moves = {'A', 'S', 'D', 'Q', 'R', 'N'};

    Bot() {
//        ui = new UI(gameBoard.WIDTH, gameBoard.HEIGHT, 45);
    }

    public static Board getMaxScore(Board currentBoard, int id, int depthLimit) throws CloneNotSupportedException {
        if(depthLimit == 0) {
            currentBoard.score += currentBoard.emptyFullRows2() * 500;
            currentBoard.score += currentBoard.getHighestEmptyRow() * 100;
            return currentBoard;
        }
        Board bestBoard = currentBoard.clone();
        int bestScore = bestBoard.score;
        for (char move: moves) {
            Board newBoard = currentBoard.clone();
            newBoard.applyButtonPress(move, id);
            newBoard = getMaxScore(newBoard, id, depthLimit - 1);
            if (newBoard.score > bestScore) {
                bestBoard = newBoard;
                bestScore = newBoard.score;
            }
        }
        return bestBoard;
    }



    public Board run() throws CloneNotSupportedException, InterruptedException {
        Board gameBoard = this.gameBoard;
        int currentPiece = 0;
        int MOVE_TIMER = 2 * 1000;
        while(true) {
            int id = gameBoard.permutation[currentPiece];
            if(!gameBoard.validPlacement(new Cords(1, 2), gameBoard.pieces.get(id)))
                break;
            gameBoard.addPiece(new Cords(1, 2), gameBoard.pieces.get(id));
//            updateDisplay(gameBoard);
            while(gameBoard.applyGravity(gameBoard.pieces.get(id))) {
                gameBoard = getMaxScore(gameBoard.clone(), id, 6);
//                TimeUnit.MILLISECONDS.sleep(MOVE_TIMER);
//                MOVE_TIMER *= 0.99;
            }
            gameBoard.score += gameBoard.emptyFullRows2() * 500;

//            updateDisplay(gameBoard);
            currentPiece++;
            if(currentPiece == 12) {
                break;
            }
        }
        return gameBoard;
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
