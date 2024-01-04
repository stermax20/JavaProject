package javaProject;

import java.util.Scanner;

public class Gomoku {

    private static final int BOARD_SIZE = 15;
    private static final char EMPTY = '+';
    private static final char BLACK = '◎';
    private static final char WHITE = '●';

    private char[][] board;
    private String player1;
    private String player2;

    public Gomoku() {
        board = new char[BOARD_SIZE][BOARD_SIZE];
        initializeBoard();
        getPlayerNames();
    }

    private void initializeBoard() {
        for (char[] row : board) {
            java.util.Arrays.fill(row, EMPTY);
        }
    }

    private void getPlayerNames() {
        Scanner sc = new Scanner(System.in);

        System.out.print("첫 번째 사용자의 이름을 입력하세요: ");
        player1 = sc.nextLine();
        System.out.print("두 번째 사용자의 이름을 입력하세요: ");
        player2 = sc.nextLine();
    }

    private void printBoard() {
        System.out.print("  ");
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (i < 9) {
                System.out.print(" " + (i + 1));
            } else {
                System.out.print(" " + (char) ('A' + i - 9));
            }
        }
        System.out.println();

        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.printf("%2d ", i + 1);

            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    private boolean isLegalMove(int row, int col) {
        return row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE && board[row][col] == EMPTY;
    }

    private boolean checkWin(int row, int col, char player) {
        int[][] directions = {{1, 0}, {0, 1}, {1, 1}, {1, -1}};

        for (int[] dir : directions) {
            int count = 1;

            for (int i = 1; i < 5; i++) {
                int newRow = row + i * dir[0];
                int newCol = col + i * dir[1];

                if (newRow >= 0 && newRow < BOARD_SIZE && newCol >= 0 && newCol < BOARD_SIZE && board[newRow][newCol] == player) {
                    count++;
                } else {
                    break;
                }
            }

            for (int i = 1; i < 5; i++) {
                int newRow = row - i * dir[0];
                int newCol = col - i * dir[1];

                if (newRow >= 0 && newRow < BOARD_SIZE && newCol >= 0 && newCol < BOARD_SIZE && board[newRow][newCol] == player) {
                    count++;
                } else {
                    break;
                }
            }

            if (count >= 5) {
                return true;
            }
        }

        return false;
    }

    public void playGame() {
        Scanner sc = new Scanner(System.in);

        char currentPlayer = BLACK;

        while (true) {
            System.out.println("현재 오목판:");
            printBoard();

            String playerName = (currentPlayer == BLACK) ? player1 : player2;

            System.out.printf("현재 돌 %c, %s님의 차례(행과 열, 띄어쓰기로 구분):%n", currentPlayer, playerName);
            int row, col;

            try {
                String input = sc.nextLine();

                if (input.equals("!")) {
                    System.out.print("정말 이번 대전을 포기하시겠습니까? (y, n): ");

                    String confirmInput = sc.nextLine();
                    if (confirmInput.equals("y")) {
                        System.out.println("게임이 끝났습니다.");

                        String winner = (currentPlayer == BLACK) ? player2 : player1;
                        System.out.printf("%s님이 대전에서 승리 하셨습니다.%n", winner);
                        sc.nextLine();
                        break;
                    } else if (confirmInput.equals("n")) {
                        System.out.println("대전을 그대로 진행합니다.");
                        continue;
                    } else {
                        System.out.println("잘못된 입력입니다. 다시 !를 눌러 대전종료 선택창으로 가세요.");
                        continue;
                    }
                }

                String[] inputArray = input.split(" ");
                row = Integer.parseInt(inputArray[0]);
                col = Integer.parseInt(inputArray[1]);
            } catch (NumberFormatException e) {
                System.out.println("잘못된 수 두기입니다. 다시 좌표값을 입력하세요.");
                continue;
            }

            if (isLegalMove(row, col)) {
                board[row][col] = currentPlayer;

                System.out.printf("%s님이 수를 놓았습니다. 위치: %d행, %d열%n", playerName, row, col);

                if (checkWin(row, col, currentPlayer)) {
                    String winner = (currentPlayer == BLACK) ? player1 : player2;
                    System.out.printf("%s %c 님이 대전에서 승리 하셨습니다.%n", winner, currentPlayer);
                    printBoard();
                    sc.nextLine();
                    break;
                }

                currentPlayer = (currentPlayer == BLACK) ? WHITE : BLACK;
            } else {
                System.out.println("잘못된 수 두기입니다. 다시 좌표값을 입력하세요.");
            }
        }

        sc.close();
    }

    public static void main(String[] args) {
        Gomoku omok = new Gomoku();
        omok.playGame();
    }
}
