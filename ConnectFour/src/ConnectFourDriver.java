import ConnectFourGame.*;
import AI.*;
import java.util.Scanner;

public class ConnectFourDriver {

	public static void main(String args[])
	{
		Board board = new Board();
		Scanner scan = new Scanner(System.in);
		ConnectFourAI opponent = new ConnectFourAI();
		
		System.out.println(board);
		
		while (true)
		{
			//User moves
			boolean validMove = false;
			while (!validMove)
			{
			System.out.println("Which column will you pick?");			
			int check = 0;
			
			while (check < 1 || check > 7)
				check = scan.nextInt();
			
			//Decrement because we ask for 1-7 instead of 0-6
			check--;
			
			//Checks to make sure we can make move
			validMove = board.makeMove(check, true);
			
			if (!validMove)
				{
					System.out.println("This column is full!");
				}
			}
			
			if (board.determineWin())
			{
				System.out.println("You win!");
				break;
			}
			
			if (board.determineDraw()) //Technically not needed, since will only draw after opponent's turn
			{
				System.out.println("Draw.");
				break;
			}
			
			//Opponent reads board
			opponent.setBoard(board);
			
			//Opponent makes move
			int opponentMove = opponent.makeMove();
			board.makeMove(opponentMove, false);
			opponentMove++;
			System.out.println("Opponent picked " + opponentMove);
			
			if (board.determineWin())
			{
				System.out.println("You lose...");
				break;
			}
			if (board.determineDraw())
			{
				System.out.println("Draw.");
				break;
			}	
		}
		
		
	}
}