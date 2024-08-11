package model;

import java.util.Objects;

public class Room implements IRoom{

    private final String roomNumber;
    private final Double roomPrice;
    private final RoomType roomType;
    private final Boolean isFree;
    public Room(String roomNumber, Double roomPrice, RoomType roomType, Boolean isFree) {
        this.roomNumber = roomNumber;
        this.roomPrice = roomPrice;
        this.roomType = roomType;
        this.isFree = isFree;
    }
    @Override
    public String getRoomNumber() {
        return this.roomNumber;
    }

    @Override
    public Double getRoomPrice() {
        return this.roomPrice;
    }

    @Override
    public RoomType getRoomType() {
        return this.roomType;
    }

    @Override
    public Boolean isFree() {
        return this.isFree;
    }

    @Override
    public String toString() {
        return "\n{" +
                "roomNumber= " + roomNumber + "\n" +
                "roomPrice= " + roomPrice + "\n" +
                "roomType= " + roomType + "\n" +
                "isFree= " + isFree + "\n" +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return Objects.equals(roomNumber, room.roomNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomNumber);
    }
}
