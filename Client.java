package GraphicalInterface;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This class is designed to store relevant information of the clients.
 * It is later on used in order to create an ObservableList of this kind of objects and populate a TableView with information
 * retrieved from a database.
 */
public class Client {

    // String properties are used because the class is used to populate a table view, which requires the existence of properties.
    private StringProperty name, cp, rfc, mail;

    /**
     * Constructor of the class
     */
    public Client(){
        name = null;
        cp = null;
        rfc = null;
        mail = null;
    }

    /**
     * Sets the value of the name
     * @param name String
     */
    public void setName(String name){
        nameProperty().set(name);
    }

    /**
     * Initializes the name property if it is null, and returns it.
     * @return StringProperty
     */
    public StringProperty nameProperty(){
        if (this.name == null){
            this.name = new SimpleStringProperty(this, "name");
        }
        return name;
    }

    /**
     * Sets the value of the cp
     * @param cp String
     */
    public void setCp(String cp){
        cpProperty().set(cp);
    }

    /**
     * Initializes the cp property if it is null, and returns it.
     * @return StringProperty
     */
    public StringProperty cpProperty(){
        if (this.cp == null){
            this.cp = new SimpleStringProperty(this, "cp");
        }
        return cp;
    }

    /**
     * Set the value of the rfc
     * @param rfc String
     */
    public void setRfc(String rfc){
        rfcProperty().set(rfc);
    }

    /**
     * Get the value of rfc
     * @return String
     */
    public String getRfc(){
        return rfcProperty().get();
    }

    /**
     * Initializes the rfc property if it is null, and returns it.
     * @return StringProperty
     */
    public StringProperty rfcProperty(){
        if (this.rfc == null){
            this.rfc = new SimpleStringProperty(this, "rfc");
        }
        return rfc;
    }

    /**
     * Sets the value of mail
     * @param mail String
     */
    public void setMail(String mail){
        mailProperty().set(mail);
    }

    /**
     * Initializes the mail property if it is null, and returns it.
     * @return StringProperty
     */
    public StringProperty mailProperty(){
        if (this.mail == null){
            this.mail = new SimpleStringProperty(this, "mail");
        }
        return mail;
    }
}