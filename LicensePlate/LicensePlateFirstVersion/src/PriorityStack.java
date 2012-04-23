/*
 * This class keeps a sorted list, from greatest to smallest, of the total
 * distance of each path from the beginning.  The path will be measured in terms
 * of the product of a string of 26s and 10s, representing A-Z and 0-9.
 * At each iteration, we pop off the one at the end, and then push the two
 * values adjacent to it, which will be distance * 10 and distance * 26,
 * unless the total distance is greater than the population, in which case we are
 * done.  This process is very similar to the Dijkastra Algorithm
 */

public class PriorityStack {

	int LicenseNode[][];
	int size;
	int counter;
	
	public PriorityStack()
	{
		size = 10;
		counter = 0;
		LicenseNode = new int[size][3];
	}
	
	//For when the list gets too big, dynamically sized
	public void expandList()
	{
		size *= 2;
		int tempList[][] = new int[size][3];
		for (int i = 0; i < size/2; i++)
		{
			tempList[i] = LicenseNode[i];
		}
		
		LicenseNode = tempList;
	}
	
	//Pop off the last one in the list, with least distance of all unexpanded nodes
	public int[] pop()
	{
		if (counter != 0)
		{
		int returnvalue[] = LicenseNode[counter-1];
		counter--;
		return returnvalue;
		}
		else
			return null; //Mostly unnecessary
		
	}
	
	//Push in and auto-sort
	public void push(int[] newValue)
	{
		int i = counter;
		
		while (i > 0 && newValue[0] > LicenseNode[i-1][0])
		{
			LicenseNode[i] = LicenseNode[i-1];
			i--;
		}
		
		LicenseNode[i] = newValue;
		counter++;
		if (counter == size)
			expandList();
	}
	
	public int distancePeek()
	{
		return LicenseNode[counter-1][0];
	}
	
}
