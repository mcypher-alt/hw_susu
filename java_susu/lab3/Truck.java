public class Truck extends Vehicle {
    private double maxCargo;
    private double currentCargo;
    private double cargoFee; // л/100км за одну тонну

    public Truck(int id, double position, double tankCapacity, double fuel, double baseConsumption,
                 Route route, double maxCargo, double currentCargo, double cargoFee) {
        super(id, position, tankCapacity, fuel, baseConsumption, route);
        this.maxCargo = maxCargo;
        this.cargoFee = cargoFee;
        this.currentCargo = Math.min(currentCargo, maxCargo);
    }

    public double getMaxCargo() { return maxCargo; }
    public double getCurrentCargo() { return currentCargo; }

    public double loadCargo(double mass) {
        if (mass <= 0) return 0.0;
        double freeSpace = maxCargo - currentCargo;
        double toLoad = Math.min(mass, freeSpace);
        currentCargo += toLoad;
        return toLoad;
    }

    public double unloadCargo(double mass) {
        if (mass <= 0) return 0.0;
        double toUnload = Math.min(mass, currentCargo);
        currentCargo -= toUnload;
        return toUnload;
    }

    @Override
    public double getFuelConsumption() {
        return super.getFuelConsumption() + (currentCargo * cargoFee);
    }

    @Override
    public String toString() {
        return String.format("%s | Грузовик [Груз: %.2f/%.2f т, надбавка: %.2f л/т]",
                super.toString(), currentCargo, maxCargo, cargoFee);
    }
}
