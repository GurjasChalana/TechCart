package model;//Class representing an electronic store
//Has an array of products that represent the items the store can sell

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class ElectronicStore {
    private String name;
    private ArrayList<Product> stock;
    private double revenue;
    private HashMap<Product, Integer> trackPurchases;
    private int totalSales;
    private double cartTotal;
    private HashMap<Product, Integer> completePurchases;
    // This is an array list, which can only be added too. Since stock has items removed from it if stocks empty, we can't use stock to show initial state.
    private ArrayList<Product> noManipulateStock = new ArrayList<>();

    // Getters
    public HashMap<Product, Integer> getCompletePurchases() {
        return completePurchases;
    }
    public double getCartTotal() {
        return cartTotal;
    }

    public void setCartTotal(double newTotal) {
        cartTotal = newTotal;
    }

    public int getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(int newTotal) {
        totalSales = newTotal;
    }

    public void setRevenue(double newRevenue) {
        revenue = newRevenue;
    }

    public HashMap<Product, Integer> getTrackPurchases() {
        return trackPurchases;
    }

    public ArrayList<Product> getStock() {
        return stock;
    }

    public double getRevenue() {
        return revenue;
    }

    public String getName() {
        return name;
    }


    // Constructor
    public ElectronicStore(String initName) {
        revenue = 0.0;
        name = initName;
        // The system is, for every store. There is a stock, this stock takes all initialized products and stores them. A hashmap that will track the cart, for every product
        // and the amount of times it's been added, there will be an associated key and value. As well, the completedPurchases hashmap, which will take all the products that
        // have been bought and the amount of times it has been bought, and store this.
        stock = new ArrayList<>();
        trackPurchases = new HashMap<>();
        completePurchases = new HashMap<>();
    }


    // If the product trying to be added is not null, it will be added to the stock, as well as the unMutable array (except being added too).
    public boolean addProduct(Product newProduct) {
        if (newProduct != null) {
            stock.add(newProduct);
            noManipulateStock.add(newProduct);
            return true;
        }
        return false;
    }

    // If the product is in our stock, it will remove that selection.
    public boolean removeProduct(Product newProduct) {
        if (stock.contains(newProduct)) {
            stock.remove(newProduct);
        }

        return false;
    }

    // addPurchase adds the item to a hashmap keeping track of cart items, and if the product exists within this hashmap, it will be updated.
    public void addPurchase(Product item) {
        if (trackPurchases.get(item) == null) {
            trackPurchases.put(item, 1);
        } else {
            trackPurchases.put(item, trackPurchases.get(item) + 1);
        }
    }

    // completePurchases is a hashmap that contains all the items that have been bought in the cart, and if the items exist already, will update their amounts.
    public void addToCompletePurchases() {
        if (completePurchases.isEmpty()) {
            completePurchases.putAll(trackPurchases);
        }
        for (Product c : trackPurchases.keySet()) {
            if (completePurchases.containsKey(c)) {
                completePurchases.put(c, completePurchases.get(c) + trackPurchases.get(c));
            } else {
                completePurchases.put(c, trackPurchases.get(c));
            }
        }
    }


    // If the product exists within the cart, it will update it by reducing its amount by 1.
    public boolean removePurchase(Product item) {
        if (trackPurchases.get(item) != null && trackPurchases.get(item) > 0) {
            trackPurchases.put(item, trackPurchases.get(item) - 1);
            return true;
        }
        return false;
    }

    // Remove the whole item from the cart, not deduct by 1.
    public void removeFromCart(Product item) {
        if(trackPurchases.containsKey(item)) {
            trackPurchases.remove(item);
        }
    }

    // Updating every product's sold quantity.
    public void setSoldQuantities() {
        for (Product c : trackPurchases.keySet()) {
            c.setSoldQuantity(c.getSoldQuantity() + trackPurchases.get(c));
        }
    }


    // Creating an arraylist of string to print out the products in a receipt format.
    public ArrayList<String> getProductReceipts() {
        ArrayList<String> receipt = new ArrayList<>();
        for (int i = 0; i < trackPurchases.size(); i++) {
            receipt.add(getAmount().get(i) + " x " + getProducts().get(i));
        }
        return receipt;
    }


    // Create two arraylists which will be used to write the receipt

    // The products array is all the products in the cart
    public ArrayList<Product> getProducts() {
        ArrayList<Product> newProduct = new ArrayList<>();
        newProduct.addAll(trackPurchases.keySet());
        return newProduct;
    }

    // The amount is the corresponding values for each of the products above
    public ArrayList<Integer> getAmount() {
        ArrayList<Integer> productAmount = new ArrayList<>();
        for (Product c : trackPurchases.keySet()) {
            productAmount.add(trackPurchases.get(c));
        }
        return productAmount;
    }


    // The mostPopular method looks if there has been any completed purchases made, if there hasn't. It will display 3 of the products from the stock (doesn't matter). On the contrary,
    // It will take the products from the completed purchases' key-set, and for each of these products it will use the collection to sort it from highest sold quantity to lowest.
    // Depending on the condition (how many purchases have been made), it will then return an array of products.
    public ArrayList<Product> mostPopular() {
        ArrayList<Product> mostPopularItemsProducts = new ArrayList<>();
        ArrayList<Product> mostPopularItems = new ArrayList<>();
        for (Product c : getCompletePurchases().keySet()) {
            mostPopularItemsProducts.add(c);
        }
        Collections.sort(mostPopularItemsProducts);

        if (getCompletePurchases().isEmpty()) {
            int random = 0;
            while (random < 3) {
                mostPopularItems.add(noManipulateStock.get(random));
                random++;
            }
            return mostPopularItems;
        }

        if (mostPopularItemsProducts.size() >= 3) {
            int top = 0;
            while (top < 3) {
                mostPopularItems.add(mostPopularItemsProducts.get(top));
                top++;
            }
        } else {
            int top = 0;
            while (top < mostPopularItemsProducts.size()) {
                mostPopularItems.add(mostPopularItemsProducts.get(top));
                top++;
            }
        }
        return mostPopularItems;
    }

    // Create store
    public static ElectronicStore createStore() {
        ElectronicStore store1 = new ElectronicStore("Watts Up Electronics");
        Desktop d1 = new Desktop(100, 10, 3.0, 16, false, 250, "Compact");
        Desktop d2 = new Desktop(200, 10, 4.0, 32, true, 500, "Server");
        Laptop l1 = new Laptop(150, 10, 2.5, 16, true, 250, 15);
        Laptop l2 = new Laptop(250, 10, 3.5, 24, true, 500, 16);
        Fridge f1 = new Fridge(500, 10, 250, "White", "Sub Zero", false);
        Fridge f2 = new Fridge(750, 10, 125, "Stainless Steel", "Sub Zero", true);
        ToasterOven t1 = new ToasterOven(25, 10, 50, "Black", "Danby", false);
        ToasterOven t2 = new ToasterOven(75, 10, 50, "Silver", "Toasty", true);
        store1.addProduct(d1);
        store1.addProduct(d2);
        store1.addProduct(l1);
        store1.addProduct(l2);
        store1.addProduct(f1);
        store1.addProduct(f2);
        store1.addProduct(t1);
        store1.addProduct(t2);
        return store1;
    }


}
