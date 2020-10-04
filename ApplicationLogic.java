package GraphicalInterface;

import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

import static javafx.scene.control.Alert.AlertType.CONFIRMATION;
import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.Alert.AlertType.INFORMATION;
import static javafx.scene.control.ButtonType.CANCEL;
import static javafx.scene.control.ButtonType.NO;
import static javafx.scene.control.ButtonType.OK;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;


public class ApplicationLogic {

    private static double cashGiven, totalSale = 0;

    /**
     * A method that will be used only to get the value of the totalSale variable, the one which stores the total fee that the
     * client has to pay depending on the products he has bought.
     * @return The value of the total sale.
     */
    public static String getTotalSale(){
        return String.valueOf(totalSale);
    }

    /**
     * An utility method whose purpose is to round the value of a double to a given number of decimal places.
     * @param value The original double
     * @param places The number of places to which it will be rounded.
     * @return A double containing the rounded decimal
     */
    public static double round(double value, int places) {
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    /**
     * Method that handles the clicks in the "Entrar" button from the Log In scene. It also manages the authentication process
     * by checking in the pharmacy's database that the username and password given by the user are valid. Furthermore, it checks the permissions that the
     * user has and controls the following behaviour of the program depending on the status.
     * @param username A TextField object, in which the user has typed the username. (at the beginning the following username may be used: "admin".
     * @param password A PasswordField object, in which the user has typed the password. (at the beginning the following password may be used: "12345".
     * @param conn The Connection object linked to the database of the pharmacy.
     * @param primaryStage The primary Stage of the program.
     * @param adminScene The Scene of the Administrator.
     * @param staffScene The Scene of the staff.
     * @throws SQLException
     */
    public static void logInClick(TextField username, PasswordField password, Connection conn, Stage primaryStage,
                                  Scene adminScene, Scene staffScene, MenuButton mainAdminMenu, MenuButton mainStaffMenu) throws SQLException {
        String realPass = "";
        String privileges = "";
        Statement logIn = conn.createStatement(); // Retrieving the password from the database given the username typed on the textfield.
        ResultSet pass = logIn.executeQuery("SELECT password FROM Users WHERE username = '".concat(username.getText()).concat("'"));
        logIn.closeOnCompletion();
        if (username.getText().isEmpty() == false){
            if (password.getText().isEmpty() == false) {
                while (pass.next()) { // The result set could be empty if the user is not valid
                    realPass = pass.getString(1); // Getting the password for the given user
                }
                if (password.getText().equals(realPass)) { // If the password and username are valid
                    Alert correct = new Alert(INFORMATION, "Correct Password!", OK);
                    correct.showAndWait(); // Confirmation
                    Statement privilegesStm = conn.createStatement(); // Getting the status of the user (either admin or staff) from the database
                    ResultSet privilegesRs = privilegesStm.executeQuery("SELECT status FROM Users WHERE username = '".concat(username.getText()).concat("'"));
                    privilegesStm.closeOnCompletion();
                    Statement session = conn.createStatement();
                    while (privilegesRs.next()){
                        privileges = privilegesRs.getString(1);
                        if (privileges.equals("admin")){
                            password.setText("");
                            username.setText("");
                            // If the status is admin, then execute the method that changes the scene to admin mode
                            GraphicalComponents.logAdmin(primaryStage, adminScene);
                            session.executeUpdate("UPDATE Users SET active = 1 WHERE status = 'admin'"); //Update the active field in database.
                        }
                        else if (privileges.equals("staff")){
                            password.setText("");
                            username.setText("");
                            GraphicalComponents.logStaff(primaryStage, staffScene);
                            session.executeUpdate("UPDATE Users SET active = 1 WHERE status = 'staff'");
                        }
                    }
                    session.closeOnCompletion();

                } else { // If the password was wrong, show an alert.
                    Alert wrong = new Alert(ERROR, "Wrong Password!", OK);
                    wrong.showAndWait();
                    username.setText("");
                    password.setText("");
                }
            }
            else{ // Alert displayed if the password field is empty
                Alert nullPas = new Alert(ERROR, "Type the password", OK);
                nullPas.showAndWait();
            }
        }
        else{// Alert displayed if the username field is empty
            Alert nullUser = new Alert(ERROR, "Type the username", OK);
            nullUser.showAndWait();
        }
    }


    /**
     * This method is designed to appropriately close the application, by closing the connection with the database and setting the active value of
     * both users to 0.
     * @param conn The connection to the database.
     * @param primaryStage The primary stage of the application.
     */
    public static void closeClick(Connection conn, Stage primaryStage) {
        try {
            Statement session = conn.createStatement();
            session.executeUpdate("UPDATE Users SET active = 0"); //To set the state of both users as not active.
            session.close();
            conn.close(); // Closing the connection to database.
        } catch (SQLException e) {
            e.printStackTrace();
        }
        primaryStage.close(); // Closing the application
    }


    /**
     * This method allows to change the user in the application without closing it. It modifies the application according
     * to the privileges of the new user.
     * @param conn The connection with the database.
     * @param primaryStage The primary stage of the application.
     * @param logInScene The log in scene.
     * @throws SQLException
     */
    public static void changeUserClick(Connection conn, Stage primaryStage, Scene logInScene) throws SQLException {
        try {
            Statement session = conn.createStatement();
            session.executeUpdate("UPDATE Users SET active = 0"); //Setting both users as not active
            session.closeOnCompletion();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        primaryStage.setScene(logInScene); // Set the scene to log in.
        primaryStage.setTitle("Log In");
    }

    /**
     * This method allows to add a product to the cart when executing a sale. If there is no stock, an alert is displayed.
     * Furthermore, if the product was already added, instead of creating a separate entry in the sale table, the "amount"
     * value is increased.
     * @param primaryStage Primary Stage of the application
     * @param conn Connection to the database
     * @param idField Textfield containing the id of the product which is to be added
     * @param total Textfield containing the total cost of the sale (it cannot be edited by the user)
     * @param productList An observable list of Product objects containing the products in the sale.
     * @throws SQLException
     */
    public static void addClick(Stage primaryStage, Connection conn, TextField idField, TextField total, ObservableList<Product> productList) throws SQLException {
        Statement prodStm = conn.createStatement();
        String id = idField.getText();
        String description = "";
        String price = "";
        boolean inCart = false;
        Alert noExist = new Alert(ERROR, "No hay stock");
        noExist.initOwner(primaryStage);
        //Checking if the product was already added.
        for (Product product : productList) {
            if (product.getId().equals(id)) { //If the id of the product to be added is the same as any other product in the cart
                inCart = true;
                ResultSet prodInfo = prodStm.executeQuery("SELECT existence FROM Products WHERE id = '".concat(id).concat("'"));
                int existence = 0;
                while(prodInfo.next()){
                    existence = parseInt(prodInfo.getString(1)); // Getting the stock of the product
                }
                if (existence - parseInt(product.getAmount()) >= 0) {
                    // If there is enough stock, considering the products already added to the cart, the increase the value of the amount in Product
                    product.addAmount();
                }
                else{
                    Toolkit.getDefaultToolkit().beep(); // If there isn't enough stock, then show an alert and play a sound.
                    noExist.showAndWait();
                }
            }
        }
        if(inCart == false){ // If the product wasn't in the cart yet
           Product product = new Product(); //Create a new product object and get its properties from the database given the id
           ResultSet prodInfo = prodStm.executeQuery("SELECT existence, description, price FROM Products WHERE id = '".concat(id).concat("'"));
           int existence = 0;
           while (prodInfo.next()){
               existence = parseInt(prodInfo.getString(1));
               description = prodInfo.getString(2);
               price = prodInfo.getString(3);
           }
           if (existence - 1 > 0) { // If there is enough stock, then set the attributes of the created product
               product.setPrice(price);
               product.setDescription(description);
               product.setId(id);
               product.setAmount("1");
               productList.add(product); // Add the product to the observable list
           }
           else{ // If there isn't enough stock, then show the alert
               Toolkit.getDefaultToolkit().beep();
                noExist.showAndWait();
           }
        }
        idField.setText(""); // Erase the id value in text field
        totalSale = 0; // Get the total cost of the sale considering the product added and the ones that were already there
        for (Product product : productList) {
            double partialCount = round(parseDouble(product.getAmount()),2)*round(parseDouble(product.getPrice()),2);
            totalSale = totalSale + partialCount;
        }
        total.setText(String.valueOf(totalSale)); //Update the value of the total text field
        prodStm.close();
    }

    public static boolean cancelSale = false;
    /**
     *
     * @param conn
     * @param products
     * @throws SQLException
     */
    public static void chargeClick( TextField rfcInput, CheckBox invoiceBool, MenuButton paymentForm,  Stage primaryStage, Connection conn, ObservableList<Product> products, TextField totalField, ObservableList<Product> soldProductList) throws SQLException {
        ButtonType SI = new ButtonType("Si");
        cancelSale = false;

        // This is a dialog that will execute the sale, tell the change, etc. It is created here instead of the UI
        // because it is only used in this method.
        Dialog<Boolean> getPayment = new Dialog<Boolean>(); // Creating the dialog
        getPayment.setHeight(250);
        getPayment.setWidth(225);
        getPayment.initOwner(primaryStage);

        Label totalName = new Label("Total: "); // Creating the graphical objects
        totalName.setId("totalName");
        Label cashLabel= new Label("Efectivo: ");
        cashLabel.setId("cashLabel");
        Label totalLabelDialog = new Label("$ ".concat(String.valueOf(totalSale)));
        totalLabelDialog.setId("totalLabelDialog");
        TextField cashInput = new TextField("$".concat(ApplicationLogic.getTotalSale()));
        cashInput.setId("cashInput");
        Button cancelButton = new Button("Cancelar");
        cancelButton.setId("cancelButton");
        Button completeButton = new Button("Completar");
        completeButton.setId("completeButton");

        GridPane container = new GridPane(); // Creating the grid pane
        container.getColumnConstraints().add(new ColumnConstraints(25)); // Setting the constraints
        container.getColumnConstraints().add(new ColumnConstraints(100));
        container.getColumnConstraints().add(new ColumnConstraints(50));
        container.getColumnConstraints().add(new ColumnConstraints(25));
        container.getColumnConstraints().add(new ColumnConstraints(50));
        container.getColumnConstraints().add(new ColumnConstraints(100));
        container.getColumnConstraints().add(new ColumnConstraints(25));
        container.getRowConstraints().add(new RowConstraints(25));
        container.getRowConstraints().add(new RowConstraints(50));
        container.getRowConstraints().add(new RowConstraints(25));
        container.getRowConstraints().add(new RowConstraints(50));
        container.getRowConstraints().add(new RowConstraints(25));
        container.getRowConstraints().add(new RowConstraints(50));
        container.getRowConstraints().add(new RowConstraints(25));

        container.add(totalName, 1, 1, 1, 1); // Adding the objects to the gridpane
        container.add(cashLabel, 1, 3, 1, 1);
        container.add(totalLabelDialog, 2, 1, 3, 1);
        container.add(cashInput, 2, 3, 3, 1);
        container.add(cancelButton, 1, 5, 2, 1);
        container.add(completeButton, 4, 5, 2, 1);
        getPayment.getDialogPane().setContent(container);

        cancelButton.setOnAction(e -> { // Managing a click on the "Cancel" button
            getPayment.setResult(Boolean.TRUE);
            getPayment.close();
            Alert confirmation = new Alert(CONFIRMATION, "Cancelar la venta?", SI, NO);
            confirmation.initOwner(primaryStage); // Confirmation of canceling the sale
            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.get() == SI){
                Alert cancel = new Alert(INFORMATION, "La venta fue cancelada", OK);
                cancel.initOwner(primaryStage);
                cancel.showAndWait();
                totalSale = 0; // Set the total to zero
                totalField.setText("$");
                cancelSale = true;

            }
            else if (result.get() == ButtonType.NO){
                confirmation.close(); // If the user did not want to end the sale, return to the menu
                getPayment.showAndWait();
            }
        });

        completeButton.setOnAction(e -> { // Click on the complete sale button
            String cashString = cashInput.getText();
            cashGiven = parseDouble(cashString.substring(1, cashString.length()));
            double change = cashGiven - totalSale;
            getPayment.setResult(Boolean.TRUE);
            getPayment.close();
            cashInput.setText("$");
            totalSale = 0;
            totalField.setText("$");
            getPayment.close();
            Alert giveChange = new Alert(INFORMATION, "Cambio: ".concat(Double.toString(change)), OK); // Tel the change to the user
            giveChange.initOwner(primaryStage);
            giveChange.showAndWait();
        });

        //This if manages the payment form, which could be either cash or card.
        Statement charge = conn.createStatement();
        if (paymentForm.getText().equals("Efectivo")){ // If cash, show the dialog created above
            getPayment.showAndWait();
        }
        else if (paymentForm.getText().equals("Débito")){ // If card, just show a confirmation that the card payment process was carried out succesfully.
            Alert cardPayment = new Alert(CONFIRMATION, "Total: $".concat(String.valueOf(round(totalSale, 2)).concat(System.lineSeparator()).concat("La operación fue finalizada con éxito?")), SI, NO);
            cardPayment.getDialogPane().setMinSize(450, 300);
            cardPayment.initOwner(primaryStage);
            Optional<ButtonType> endSale = cardPayment.showAndWait();
            if (endSale.get() == SI){
                Alert confirmation = new Alert(CONFIRMATION, "La venta ha sido completada", OK);
                confirmation.initOwner(primaryStage); // If the operation was succesful, then finish the sale
                confirmation.showAndWait();
            }
            else if (endSale.get() == ButtonType.NO){
                Alert cancel = new Alert(CONFIRMATION, "La venta ha sido cancelada", OK);
                cancel.initOwner(primaryStage); // If the operation could not be performed, cancel the sale.
                cancel.showAndWait();
                cancelSale = true;
            }

        }
        else{ // If a payment method is not selected, show an alert.
            Alert noPay = new Alert(ERROR, "No has seleccionado una forma de pago", OK);
            noPay.initOwner(primaryStage);
            noPay.showAndWait();
            return;
        }

        //This if manages empty sales or sales that finish appropriately.
        if (products.isEmpty()||cancelSale == true){ // If there were no products or if the sale was canceled
            Alert noProd = new Alert(ERROR, "No hay productos seleccionados", OK);
            noProd.initOwner(primaryStage);
            Toolkit.getDefaultToolkit().beep();
            noProd.showAndWait();
        }
        else if (products.isEmpty() == false){ // If the cart was not empty, and the sale was not canceled
            for (Product product: products){
                int existence = 0;
                String id = product.getId(); // For each product sold, get the stock in the database
                ResultSet existRs = charge.executeQuery("SELECT existence FROM Products WHERE id = '".concat(id).concat("'"));
                while (existRs.next()){
                    existence = parseInt(existRs.getString(1));
                }
                existence = existence - Integer.valueOf(product.getAmount()); // Substract the sold products from the inventory
                charge.executeUpdate("UPDATE Products SET existence = ".concat(String.valueOf(existence).concat(" WHERE id = '").concat(id).concat("'")));
            }
        }

        if (invoiceBool.isSelected()){ // If the invoice check box was selected, then
            String givClient = rfcInput.getText();
            String name = "";
            Statement invoiceStm = conn.createStatement();
            ResultSet realClient = invoiceStm.executeQuery("SELECT name FROM Clients WHERE RFC = '".concat(givClient).concat("'"));
            while(realClient.next()){
                name = realClient.getString(1);
            }
            realClient.close();
            invoiceStm.close();
            if (name.isEmpty()){
                Toolkit.getDefaultToolkit().beep();
                Alert noClient = new Alert(ERROR, "El cliente no está registrado", OK);
                noClient.initOwner(primaryStage);
                noClient.showAndWait();
            }
            else {
                Alert invoiceAlert = new Alert(CONFIRMATION, "La factura se generará al siguiente nombre:".concat(System.lineSeparator()).concat(name), OK, CANCEL);
                invoiceAlert.initOwner(primaryStage);
                invoiceAlert.getDialogPane().setPrefSize(400, 300);
                Optional <ButtonType> result = invoiceAlert.showAndWait();
                if (result.get() == OK) { // If the name of the client is correctly written
                    Statement genInvoice = conn.createStatement();
                    for (Product product : products) { // Add all the products to the day sales table.
                        // A mechanism to produce the invoices is missing, since the administrative requirements were out of the scope of this project
                        genInvoice.executeUpdate("INSERT INTO DaySales VALUES ('".concat(product.getId()).concat("' , '").concat(product.getAmount()).concat("' , '").concat(paymentForm.getText()).concat("' , ").concat("true").concat(", '").concat(rfcInput.getText()).concat("')"));
                    }
                    genInvoice.closeOnCompletion();
                } else if (result.get() == CANCEL) { // If the user cancels the invoice
                    Alert cancelInvoice = new Alert(CONFIRMATION, "La factura fue cancelada", OK);
                    cancelInvoice.initOwner(primaryStage); // Show an alert
                    cancelInvoice.showAndWait();
                    for (Product product : products) {
                        Statement daySale = conn.createStatement();// Add the sold products to the database
                        ResultSet alreadySold = daySale.executeQuery("SELECT sold FROM DaySales WHERE invoice = false AND id = '".concat(product.getId().concat("'")));
                        String amountSoldString = "";
                        while (alreadySold.next()) { // If the product was already sold, then just increase the amount sold
                            // This cannot be done for the invoice situation, because we must store the rfc of the client
                            amountSoldString = alreadySold.getString(1);
                        }
                        if (amountSoldString.isEmpty() == false && Double.valueOf(amountSoldString) > 0) {
                            daySale.executeUpdate("UDPATE DaySales SET sold = ".concat(String.valueOf(parseDouble(amountSoldString) + parseDouble(product.getAmount()))).concat(" WHERE id = '").concat(product.getId()).concat("'"));
                        } else {
                            daySale.executeUpdate("INSERT INTO DaySales VALUES ('".concat(product.getId()).concat("' , '").concat(product.getAmount()).concat("' , '").concat(paymentForm.getText()).concat("' , ").concat("false").concat(", ").concat("NULL").concat(")"));
                        }
                    }
                } else { // If the user closes the dialog of the invoice creation, perform the necessary operations to add the sold product to the database
                    for (Product product : products) {
                        Statement daySale = conn.createStatement();
                        ResultSet alreadySold = daySale.executeQuery("SELECT sold FROM DaySales WHERE invoice = false AND id = '".concat(product.getId().concat("'")));
                        String amountSoldString = "";
                        while (alreadySold.next()) {
                            amountSoldString = alreadySold.getString(1);
                        }
                        if (Double.valueOf(amountSoldString) > 0) {
                            daySale.executeUpdate("UDPATE DaySales SET sold = ".concat(String.valueOf(parseDouble(amountSoldString) + parseDouble(product.getAmount()))).concat(" WHERE id = '").concat(product.getId()).concat("'"));
                        } else {
                            daySale.executeUpdate("INSERT INTO DaySales VALUES ('".concat(product.getId()).concat("' , '").concat(product.getAmount()).concat("' , '").concat(paymentForm.getText()).concat("' , ").concat("false").concat(", ").concat("NULL").concat(")"));
                        }
                    }
                }
            }
        }

        Statement tool = conn.createStatement();
        ResultSet tool2 = tool.executeQuery("SELECT id, sold, rfc FROM DaySales");
        while(tool2.next()){
            String id1 = "";
            String sold1 = "";
            String rfc1 = "";
            id1 = tool2.getString(1);
            sold1 = tool2.getString(2);
            rfc1 = tool2.getString(3);
            Product product1 = new Product(); // Retrieve the sold products from the database and add them to the sold products observable list.
            product1.setId(id1);
            product1.setExistence(sold1);
            product1.setRfc(rfc1);
            soldProductList.add(product1);
        }

        rfcInput.setText(""); // Clean the text fields.
        charge.close();
        products.clear();
        totalField.setText("  ");
    }

    // When the user selects cash as payment method
    public static void cashClick(MenuButton menu){
        menu.setText("Efectivo");
    }

    // When the user selects card as payment method
    public static void cardClick(MenuButton menu){
        menu.setText("Débito");
    }

    /**
     *This method adds new clients to the GlafiraPharmacy database, to the Clients table. It takes the input of the user in the
     * graphical interface.
     * @param primaryStage The primary stage of the application
     * @param conn The connection to the database
     * @param name The text field containing the name of the client
     * @param address The textfield containing the address of the client
     * @param mayoralty The textfield containing the mayoralty of the client
     * @param cp The textfield containing the cp of the client
     * @param rfc The textfield containing the rfc of the client
     * @param mail The textfield containing the mail of the client
     * @throws SQLException
     */
    public static void newClientButtonClick(Stage primaryStage, Connection conn, TextField name, TextField address, TextField mayoralty, TextField cp, TextField rfc, TextField mail, ObservableList<Client> clientList) throws SQLException {
        String name1 = name.getText(); // Getting the text
        if(name1.length() < 5){ // Validating the length
            Alert nullName = new Alert(ERROR, "Nombre no válido", OK);
            nullName.initOwner(primaryStage);
            Toolkit.getDefaultToolkit().beep();
            nullName.showAndWait();
            return;
        }
        String address1 = address.getText();
        if(address1.length() < 5){ // Validating the address
            Alert nullAddress = new Alert(ERROR, "Dirección no válida", OK);
            nullAddress.initOwner(primaryStage);
            Toolkit.getDefaultToolkit().beep();
            nullAddress.showAndWait();
            return;
        }
        String mayoralty1 = mayoralty.getText();
        if(mayoralty1.length() < 5){ // Validating the mayoralty
            Alert nullMayoralty = new Alert(ERROR, "Delegación no válida", OK);
            nullMayoralty.initOwner(primaryStage);
            Toolkit.getDefaultToolkit().beep();
            nullMayoralty.showAndWait();
            return;
        }
        String cp1 = cp.getText();
        if(cp1.length() != 5){ // Validating the CP
            Alert nullCp = new Alert(ERROR, "Código postal no válido", OK);
            nullCp.initOwner(primaryStage);
            Toolkit.getDefaultToolkit().beep();
            nullCp.showAndWait();
            return;
        }
        String rfc1 = rfc.getText();
        if(rfc1.length() > 15){ // Validating the rfc
            Alert nullRfc = new Alert(ERROR, "R.F.C. no válido", OK);
            nullRfc.initOwner(primaryStage);
            Toolkit.getDefaultToolkit().beep();
            nullRfc.showAndWait();
            return;
        }
        String mail1 = mail.getText();
        if(mail1.length() < 5){ // Validating the mail
            Alert nullMail = new Alert(ERROR, "Correo no válido", OK);
            nullMail.initOwner(primaryStage);
            Toolkit.getDefaultToolkit().beep();
            nullMail.showAndWait();
            return;
        }

        Statement clientStm = conn.createStatement(); // Creating a connection to the database and adding the new client
        clientStm.executeUpdate("INSERT INTO Clients VALUES ('".concat(name1).concat("','").concat(address1).concat("','").concat(mayoralty1).concat("','").concat(cp1).concat("','").concat(rfc1).concat("','").concat(mail1).concat("')"));
        name.setText(""); //Cleaning the textboxes
        address.setText("");
        mayoralty.setText("");
        cp.setText("");
        rfc.setText("");
        mail.setText("");
        clientStm.closeOnCompletion();

        Client client = new Client(); // Crearing the new client object
        client.setRfc(rfc1);
        client.setName(name1);
        client.setMail(mail1);
        client.setCp(cp1);
        clientList.add(client); //Adding the new client to the observable list

        Alert nullPas = new Alert(INFORMATION, "El cliente fue agregado a la base de datos", OK);
        nullPas.initOwner(primaryStage); // Showing a confirmation message
        nullPas.showAndWait();

    }

    /**
     * This method deletes a client from the clients database given a valid rfc.
     * @param clientList observable list of clients
     * @param conn connection to the database
     * @param rfcDelete textfield contaning the rfc of the client to be deleted
     * @param primaryStage the primary stage of the application
     * @throws SQLException
     */
    public static void deleteClient(ObservableList<Client> clientList, Connection conn, TextField rfcDelete, Stage primaryStage) throws SQLException {
        String rfcString = rfcDelete.getText();
        if (rfcString.isEmpty()){ // If the rfc textfield is empty
            Alert error = new Alert(ERROR, "No has seleccionado un cliente válido", OK);
            error.initOwner(primaryStage);
            error.showAndWait();
        }
        else {
            Statement deleteStm = conn.createStatement(); // If the rfc text field is not empty, then delete the client with that rfc from the database
            deleteStm.executeUpdate("DELETE FROM Clients WHERE rfc  = '".concat(rfcString).concat("'"));
            for (Client client : clientList) {
                if (client.getRfc().equals(rfcString)) {
                    clientList.remove(client); // Delete the client from the observable list
                }
            }
            rfcDelete.setText(""); // COnfirm that the client was deleted
            Alert confirmation = new Alert(CONFIRMATION, "The client was removed from the database", OK);
            confirmation.initOwner(primaryStage);
            confirmation.showAndWait();
        }
    }

    /**
     * This method adds a product to the inventory
     * @param id id of the product (it is not designed to autoincrement, but rather to be the barcode in digits)
     * @param description Description of the product
     * @param price Price of the product
     * @param existence The initial sotck
     * @param products The observable list of products
     * @param conn The connection to the database
     * @param primaryStage The primary stage of the application
     * @throws SQLException
     */
    public static void addProductInventoryClick(TextField id, TextField description, TextField price, TextField existence, ObservableList<Product> products, Connection conn, Stage primaryStage ) throws SQLException {
        String idS = id.getText(); // Retrieving values from the database
        String descriptionS = description.getText();
        String priceS = price.getText();
        String existenceS = existence.getText();

        if (idS.isEmpty()||descriptionS.isEmpty()||priceS.isEmpty()||existenceS.isEmpty()){ // If any field is empty, display and error
            Alert error = new Alert(ERROR, "NLos datos no son válidos", OK);
            error.initOwner(primaryStage);
            error.showAndWait();
        }else {
            Statement addStm = conn.createStatement(); // Else, add the product to the database
            addStm.executeUpdate("INSERT INTO Products VALUES ( '".concat(idS).concat("','").concat(descriptionS).concat(" ', '").concat(priceS).concat("' , '").concat(existenceS).concat("' , NULL, NULL, NULL)"));
            Alert confirmation = new Alert(CONFIRMATION, "EL producto fue agregado", OK); // Show a confirmation
            confirmation.initOwner(primaryStage);
            confirmation.showAndWait();

            Product product = new Product(); // Create a new product object
            product.setDescription(descriptionS);
            product.setPrice(priceS);
            product.setExistence(existenceS);
            product.setId(idS);
            products.add(product); // Add the product to the database
            id.setText(""); // Clean the text fields
            description.setText("");
            price.setText("");
            existence.setText("");
        }
    }

    /**
     * THis method deletes products from the database given a valid id
     * @param id The id of the product to be deleted
     * @param productList The observable list of products
     * @param conn The connection to the database
     * @param primaryStage The primary stage of the application
     * @throws SQLException
     */
    public static void deleteProductClick(TextField id, ObservableList<Product> productList, Connection conn, Stage primaryStage) throws SQLException {
        String idS = id.getText();
        if (idS.isEmpty()){ // If the id field is empty
            Alert error = new Alert(ERROR, "No has seleccionado un producto válido", OK);
            error.initOwner(primaryStage);
            error.showAndWait();
        }
        else {
            Statement del = conn.createStatement(); // If the field is not empty, delete the product with the given ID from the database
            del.executeUpdate("DELETE FROM Products WHERE id = '".concat(idS).concat("'"));
            for (Product product : productList) {
                if (product.getId().equals(idS)) { // Also delete the product from the observable list
                    productList.remove(product);
                }
            }
            Alert confirmation = new Alert(CONFIRMATION, "El producto ha sido eliminado", OK);
            confirmation.initOwner(primaryStage); // Display a confirmation alert
            confirmation.showAndWait();
        }
    }

    /**
     * This method manages and increase in the stock of a product given its id and the amount added
     * @param idField id of the product text field
     * @param amountField amount added text field
     * @param conn Connection to the database
     * @param primaryStage Primary Stage of the application
     * @param productList List of products
     * @throws SQLException
     */
    public static void updateExistence(TextField idField, TextField amountField, Connection conn, Stage primaryStage, ObservableList<Product> productList) throws SQLException {
        String id = idField.getText();
        Integer amount = Integer.valueOf(amountField.getText());
        if (amount > 0 || id.isEmpty() == false){ // If the amount and id are valid
            Statement updateStm = conn.createStatement(); // get the current stock
            ResultSet updateRs = updateStm.executeQuery("SELECT existence FROM Products WHERE id = '".concat(id).concat("'"));
            int stock = 0;
            while (updateRs.next()){
                stock = Integer.valueOf(updateRs.getString(1));
            }
            stock = stock + amount; // Add the amount given to the stock and update the value in the database
            updateStm.executeUpdate("UPDATE Products SET existence = ".concat(String.valueOf(stock)).concat(" WHERE id = '".concat(id).concat("'")));
            Alert confirmation = new Alert(CONFIRMATION, "El stock fue agregado", OK); // Show a confirmation message
            confirmation.initOwner(primaryStage);
            confirmation.showAndWait();

            for (Product product : productList){ //Modify the value of the Product object from the observable list
                if (product.getId().equals(id)){
                    product.setExistence(String.valueOf(stock));
                }
            }
        }
        else{ // If the parameters are not valid, display an error alert
            Alert error = new Alert(ERROR, "Datos incorrectos", OK);
            error.initOwner(primaryStage);
            error.showAndWait();
        }
    }

    /**
     * This method ends the day by deleting the day sales table and creating a new one, to be used to register the products from the next day
     * @param productList Observable list of products
     * @param conn Connection to the database
     * @throws SQLException
     */
    public static void endDay(ObservableList<Product> productList, Connection conn) throws SQLException {
        Statement endStm = conn.createStatement();
        endStm.executeUpdate("DROP TABLE DaySales"); // delete the table and create a new one.
        endStm.executeUpdate("CREATE TABLE DaySales (id VARCHAR(50), sold INT(3), payment VARCHAR(20), invoice boolean, rfc VARCHAR(14))");
        endStm.closeOnCompletion();
        productList.clear();
    }
}