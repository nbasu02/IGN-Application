import java.util.Scanner;

public class LicenseDriver 
{

	public static void main(String[] args) 
	{
		Scanner scan = new Scanner(System.in);
		System.out.println("Input population:");
		int population = scan.nextInt();
		
		if (population <= 0)
		{
			System.out.println("No one here.  No license plates to make.");
			System.exit(0);
		}
		
		//Oversized, but better to have and don't need
		int multTable[][] = new int[population*2][population*2];
		//This will determine which path in multTable to take
		PriorityStack findingShortest = new PriorityStack();
		
		int i = 0;
		int j = 0;
		
		multTable[0][0] = 1;
		
		//While I could have put in constants for below line, this is a basis
		//for what I will be pushing into the array: the value of the node and its
		//indices.  However, this will not be a double for loop
		int tempArray[] = {multTable[i][j], i ,j};
		findingShortest.push(tempArray);
		
		while (population - findingShortest.distancePeek() > 0)
		{
			//First remove current node
			int nodeToExpand[] = findingShortest.pop();
			
			i = nodeToExpand[1];
			j = nodeToExpand[2];
			
			//Create entries to the right and below, and push to stack
			multTable[i+1][j] = multTable[i][j]*10;
			multTable[i][j+1] = multTable[i][j]*26;
			int tempArray1[] = {multTable[i+1][j], i+1, j};
			int tempArray2[] = {multTable[i][j+1], i, j+1};
			
			findingShortest.push(tempArray1);
			findingShortest.push(tempArray2);
		}
		
		int finalResult[] = findingShortest.pop();
		int total = finalResult[0];
		int extra = finalResult[0] - population;
		int numDigit = finalResult[1];
		int numAlpha = finalResult[2];
		
		
		System.out.println("Population = " + population);
		System.out.println("Number of digits: " + numDigit);
		System.out.println("Number of letters: " + numAlpha);
		System.out.println("Number printed: " + total);
		System.out.println("Number of extra: " + extra);
		
	}

}
