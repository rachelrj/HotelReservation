package service;

import model.Customer;
import model.IRoom;
import model.Reservation;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Map;

public class ReservationService {

    private static ReservationService reservationServiceInstance;

    private final Set<Reservation> reservations = new HashSet<>();
    private final Map<String, IRoom> rooms = new HashMap<>();
    public static ReservationService getInstance() {
        if (reservationServiceInstance == null) {
            reservationServiceInstance = new ReservationService();
        }
        return reservationServiceInstance;
    }

    public void addRoom(IRoom room) {
        rooms.put(room.getRoomNumber(), room);
    }

    public IRoom getARoom(String roomId) {
        return rooms.get(roomId);
    }

    public Set<IRoom> getAllRooms() {
        return new HashSet<>(rooms.values());
    }

    public Set<IRoom> findARoom(Date checkInDate, Date checkOutDate) {
        Set<IRoom> availableRooms = getAllRooms();
        for (Reservation reservation: reservations) {
            if (reservation.getCheckInDate().before(checkOutDate) && reservation.getCheckOutDate().after(checkInDate)) {
                availableRooms.remove(reservation.getRoom());
            }
        }
        return availableRooms;
    }

    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        Set<IRoom> availableRooms = findARoom(checkInDate, checkOutDate);
        if (availableRooms.contains(room)) {
            Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);
            reservations.add(reservation);
            return reservation;
        }
        else {
            throw new NoSuchElementException("No rooms available at this time");
        }

    }

    public Set<Reservation> getCustomerReservations(Customer customer) {
        Set<Reservation> customerReservations = new HashSet<>();
        for (Reservation reservation : reservations) {
            if(reservation.getCustomer().equals(customer)) {
                customerReservations.add(reservation);
            }
        }
        return customerReservations;
    }

    public void printAllReservations() {
        for (Reservation reservation: reservations) {
            System.out.println(reservation);
        }
    }
}
