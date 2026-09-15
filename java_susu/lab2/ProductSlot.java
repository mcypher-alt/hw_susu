public class ProductSlot {
    private int id;
    private String name;
    private int price;
    private int quantity;

    public ProductSlot(int id, String name, int price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getPrice() { return price; }
    public int getQuantity() { return quantity; }

    public void reduceQuantity() {
        if (quantity > 0) {
            quantity--;
        }
    }

    @Override
    public String toString() {
        return String.format("%d\t| %s\t| %d\t| %d", id, name, price, quantity);
    }
}
