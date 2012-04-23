import java.util.Scanner;

public class LicenseDriver 
{
	public static void main(String args[])
	{
		Scanner scan = new Scanner(System.in);
		System.out.println("Input population:");
		int population = scan.nextInt();
	
		if (population <= 0)
		{
		System.out.println("No one here.  No license plates to make.");
		System.exit(0);
		}
		
		if (population == 1)
		{
			System.out.println("We made one license plate for you.  Here it is: \"1\"");
			System.exit(0);
		}
		
		int setsOfDigits[] = new int[population];
		int digitcount = 0;
		int setsOfLetters[] = new int[population];
		int lettercount = 0;
		int extra = 0; //Counts how many extra plates are made
		int output;
		
		//Greater than 1 because dividing until 0 will give one extra subset
		while (population > 1)
		{
			if (population % 2 == 0)
				output = findBestEvenCase(population);
			else 
			{
				output = findBestOddCase(population);
				if (output % 2 == 0)
					extra++;
			}
			
			if (output <= 10)
				{
				setsOfDigits[digitcount] = output;
				digitcount++;
				if (output % 2 == 0 && population % 2 == 1)
				{
				population++;
				}
				
				}
			else
				{
				setsOfLetters[lettercount] = output;
				lettercount++;
					if (output % 2 == 0 && population % 2 == 1)
					{
					population++; 
					//To keep it consistent, because population + 1 was
					//inserted into findBestEvenCase.  Dividing output by population
					//would otherwise give us a result 1 higher than expected
					}
				}
			
			population /= output;
		}
		
		
		System.out.print("We used " + lettercount + " subsets of the alphabet A-Z.  ");
		
		if (lettercount != 0)
			System.out.print("Length of each subset: ");
		
		for (int i = 0; i < lettercount; i++)
			{
			System.out.print(" " + setsOfLetters[i]);
			if (i != lettercount - 1)
				System.out.print(", ");
			}
		
		System.out.println();
		
		System.out.print("We used " + digitcount + " subsets of the digits 0-9.  ");
		
		if (digitcount != 0)
			System.out.print("Length of each subset: ");
		
		for (int i = 0; i < digitcount; i++)
			{
			System.out.print(" " + setsOfDigits[i]);
			if (i != digitcount - 1)
				System.out.print(", ");
			}
		
		System.out.println();
		
		System.out.println("Number of extra plates: " + extra);
		
	}
	
	public static int findBestOddCase(int input)
	{
		//This case returns a list of a certain number of letters be printed out
		for (int i = 1; i < 17; i += 2)
		{
			if (input % (26 - i) == 0)
				return 26-i;
		
		}
		
		//This one returns a certain number of digits to be printed out
		for (int i = 1; i < 9; i += 2)
		{
			if (input % (10 - i) == 0)
				return 10-i;
		
		}
		
		//Will cost one extra plate.  This is often necessary in case of larger primes
		//BE SURE TO CHECK FOR PARITY WHEN USING THIS FUNCTION
		return findBestEvenCase(input+1); 
		
	}
	
	public static int findBestEvenCase(int input)
	{
		//Making a preference toward the entire alphabet
		if (input % 26 == 0)
			return 26;
		
		if (input % 10 == 0)
			return 10;
		
		//This case returns a list of a certain number of letters be printed out
		for (int i = 2; i < 16; i += 2)
		{
			if (input % (26 - i) == 0)
				return 26-i;
		
		}
		
		//This one returns a certain number of digits to be printed out
		for (int i = 2; i < 10; i += 2)
		{
			if (input % (10 - i) == 0)
				return 10-i;
		
		}
		
		return 0;
	}
}
