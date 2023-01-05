package model;

import java.util.ArrayList;
import java.util.HashMap;

//Base class for all products the store will sell
public abstract class Product implements Comparable<Product>{
    private double price;
    private int stockQuantity;
    private int soldQuantity;
    private double revenue;

    public Product(double initPrice, int initQuantity) {
        price = initPrice;
        stockQuantity = initQuantity;
        soldQuantity = 0;
    }


    public void setSoldQuantity (int newSold) {soldQuantity = newSold;}
    public int getStockQuantity() {
        return stockQuantity;
    }

    public int getSoldQuantity() {
        return soldQuantity;
    }

    public double getPrice() {
        return price;
    }

    public void setStockQuantity(int newStock) {
        stockQuantity = newStock;
    }


    @Override
    public int compareTo(Product a) {
        if ((this.soldQuantity - a.soldQuantity < 0)) {
            return 1;
        } else if ((this.soldQuantity - a.soldQuantity > 0)){
            return -1;
        } else if ((this.soldQuantity - a.soldQuantity == 0)) {
            return 0;
        }
        return 0;
    }

}