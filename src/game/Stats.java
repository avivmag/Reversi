package game;

/**
 * Class which holds all static variables that needed to be saved between all screens of the program.
 */
public class Stats {
	private static Player player1;
	private static Player player2;
	private static Player[][] board;
	private static Player currentPlayer;
	private static int n;
	private static int m;
	public enum DifficultyType { Penny, Lenard, Sheldon };
	private static DifficultyType difficulty;
	private static boolean player1Human;
	private static boolean player2Human;
	
	public static Player getPlayer1() {
		return player1;
	}
	public static void setPlayer1(Player player1) {
		Stats.player1 = player1;
	}
	public static Player getPlayer2() {
		return player2;
	}
	public static void setPlayer2(Player player2) {
		Stats.player2 = player2;
	}
	public static Player[][] getBoard() {
		return board;
	}
	public static void setBoard(Player[][] board) {
		Stats.board = board;
	}
	public static Player getCurrentPlayer() {
		return currentPlayer;
	}
	public static void setCurrentPlayer(Player currentPlayer) {
		Stats.currentPlayer = currentPlayer;
	}
	public static int getN() {
		return n;
	}
	public static void setN(int n) {
		Stats.n = n;
	}
	public static int getM() {
		return m;
	}
	public static void setM(int m) {
		Stats.m = m;
	}
	public static DifficultyType getDifficulty() {
		return difficulty;
	}
	public static void setDifficulty(DifficultyType difficulty) {
		Stats.difficulty = difficulty;
	}
	public static boolean isPlayer1Human() {
		return player1Human;
	}
	public static void setPlayer1Human(boolean player1Human) {
		Stats.player1Human = player1Human;
	}
	public static boolean isPlayer2Human() {
		return player2Human;
	}
	public static void setPlayer2Human(boolean player2Human) {
		Stats.player2Human = player2Human;
	}
}