package GraphicalInterface;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This class is designed to store relevant information of the products that are being sold to a client at a given sale.
 * It is later on used in order to create an ObservableList of this kind of objects and populate a TableView with information
 * retrieved from a database.
 */
public class Product{

    // String properties are used instead of strings because the values are going to be observed in the interface. The variable identifier is
    // used in the graphical interface in order to retrieve the value of the attributes and display it in a column to populate the tables.
    // Strings cannot work with table view because they are not properties.
    private StringProperty id, description, price, amount, existence, rfc;
    private int amountNum;

    /**
     * This constructor allows the user to create a new Product object, it does not initialize the StringProperty
     * variables.
     */
    public Product(){
        id = null;
        description = null;
        price = null;
        amountNum = 0;
        amount = null;
        rfc = null;

    }

    /**
     * This method allows the user to set the value of the id StringProperty
     * @param id The value of the id
     */
    public void setId(String id){
        idProperty().set(id);
    }

    /**
     * This method returns the value of the id.
     * @return  A String continaing the id of the product.
     */
    public String getId(){
        return idProperty().get();
    }

    /**
     * This method initializes the id StringProperty if it is not initialized already.
     * @return the initilaized id StringProperty
     */
    public StringProperty idProperty(){
        if (id == null){
            id = new SimpleStringProperty(this, "id");
        }
        return id;
    }

    /**
     * Set the description of a product
     * @param description string
     */
    public void setDescription(String description){
        descriptionProperty().set(description);
    }

    /**
     * Initializes the description property if it is null, and returns it
     * @return StringProperty
     */
    public StringProperty descriptionProperty(){
        if (this.description == null){
            this.description = new SimpleStringProperty(this, "description");
        }
        return this.description;
    }

    /**
     * Set the price of the product
     * @param price string
     */
    public void setPrice(String price){
        priceProperty().set(price);
    }

    /**
     * Return the price of the product
     * @return String
     */
    public String getPrice(){
        return priceProperty().get();
    }

    /**
     * Initializes the price property if it is null, and returns it
     * @return StringProperty
     */
    public StringProperty priceProperty(){
        if (price == null){
            price = new SimpleStringProperty(this, "price");
        }
        return price;
    }

    /**
     * Set the value of amount
     * @param amount string
     */
    public void setAmount(String amount){
        amountProperty().set(amount);
        amountNum = Integer.valueOf(amount);
    }

    /**
     * Get the amount value
     * @return String
     */
    public String getAmount(){
        return amountProperty().get();
    }

    /**
     * Initializes the amount property if it is null, and returns it
     * @return String Property
     */
    public StringProperty amountProperty(){
        if (amount == null){
            amount = new SimpleStringProperty(this, "amount");
        }
        return amount;
    }

    /**
     * Increase the value of amount by one
     */
    public void addAmount(){
        this.amountNum = this.amountNum + 1;
        amount.set(String.valueOf(amountNum));
    }

    /**
     * Initializes the existence property if it is null, and returns it
     * @param existence String
     */
    public void setExistence(String existence){
        existenceProperty().set(existence);
    }

    /**
     * Initializes the exact property if it is null, and returns it
     * @return StringProperty
     */
    public StringProperty existenceProperty(){
        if (existence == null){
            existence = new SimpleStringProperty(this, "existence");
        }
        return existence;
    }

    /**
     * Set the value of rfc
     * @param rfc string
     */
    public void setRfc(String rfc){
        rfcProperty().set(rfc);
    }

    /**
     * Initializes the rfc property if it is null, and returns it
     * @return StringProperty
     */
    public StringProperty rfcProperty(){
        if (rfc == null){
            rfc = new SimpleStringProperty(this, "rfc");
        }
        return rfc;
    }
}