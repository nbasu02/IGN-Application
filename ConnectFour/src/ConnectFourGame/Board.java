package ConnectFourGame;

public class Board
{

Piece AllPieces[][];
char Pieces[][];
int columnOccupancy[];
boolean spotTaken[][];
boolean gameWon;

	public Board()
	{
		AllPieces = new Piece[7][6];
		Pieces = new char[7][6];
	    int[] tempcolumnOccupancy = {0, 0, 0, 0, 0, 0, 0};
	    columnOccupancy = tempcolumnOccupancy;
	    spotTaken = new boolean[7][6];
	    gameWon = false;
	    
	    for (int i = 0; i < 6; i++)
	    	for (int j = 0; j < 5; j++)
	    		spotTaken[i][j] = false;
	}
	
	public Board(Board other)
	{
		AllPieces = new Piece[7][6];
		
		for (int i = 0; i < 7; i++)
			for (int j = 0; j < 6; j++)
				try 
				{
					AllPieces[i][j] = new Piece(other.getAllPieces()[i][j]);
				}
				catch (NullPointerException e)
				{
					
				}
		
		gameWon = false;
		Pieces = other.getBoard();
		columnOccupancy = other.getColumns();
		spotTaken = other.getTaken();
		
	}
	
	public boolean makeMove(int column, boolean isPlayer)
	{
		if (columnOccupancy[column] < 6)
		{
			if (isPlayer)
				Pieces[column][columnOccupancy[column]] = 'P';
			else
				Pieces[column][columnOccupancy[column]] = 'C';
			
			AllPieces[column][columnOccupancy[column]] = new Piece(column, columnOccupancy[column], isPlayer, this);
			AllPieces[column][columnOccupancy[column]].findAndUpdateNeighbors(isPlayer, this, true);
			spotTaken[column][columnOccupancy[column]] = true;
			
			gameWon = AllPieces[column][columnOccupancy[column]].foundFour();
			findDeadEnds();
			
			columnOccupancy[column]++;
			
			System.out.println(this.toString());
			
			return true;
		}
		else
			return false;
	}
	
//Used by AI to analyze moves ahead of time	
public Piece makeMoveTester(int column, boolean isPlayer)
	{
		Piece returnPiece = new Piece(column, columnOccupancy[column], isPlayer, this);
		returnPiece.findAndUpdateNeighbors(isPlayer, this, false);
		
		return returnPiece;
	}	
	
public void findDeadEnds()
	{
		for (int i = 0; i < columnOccupancy.length; i++)
			for (int j = 0; j < columnOccupancy[i]; j++)
			{
				//Find horizontal dead-ends
				if (i < 3)
				{
				for (int k = 3; k > i; k--)
				{
					try
				
						{
							if (AllPieces[k][j].getIsPlayer() != AllPieces[i][j].getIsPlayer())
								AllPieces[i][j].manualChangeLength(0, 0);	
						}
					catch (NullPointerException e)
						{
							
						}
					}
				}
				else if (i > 3)
				{
					for (int k = 3; k < i; k++)
					{
						try
						{
							if (AllPieces[k][j].getIsPlayer() != AllPieces[i][j].getIsPlayer())
								AllPieces[i][j].manualChangeLength(0, 0);
						}
						catch (NullPointerException e)
						{
							
						}
					}		
				}
				
				//Find vertical dead-ends
				if (j < 3)
				{
				for (int k = 3; k > j; k--)
				{
					try
				
						{
							if (AllPieces[i][k].getIsPlayer() != AllPieces[i][j].getIsPlayer())
								AllPieces[i][j].manualChangeLength(1, 0);	
						}
					catch (NullPointerException e)
						{
							
						}
					}
				}
				else if (j > 2)
				{
					for (int k = 2; k < j; k++)
					{
						try
						{
							if (AllPieces[i][k].getIsPlayer() != AllPieces[i][j].getIsPlayer())
								AllPieces[i][j].manualChangeLength(1, 0);
						}
						catch (NullPointerException e)
						{
							
						}
					}		
				}
				
				//Diagonal cases, up-right
				if (i < 3 && j < 3)
				{
					int max;
					
					if (j > i)
						max = j;
					else
						max = i;
						
					for (int k = 1; k < 4 - max; k++)
					{
						try
						{
							if (AllPieces[i+k][j+k].getIsPlayer() != AllPieces[i][j].getIsPlayer())
								AllPieces[i][j].manualChangeLength(2, 0);
						}
						catch (NullPointerException e)
						{
							
						}
						catch (ArrayIndexOutOfBoundsException f)
						{
							
						}
					}
				}
				
				if (i > 3 && j > 2)
				{
					int min;
					int lowerBound;
					
					if (7 - i < 6 - j) //Closer to right edge
					{
						min = i;
						lowerBound = 3; //So we know bounded below by rows, not columns
					}
					else
					{
						min = j;
						lowerBound = 2;
					}
					
					for (int k = 1; k >= min - lowerBound; k--)
					{
						try
						{
							if (AllPieces[i-k][j-k].getIsPlayer() != AllPieces[i][j].getIsPlayer())
							AllPieces[i][j].manualChangeLength(2, 0);
						}
						catch (NullPointerException e)
						{
							
						}
						catch (ArrayIndexOutOfBoundsException f)
						{
							
						}
						
					}
					
				}
				
				//Diagonal cases, up-left
				if (i > 3 && j < 3)
				{
					int min;
					int bound = 3;
					
					if (7 - i < j)
						{
						min = i;
						}
					else
						{
						min = j;
						}
						
					for (int k = 1; k < Math.abs(min-bound); k++)
					{
						try
						{
							if (AllPieces[i-k][j+k].getIsPlayer() != AllPieces[i][j].getIsPlayer())
								AllPieces[i][j].manualChangeLength(3, 0);
						}
						catch (NullPointerException e)
						{
							
						}
						catch (ArrayIndexOutOfBoundsException f)
						{
							
						}
					}
				}
				
				if (i < 3 && j > 2)
				{
					int min;
					int bound;
					
					if (i < 6 - j) //Closer to right edge
					{
						min = i;
						bound = 3; //So we know bounded below by rows, not columns
					}
					else
					{
						min = j;
						bound = 2;
					}
					
					for (int k = 1; k >= Math.abs(min-bound); k--)
					{
						try
						{
							if (AllPieces[i+k][j-k].getIsPlayer() != AllPieces[i][j].getIsPlayer())
							AllPieces[i][j].manualChangeLength(3, 0);
						}
						catch (NullPointerException e)
						{
							
						}
						catch (ArrayIndexOutOfBoundsException f)
						{
							
						}
					}
					
				}				
				
				//Next, we do the cases that require two opposing pieces to make a dead end
				int checkHorizontal = -20;
				int checkVertical = -20;
				int checkUpRight = -20;
				int checkUpLeft = -20;
				
				for (int k = -3; k <= 3; k++)
				{
					try
					{
						if (AllPieces[i-k][j].getIsPlayer() != AllPieces[i][j].getIsPlayer())
						{
							if (Math.abs(checkHorizontal-k) <= 4)
								AllPieces[i][j].manualChangeLength(0, 0);
							else
								checkHorizontal = k; //This will check for two other pieces blocking a connect four
							
						}
						
						if (AllPieces[i][j-k].getIsPlayer() != AllPieces[i][j].getIsPlayer())
							if (Math.abs(checkVertical-k) <= 4)
								AllPieces[i][j].manualChangeLength(1, 0);
							else
								checkVertical = k;
						
						if (AllPieces[i-k][j-k].getIsPlayer() != AllPieces[i][j].getIsPlayer())
							if (Math.abs(checkUpRight-k) <= 4)
								AllPieces[i][j].manualChangeLength(2, 0);
							else
								checkUpRight = k;
					
						if (AllPieces[i+k][j-k].getIsPlayer() != AllPieces[i][j].getIsPlayer())
							if (Math.abs(checkUpLeft-k) <= 4)
								AllPieces[i][j].manualChangeLength(3, 0);
							else
								checkUpLeft = k;
					}
					catch (NullPointerException e)
					{
						
					}
					catch (ArrayIndexOutOfBoundsException f)
					{
						
					}
				}
				
			}
	}


	public int numOfPieces()
	{
		int returnvalue = 0;
		for (int i = 0; i < columnOccupancy.length; i++)
			returnvalue += columnOccupancy[i];
		
		return returnvalue;
	}
	
	public Piece getPiece(int posX, int posY)
	{
		return AllPieces[posX][posY];
	}
	
	public char[][] getBoard()
	{
		return Pieces;
	}
	
	public boolean[][] getTaken()
	{
		return spotTaken;
	}
	
	public int[] getColumns()
	{
		return columnOccupancy;
	}
	
	public Piece[][] getAllPieces()
	{
		return AllPieces;
	}
	
	public String toString()

	{
		String board = "";
		
		//Print top row first to show the board like in real life.  It's almost a physics engine!
		for (int i = 5; i >= 0; i--)
			{
			if (i != 5)
				board += "\n";
			for (int j = 0; j < 7; j++)
				{
				if (Pieces[j][i] != '\0') 
					board += "[" + Pieces[j][i] + "] ";
				else
					board += "[ ] ";
				}
			}	
		
		board += "\n 1   2   3   4   5   6   7";
		board += "\n----------------------------\n";
		
		return board;
	}
	
	public void setPiece(int posX, int posY, Piece changePieceParams)
	{
		AllPieces[posX][posY] = changePieceParams;
	}
	
	public char getColor(int posX, int posY)
	{
		return Pieces[posX][posY];
	}
	
	public boolean isSameColor(int posX, int posY, boolean isPlayer)
	{
		return (isPlayer == AllPieces[posX][posY].getIsPlayer());
	}
	
	public boolean foundFour(int rowArray[])
	{
		for (int i = 0; i < rowArray.length; i++)
			if (rowArray[i] >= 4)
				return true;
		
		return false;
	}
	
	public boolean determineWin()
	{
		return gameWon;
	}
	
	public boolean determineDraw()
	{
		return (numOfPieces() == 42 && !determineWin());
	}


}


