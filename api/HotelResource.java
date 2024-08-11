package api;

import model.Customer;
import model.IRoom;
import model.Reservation;
import service.CustomerService;
import service.ReservationService;

import java.util.Date;
import java.util.Set;

public class HotelResource {
    private static HotelResource hotelResourceInstance;

    public static HotelResource getInstance() {
        if (hotelResourceInstance == null) {
            hotelResourceInstance = new HotelResource();
        }
        return hotelResourceInstance;
    }

    private static final ReservationService reservationService = ReservationService.getInstance();
    private static final CustomerService customerService = CustomerService.getInstance();

    public Customer getCustomer(String email) {
        return customerService.getCustomerByEmail(email);
    }

    public void createACustomer(String email, String firstName, String lastName) {
        customerService.addCustomer(email, firstName, lastName);
    }

    public IRoom getRoom(String roomNumber) {
        return reservationService.getARoom(roomNumber);
    }

    public Reservation bookARoom(String customerEmail, IRoom room, Date checkInDate, Date checkOutDate) {
        Customer customer = customerService.getCustomerByEmail(customerEmail);
        return reservationService.reserveARoom(customer, room, checkInDate, checkOutDate);
    }

    public Set<Reservation> getCustomerReservations(String customerEmail){
        Customer customer = customerService.getCustomerByEmail(customerEmail);
        return reservationService.getCustomerReservations(customer);
    }

    public Set<IRoom> findARoom(Date checkIn, Date checkOut) {
        return reservationService.findARoom(checkIn, checkOut);
    }

}
