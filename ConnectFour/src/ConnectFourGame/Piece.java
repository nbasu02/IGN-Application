package ConnectFourGame;

public class Piece {
	
	Piece samePlayerPieces[];
	int posX;
	int posY;
	int pieceCount;
	int pathLengths[];
	boolean isPlayer;
	//First index represents how many pieces in a row you have horizontally
	//Second is vertical
	//Third is bottom-left to top-right
	//Fourth is bottom-right to top-left
	
	public Piece(int locX, int locY, boolean isPlayerPiece, Board currentBoard)
	{
		posX = locX;
		posY = locY;
		samePlayerPieces = new Piece[8];
		int pathLengthsTemp[] = {1, 1, 1, 1};
		pathLengths = pathLengthsTemp;
		isPlayer = isPlayerPiece;			
		
		pieceCount = 0;
	}
	
	public Piece(Piece other)
	{
		samePlayerPieces = other.getAdjacencyMatrix();
		posX = other.getPosition()[0];
		posY = other.getPosition()[1];
		pathLengths = other.getLengths();
		isPlayer = other.getIsPlayer();
		pieceCount = other.getPieceCount();
	}
	
	public int getDirectionalValue(int locX, int locY)
	{
		//Input values are intended to be from the new pieces.  Used by AI to determine next move
		
		
		//If input Piece is next to this piece
		if (locY == posY && Math.abs(locX-posX) == 1)
			return pathLengths[0];
		
		//If input Piece is above/below
		if (locX == posX && Math.abs(locY-posY) == 1)
			return pathLengths[1];
		
		//Diagonal, up-rightward
		if ((locX - posX == 1 && locY - posY == 1) || (locX - posX == -1 && locY - posY == -1))
			return pathLengths[2];
		
		//Diagonal, up-leftward
		if ((locX - posX == 1 && locY - posY == -1) || (locX - posX == -1 && locY - posY == 1))
			return pathLengths[3];
		
		//Else, return 0 as an error
		return 0;
	}
	
	public boolean foundFour()
	{
		for (int i = 0; i < pathLengths.length; i++)
			if (pathLengths[i] >= 4)
				return true;
		
		return false;
	}
	
	//This function returns how much value a piece's total is
	//i.e. If a piece is a part of a horizontal chain of 3, vertical chain of 2,
	//and diagonal chains of 1 and 2, will return 8
	//Used by AI
	public int measureIncreaseValue()
	{
		int total = 0;
		
		for (int i = 0; i < pathLengths.length; i++)
			total += pathLengths[i];
		
		return total;
	}
	
	public int maxLength()
	{
		int max = 1;
		for (int i = 0; i < pathLengths.length; i++)
			if (pathLengths[i] > max)
				max = pathLengths[i];
		
		return max;
	}
	
	public int[] getPosition()
	{
		int position[] = {posX, posY};
		return position;
	}
	
	public int getPieceCount()
	{
		return pieceCount;
	}
	
	public void addNewAdjacentPiece(Piece newPiece, int changeIndex, Board currentBoard)
	{
		
		while (samePlayerPieces[pieceCount] != null)
			pieceCount++; //For some reason it keeps resetting itself when I call this function.  Will try to fix this later
		
		samePlayerPieces[pieceCount] = newPiece;
		
	
		changeLength(newPiece, changeIndex, currentBoard);
		
		pieceCount++;
		
	}
	
	//This method will look for each piece in a certain direction i.e. left, right, up, down, etc. and change their path lengths
	//representing those paths accordingly.  Ex. if a new piece is placed between two of the same color, tell both of them their values
	//are now 3 horizontally instead of 1.  Likewise, if we drop 1 next to two, we need to tell those two their values are now 3
	public void changeLength(Piece lastPiece, int changeIndex, Board currentBoard)
	{
		int position[] = lastPiece.getPosition();
		
		
		pathLengths[changeIndex] = lastPiece.getLengths()[changeIndex];
		currentBoard.getPiece(position[0], position[1]).setLengths(pathLengths[changeIndex], changeIndex);

		int location[] = getOppositeSide(lastPiece, currentBoard);
		
		if (location != null)
		{
		try 
			{
				if (currentBoard.getTaken()[location[0]][location[1]] 
					&& currentBoard.isSameColor(location[0], location[1], isPlayer))
				{
					currentBoard.getPiece(location[0], location[1]).changeLength(this, changeIndex, currentBoard);
				}
			}
		catch (NullPointerException n)
			{			
				
			}
		}
	}
	
	//Primarily used to find dead-end chains
	public void manualChangeLength(int changeIndex, int newValue)
	{
		pathLengths[changeIndex] = newValue;
	}

	//Returns the next adjacent piece on board on the opposite side of the input one
	public int[] getOppositeSide(Piece incoming, Board currentBoard)
	{
		try
		{
			
		int location[] = incoming.getPosition();

		location[0] = posX + (posX - location[0]); //Second part should be 1 or -1
		location[1] = posY + (posY - location[1]);
				
		//Use this to check for ArrayOutOfBounds
		Piece check = currentBoard.getPiece(location[0], location[1]);
		
		return location;
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			//System.out.println("Woo");
			return null;
		}
	}
	
	
public void setLengths(int newlength, int index)
	{
		pathLengths[index] = newlength;
	}	
	
	
	public int[] getLengths()
	{
		return pathLengths;
	}
	
	public Piece[] getAdjacencyMatrix()
	{
		return samePlayerPieces;
	}
	
	public String toString()
	{
		String message = "";
		
		if (isPlayer)
		message += "Player's piece at position " + this.getPosition()[0] + ", " + this.getPosition()[1];
		else
		message += "Computer's piece at position " + this.getPosition()[0] + ", " + this.getPosition()[1];
		
		message += "\nHorizontal value is " + pathLengths[0];
		message += "\nVertical value is " + pathLengths[1];
		message += "\nBottom-left to top-right value is " + pathLengths[2];
		message += "\nBottom-right to top-left value is " + pathLengths[3];
		
		return message;
		
	}
	
	
	
	//This is going to be messy, but first need make the list of all surrounding same-colored pieces and then
	//have them copy the current piece
	//We will also add together the values to find the value of each path, i.e. how many in a row you have
	//A value of 4 or more in any of these indices means you win
	
	public boolean getIsPlayer()
	{
		return isPlayer;
	}
	
	//There should be an easy way to make one index 0 if the path is dead
	
	void findAndUpdateNeighbors(boolean isPlayer, Board currentBoard, boolean UpdateOthers)

	{
		if (posX != 0)
		{
			if (currentBoard.getPiece(posX-1, posY) != null && currentBoard.isSameColor(posX-1, posY, isPlayer))
				{
				samePlayerPieces[pieceCount] = currentBoard.getPiece(posX-1, posY);
				pathLengths[0] += samePlayerPieces[pieceCount].getLengths()[0];
				pieceCount++;
				}
			
			if (posY != 0)
				if (currentBoard.getPiece(posX-1, posY-1) != null && currentBoard.isSameColor(posX-1, posY-1, isPlayer))
				{
					samePlayerPieces[pieceCount] = currentBoard.getPiece(posX-1, posY-1);
					pathLengths[2] += samePlayerPieces[pieceCount].getLengths()[2];
					//System.out.println("Up-right: " + pathLengths[2]);
					pieceCount++;
				}
			
			if (posY != 5)
				if (currentBoard.getPiece(posX-1, posY+1) != null && currentBoard.isSameColor(posX-1, posY+1, isPlayer))
				{
					samePlayerPieces[pieceCount] = currentBoard.getPiece(posX-1, posY+1);
					pathLengths[3] += samePlayerPieces[pieceCount].getLengths()[3];
					pieceCount++;
				}
		}
		
		
		if (posY != 0)
		{

			if (currentBoard.getPiece(posX, posY-1) != null && currentBoard.isSameColor(posX, posY-1, isPlayer))
			{
				if (currentBoard.getPiece(posX, posY-1) != null && currentBoard.isSameColor(posX, posY-1, isPlayer))
				{
					samePlayerPieces[pieceCount] = currentBoard.getPiece(posX, posY-1);
					pathLengths[1] += samePlayerPieces[pieceCount].getLengths()[1];
					pieceCount++;
				}
			}
		}		
		
		if (posX != 6)
		{
			if (currentBoard.getPiece(posX+1, posY) != null && currentBoard.isSameColor(posX+1, posY, isPlayer))
				{
				samePlayerPieces[pieceCount] = currentBoard.getPiece(posX+1, posY);
				pathLengths[0] += samePlayerPieces[pieceCount].getLengths()[0];
				pieceCount++;
				}
			
			if (posY != 0)
				if (currentBoard.getPiece(posX+1, posY-1) != null && currentBoard.isSameColor(posX+1, posY-1, isPlayer))
				{
					samePlayerPieces[pieceCount] = currentBoard.getPiece(posX+1, posY-1);
					pathLengths[3] += samePlayerPieces[pieceCount].getLengths()[3];
					pieceCount++;
				}
			
			if (posY != 5)
				if (currentBoard.getPiece(posX+1, posY+1) != null && currentBoard.isSameColor(posX+1, posY+1, isPlayer))
				{
					samePlayerPieces[pieceCount] = currentBoard.getPiece(posX+1, posY+1);
					pathLengths[2] += samePlayerPieces[pieceCount].getLengths()[2];
					pieceCount++;
				}
		}

		if (UpdateOthers)
		{
		if (posX != 0)
		{
			if (currentBoard.getPiece(posX-1, posY) != null && currentBoard.isSameColor(posX-1, posY, isPlayer))
				{
				currentBoard.getPiece(posX-1, posY).addNewAdjacentPiece(this, 0, currentBoard);
				}
			
			if (posY != 0)
				if (currentBoard.getPiece(posX-1, posY-1) != null && currentBoard.isSameColor(posX-1, posY-1, isPlayer))
				{
					
					currentBoard.getPiece(posX-1, posY-1).addNewAdjacentPiece(this, 2, currentBoard);
				}
			
			if (posY != 5)
				if (currentBoard.getPiece(posX-1, posY+1) != null && currentBoard.isSameColor(posX-1, posY+1, isPlayer))
				{
					currentBoard.getPiece(posX-1, posY+1).addNewAdjacentPiece(this, 3, currentBoard);
				}
		}
		

		if (posX != 6)
		{
			if (currentBoard.getPiece(posX+1, posY) != null && currentBoard.isSameColor(posX+1, posY, isPlayer))
				{
				currentBoard.getPiece(posX+1, posY).addNewAdjacentPiece(this, 0, currentBoard);
				}
			
			if (posY != 0)
				if (currentBoard.getPiece(posX+1, posY-1) != null && currentBoard.isSameColor(posX+1, posY-1, isPlayer))
				{
					currentBoard.getPiece(posX+1, posY-1).addNewAdjacentPiece(this, 3, currentBoard);
				}
			
			if (posY != 5)
				if (currentBoard.getPiece(posX+1, posY+1) != null && currentBoard.isSameColor(posX+1, posY+1, isPlayer))
				{
					currentBoard.getPiece(posX+1, posY+1).addNewAdjacentPiece(this, 2, currentBoard);
				}
		}
		
		if (posY != 0)
		{
			if (currentBoard.getPiece(posX, posY-1) != null && currentBoard.isSameColor(posX, posY-1, isPlayer))
				currentBoard.getPiece(posX, posY-1).addNewAdjacentPiece(this, 1, currentBoard);
		}
		}
		
	}
	
}