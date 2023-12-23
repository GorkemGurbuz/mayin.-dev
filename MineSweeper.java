import java.util.Random;
import java.util.Scanner;

public class MineSweeper {
    private char[][] mineField;
    private char[][] userField;
    private int rows;
    private int cols;
    private int totalMines;
    private int remainingTiles;

    public MineSweeper(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.totalMines = rows * cols / 4;
        this.remainingTiles = rows * cols - totalMines;
        this.mineField = new char[rows][cols];
        this.userField = new char[rows][cols];
        initializeFields();
        placeMines();
    }

    private void initializeFields() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                mineField[i][j] = '-';
                userField[i][j] = '-';
            }
        }
    }

    private void placeMines() {
        Random random = new Random();
        int minesPlaced = 0;
        while (minesPlaced < totalMines) {
            int row = random.nextInt(rows);
            int col = random.nextInt(cols);
            if (mineField[row][col] != '*') {
                mineField[row][col] = '*';
                minesPlaced++;
            }
        }
    }

    private void printUserField() {
        System.out.println("  " + getColumnIndexes());
        for (int i = 0; i < rows; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < cols; j++) {
                System.out.print(userField[i][j] + " ");
            }
            System.out.println();
        }
    }

    private String getColumnIndexes() {
        StringBuilder indexes = new StringBuilder(" ");
        for (int i = 0; i < cols; i++) {
            indexes.append((i + 1)).append(" ");
        }
        return indexes.toString();
    }

    private void revealNeighbors(int row, int col) {
        int mineCount = countSurroundingMines(row, col);

        if (mineCount > 0) {
            userField[row][col] = (char) (mineCount + '0');
            remainingTiles--;
        } else {
            userField[row][col] = ' ';
            remainingTiles--;
            for (int i = row - 1; i <= row + 1; i++) {
                for (int j = col - 1; j <= col + 1; j++) {
                    if (i >= 0 && i < rows && j >= 0 && j < cols && userField[i][j] == '-') {
                        revealTile(i, j);
                    }
                }
            }
        }
    }

    private int countSurroundingMines(int row, int col) {
        int mineCount = 0;
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i >= 0 && i < rows && j >= 0 && j < cols && mineField[i][j] == '*') {
                    mineCount++;
                }
            }
        }
        return mineCount;
    }

    private void revealTile(int row, int col) {
        if (mineField[row][col] == '0') {
            userField[row][col] = ' ';
            remainingTiles--;
            revealNeighbors(row, col);
        } else {
            userField[row][col] = (char) (countSurroundingMines(row, col) + '0');
            remainingTiles--;
        }
    }

    private boolean isValidMove(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            System.out.println("Geçersiz koordinat! Tekrar deneyin.");
            return false;
        } else if (userField[row][col] != '-') {
            System.out.println("Bu koordinat daha önce seçildi. Başka bir koordinat girin.");
            return false;
        }
        return true;
    }

    private void printMineField() {
        System.out.println("Mayın Alanı:");
        System.out.println("  " + getColumnIndexes());
        for (int i = 0; i < rows; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < cols; j++) {
                System.out.print(mineField[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void playGame() {
        Scanner scanner = new Scanner(System.in);

        while (remainingTiles > 0) {
            printUserField();
            int row, col;
            do {
                System.out.print("Satır girin (1-" + rows + "): ");
                row = scanner.nextInt() - 1;
                System.out.print("Sütun girin (1-" + cols + "): ");
                col = scanner.nextInt() - 1;
            } while (!isValidMove(row, col));

            if (mineField[row][col] == '*') {
                System.out.println("Mayına bastınız! Oyun bitti!");
                printMineField();
                return;
            } else {
                revealNeighbors(row, col);
            }
        }

        System.out.println("Tebrikler! Tüm kutuları açtınız. Oyunu kazandınız!");
        printMineField();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Satır sayısını girin: ");
        int rows = scanner.nextInt();
        System.out.print("Sütun sayısını girin: ");
        int cols = scanner.nextInt();

        if (rows < 2 || cols < 2) {
            System.out.println("Geçersiz boyut! Minimum 2x2 boyutunda olmalıdır.");
            return;
        }

        MineSweeper game = new MineSweeper(rows, cols);
        game.playGame();
    }
}
