import java.util.Scanner;

public class abcdefi {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String[] menu = { "Burger", "Fries", "Juice", "Rice Meal", };
        int[] price = {50, 30, 25, 80};

        String[] names = new String[5];
        String[] orders = new String[5];
        int[] order_prices = new int[5];

        int total_price = 0;
        int order_reply = 0;

        String Remark = null;

        for (int i = 0; i < 5; i++) {
            System.out.print("Whats ur name? : ");
            names[i] = sc.nextLine();

            System.out.println("\n=====MENU=====");
            for (int r = 0; r < menu.length; r++) {
                System.out.println("[" + r + "]" + "-" + menu[r] + " (" + "PHP" + price[r] + ")");
            }
            System.out.print("What's your order? : ");
            order_reply = Integer.parseInt(sc.nextLine());

            switch (order_reply) {
                case 0:
                    orders[i] = "Burger";
                    order_prices[i] = 50;
                    break;

                case 1:
                    orders[i] = "Fries";
                    order_prices[i] = 30;
                    break;

                case 2:
                    orders[i] = "Juice";
                    order_prices[i] = 25;
                    break;

                case 3:
                    orders[i] = "Rice Meal";
                    order_prices[i] = 80;
                    break;
                
            }

            System.out.println("Order taken. Thank you");
            System.out.println("");
        }

        if (total_price >= 200) Remark = "High Spender"; else {Remark = "Regular Spender";};

        System.out.println("====== STUDENT ORDERS ======");
        for (int i = 0; i < orders.length; i++) {
            System.out.println("----------------------");
            System.out.println("Student: " + names[i]);
            System.out.println("Order: " + orders[i]);
            System.out.println("Price: " + order_prices[i]);
        }

        for (int i = 0; i < order_prices.length; i++) {
            total_price += order_prices[i];
        }
        
        System.out.println("\nTOTAL SALES: " + total_price);
        System.out.println("REMARK: " + Remark);
    }
}
