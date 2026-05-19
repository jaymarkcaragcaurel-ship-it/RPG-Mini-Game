import java.util.Arrays;
import java.util.Scanner;

public class Act4  {

    public static void main(String[] args) {

        Scanner Scanner = new Scanner(System.in);

        String[][] paglagyan = {
                {"v", "v", "v", "v", "v"},
                {"v", "v", "v", "v", "v"},
                {"v", "v", "v", "v", "v"},
                {"v", "v", "v", "v", "v"},
                {"v", "v", "v", "v", "v"}
        };

        int seats_vacant = 0;
        String admin_reply;

        for (int column = 0; column < paglagyan.length; column++) {
            System.out.println("Aisle " + column + ": "
                    + Arrays.toString(paglagyan[column]));

            for (int seat = 0; seat < paglagyan[column].length; seat++) {
                if (paglagyan[column][seat].equals("v")) {
                    seats_vacant++;
                }
            }
        }

        System.out.println("Seats Vacant: " + seats_vacant);

        do {

            int clients = 0;
            seats_vacant = 0;
            int aisle_chosen = 0;
            int row_chosen = 0;

            System.out.print("\nHow many clients do you want to enter? | ");
            clients = Integer.parseInt(Scanner.nextLine());

            for (int i = 0; i < clients; i++) {

                System.out.print("\nWhat aisle do you want to seat? Choose between 0-4 | ");
                aisle_chosen = Integer.parseInt(Scanner.nextLine());

                System.out.println("");
                System.out.println("Available seats: "
                        + Arrays.toString(paglagyan[aisle_chosen]));

                System.out.print("\nWhat row would you like to seat? Choose between 0-4 | ");
                row_chosen = Integer.parseInt(Scanner.nextLine());

                while (paglagyan[aisle_chosen][row_chosen].equals("o")) {

                    System.out.println("\nSeat is taken, please choose another one");

                    System.out.print("\nWhat aisle do you want to seat? Choose between 0-4? | ");
                    aisle_chosen = Integer.parseInt(Scanner.nextLine());

                    System.out.println("Available seats: "
                            + Arrays.toString(paglagyan[aisle_chosen]));

                    System.out.print("\nWhat row would you like to seat? Choose between 0-4 | ");
                    row_chosen = Integer.parseInt(Scanner.nextLine());
                }

                paglagyan[aisle_chosen][row_chosen] = "o";

                System.out.println("Aisle " + aisle_chosen + ": "
                        + Arrays.toString(paglagyan[aisle_chosen]));

                System.out.println("\nSeat reserved for client. Thank you");
            }

            System.out.println("");

            for (int column = 0; column < paglagyan.length; column++) {

                System.out.println("Aisle " + column + ": "
                        + Arrays.toString(paglagyan[column]));

                for (int seat = 0; seat < paglagyan[column].length; seat++) {

                    if (paglagyan[column][seat].equals("v")) {
                        seats_vacant++;
                    }
                }
            }

            System.out.println("Seats Vacant: " + seats_vacant);

            System.out.print("\nDo you still want to continue? [Y/N] ");
            admin_reply = Scanner.nextLine();

        } while (admin_reply.equalsIgnoreCase("Y"));

        System.out.println("\nTERMINATING PROGRAM");

        Scanner.close();
    }
}