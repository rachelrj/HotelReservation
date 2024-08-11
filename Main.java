import api.AdminResource;
import api.HotelResource;
import model.Customer;
import model.FreeRoom;
import model.IRoom;
import model.Reservation;
import model.Room;
import model.RoomType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.Set;

public class Main {

    private static Scanner scanner = new Scanner(System.in);

    private static final AdminResource adminResource = AdminResource.getInstance();
    private static final HotelResource hotelInstance = HotelResource.getInstance();

    public static void main(String[] args) {
        System.out.println("Welcome to the Hotel Reservation Application");
        System.out.println("---------------------------------------------");
        switchOnMainMenu();
    }

    public static void switchOnMainMenu() {
        boolean exit = false;
        while (!exit) {
            printMainMenu();
            String userInput = scanner.nextLine().trim();
            switch (userInput) {
                case "1":
                    bookARoom();
                    break;
                case "2":
                    getReservations();
                    break;
                case "3":
                    createAnAccount();
                    break;
                case "4":
                    adminMenu();
                    break;
                case "5":
                    exit = true;
                    scanner.close();
                    break;
                default:
                    System.out.println("Invalid selection, please try again.");
            }
        }
    }

    private static Date checkDate(String whichDate) {
        System.out.println("Enter a " + whichDate + " date in mm/dd/yyyy");
        while (true) {
            String dateString = scanner.nextLine().trim();
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                Date date = formatter.parse(dateString);

                if (date.before(new Date())) {
                    System.out.println("The date must be in the future. Please try again.");
                } else {
                    return date;
                }
            } catch (ParseException e) {
                System.out.println("Invalid date format. Please try again.");
            }
        }
    }

    private static void createAnAccount() {
        System.out.println("What is your first name?");
        String firstInput = scanner.nextLine().trim();
        System.out.println("What is your last name?");
        String lastInput = scanner.nextLine().trim();
        Boolean validEmailInput = false;
        while (!validEmailInput)
        try {
            System.out.println("What is your email address?");
            String emailInput = scanner.nextLine().trim();
            if (hotelInstance.getCustomer(emailInput) != null){
                System.out.println("You are about to recreate your account. Type Q to quit");
                String quitOption = scanner.nextLine().trim();
                if(quitOption.toUpperCase().equals("Q")) {
                    return;
                }
            }
            hotelInstance.createACustomer(emailInput, firstInput, lastInput);
            validEmailInput = true;
        } catch(IllegalArgumentException e) {
            System.out.println("Invalid email. Try again.");
        }
    }

    private static void getReservations() {
        System.out.println("What is your email address?");
        String emailInput = scanner.nextLine().trim();
        Set<Reservation> reservations = hotelInstance.getCustomerReservations(emailInput);
        if (reservations == null) {
            System.out.println("No reservations");
        } else {
            System.out.println(reservations);
        }
    }

    private static void bookARoom() {
        System.out.println("What is your email address on file?");
        String emailInput = scanner.nextLine().trim();
        Customer bookingCustomer = hotelInstance.getCustomer(emailInput);
        if (bookingCustomer == null) {
            System.out.println("You need to create an account first");
            return;
        }
        Date checkInDate = checkDate("check in");
        Date checkOutDate = checkDate("check out");
        while (checkInDate.after(checkOutDate)) {
            checkInDate = checkDate("check in");
            checkOutDate = checkDate("check out");
        }
        Set<IRoom> hotelInstances = hotelInstance.findARoom(checkInDate, checkOutDate);
        System.out.println(hotelInstances + "are available");
        System.out.println("Chose a room. Press Q to abort.");
        Boolean q = false;
        while (!q) {
            String roomNumber = scanner.nextLine().trim();
            if(roomNumber.trim().toUpperCase() == "Q") {
                return;
            }
            for (IRoom room : hotelInstances) {
                if(room.getRoomNumber().equals(roomNumber));
                hotelInstance.bookARoom(emailInput, room, checkInDate, checkOutDate);
                System.out.println("Successfully booked");
                return;
            }
            System.out.println("Invalid choice. Try again");
        }
    }

    private static void printMainMenu() {
        System.out.println("1. Find and reserve a room");
        System.out.println("2. See my reservations");
        System.out.println("3. Create an account");
        System.out.println("4. Admin");
        System.out.println("5. Exit");
        System.out.println("Please select a number for the menu selection");
    }

    private static void printAdminMenu() {
        System.out.println("1. See all customers");
        System.out.println("2. See all rooms");
        System.out.println("3. See all reservations");
        System.out.println("4. Add a room(s)");
        System.out.println("5. Back to main menu");
        System.out.println("Please select a number for the menu selection");
    }



    private static void adminMenu() {
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

    private static String getValidRoomNumber() {
        while (true) {
            System.out.println("Please enter room number:");
            String roomNumber = scanner.nextLine().trim();
            try {
                Integer.parseInt(roomNumber); // Validate if the input is a valid integer
                return roomNumber;
            } catch (NumberFormatException e) {
                System.out.println("Invalid room number. Please try again.");
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
}