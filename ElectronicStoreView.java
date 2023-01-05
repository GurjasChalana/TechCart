package view;

import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.ElectronicStore;
import model.Product;
import org.w3c.dom.Text;

import javax.management.StringValueExp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static javafx.application.Application.launch;

public class ElectronicStoreView extends Pane {
    private ListView<Product> storeStock, mostPopular;
    private ListView<String> currentCart;
    private TextField numberOfSales, revenue, moneyPerSale;
    private Button resetStore, addToCart, removeFromCart, completeSale;
    private Label currentCartText, saleText, $saleText;

    // Getters
    public TextField getNumberOfSales () {return numberOfSales;}
    public TextField getRevenue() {return revenue;}
    public TextField getMoneyPerSale () {return moneyPerSale;}
    public ListView<String> getCurrentCart() {
        return currentCart;
    }
    public ListView<Product> getStoreStock() {
        return storeStock;
    }
    public ListView<Product> getMostPopular() {return mostPopular;}
    public Label getCurrentCartText() {
        return currentCartText;
    }
    public Button getAddToCart() {
        return addToCart;
    }
    public Button getRemoveFromCart() {return removeFromCart;}
    public Button getCompleteSale() {return completeSale; }
    public Button getResetStore() { return resetStore;}


    public ElectronicStoreView () {

        // listViews

        // storeStock
        storeStock = new ListView<>();
        storeStock.relocate(195, 65);
        storeStock.setPrefSize(285, 270);

        // currentCart
        currentCart = new ListView<>();
        currentCart.relocate(491, 65);
        currentCart.setPrefSize(285, 270);

        mostPopular = new ListView<>();
        mostPopular.relocate(16, 187);
        mostPopular.setPrefSize(168, 148);

        // textFields

        //numberOfSales
        numberOfSales = new TextField();
        numberOfSales.relocate(89, 65);
        numberOfSales.setPrefSize(95, 25);
        numberOfSales.setText("0");

        //revenue
        revenue = new TextField();
        revenue.relocate(89, 95);
        revenue.setPrefSize(95, 25);
        revenue.setText("0.00");

        // moneyPerSale
        moneyPerSale = new TextField();
        moneyPerSale.relocate(89, 125);
        moneyPerSale.setPrefSize(95, 25);
        moneyPerSale.setText("N/A");

        // Labels

        Label storeSummary = new Label();
        storeSummary.setText("Store Summary:");
        storeSummary.relocate(54, 43);

        saleText = new Label();
        saleText.setText("# Sales:");
        saleText.relocate(44, 68);

        Label revenueText = new Label();
        revenueText.setText("Revenue:");
        revenueText.relocate(35, 98);

        $saleText = new Label();
        $saleText.setText("$ / Sale:");
        $saleText.relocate(41, 129);

        Label mostPopularItems = new Label();
        mostPopularItems.setText("Most Popular Items:");
        mostPopularItems.relocate(47, 162);

        Label storeStockText = new Label();
        storeStockText.setText("Store Stock:");
        storeStockText.relocate(300, 43);

        currentCartText = new Label();
        currentCartText.setText("Current Cart:");
        currentCartText.relocate(590, 43);

        // Buttons

        // resetStore
        resetStore = new Button();
        resetStore.setText("Reset Store");
        resetStore.relocate(32, 339);
        resetStore.setPrefSize(141, 44);

        // addToCart
        addToCart = new Button();
        addToCart.setText("Add to Cart");
        addToCart.relocate(269, 339);
        addToCart.setPrefSize(141, 44);

        // removeFromCart
        removeFromCart = new Button();
        removeFromCart.setText("Remove From Cart");
        removeFromCart.relocate(494, 339);
        removeFromCart.setPrefSize(141, 44);

        // completeSale
        completeSale = new Button();
        completeSale.setText("Complete Sale");
        completeSale.relocate(635, 339);
        completeSale.setPrefSize(141, 44);


        // adding all to pane
        getChildren().addAll(storeStock, currentCart, mostPopular, numberOfSales, revenue, moneyPerSale, storeSummary, saleText, revenueText, $saleText, mostPopularItems, storeStockText, currentCartText, resetStore, addToCart, removeFromCart, completeSale);

    }


        public void update(ElectronicStore model) {
        // set all the items in the listview, calling the model to return two separate arrayLists to show. The stock and the most popular products.
        getStoreStock().setItems(FXCollections.observableArrayList(model.getStock()));
        getMostPopular().setItems(FXCollections.observableArrayList(model.mostPopular()));

        // if there is no selection, the add button is disabled
        if (!getStoreStock().getSelectionModel().isEmpty()) {
            getAddToCart().setDisable(false);
        } else {
            getAddToCart().setDisable(true);
        }
        // prints cart with receipt format, by converting "amount x selection" to a string ArrayList.
        getCurrentCart().setItems(FXCollections.observableArrayList(model.getProductReceipts()));
        // if there is a product in the cart, then the user can complete a sale or remove any product in the cart. As well, the cart total will be displayed.
        if (!model.getTrackPurchases().isEmpty()) {
            getCompleteSale().setDisable(false);
            getRemoveFromCart().setDisable(false);
            getCurrentCartText().setText("Current Cart: (" + "$" + String.valueOf(model.getCartTotal()) + ")");
        }
        // if nothing is added to cart, can't complete sale, or remove an item. The cartTotal would be $0.00.
        else {
            getCompleteSale().setDisable(true);
            getRemoveFromCart().setDisable(false);
            getCurrentCartText().setText("Current Cart: ($0.00)");
        }
        // Setting the # of the sales
        getNumberOfSales().setText(String.valueOf(model.getTotalSales()));
        // Setting the revenue made through the sales
        getRevenue().setText(String.valueOf(model.getRevenue()));
        // If no complete purchases have been made, then there has been no money per sale
        if (model.getCompletePurchases().isEmpty()) {
            getMoneyPerSale().setText("N/A");
        }
        // If there has, will show the revenue divided by total sales
        else {
            getMoneyPerSale().setText(String.valueOf(model.getRevenue() / model.getTotalSales()));
        }


    }


}

