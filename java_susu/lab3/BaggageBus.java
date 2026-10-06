import java.util.ArrayList;

public class BaggageBus extends Bus {
    private double maxBaggage; // тонн
    private double baggageFee;  // л/100км за 1 тонну багажа

    private static class BaggageGroup {
        int passengers;
        double weightPerPerson;

        BaggageGroup(int passengers, double weightPerPerson) {
            this.passengers = passengers;
            this.weightPerPerson = weightPerPerson;
        }

        double getTotalWeight() {
            return passengers * weightPerPerson;
        }
    }

    private ArrayList<BaggageGroup> groups = new ArrayList<>();

    public BaggageBus(int id, double position, double tankCapacity, double fuel, double baseConsumption,
                      Route route, int maxPassengers, int currentPassengers, double passengerFee,
                      double maxBaggage, double baggageFee) {
        super(id, position, tankCapacity, fuel, baseConsumption, route, maxPassengers, 0, passengerFee);
        this.maxBaggage = maxBaggage;
        this.baggageFee = baggageFee;

        // Если заданы начальные пассажиры, сажаем их без багажа
        if (currentPassengers > 0) {
            boardWithBaggage(currentPassengers, 0.0);
        }
    }

    public double getMaxBaggage() { return maxBaggage; }
    public double getBaggageFee() { return baggageFee; }

    public double getCurrentBaggageWeight() {
        double total = 0.0;
        for (BaggageGroup g : groups) {
            total += g.getTotalWeight();
        }
        return total;
    }

    public int boardWithBaggage(int passengerCount, double weightPerPersonTons) {
        if (passengerCount <= 0 || weightPerPersonTons < 0) return 0;

        int freeSeats = getMaxPassengers() - getCurrentPassengers();
        double freeBaggage = maxBaggage - getCurrentBaggageWeight();

        int bySeats = freeSeats;
        int byBaggage = (weightPerPersonTons > 0) ? (int) (freeBaggage / weightPerPersonTons) : passengerCount;

        int actualToBoard = Math.min(passengerCount, Math.min(bySeats, byBaggage));
        if (actualToBoard <= 0) return 0;

        super.boardPassengers(actualToBoard);
        groups.add(new BaggageGroup(actualToBoard, weightPerPersonTons));
        return actualToBoard;
    }

    public int unboardFIFO(int countToUnboard) {
        if (countToUnboard <= 0) return 0;
        int remainingToUnboard = countToUnboard;
        int totalUnboarded = 0;

        while (remainingToUnboard > 0 && !groups.isEmpty()) {
            BaggageGroup firstGroup = groups.get(0);
            if (firstGroup.passengers <= remainingToUnboard) {
                totalUnboarded += firstGroup.passengers;
                remainingToUnboard -= firstGroup.passengers;
                groups.remove(0); // Высаживается вся группа вместе с багажом
            } else {
                firstGroup.passengers -= remainingToUnboard;
                totalUnboarded += remainingToUnboard;
                remainingToUnboard = 0;
            }
        }

        super.unboardPassengers(totalUnboarded);
        return totalUnboarded;
    }

    @Override
    public double getFuelConsumption() {
        return super.getFuelConsumption() + (getCurrentBaggageWeight() * baggageFee);
    }

    @Override
    public String toString() {
        return String.format("%s | [Багаж: %.2f/%.2f т, надбавка: %.2f л/т, групп: %d]",
                super.toString(), getCurrentBaggageWeight(), maxBaggage, baggageFee, groups.size());
    }
}
