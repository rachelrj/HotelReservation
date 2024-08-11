package api;

import model.Customer;
import model.IRoom;
import service.CustomerService;
import service.ReservationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AdminResource {

    private static AdminResource adminResourceInstance;

    public static AdminResource getInstance() {
        if (adminResourceInstance == null) {
            adminResourceInstance = new AdminResource();
        }
        return adminResourceInstance;
    }

    private static final ReservationService reservationServiceInstance = ReservationService.getInstance();
    private static final CustomerService customerServiceInstance = CustomerService.getInstance();

    public Customer getCustomer(String email){
        return customerServiceInstance.getCustomerByEmail(email);
    }

    public void addRoom(ArrayList<IRoom> list) {
        for (IRoom room : list) {
            reservationServiceInstance.addRoom(room);
        }
    }

    public Set<IRoom> getAllRooms() {
        return reservationServiceInstance.getAllRooms();
    }

    public Set<Customer> getAllCustomers() {
        return customerServiceInstance.getAllCustomers();
    }

    public void displayAllReservations() {
        reservationServiceInstance.printAllReservations();
    }
}
