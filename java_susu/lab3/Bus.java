public class Bus extends Vehicle {
    private int maxPassengers;
    private int currentPassengers;
    private double passengerFee; // л/100км за одного пассажира

    public Bus(int id, double position, double tankCapacity, double fuel, double baseConsumption,
               Route route, int maxPassengers, int currentPassengers, double passengerFee) {
        super(id, position, tankCapacity, fuel, baseConsumption, route);
        this.maxPassengers = maxPassengers;
        this.passengerFee = passengerFee;
        this.currentPassengers = Math.min(currentPassengers, maxPassengers);
    }

    public int getMaxPassengers() { return maxPassengers; }
    public int getCurrentPassengers() { return currentPassengers; }
    public double getPassengerFee() { return passengerFee; }

    public int boardPassengers(int count) {
        if (count <= 0) return 0;
        int freeSeats = maxPassengers - currentPassengers;
        int toBoard = Math.min(count, freeSeats);
        currentPassengers += toBoard;
        return toBoard;
    }

    public int unboardPassengers(int count) {
        if (count <= 0) return 0;
        int toUnboard = Math.min(count, currentPassengers);
        currentPassengers -= toUnboard;
        return toUnboard;
    }

    @Override
    public double getFuelConsumption() {
        return super.getFuelConsumption() + (currentPassengers * passengerFee);
    }

    @Override
    public String toString() {
        return String.format("%s | Автобус [Пасс: %d/%d, надбавка: %.2f л/чел]",
                super.toString(), currentPassengers, maxPassengers, passengerFee);
    }
}
