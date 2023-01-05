package controller;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.ElectronicStore;
import model.Product;
import view.ElectronicStoreView;

import java.util.ArrayList;
import java.util.HashMap;

public class ElectronicStoreApp extends Application {
    private ElectronicStore model;
    private ElectronicStoreView view;

    // For the controller, I need to initialize a model and view
    public ElectronicStoreApp () {
        this.model = ElectronicStore.createStore();
        this.view = new ElectronicStoreView();
    }

    public void start(Stage primaryStage) {
        // The pane should display the view
        Pane pane = new Pane();
        pane.getChildren().add(view);

        // init the store with base values, since the model hasn't been touched
        view.update(model);

        // event handler for listview selection affecting addButton
        view.getStoreStock().setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                handleListView();
            }
        });

        // event handler for add button, to add items to the cart
        view.getAddToCart().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                handleAddButton();
            }
        });

        // event handler for remove button, to remove items from the cart
        view.getRemoveFromCart().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                handleRemoveButton();
            }
            });

        // event handle for completeSale button, to complete a sale
        view.getCompleteSale().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                handleCompleteSale();
            }
        });

        // event handler for reset store button, to reset the state of the store
        view.getResetStore().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                handleResetStore();
            }
        });

        // stage
        primaryStage.setTitle(model.getName());
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(pane, 800,400));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    // handling methods

    public void handleListView() {

       view.update(model);
    }
    public void handleAddButton() {
        // subtract it from stockQuantity for that product
        view.getStoreStock().getSelectionModel().getSelectedItem().setStockQuantity(view.getStoreStock().getSelectionModel().getSelectedItem().getStockQuantity() - 1);
        // add that purchase to the stores trackPurchase hashmap to keep note of the item and how much its bought
        model.addPurchase(view.getStoreStock().getSelectionModel().getSelectedItem());
        // set the cart total
        model.setCartTotal(model.getCartTotal() + view.getStoreStock().getSelectionModel().getSelectedItem().getPrice());
        // if there's none of that item left, remove it from the stock listview
        if (view.getStoreStock().getSelectionModel().getSelectedItem().getStockQuantity() <= 0) {
            model.removeProduct(view.getStoreStock().getSelectionModel().getSelectedItem());
        }
        view.update(model);
    }

    // Since cart is a string, we can obtain the product version of it by getting the index, and it's corresponding product
    public Product getProductFromCart(int index) {
        Product p = null;
        for (int i = 0; i < model.getProducts().size(); i++) {
            if (i == index) {
                p = model.getProducts().get(i);
            }
        }
        return p;
    }

    public void handleRemoveButton() {
        // Dealing with selections in the cart will require the use of the getProductFromCart method
        boolean flag = false;
        // Make it easier to keep track
        Product p = getProductFromCart(view.getCurrentCart().getSelectionModel().getSelectedIndex());
        // If the item is able to be removed, it will deduct it from the cart.
        if (model.removePurchase(p)) {
            // The cart total will be updated, subtracting the products price.
            model.setCartTotal(model.getCartTotal() - p.getPrice());
            // The stock quantity will increase.
            p.setStockQuantity(p.getStockQuantity() + 1);
            // If the stock quantity of that product is equal to 1, as in it was completely bought out, and then removed. It will add it back into the stock.
            while (!flag) {
                if (p.getStockQuantity() == 1) {
                    model.addProduct(p);
                }
                flag = true;
            }
            // If it has been added to the cart, then had the purchases removed. Will add it back to the stock.
            if (model.getTrackPurchases().get(p) == 0) {
                model.removeFromCart(p);
            }
            view.update(model);
        }
    }

    public void handleCompleteSale() {
        // Sold quantities are updated, when the products are officially sold.
       model.setSoldQuantities();
       // Will add the current cart to the completed purchases hashmap, and if they already exist within it will update.
       model.addToCompletePurchases();
       // Clear the cart
       model.getTrackPurchases().clear();
       // The revenue will be updated after a complete purchase, equal to the current revenue + cart total
       model.setRevenue(model.getRevenue() + model.getCartTotal());
       // Total sales go up by 1
       model.setTotalSales(model.getTotalSales() +1);
       // The cart total goes back to 0
       model.setCartTotal(0);
       view.update(model);
    }

    public void handleResetStore() {
        // If rested, will go back to initial state which is the preexisting model using createStore().
        this.model = ElectronicStore.createStore();
        view.update(model);
    }


}
