package menu;

import api.HotelResource;
import model.Customer;
import model.IRoom;
import model.Reservation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class MainMenu {

    protected static Scanner scanner = null;

    private static AdminMenu adminMenu;
    private static final HotelResource hotelInstance = HotelResource.getInstance();

    public MainMenu(Scanner scanner) {
        this.scanner = scanner;
        adminMenu =  new AdminMenu(scanner);
    }

    private static void printMainMenu() {
        System.out.println("1. Find and reserve a room");
        System.out.println("2. See my reservations");
        System.out.println("3. Create an account");
        System.out.println("4. Admin");
        System.out.println("5. Exit");
        System.out.println("Please select a number for the menu selection");
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
                      adminMenu.displayAdminMenu();
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

    static void checkAlternativeRooms(String email, Date checkIn, Date checkOut) {
        int range = (int) ChronoUnit.DAYS.between(
                checkIn.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                checkOut.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        System.out.println("Searching for dates. If dates are strict, enter 'end' to abort.");

        for (int i = -range; i <= range; i++) {
            Calendar calendarIn = Calendar.getInstance();
            calendarIn.setTime(checkIn);
            calendarIn.add(Calendar.DAY_OF_MONTH, i);

            Calendar calendarOut = Calendar.getInstance();
            calendarOut.setTime(checkOut);
            calendarOut.add(Calendar.DAY_OF_MONTH, i);

            Date dateIn = calendarIn.getTime();
            Date dateOut = calendarOut.getTime();

            Set<IRoom> hotelInstances = hotelInstance.findARoom(dateIn, dateOut);

            if (!hotelInstances.isEmpty()) {
                System.out.println(hotelInstances + " are available from " + dateIn + " to " + dateOut);
                System.out.println("Choose a room. Press Q to move on to new dates.");
                System.out.println("Type 'end' to stop searching.");

                boolean q = true;
                while (q) {
                    String roomNumber = scanner.nextLine().trim().toUpperCase();

                    if (roomNumber.equals("Q")) {
                        break;
                    }
                    if (roomNumber.equals("END")) {
                        return;
                    }

                    for (IRoom room : hotelInstances) {
                        if (room.getRoomNumber().equals(roomNumber)) {
                            hotelInstance.bookARoom(email, room, dateIn, dateOut);
                            System.out.println("Successfully booked room " + room.getRoomNumber() + " from " + dateIn + " to " + dateOut);
                            return;
                        }
                    }

                    System.out.println("Invalid choice. Try again.");
                }
            }
        }

        System.out.println("We're sorry. We couldn't find anything.");
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
        if (!hotelInstances.isEmpty()) {
            System.out.println(hotelInstances + "are available");
            System.out.println("Chose a room. Press Q to abort.");
            Boolean q = false;
            while (!q) {
                String roomNumber = scanner.nextLine().trim();
                if(roomNumber.trim().toUpperCase().equals("Q")) {
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
        } else {
            checkAlternativeRooms(emailInput, checkInDate, checkOutDate);
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

}
