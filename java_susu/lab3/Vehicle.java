public abstract class Vehicle {
    private int id;
    private double position;
    private double tankCapacity;
    private double fuel;
    private double baseConsumption; // л на 100 км
    private double mileage;
    private Route route;

    public Vehicle(int id, double position, double tankCapacity, double fuel, double baseConsumption, Route route) {
        this.id = id;
        this.position = position;
        this.tankCapacity = tankCapacity;
        this.fuel = Math.min(fuel, tankCapacity);
        this.baseConsumption = baseConsumption;
        this.mileage = 0.0;
        this.route = route;
    }

    public int getId() { return id; }
    public double getPosition() { return position; }
    public double getTankCapacity() { return tankCapacity; }
    public double getFuel() { return fuel; }
    public double getMileage() { return mileage; }
    public Route getRoute() { return route; }

    public void forceSetPosition(double newPos) {
        this.position = newPos;
    }

    public double getFuelConsumption() {
        return baseConsumption;
    }

    public double getMaxDistance() {
        double consumption = getFuelConsumption();
        if (consumption <= 0) return Double.MAX_VALUE;
        return (fuel / consumption) * 100.0;
    }

    public boolean move(double targetPosition, boolean forceMaxIfLowFuel) {
        if (targetPosition < 0 || targetPosition > route.getLength()) {
            return false;
        }

        double requestedDistance = Math.abs(targetPosition - position);
        if (requestedDistance == 0) return true;

        double consumption = getFuelConsumption();
        double fuelNeeded = (requestedDistance * consumption) / 100.0;
        double direction = (targetPosition > position) ? 1.0 : -1.0;

        if (fuel >= fuelNeeded) {
            position = targetPosition;
            fuel -= fuelNeeded;
            mileage += requestedDistance;
            return true;
        } else {
            if (!forceMaxIfLowFuel) {
                return false;
            }
            double possibleDistance = (fuel / consumption) * 100.0;
            position += direction * possibleDistance;
            mileage += possibleDistance;
            fuel = 0.0;
            return true;
        }
    }

    public boolean refuel(double amount) {
        if (!route.hasGasStation(position)) return false;
        if (amount <= 0) return false;

        double freeSpace = tankCapacity - fuel;
        double toAdd = Math.min(amount, freeSpace);
        fuel += toAdd;
        return true;
    }

    @Override
    public String toString() {
        return String.format("№%d | Поз: %.1f км | Бак: %.1f/%.1f л | Расход: %.2f л/100км | Пробег: %.1f км",
                id, position, fuel, tankCapacity, getFuelConsumption(), mileage);
    }
}
