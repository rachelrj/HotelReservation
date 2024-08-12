import menu.MainMenu;
import java.util.Scanner;

public class HotelApplication {

    private static Scanner scanner = new Scanner(System.in);

    private static final MainMenu mainMenu = new MainMenu(scanner);

    public static void main(String[] args) {
        System.out.println("Welcome to the Hotel Reservation Application");
        System.out.println("---------------------------------------------");
        mainMenu.switchOnMainMenu();
    }
}