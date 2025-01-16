package expensetracker;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Item implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String itemName;
    private String itemDescription;
    private double itemPrice;
    private int itemQuantity;
    private LocalDate dateBought;
    private static final int MAX_DAYS_VALID = 15;
    private static final LocalDate DEFAULT_DATE = LocalDate.of(2000, 1, 1);
    private Stores storeName;

    public Item() {
        this.itemName = "Unknown";
        this.itemDescription = "No description";
        this.itemPrice = 0.0;
        this.itemQuantity = 0;
        this.storeName = Stores.INVALID;
        this.dateBought = DEFAULT_DATE;
    }

    public Item(String itemName, String itemDescription, double itemPrice, int itemQuantity, Stores storeName, LocalDate dateBought) {
        if (ChronoUnit.DAYS.between(dateBought, LocalDate.now()) >= MAX_DAYS_VALID) {
            this.dateBought = DEFAULT_DATE;
            this.itemName = "Overdue";
            this.itemDescription = "Overdue item";
            this.itemPrice = 0.0;
            this.itemQuantity = 0;
            this.storeName = Stores.INVALID;
        } else {
            this.dateBought = dateBought;
            this.itemName = itemName;
            this.itemDescription = itemDescription;
            this.itemPrice = itemPrice * itemQuantity;
            this.itemQuantity = itemQuantity;
            this.storeName = storeName;
        }
    }

    @Override
    public String toString() {
        return String.format("Store: %s, Item: %s, Description: %s, Quantity: %d, Total Price: %.2f Euro, Date: %s",
                storeName, itemName, itemDescription, itemQuantity, itemPrice, dateBought);
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public Stores getStoreName() {
        return storeName;
    }

    public void setStoreName(Stores storeName) {
        this.storeName = storeName;
    }

    public LocalDate getDateBought() {
        return dateBought;
    }

    public void setDateBought(LocalDate dateBought) {
        this.dateBought = dateBought;
    }
}
