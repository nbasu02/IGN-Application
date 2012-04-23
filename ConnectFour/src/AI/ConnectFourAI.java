package AI;
import java.lang.Math;
import java.util.Random;
import ConnectFourGame.*;

public class ConnectFourAI {
	
	Board theBoard;
	Random rand;
	
	public ConnectFourAI()
	{
		rand = new Random();
	}
	
	public void setBoard(Board afterMove)
	{
		theBoard = afterMove;
	}
	
	//We want to keep indices in original order, so no sorting will be done
	//Returns {max, index}
	public int[] getMax(int array[])
	{
		int max = 0;
		int index = 0;
		
		for (int i = 0; i < array.length; i++)
		{
			if (array[i] > max)
			{
				max = array[i];
				index = i;
			}
		}
		
		int returnvalue[] = new int[7];
		int count = 0;
		for (int i = 0; i < array.length; i++)
		{
			if (array[i] == max)
			{
			returnvalue[count] = i;
			count++;
			}
		}
		
		int finalReturnValue[] = new int[count];
		
		for (int i = 0; i < count; i++)
			finalReturnValue[i] = returnvalue[i];
		
		return finalReturnValue;
	}
	
	public int makeMove()
	{
		int moveWeights[] = {0, 0, 0, 0, 0, 0, 0};
		Board copyForColumns = new Board(theBoard);
		int[] columns = copyForColumns.getColumns();
		
		for (int i = 0; i < 7; i++)
		{
			//Checks for any winning moves
			if (columns[i] != 6)
			{
				Board copy = new Board(theBoard);
				Piece testPiece = copy.makeMoveTester(i, false);
				if (testPiece.foundFour())
					return i; //AI wins!
			}
			else
			{
				moveWeights[i] = -100; //Take this move out of the running for which move to make
			}
		}
		
		for (int i = 0; i < 7; i++)
		{
			//This may look almost identical to the loop above, however the key difference
			//is that this checks for any winning moves that the Player may have, and blocks the first
			//one it sees.  If more than one winning moves exist... the AI already lost anyway
			if (columns[i] != 6)
			{
				Board copy = new Board(theBoard);
				Piece testPiece = copy.makeMoveTester(i, true);
				if (testPiece.foundFour())
					return i; //AI blocks player's winning move
				else if (testPiece.maxLength() > 1)
					moveWeights[i] += testPiece.maxLength()-1;

			}
		}
		
		for (int i = 0; i < 7; i++)
		{
			if (columns[i] != 6)
			{
				Board copy = new Board(theBoard);
				Piece testPiece = copy.makeMoveTester(i, false);
				moveWeights[i] += testPiece.measureIncreaseValue(); 
				//Adds on the length of each row made by this move, to give AI
				//maximal benefits
				
				int testOppMoves = 0;
				int maxOppMove = 0;
				boolean foundLosingMove = false;
				
				for (int j = 0; j < 7; j++)
				{
					if (columns[j] != 6)
					{
					Board secondCopy = new Board(copy);
					Piece oppTestPiece = secondCopy.makeMoveTester(j, true);
					testOppMoves += oppTestPiece.measureIncreaseValue();
					if (testOppMoves > maxOppMove)
						maxOppMove = testOppMoves;
					
					if (oppTestPiece.foundFour() && !foundLosingMove)
						{
						moveWeights[i] -= 50; //Highly discouraging moves that cause AI to lose, but they still get chosen over invalid moves
						foundLosingMove = true;
						}
					}
				}
				
			}
		}
		
		int validMoves[] = getMax(moveWeights);
		int choice = Math.abs(rand.nextInt()) % validMoves.length;
		
		return validMoves[choice];
	}
	
	
	
}