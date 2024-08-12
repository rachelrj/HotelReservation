package menu;

import api.AdminResource;
import model.FreeRoom;
import model.IRoom;
import model.Room;
import model.RoomType;

import java.util.ArrayList;
import java.util.Scanner;

public class AdminMenu {

    private static Scanner scanner;

    public AdminMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    private static final AdminResource adminResource = AdminResource.getInstance();
    private static void printAdminMenu() {
        System.out.println("1. See all customers");
        System.out.println("2. See all rooms");
        System.out.println("3. See all reservations");
        System.out.println("4. Add a room(s)");
        System.out.println("5. Back to main menu");
        System.out.println("Please select a number for the menu selection");
    }

    protected static void displayAdminMenu() {
        boolean backToMain = false;
        while (!backToMain) {
            printAdminMenu();
            String userInput = scanner.nextLine().trim();

            switch (userInput) {
                case "1":
                    System.out.println(adminResource.getAllCustomers());
                    break;
                case "2":
                    System.out.println(adminResource.getAllRooms());
                    break;
                case "3":
                    adminResource.displayAllReservations();
                    break;
                case "4":
                    adminResource.addRoom(createRooms());
                    break;
                case "5":
                    backToMain = true;
                    break;
                default:
                    System.out.println("Invalid selection, please try again.");
                    break;
            }
        }
    }

    private static Double getValidRoomPrice() {
        while (true) {
            System.out.println("Please enter room price:");
            String priceInput = scanner.nextLine().trim();
            try {
                return Double.parseDouble(priceInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid room price. Please try again.");
            }
        }
    }

    private static RoomType getValidRoomType() {
        while (true) {
            System.out.println("Please enter room type. Please enter 'S' for Single or 'D' for Double:");
            String roomTypeInput = scanner.nextLine().trim().toUpperCase();

            if (roomTypeInput.equals("S")) {
                return RoomType.SINGLE;
            } else if (roomTypeInput.equals("D")) {
                return RoomType.DOUBLE;
            } else {
                System.out.println("Invalid room type. Please enter 'S' for Single or 'D' for Double.");
            }
        }
    }

    private static String getValidRoomNumber() {
        while (true) {
            System.out.println("Please enter room number:");
            String roomNumber = scanner.nextLine().trim();
            try {
                Integer.parseInt(roomNumber);
                return roomNumber;
            } catch (NumberFormatException e) {
                System.out.println("Invalid room number. Please try again.");
            }
        }
    }
    public static IRoom createARoom() {
        String roomNumber = getValidRoomNumber();
        Double roomPrice = getValidRoomPrice();
        RoomType roomType = getValidRoomType();

        if (roomPrice == 0) {
            return new FreeRoom(roomNumber, roomPrice, roomType);
        } else {
            return new Room(roomNumber, roomPrice, roomType, false);
        }
    }

    public static ArrayList<IRoom> createRooms() {
        ArrayList<IRoom> list = new ArrayList<>();
        list.add(createARoom());
        boolean createMore = true;

        System.out.println("Would you like to add another room? (Y/N)");
        while (createMore) {
            String input = scanner.nextLine().trim().toUpperCase();

            if (input.equals("Y")) {
                list.add(createARoom());
                System.out.println("Would you like to add another room? (Y/N)");
            } else if (input.equals("N")) {
                createMore = false;
            } else {
                System.out.println("Invalid entry. Please enter 'Y' for yes or 'N' for no.");
            }
        }
        return list;
    }
}
