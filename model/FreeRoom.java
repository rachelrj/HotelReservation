package model;

public class FreeRoom extends Room {

    public FreeRoom(String roomNumber, Double roomPrice, RoomType roomType) {
        super(roomNumber, roomPrice, roomType, true);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
