import java.util.ArrayList;

public class Route {
    private double length;
    private ArrayList<Double> gasStations;

    public Route(double length) {
        this.length = length;
        this.gasStations = new ArrayList<>();
    }

    public double getLength() {
        return length;
    }

    public ArrayList<Double> getGasStations() {
        return new ArrayList<>(gasStations);
    }

    public boolean setLength(double newLength, ArrayList<Vehicle> vehicles) {
        if (newLength <= 0) return false;
        this.length = newLength;

        // Удаление заправок за новой границей
        gasStations.removeIf(station -> station > newLength);

        // Перемещение транспорта за границей в конец маршрута
        for (Vehicle v : vehicles) {
            if (v.getPosition() > newLength) {
                v.forceSetPosition(newLength);
            }
        }
        return true;
    }

    public boolean addGasStation(double position) {
        if (position < 0 || position > length) return false;
        for (double st : gasStations) {
            if (Math.abs(st - position) < 0.001) return false;
        }
        gasStations.add(position);
        gasStations.sort(Double::compareTo);
        return true;
    }

    public boolean removeGasStation(double position) {
        return gasStations.removeIf(st -> Math.abs(st - position) < 0.001);
    }

    public boolean hasGasStation(double position) {
        for (double st : gasStations) {
            if (Math.abs(st - position) < 0.001) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("Маршрут: длина %.1f км, заправки на км: %s", length, gasStations.toString());
    }
}
