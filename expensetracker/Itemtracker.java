package expensetracker;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class Itemtracker {
    private static final String FILENAME = "Tracker.dat";
    private static final String FILENAME_CSV = "Tracker.csv"; // CSV filename

    public static void main(String[] args) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Scanner sc = new Scanner(System.in);
        ArrayList<Item> items = loadItems();

        System.out.println("********Welcome to Expense Tracker********");

        int choice = 0;
        while (choice != 6) {
            System.out.println("***Menu***\n1. Add Item\n2. Pay Item\n3. Show Items\n4. Clear Items\n5. Edit Item\n6. Exit");
            System.out.print("Enter choice: ");
            if (!sc.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                sc.next();
                continue;
            }
            choice = sc.nextInt();
            sc.nextLine();

            try {
                switch (choice) {
                    case 1: // Add item
                        System.out.print("Name: ");
                        String naam = sc.next();
                        sc.nextLine();

                        System.out.print("Description: ");
                        String description = sc.next();
                        sc.nextLine();

                        System.out.print("Price: ");
                        if (!sc.hasNextDouble()) {
                            System.out.println("Invalid input. Please enter a number.");
                            sc.next();
                            break;
                        }
                        double price = sc.nextDouble();
                        sc.nextLine();

                        System.out.print("Quantity: ");
                        if (!sc.hasNextInt()) {
                            System.out.println("Invalid input. Please enter a number.");
                            sc.next();
                            break;
                        }
                        int quantity = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Store (e.g., JUMBO, ALDI): ");
                        Stores store = getStoreFromInput(sc.nextLine());
                        if (store == null) {
                            store = Stores.INVALID;
                            break;
                        }

                        Item item = new Item(naam, description, price, quantity, store, LocalDate.now());
                        System.out.print(item.toString() + "\nIs this item correct? (Y/N): ");
                        String answer = sc.nextLine().toUpperCase();
                        if (answer.equals("Y")) {
                            items.add(item);
                            saveItems(items); // Save to .dat file
                            saveItemsToCSV(items); // Save to CSV file
                            System.out.println("Item added successfully!");
                        } else {
                            System.out.println("Item not added!");
                        }
                        break;

                    case 2: // Pay item
                        if (items.isEmpty()) {
                            System.out.println("Nothing to pay!");
                            break;
                        }
                        System.out.println("Please press 1 to pay an item.");
                        choice = sc.nextInt();
                        System.out.print("Items to pay:\n ");
                        for (int i = 0; i < items.size(); i++) {
                            System.out.println(i + ": " + items.get(i).toString());
                        }
                        System.out.print("Enter an item ID to pay: ");
                        if (!sc.hasNextInt()) {
                            System.out.println("Invalid input. Please enter a number.");
                            sc.next();
                            break;
                        }
                        int itemID = sc.nextInt();
                        sc.nextLine();
                        if (itemID < 0 || itemID >= items.size()) {
                            System.out.println("Invalid input. Please enter a valid item ID.");
                        } else {
                            items.remove(itemID);
                            saveItems(items);
                            saveItemsToCSV(items); // Save to CSV after removing item
                            System.out.println("Item paid successfully!");
                        }
                        break;

                    case 3: // Show pending items
                        if (items.isEmpty()) {
                            System.out.println("Nothing to display!");
                        } else {
                            System.out.println("All items currently saved: ");
                            for (Item i : items) {
                                System.out.println(i.toString());
                            }
                        }
                        break;

                    case 4: // Clear storage
                        System.out.println("Are you sure you want to clear all items? (Y/N)");
                        char pick = sc.next().toUpperCase().charAt(0);
                        if (pick == 'Y') {
                            items.clear();
                            saveItems(items);
                            saveItemsToCSV(items); // Save to CSV after clearing items
                            System.out.println("Items cleared successfully!");
                        } else {
                            System.out.println("Items won't be cleared.");
                        }
                        break;

                    case 5: // Edit Item
                        if (items.isEmpty()) {
                            System.out.println("No items to edit.");
                            break;
                        }
                        System.out.println("Items:");
                        for (int i = 0; i < items.size(); i++) {
                            System.out.println(i + ": " + items.get(i));
                        }
                        System.out.print("Enter the item number to edit: ");
                        if (!sc.hasNextInt()) {
                            System.out.println("Invalid input. Please enter a valid number.");
                            sc.next();
                            break;
                        }
                        itemID = sc.nextInt();
                        sc.nextLine();
                        if (itemID < 0 || itemID >= items.size()) {
                            System.out.println("Invalid item number.");
                        } else {
                            Item itemToEdit = items.get(itemID);
                            System.out.println("Editing: " + itemToEdit);

                            System.out.print("New Name (Leave blank to keep current): ");
                            String newName = sc.nextLine();
                            if (!newName.isBlank()) itemToEdit.setItemName(newName);

                            System.out.print("New Description (Leave blank to keep current): ");
                            String newDescription = sc.nextLine();
                            if (!newDescription.isBlank()) itemToEdit.setItemDescription(newDescription);

                            System.out.print("New Price (Leave blank to keep current): ");
                            String newPrice = sc.nextLine();
                            if (!newPrice.isBlank()) itemToEdit.setItemPrice(Double.parseDouble(newPrice));

                            System.out.print("New Quantity (Leave blank to keep current): ");
                            String newQuantity = sc.nextLine();
                            if (!newQuantity.isBlank()) itemToEdit.setItemQuantity(Integer.parseInt(newQuantity));

                            saveItems(items); // Save updated list to file
                            saveItemsToCSV(items); // Save updated list to CSV file
                            System.out.println("Item updated successfully!");
                        }
                        break;

                    case 6: // Exit
                        System.out.print("Are you sure you want to exit? (Y/N): ");
                        char exitChoice = sc.nextLine().toUpperCase().charAt(0);
                        if (exitChoice == 'Y') {
                            System.out.println("Thank you for using Expense Tracker!");
                            sc.close();
                            System.exit(0);
                        }
                        break;

                    default:
                        System.out.println("Invalid input. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred." + e.getMessage());
                sc.nextLine();
            }
        }
    }

    // Method to get store from user input
    public static Stores getStoreFromInput(String input) {
        try {
            return Stores.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Stores.INVALID;
        }
    }

    // Save items to .dat file (serialization)
    public static void saveItems(ArrayList<Item> items) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILENAME))) {
            oos.writeObject(items);
        } catch (Exception e) {
            System.out.println("Failed to save items: " + e.getMessage());
        }
    }

    // Load items from .dat file (deserialization)
    @SuppressWarnings("unchecked")
    private static ArrayList<Item> loadItems() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILENAME))) {
            return (ArrayList<Item>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No previous data found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Failed to load items: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    // Save items to CSV file (human-readable)
    public static void saveItemsToCSV(ArrayList<Item> items) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILENAME_CSV))) {
            writer.write("ItemName,Description,Price,Quantity,Store,DateBought\n");
            for (Item item : items) {
                writer.write(String.format("%s, %s, %.2f Euro, %d, %s, %s\n",
                        item.getItemName(),
                        item.getItemDescription(),
                        item.getItemPrice(),
                        item.getItemQuantity(),
                        item.getStoreName(),
                        item.getDateBought()));
            }
        } catch (IOException e) {
            System.out.println("Failed to save items to CSV: " + e.getMessage());
        }
    }
}
