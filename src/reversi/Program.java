package reversi;

import java.util.Random;

import game.ComputerPlayer;
import game.Player;
import game.Stats;
import game.Stats.DifficultyType;

public class Program {

	public static void main(String[] args) {
		Stats.setM(8);
		Stats.setN(8);
		Stats.setPlayer1(new Player(-1,-1,"player1"));
		Stats.setDifficulty(DifficultyType.Lenard);
		Stats.setPlayer2(new ComputerPlayer(Stats.getPlayer1().getHue(), Stats.getPlayer1().getImageID(),"Computer"));

		Random rand = new Random();
		int r = rand.nextInt(2);
		if (r == 0)
			Stats.setCurrentPlayer(Stats.getPlayer1());
		else
			Stats.setCurrentPlayer(Stats.getPlayer2());
		new MainScreen();
	}
}
