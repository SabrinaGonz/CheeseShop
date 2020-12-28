
import java.util.Scanner;
public class Shop {
	//partner: Jude Osura 

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);

		String[] names = new String[0];
		double[] prices = new double[0];
		int[] amounts = new int[0];
		double[] discounts = new double[0];

		double discountThreshold = 0;
		double additionalDiscountRate = 0;

		//make a loop that continues the sequence of choosing the function wanted
		int i = 1;
		while (i == 1) {
			intro();
			int function = input.nextInt();


			if (function == 1) { // Setting up shop
				System.out.print("Please enter the number of items to setup shop: ");
				int maxItem = input.nextInt();
				names = new String[maxItem];
				prices = new double[maxItem];
				amounts = new int [maxItem];
				discounts = new double[maxItem];

				i = setup(input, names, prices, discounts);

				System.out.print("\nEnter the dollar amount to qualify for Additional Discount (or 0 if non offered): ");
				discountThreshold = input.nextDouble();

				if (discountThreshold > 0) {
					System.out.print("Enter the Additional Discount rate (e.g., 0.1 or 10%): ");
					additionalDiscountRate = input.nextDouble();
					while (additionalDiscountRate < 0.0 || additionalDiscountRate > 0.5) {
						System.out.print("Invalid input. Enter a value > 0 and <= 0.5: ");
						additionalDiscountRate = input.nextDouble();
					}
				}
				System.out.println(" ");
			}
			if (function == 2) { // Buying Items
				i = buy(input, names, amounts);
			}	
			if(function == 3) { //Listing out bought items
				i = list (names, amounts, prices);
			}
			if (function == 4) { // Checkout
				int reRun = checkout (input, names, amounts, prices, discounts, discountThreshold, additionalDiscountRate);
				if (reRun == 1) {
					names = new String[0];
					prices = new double[0];
					amounts = new int [0];
					discounts = new double[0];
				}
				else 
					break;
			}

		}

	}


	// Beginning Messages
	public static void intro() {
		System.out.println("This program supports 4 functions:");
		System.out.println("   1.  Setup shop");
		System.out.println("   2.  Buy");
		System.out.println("   3.  List Items");
		System.out.println("   4.  Checkout");
		System.out.print("Please choose the function you want: ");
	}

	// Setting up Shop
	public static int setup(Scanner input,String names[], double prices[], double discounts[]) {
		System.out.println("");
		for (int i = 0; i < names.length; i++) {
			System.out.print("Enter the name of the " + numSuffix(i + 1) + " product: ");
			names[i] = input.next();

			System.out.print("Enter the per package price of " + names[i] + ": ");
			prices[i] = input.nextDouble();

			System.out.println("Enter the number of packages ('x') to qualify for Special Discount (Buy 'x' get 1 free)");
			System.out.print("for " + names[i] + ", or 0 if no Special Discount offered: ");
			discounts[i] = input.nextDouble();

		}

		return 1;
	}


	// Buying items from shop
	public static int buy(Scanner input, String names[], int amounts[]) {
		if (names.length == 0) {
			System.out.println("\nShop is not set up yet!\n");
			return 1;
		}
		// User buys items
		System.out.println("");
		for (int i = 0; i < names.length; i++) {
			System.out.print("Enter the number of " + names[i] + " packages to buy: ");
			amounts[i] = input.nextInt();

			while (amounts[i] < 0.0) {
				System.out.print("invalid input. Enter a value >= 0: ");
				amounts[i] = input.nextInt();
			}
		}
		System.out.println("");
		return 1;
	}


	// Lists out bought items
	public static int list(String names[], int amounts[], double prices[]) {
		if (names.length == 0) {
			System.out.println("\nShop is not set up yet!\n");
			return 1;
		}
		// checking if user has bought items
		else { 
			// goes through array and uses counter to check if user has actually bought items or not
			int amountCounter = 0;
			for (int i = 0; i < names.length; i++) {
				if (amounts[i] > 0)
					amountCounter++;
			}
			// if the user has not bought items, tells user to buy items first
			if (amountCounter == 0) {
				System.out.println("\nYou have not bought anything!\n");
				return 1;
			}

			// if the user has bought items, then prints the list of items purchased
			if (amountCounter > 0) {
				for (int i = 0; i < names.length; i++) {
					if (amounts[i] > 0)
						System.out.printf("\n" + amounts[i] + " packages of " + names[i] + " @ $%.2f per pkg = $%.2f", prices[i], (prices[i] * amounts[i]));
				}
			}
			System.out.println("\n");
			return 1;
		}

	}

	// Checking out of store
	public static int checkout(Scanner input, String names[], int amounts[], double prices[], double discounts[], double discountThreshold, double additionalDiscountRate) {
		if (amounts.length == 0) {
			System.out.println("\nShop is not set up yet!\n");
			return 1;
		}
		// if the user has not bought items, tells user to buy items first
		int amountCounter = 0;
		for (int i = 0; i < amounts.length; i++) {
			if (amounts[i] > 0)
				amountCounter++;
		}
		if (amountCounter == 0) {
			System.out.println("\nYou have not bought anything!\n");
			return 1;
		}

		double originalSubtotal = 0;
		double specialDiscounts = 0;
		for (int i = 0; i < amounts.length; i++) {
			originalSubtotal += (prices[i] * amounts[i]);
			if (discounts[i] > 0) {
				specialDiscounts += (int)(amounts[i] / (discounts[i] + 1)) * prices[i];
			}
		}
		System.out.printf("\nOriginal Sub Total:              $%.2f\n", originalSubtotal);

		if (specialDiscounts > 0) 
			System.out.printf("Special Discounts:              -$%.2f\n", specialDiscounts);
		else
			System.out.println("No Special Discounts applied");

		double newSubtotal = originalSubtotal - specialDiscounts;
		System.out.printf("New sub Total:                   $%.2f\n", (newSubtotal));

		double additionalDiscount = 0;
		if (discountThreshold == 0 || newSubtotal < discountThreshold) {
			System.out.println("You did not qualify for an Additional Discount");
		}
		else {
			additionalDiscount = newSubtotal * additionalDiscountRate;
			System.out.print("Additional " + (int)(additionalDiscountRate * 100) + "% Discount:");
			System.out.printf("        -$%.2f\n", additionalDiscount);
		}

		double finalSubtotal = newSubtotal - additionalDiscount;
		System.out.printf("Final Sub Total:                 $%.2f", finalSubtotal);

		System.out.println("\n\nThanks for coming!\n");
		System.out.println("-------------------------------------------------");
		System.out.print("Would you like a re-run (1 for yes, 0 for no)? ");
		int reRun = input.nextInt();
		System.out.println("-------------------------------------------------\n");

		return reRun;
	}


	// Number Suffixes
	public static String numSuffix(int i) {
		int rem = i % 10;

		switch (rem) {
		case 0:
		case 4:
		case 5:
		case 6:
		case 7: 
		case 8:
		case 9:
			return(i + "th");
		case 1:
			if (i % 100 != 11)
				return (i + "st");
			else 
				return (i + "th");
		case 2:
			if (i % 100 != 12)
				return (i + "nd");
			else 
				return (i + "th");
		case 3:
			if (i % 100 != 13)
				return (i + "rd");
			else 
				return ( i + "th");
		default:
			break;
		}
		return "";
	}

}
