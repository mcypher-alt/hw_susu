public class Bus extends Vehicle {
    private int maxPassengers;
    private int currentPassengers;
    private double passengerFee;

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

    public boolean callAssistance() {
        if (getFuel() > 0.001) {
            System.out.println("Отказ вызова помощи: бак автобуса не пуст.");
            return false;
        }
        if (getRoute().hasGasStation(getPosition())) {
            System.out.println("Отказ вызова помощи: автобус уже находится на действующей заправке.");
            return false;
        }

        double cons = getFuelConsumption();
        double maxReachableDistance = (getTankCapacity() / cons) * 100.0;

        Double targetStation = null;
        double minDistance = Double.MAX_VALUE;

        for (Double station : getRoute().getGasStations()) {
            double dist = Math.abs(station - getPosition());
            if (dist <= maxReachableDistance && dist > 0.001) {
                if (dist < minDistance - 0.0001) {
                    minDistance = dist;
                    targetStation = station;
                } else if (Math.abs(dist - minDistance) < 0.0001) {
                    if (targetStation != null && station < targetStation) {
                        targetStation = station;
                    }
                }
            }
        }

        if (targetStation == null) {
            System.out.println("Отказ: служба помощи не нашла заправок в пределах максимального запаса хода автобуса.");
            return false;
        }

        double fuelNeeded = (minDistance * cons) / 100.0;
        System.out.printf("Служба помощи выбрала заправку на отметке %.1f км (расстояние %.1f км).\n", targetStation, minDistance);
        System.out.printf("Доставлено ровно %.2f л топлива для проезда к заправке.\n", fuelNeeded);

        setDirectFuel(fuelNeeded);
        move(targetStation, false);
        setDirectFuel(0.0);

        System.out.printf("Автобус №%d прибыл на заправку (%.1f км). Топливо: 0.0 л. Пробег: %.1f км.\n",
                getId(), getPosition(), getMileage());
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s | Автобус [Пассажиры: %d/%d, надбавка: %.2f л/чел]",
                super.toString(), currentPassengers, maxPassengers, passengerFee);
    }
}
