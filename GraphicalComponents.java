package GraphicalInterface;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.sql.*;
import static javafx.scene.input.KeyCode.ENTER;

public class GraphicalComponents extends Application {

    public static Connection conn; // To store the connection to the database

    public static void main(String[] args) throws SQLException{
        // Connecting to a database created to showcase the functionality of the program. No real information is stored there.
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/GlafiraPharmacy?serverTimezone=UTC","glafiraPharmacy", "g1@QHm&?" );
        Application.launch(args); //In order to run the JavaFX Application
    }

    @Override
    /**
     * Overrides the default start method in order to create the necessary scenes in the program. Firs, ll the graphical interface
     * are created, and then the event handlers are added.
     * @param primaryStage
     */
    public void start(Stage primaryStage) throws SQLException{

            // CREATING THE INTERFACE OF THE LOG IN SCENE ****************************************************************

            Label usernameLabel = new Label("Usuario:"); // Creating the objects
            Label password = new Label("Contraseña:");
            TextField usernameInput = new TextField();
            PasswordField passwordInput = new PasswordField();
            Button logInBtn = new Button("Entrar");
            logInBtn.setId("logIn");
            AnchorPane logInParent = new AnchorPane(usernameLabel, password, usernameInput, passwordInput, logInBtn);

            AnchorPane.setTopAnchor(usernameLabel, 60d ); // Setting their positions
            AnchorPane.setLeftAnchor(usernameLabel, 85d);
            AnchorPane.setTopAnchor(password, 100d);
            AnchorPane.setLeftAnchor(password, 50d);
            AnchorPane.setTopAnchor(usernameInput, 60d);
            AnchorPane.setRightAnchor(usernameInput, 65d);
            AnchorPane.setTopAnchor(passwordInput, 100d);
            AnchorPane.setRightAnchor(passwordInput, 65d);
            AnchorPane.setBottomAnchor(logInBtn, 20d);
            AnchorPane.setRightAnchor(logInBtn, 50d);

            Scene logInScene = new Scene(logInParent, 400, 200); // Creating the scene and loading the css stylesheet
            logInScene.getStylesheets().add(getClass().getResource("LogIn.css").toExternalForm());

            // Setting this scene in the primary stage, since it is the first one that should be displayed when the application starts.
            primaryStage.setTitle("Entrar al sistema");
            primaryStage.setScene(logInScene);
            primaryStage.show();


            // CREATING THE STAFF SCENE ***********************************************************************************

            // Creating the staffHeader

            Rectangle saleMenuStaffRectangle = new Rectangle(375, 235); // Creating all the graphical objects
            // An id is set for the graphical elements that must be displayed with a different format, and it is used in the
            saleMenuStaffRectangle.setId("saleMenuStaffRectangle"); // CSS stylesheet.
            Rectangle newClientStaffRectangle = new Rectangle(375, 425);
            newClientStaffRectangle.setId("newClientStaffRectangle");

            Rectangle staffSalesRectangle = new Rectangle(1000, 790);
            staffSalesRectangle.setId("staffSalesRectangle");

            Label uPos = new Label("UPOS");
            uPos.setId("uPos");
            Label pharmacy = new Label("Farmacia Glafira");
            pharmacy.setId("pharmacy");
            Image usrImg = new Image("https://www.qualiscare.com/wp-content/uploads/2017/08/default-user.png", true);
            ImageView usrView = new ImageView(usrImg);
            usrView.setFitHeight(25);
            usrView.setFitWidth(25);
            Statement userStm = conn.createStatement();
            ResultSet userRes = userStm.executeQuery("SELECT username FROM Users WHERE status = 'staff'");
            String username = "";
            while(userRes.next()){
                username = userRes.getString(1);
            }
            MenuButton mainStaffMenu = new MenuButton( username, usrView);
            mainStaffMenu.setId("mainStaffMenu");
            MenuItem close = new MenuItem("Salir");
            MenuItem changeUser = new MenuItem("Cambiar de Usuario");
            mainStaffMenu.getItems().add(changeUser);
            mainStaffMenu.getItems().add(close);

            GridPane staffHeader = new GridPane(); // Creating the grid pane for the header and adding the objects
            staffHeader.getColumnConstraints().add(new ColumnConstraints(20));
            staffHeader.getColumnConstraints().add(new ColumnConstraints(200));
            staffHeader.getColumnConstraints().add(new ColumnConstraints(280));
            staffHeader.getColumnConstraints().add(new ColumnConstraints(400));
            staffHeader.getColumnConstraints().add(new ColumnConstraints(280));
            staffHeader.getColumnConstraints().add(new ColumnConstraints(200));
            staffHeader.getColumnConstraints().add(new ColumnConstraints(18));
            staffHeader.getRowConstraints().add(new RowConstraints(50));
            staffHeader.add(uPos, 1, 0);
            staffHeader.add(pharmacy, 3, 0);
            staffHeader.add(mainStaffMenu, 5, 0);
            GridPane.setHalignment(uPos, HPos.CENTER);
            GridPane.setHalignment(pharmacy, HPos.CENTER);
            GridPane.setHalignment(mainStaffMenu, HPos.CENTER);

            staffHeader.setGridLinesVisible(false);
            staffHeader.setId("staffHeader");
            userStm.closeOnCompletion();

            // Creating the staffSalesMenu

            CheckBox invoiceTool = new CheckBox();  //Creating the graphical objects
            Label invoice = new Label("Factura");
            Label clientInput = new Label("R.F.C.:");
            TextField client = new TextField();

            Label payment = new Label("Pago:");
            MenuButton paymentForm = new MenuButton("Seleccionar..."); // Creating a dropdown menu to select payment form
            MenuItem cash = new MenuItem("Efectivo");
            MenuItem card = new MenuItem("Débito");

            paymentForm.setMinWidth(225);
            paymentForm.getItems().add(cash);
            paymentForm.getItems().add(card);

            Button charge = new Button("Finalizar compra");
            charge.setId("charge");
            Label newClientLabelStaff = new Label("Nuevo cliente");
            Button newClientButtonStaff = new Button("Agregar cliente");
            newClientButtonStaff.setId("newClientButtonStaff");

            Label nameLabelStaff = new Label("Nombre:");
            Label directLabelStaff = new Label("Dirección:");
            Label delegLabelStaff = new Label("Deleg.:");
            Label cpLabelStaff = new Label("C.P.:");
            Label rfcLabelStaff = new Label("R.F.C.:");
            Label mailLabelStaff = new Label("Correo: ");

            TextField nameFieldStaff = new TextField();
            TextField directFieldStaff = new TextField();
            TextField delegFieldStaff = new TextField();
            TextField cpFieldStaff = new TextField();
            TextField rfcFieldStaff = new TextField();
            TextField mailFieldStaff = new TextField();

            GridPane staffSalesMenu = new GridPane(); // Creating the gridpane, setting the sizes and adding the elements.
                                                      // The constraints were calculated by using a drawing of the desired
                                                      // interface and dividing both the width and length as appropriate.
                                                      // They constraints were used as "slots" to place the objects.

            staffSalesMenu.getColumnConstraints().add(new ColumnConstraints(25)); // Column constraints
            staffSalesMenu.getColumnConstraints().add(new ColumnConstraints(25));
            staffSalesMenu.getColumnConstraints().add(new ColumnConstraints(75));
            staffSalesMenu.getColumnConstraints().add(new ColumnConstraints(225));
            staffSalesMenu.getColumnConstraints().add(new ColumnConstraints(25));

            staffSalesMenu.getRowConstraints().add(new RowConstraints(25)); // Row constraints
            staffSalesMenu.getRowConstraints().add(new RowConstraints(50));
            staffSalesMenu.getRowConstraints().add(new RowConstraints(50));
            staffSalesMenu.getRowConstraints().add(new RowConstraints(50));
            staffSalesMenu.getRowConstraints().add(new RowConstraints(50));
            staffSalesMenu.getRowConstraints().add(new RowConstraints(55));
            staffSalesMenu.getRowConstraints().add(new RowConstraints(50));
            staffSalesMenu.getRowConstraints().add(new RowConstraints(45));
            staffSalesMenu.getRowConstraints().add(new RowConstraints(50));
            staffSalesMenu.getRowConstraints().add(new RowConstraints(50));
            staffSalesMenu.getRowConstraints().add(new RowConstraints(50));
            staffSalesMenu.getRowConstraints().add(new RowConstraints(50));
            staffSalesMenu.getRowConstraints().add(new RowConstraints(50));
            staffSalesMenu.getRowConstraints().add(new RowConstraints(50));
            staffSalesMenu.getRowConstraints().add(new RowConstraints(50));
            staffSalesMenu.getRowConstraints().add(new RowConstraints(50));
            staffSalesMenu.getRowConstraints().add(new RowConstraints(25));

            staffSalesMenu.add(invoiceTool, 1, 1 ); // Adding the elements, some of the occupy more than one row or column
            staffSalesMenu.add(invoice, 2, 1, 2, 1 );
            staffSalesMenu.add(clientInput, 1, 2, 2, 1);
            staffSalesMenu.add(client, 3,2, 1, 1);
            staffSalesMenu.add(payment, 1, 3, 2, 1);
            staffSalesMenu.add(paymentForm, 3, 3, 2, 1 );
            staffSalesMenu.add(charge, 1, 4, 3, 1);
            staffSalesMenu.add(newClientLabelStaff, 1, 8, 3, 1);
            staffSalesMenu.add(nameLabelStaff, 1, 9, 2, 1);
            staffSalesMenu.add(nameFieldStaff, 3, 9);
            staffSalesMenu.add(directLabelStaff, 1, 10, 2, 1);
            staffSalesMenu.add(directFieldStaff, 3, 10);
            staffSalesMenu.add(delegLabelStaff, 1, 11, 2, 1);
            staffSalesMenu.add(delegFieldStaff, 3, 11);
            staffSalesMenu.add(cpLabelStaff, 1, 12, 2, 1);
            staffSalesMenu.add(cpFieldStaff, 3, 12);
            staffSalesMenu.add(rfcLabelStaff, 1, 13, 2, 1);
            staffSalesMenu.add(rfcFieldStaff, 3, 13);
            staffSalesMenu.add(mailLabelStaff, 1, 14, 2, 1);
            staffSalesMenu.add(mailFieldStaff, 3, 14);
            staffSalesMenu.add(newClientButtonStaff, 1, 15, 3, 1);

            GridPane.setHalignment(charge, HPos.CENTER);
            GridPane.setHalignment(newClientButtonStaff, HPos.CENTER);
            GridPane.setHalignment(newClientLabelStaff, HPos.CENTER);

            staffSalesMenu.setGridLinesVisible(false);

            //Creating the staff Sales GridPane

            Label idLabel = new Label("ID:"); // Creating the graphical objects
            idLabel.setId("idLabel");
            TextField id = new TextField();
            Button add = new Button("Agregar producto");
            add.setId("add");

            TableView<Product> sale = new TableView<Product>(); // The table to display the products in the cart
            ObservableList<Product> products = FXCollections.observableArrayList(); // An observable list of products to display their attributes
            sale.setItems(products); // Setting the contents of the observable list in the table view

            TableColumn<Product, String> number = new TableColumn("Cantidad"); // Creating the columns
            number.setCellValueFactory(new PropertyValueFactory("amount")); // Setting the attribute of Product class to display
            number.setMinWidth(125);

            TableColumn<Product, String> idSale = new TableColumn("ID");
            idSale.setCellValueFactory(new PropertyValueFactory("id"));
            idSale.setMinWidth(350);

            TableColumn<Product, String> description = new TableColumn("Descripción");
            description.setCellValueFactory(new PropertyValueFactory("description"));
            description.setMinWidth(350);

            TableColumn<Product, String> price = new TableColumn("Precio");
            price.setCellValueFactory(new PropertyValueFactory("price"));
            price.setMinWidth(125);

            sale.getColumns().setAll(number, idSale, description, price); // Add the columns to the observable list

            Label totalLabel = new Label("Total:  ");
            totalLabel.setId("totalLabel");
            TextField totalField = new TextField();
            totalField.setId("totalField");
            totalField.setMinHeight(50);
            totalField.setEditable(false);

            GridPane staffSales = new GridPane(); // Creating the grid pane

            staffSales.getColumnConstraints().add(new ColumnConstraints(25)); // Column constraints
            staffSales.getColumnConstraints().add(new ColumnConstraints(50));
            staffSales.getColumnConstraints().add(new ColumnConstraints(250));
            staffSales.getColumnConstraints().add(new ColumnConstraints(75));
            staffSales.getColumnConstraints().add(new ColumnConstraints(150));
            staffSales.getColumnConstraints().add(new ColumnConstraints(175));
            staffSales.getColumnConstraints().add(new ColumnConstraints(100));
            staffSales.getColumnConstraints().add(new ColumnConstraints(150));
            staffSales.getColumnConstraints().add(new ColumnConstraints(25));

            staffSales.getRowConstraints().add(new RowConstraints(25)); // Row constraints
            staffSales.getRowConstraints().add(new RowConstraints(50));
            staffSales.getRowConstraints().add(new RowConstraints(25));
            staffSales.getRowConstraints().add(new RowConstraints(600));
            staffSales.getRowConstraints().add(new RowConstraints(25));
            staffSales.getRowConstraints().add(new RowConstraints(50));
            staffSales.getRowConstraints().add(new RowConstraints(25));

            staffSales.add(idLabel, 1, 1); //Adding the graphical objects
            staffSales.add(id, 2, 1, 2, 1);
            staffSales.add(add, 4, 1, 2, 1);
            staffSales.add(sale, 1, 3, 7, 1);
            staffSales.add(totalLabel, 6, 5);
            staffSales.add(totalField, 7, 5);

            GridPane.setHalignment(add, HPos.CENTER);
            GridPane.setHalignment(totalLabel, HPos.RIGHT);
            staffSales.setGridLinesVisible(false);


            //Creating the  Staff anchorPane (parent). It will contain the header, sales table, and sales menu, in order to keep them at a fixed position.

            AnchorPane staffParent = new AnchorPane( saleMenuStaffRectangle, newClientStaffRectangle, staffSalesRectangle, staffHeader, staffSalesMenu, staffSales);
            staffParent.setPrefSize(1440, 900);

            AnchorPane.setLeftAnchor(staffSalesMenu, 15d); // Setting the anchors
            AnchorPane.setTopAnchor(staffSalesMenu, 75d);
            AnchorPane.setTopAnchor(staffHeader, 15d);
            AnchorPane.setLeftAnchor(staffHeader, 15d);
            AnchorPane.setTopAnchor(saleMenuStaffRectangle, 85d);
            AnchorPane.setLeftAnchor(saleMenuStaffRectangle,15d);
            AnchorPane.setTopAnchor(newClientStaffRectangle, 450d);
            AnchorPane.setLeftAnchor(newClientStaffRectangle,15d);
            AnchorPane.setRightAnchor(staffSales, 25d);
            AnchorPane.setTopAnchor(staffSales, 75d);
            AnchorPane.setTopAnchor(staffSalesRectangle, 85d);
            AnchorPane.setRightAnchor(staffSalesRectangle, 25d);

            Scene staffView = new Scene(staffParent);
            staffView.getStylesheets().add(getClass().getResource("staffScene.css").toExternalForm()); // Using the css stylesheet.

            // CREATING THE ADMIN SCENE **********************************************************************************


            // Creating the admin header

            Label uPosAdmin = new Label("UPOS"); // Creating all the graphical objects
            uPosAdmin.setId("uPosAdmin");
            Label pharmacyAdmin = new Label("Farmacia Glafira");
            pharmacyAdmin.setId("pharmacyAdmin");
            Image usrImgAdmin = new Image("https://www.qualiscare.com/wp-content/uploads/2017/08/default-user.png", true);
            ImageView usrViewAdmin = new ImageView(usrImgAdmin);
            usrViewAdmin.setFitHeight(25);
            usrViewAdmin.setFitWidth(25);
            Statement userStmAdmin = conn.createStatement(); // Setting the username
            ResultSet userResAdmin = userStmAdmin.executeQuery("SELECT username FROM Users WHERE status = 'admin'");
            String usernameAdmin = "";
            while(userResAdmin.next()){
                    usernameAdmin = userResAdmin.getString(1);
            }
            MenuButton mainAdminMenu = new MenuButton( usernameAdmin, usrView);
            mainAdminMenu.setId("mainAdminMenu");
            MenuItem closeAdmin = new MenuItem("Salir");
            MenuItem changeUserAdmin = new MenuItem("Cambiar de Usuario");
            mainAdminMenu.getItems().add(changeUserAdmin);
            mainAdminMenu.getItems().add(closeAdmin);

            GridPane adminHeader = new GridPane(); // Creating the header grid pane and setting the constraints.
            adminHeader.getColumnConstraints().add(new ColumnConstraints(20));
            adminHeader.getColumnConstraints().add(new ColumnConstraints(200));
            adminHeader.getColumnConstraints().add(new ColumnConstraints(280));
            adminHeader.getColumnConstraints().add(new ColumnConstraints(400));
            adminHeader.getColumnConstraints().add(new ColumnConstraints(280));
            adminHeader.getColumnConstraints().add(new ColumnConstraints(200));
            adminHeader.getColumnConstraints().add(new ColumnConstraints(18));
            adminHeader.getRowConstraints().add(new RowConstraints(50));

            adminHeader.add(uPosAdmin, 1, 0); //Adding the elements
            adminHeader.add(pharmacyAdmin, 3, 0);
            adminHeader.add(mainAdminMenu, 5, 0);
            GridPane.setHalignment(uPosAdmin, HPos.CENTER);
            GridPane.setHalignment(pharmacyAdmin, HPos.CENTER);
            GridPane.setHalignment(mainAdminMenu, HPos.CENTER);
            adminHeader.setGridLinesVisible(false);
            adminHeader.setId("adminHeader"); //The id is used to identify this particular section in the css stylesheet
            userStmAdmin.closeOnCompletion();

            // Creating the tab pane for the admin scene ***************************************************************
            // The admin scene includes different functionalities such as adding, modifying and removing clients and products, performing
            // a sale, viewing the list of sold products. Therefore, a tab pane is created in order to display different menus and
            // tables in each tab.

            TabPane adminTabPane = new TabPane(); // The tab pane
            adminTabPane.setPrefSize(1440d, 900d); // TODO modify the tab pane on the anchor pane.

            // Creating the tab to execute sales as admin ---------------------------------- TAB 1
            Tab sales = new Tab();
            sales.setText("Ventas");

            Label idLabelAdmin = new Label("ID:"); // Creating the graphical objects and setting ids when necessary
            idLabelAdmin.setId("idLabelAdmin");
            TextField idAdmin = new TextField();
            Button addAdmin = new Button("Agregar");
            addAdmin.setId("addAdmin");
            Label totalLabelAdmin = new Label("Total:  ");
            totalLabelAdmin.setId("totalLabelAdmin");
            TextField totalFieldAdmin = new TextField();
            totalFieldAdmin.setId("totalFieldAdmin");
            totalFieldAdmin.setMinHeight(50);
            totalFieldAdmin.setEditable(false);

            TableView<Product> saleAdmin = new TableView<Product>(); // Creating a table to display the products added to the cart
            ObservableList<Product> productsAdmin = FXCollections.observableArrayList(); // An observable list to store the products
            saleAdmin.setItems(productsAdmin); // Setting the list elements as the content of the table

            TableColumn<Product, String> numberAdmin = new TableColumn("Cantidad"); // Creating the columns and setting the Product attribute to display
            numberAdmin.setCellValueFactory(new PropertyValueFactory("amount"));
            numberAdmin.setMinWidth(200);

            TableColumn<Product, String> idSaleAdmin = new TableColumn("ID");
            idSaleAdmin.setCellValueFactory(new PropertyValueFactory("id"));
            idSaleAdmin.setMinWidth(200);

            TableColumn<Product, String> descriptionAdmin = new TableColumn("Descripción");
            descriptionAdmin.setCellValueFactory(new PropertyValueFactory("description"));
            descriptionAdmin.setMinWidth(350);

            TableColumn<Product, String> priceAdmin = new TableColumn("Precio");
            priceAdmin.setCellValueFactory(new PropertyValueFactory("price"));
            priceAdmin.setMinWidth(150);

            saleAdmin.getColumns().setAll(numberAdmin, idSaleAdmin, descriptionAdmin, priceAdmin); // Adding the columns to the table

            GridPane adminSales = new GridPane(); // Creting the gridpane for the sales tab

            adminSales.getColumnConstraints().add(new ColumnConstraints(25)); // Setting columns and row constraints
            adminSales.getColumnConstraints().add(new ColumnConstraints(50));
            adminSales.getColumnConstraints().add(new ColumnConstraints(250));
            adminSales.getColumnConstraints().add(new ColumnConstraints(75));
            adminSales.getColumnConstraints().add(new ColumnConstraints(150));
            adminSales.getColumnConstraints().add(new ColumnConstraints(175));
            adminSales.getColumnConstraints().add(new ColumnConstraints(100));
            adminSales.getColumnConstraints().add(new ColumnConstraints(150));
            adminSales.getColumnConstraints().add(new ColumnConstraints(25));

            adminSales.getRowConstraints().add(new RowConstraints(25));
            adminSales.getRowConstraints().add(new RowConstraints(50));
            adminSales.getRowConstraints().add(new RowConstraints(25));
            adminSales.getRowConstraints().add(new RowConstraints(550));
            adminSales.getRowConstraints().add(new RowConstraints(25));
            adminSales.getRowConstraints().add(new RowConstraints(50));
            adminSales.getRowConstraints().add(new RowConstraints(25));

            adminSales.add(idLabelAdmin, 1, 1); // Adding the elements to the grid pane adn customizing some elements
            adminSales.add(idAdmin, 2, 1, 2, 1);
            adminSales.add(addAdmin, 4, 1, 2, 1);
            adminSales.add(saleAdmin, 1, 3, 7, 1);
            adminSales.add(totalLabelAdmin, 6, 5);
            adminSales.add(totalFieldAdmin, 7, 5);

            GridPane.setHalignment(addAdmin, HPos.CENTER);
            GridPane.setHalignment(totalLabelAdmin, HPos.RIGHT);

            adminSales.setGridLinesVisible(false);

            // Creating the menu for the sales tab

            CheckBox invoiceToolAdmin = new CheckBox(); // Creating the graphical elements
            Label invoiceAdmin = new Label("Factura");
            Label clientInputAdmin = new Label("R.F.C.:");
            TextField clientAdmin = new TextField();
            //client.setMinSize(325, 40);
            Label paymentAdmin = new Label("Pago:");
            MenuButton paymentFormAdmin = new MenuButton("Seleccionar...");
            MenuItem cashAdmin = new MenuItem("Efectivo");
            MenuItem cardAdmin = new MenuItem("Débito");
            paymentFormAdmin.setMinWidth(225);
            paymentFormAdmin.getItems().add(cashAdmin);
            paymentFormAdmin.getItems().add(cardAdmin);
            Button chargeAdmin = new Button("Finalizar compra");
            chargeAdmin.setId("chargeAdmin");

            GridPane adminSalesMenu = new GridPane(); // Creating the grid Pane

            adminSalesMenu.getColumnConstraints().add(new ColumnConstraints(25)); //Setting the row and column constraints
            adminSalesMenu.getColumnConstraints().add(new ColumnConstraints(25));
            adminSalesMenu.getColumnConstraints().add(new ColumnConstraints(75));
            adminSalesMenu.getColumnConstraints().add(new ColumnConstraints(225));
            adminSalesMenu.getColumnConstraints().add(new ColumnConstraints(25));

            adminSalesMenu.getRowConstraints().add(new RowConstraints(25));
            adminSalesMenu.getRowConstraints().add(new RowConstraints(50));
            adminSalesMenu.getRowConstraints().add(new RowConstraints(50));
            adminSalesMenu.getRowConstraints().add(new RowConstraints(50));
            adminSalesMenu.getRowConstraints().add(new RowConstraints(50));
            adminSalesMenu.getRowConstraints().add(new RowConstraints(55));
            adminSalesMenu.getRowConstraints().add(new RowConstraints(50));

            adminSalesMenu.add(invoiceToolAdmin, 1, 1 ); // Adding the elements to the pane
            adminSalesMenu.add(invoiceAdmin, 2, 1, 2, 1 );
            adminSalesMenu.add(clientInputAdmin, 1, 2, 2, 1);
            adminSalesMenu.add(clientAdmin, 3,2, 1, 1);
            adminSalesMenu.add(paymentAdmin, 1, 3, 2, 1);
            adminSalesMenu.add(paymentFormAdmin, 3, 3, 2, 1 );
            adminSalesMenu.add(chargeAdmin, 1, 4, 3, 1);

            Rectangle saleMenuAdminRectangle = new Rectangle(375, 235); // Rectangles to be used as visual containers
            saleMenuAdminRectangle.setId("saleMenuAdminRectangle");

            Rectangle adminSalesRectangle = new Rectangle(1000, 790);
            adminSalesRectangle.setId("adminSalesRectangle");

            // Creating the anchor pane for the sales tab
            AnchorPane adminSalesParent = new AnchorPane( saleMenuAdminRectangle, adminSalesRectangle, adminSalesMenu, adminSales);
            adminSalesParent.setPrefSize(1440, 700);
            AnchorPane.setLeftAnchor(adminSalesMenu, 15d);
            AnchorPane.setTopAnchor(adminSalesMenu, 15d);
            AnchorPane.setRightAnchor(adminSales, 25d);
            AnchorPane.setTopAnchor(adminSales, 15d);
            AnchorPane.setLeftAnchor(saleMenuAdminRectangle, 15d);
            AnchorPane.setTopAnchor(saleMenuAdminRectangle, 15d);
            AnchorPane.setRightAnchor(adminSalesRectangle, 25d);
            AnchorPane.setTopAnchor(adminSalesRectangle, 15d);

            sales.setContent(adminSalesParent);

            // Creating the tab for the client management ----------------------------- TAB 2

            Tab clients = new Tab(); // Creating the new tab
            clients.setText("Clientes");

            // Creating the menu to add /remove clients

            Label newClientLabelAdmin = new Label("Nuevo cliente"); // Creating all the graphical elements an setting id when necessary
            newClientLabelAdmin.setId("newClient");
            Button newClientButtonAdmin = new Button("Agregar cliente");
            Label deleteRfcLabel = new Label("R.F.C.: ");
            TextField deleteClientField = new TextField();
            Button deleteClientButton = new Button("Eliminar");
            Label deleteClientLabel = new Label("Eliminar cliente");
            newClientButtonAdmin.setId("newClientButtonStaff");

            Label nameLabelAdmin = new Label("Nombre: ");
            Label directLabelAdmin = new Label("Dirección: ");
            Label delegLabelAdmin = new Label("Deleg.: ");
            Label cpLabelAdmin = new Label("C.P.: ");
            Label rfcLabelAdmin = new Label("R.F.C.: ");
            Label mailLabelAdmin = new Label("Correo: ");

            TextField nameFieldAdmin = new TextField(); // Text Fields for the user to type the data
            TextField directFieldAdmin = new TextField();
            TextField delegFieldAdmin = new TextField();
            TextField cpFieldAdmin = new TextField();
            TextField rfcFieldAdmin = new TextField();
            TextField mailFieldAdmin = new TextField();

            GridPane addClients  = new GridPane(); // Grid pane for the menu

            addClients.getColumnConstraints().add(new ColumnConstraints(25)); // Setting the constraints
            addClients.getColumnConstraints().add(new ColumnConstraints(25));
            addClients.getColumnConstraints().add(new ColumnConstraints(75));
            addClients.getColumnConstraints().add(new ColumnConstraints(225));
            addClients.getColumnConstraints().add(new ColumnConstraints(25));

            addClients.getRowConstraints().add(new RowConstraints(15));
            addClients.getRowConstraints().add(new RowConstraints(50));
            addClients.getRowConstraints().add(new RowConstraints(50));
            addClients.getRowConstraints().add(new RowConstraints(50));
            addClients.getRowConstraints().add(new RowConstraints(50));
            addClients.getRowConstraints().add(new RowConstraints(50));
            addClients.getRowConstraints().add(new RowConstraints(50));
            addClients.getRowConstraints().add(new RowConstraints(50));
            addClients.getRowConstraints().add(new RowConstraints(50));
            addClients.getRowConstraints().add(new RowConstraints(50));
            addClients.getRowConstraints().add(new RowConstraints(50));
            addClients.getRowConstraints().add(new RowConstraints(50));
            addClients.getRowConstraints().add(new RowConstraints(50));
            addClients.getRowConstraints().add(new RowConstraints(50));

            addClients.add(newClientLabelAdmin, 1, 1, 3, 1); // Adding the elements
            addClients.add(nameLabelAdmin, 1, 2, 2, 1);
            addClients.add(nameFieldAdmin, 3, 2);
            addClients.add(directLabelAdmin, 1, 3, 2, 1);
            addClients.add(directFieldAdmin, 3, 3);
            addClients.add(delegLabelAdmin, 1, 4, 2, 1);
            addClients.add(delegFieldAdmin, 3, 4);
            addClients.add(cpLabelAdmin, 1, 5, 2, 1);
            addClients.add(cpFieldAdmin, 3, 5);
            addClients.add(rfcLabelAdmin, 1, 6, 2, 1);
            addClients.add(rfcFieldAdmin, 3, 6);
            addClients.add(mailLabelAdmin, 1, 7, 2, 1);
            addClients.add(mailFieldAdmin, 3, 7);
            addClients.add(newClientButtonAdmin, 1, 8, 3, 1);
            addClients.add(deleteClientLabel, 1, 10, 3, 1);
            addClients.add(deleteRfcLabel, 1, 11, 2, 1);
            addClients.add(deleteClientField, 3, 11, 1, 1);
            addClients.add(deleteClientButton, 1,12, 3, 1 );

            GridPane.setHalignment(newClientLabelAdmin,HPos.CENTER ); // Customizing the position of some elements
            GridPane.setHalignment(newClientButtonAdmin,HPos.CENTER );
            GridPane.setHalignment(deleteClientLabel, HPos.CENTER);
            GridPane.setHalignment(deleteClientButton, HPos.CENTER);

            Rectangle recAddClientsAdmin = new Rectangle(375, 430); // Rectangles to be used as visual containers.
            recAddClientsAdmin.setId("recAddClientsAdmin");
            Rectangle recDelClientAdmin = new Rectangle(375, 200);
            recDelClientAdmin.setId("recDelClientAdmin");

            TableView<Client> clientTable = new TableView<Client>(); // Creating the table to display the information of the clients
            ObservableList<Client> clientList = FXCollections.observableArrayList(); // A list with the Client objects
            clientTable.setItems(clientList); // Setting the elements of the list as the contents of the table
            clientTable.setPrefSize(900, 700);

            TableColumn<Client, String> name = new TableColumn("Nombre"); // Creating the columns
            name.setCellValueFactory(new PropertyValueFactory("name")); // Specifying the Client attribute to be displayed on the column
            name.setMinWidth(200);

            TableColumn<Client, String> cp  = new TableColumn("C.P.");
            cp.setCellValueFactory(new PropertyValueFactory("cp"));
            cp.setMinWidth(125);

            TableColumn<Client, String> rfc = new TableColumn("R.F.C.");
            rfc.setCellValueFactory(new PropertyValueFactory("rfc"));
            rfc.setMinWidth(250);

            TableColumn<Client, String> mail = new TableColumn("Correo");
            mail.setCellValueFactory(new PropertyValueFactory("mail"));
            mail.setMinWidth(250);

            clientTable.getColumns().setAll(name, cp, rfc, mail); // Adding the columns to the table

            Statement clientsStatement = conn.createStatement(); // Retrieving the information of all the registered clients from the database
            ResultSet clientsResult = clientsStatement.executeQuery("SELECT name, cp, RFC, mail FROM Clients");

            while (clientsResult.next()){
                    String nameRs = clientsResult.getString(1);
                    String cpRs = clientsResult.getString(2);
                    String rfcRs = clientsResult.getString(3);
                    String mailRs = clientsResult.getString(4);

                    Client clientElement = new Client(); // Creating a new Client object for each row retrieved.
                    clientElement.setCp(cpRs);
                    clientElement.setMail(mailRs);
                    clientElement.setName(nameRs);
                    clientElement.setRfc(rfcRs);

                    clientList.add(clientElement); // Adding the element to the observable list, so that they are displayed.
            }

            clientsStatement.closeOnCompletion();

            // Creating the anchor pane for the Clients tab
            AnchorPane clientsParent = new AnchorPane(recDelClientAdmin, recAddClientsAdmin, clientTable, addClients);
            clientsParent.setPrefSize(1440, 750);
            AnchorPane.setTopAnchor(recAddClientsAdmin, 15d);
            AnchorPane.setLeftAnchor(recAddClientsAdmin, 15d);
            AnchorPane.setTopAnchor(recDelClientAdmin,475d);
            AnchorPane.setLeftAnchor(recDelClientAdmin,15d);
            AnchorPane.setTopAnchor(clientTable,15d);
            AnchorPane.setRightAnchor(clientTable, 30d);
            AnchorPane.setTopAnchor(addClients,15d);
            AnchorPane.setLeftAnchor(addClients,25d );

            clients.setContent(clientsParent);

            // Creating the tab for product stock management ---------------------------------- TAB 3

            Tab productTab = new Tab(); // Creating the tab
            productTab.setText("Productos");

            // Creating the product management menu
            Label newProduct = new Label("Nuevo producto");// Creating the graphical elements an setting id if necessary
            Label newIdLabel  = new Label("ID: ");
            Label newDescriptionLabel = new Label("Descripción: ");
            Label newPriceLabel = new Label("Precio: ");
            Label newExistenceLabel = new Label("Existencia: ");
            Label id2 = new Label("ID: ");
            Label id3 = new Label("ID: ");

            TextField newIdField = new TextField(); // Text fields for the user to type the information.
            TextField newDescriptionField = new TextField();
            TextField newPriceField = new TextField();
            TextField newExistenceField = new TextField();

            Button addNewProduct = new Button("Agregar producto al inventario");
            Button deleteProduct = new Button("Eliminar producto");
            TextField deleteId = new TextField();

            Label addExistence = new Label("Agregar Stock");
            TextField addExistenceId = new TextField();
            TextField addExistenceAmount = new TextField();
            Button addExistenceButton = new Button("Agregar");

            GridPane productsMenuGrid = new GridPane(); // Creating the grid pane for the sales menu

            productsMenuGrid.getColumnConstraints().add(new ColumnConstraints(25)); // Setting the constraints
            productsMenuGrid.getColumnConstraints().add(new ColumnConstraints(25));
            productsMenuGrid.getColumnConstraints().add(new ColumnConstraints(75));
            productsMenuGrid.getColumnConstraints().add(new ColumnConstraints(225));
            productsMenuGrid.getColumnConstraints().add(new ColumnConstraints(25));

            productsMenuGrid.getRowConstraints().add(new RowConstraints(15));
            productsMenuGrid.getRowConstraints().add(new RowConstraints(50));
            productsMenuGrid.getRowConstraints().add(new RowConstraints(50));
            productsMenuGrid.getRowConstraints().add(new RowConstraints(50));
            productsMenuGrid.getRowConstraints().add(new RowConstraints(50));
            productsMenuGrid.getRowConstraints().add(new RowConstraints(50));
            productsMenuGrid.getRowConstraints().add(new RowConstraints(50));
            productsMenuGrid.getRowConstraints().add(new RowConstraints(50));
            productsMenuGrid.getRowConstraints().add(new RowConstraints(50));
            productsMenuGrid.getRowConstraints().add(new RowConstraints(50));
            productsMenuGrid.getRowConstraints().add(new RowConstraints(50));
            productsMenuGrid.getRowConstraints().add(new RowConstraints(50));
            productsMenuGrid.getRowConstraints().add(new RowConstraints(50));

            productsMenuGrid.add(newProduct, 1, 1, 3, 1); // Adding the elements
            productsMenuGrid.add(newIdLabel, 1, 2, 2, 1);
            productsMenuGrid.add(newIdField, 3, 2);
            productsMenuGrid.add(newDescriptionLabel, 1, 3, 2, 1);
            productsMenuGrid.add(newDescriptionField, 3, 3);
            productsMenuGrid.add(newPriceLabel, 1, 4, 2, 1);
            productsMenuGrid.add(newPriceField, 3, 4);
            productsMenuGrid.add(newExistenceLabel, 1, 5, 2, 1);
            productsMenuGrid.add(newExistenceField, 3, 5);
            productsMenuGrid.add(addNewProduct, 1, 6, 3, 1);
            productsMenuGrid.add(deleteProduct, 1, 8, 3, 1);
            productsMenuGrid.add(id2, 1,  9, 2, 1);
            productsMenuGrid.add(deleteId, 3, 9, 1, 1);
            productsMenuGrid.add(addExistence, 1, 10, 3, 1);
            productsMenuGrid.add(addExistenceId, 3, 11, 1, 1);
            productsMenuGrid.add(id3, 1, 11, 2, 1);
            productsMenuGrid.add(addExistenceAmount, 1, 12, 2, 1);
            productsMenuGrid.add(addExistenceButton, 3, 12,1, 1 );

            // Creating the products table
            TableView<Product> productInventoryTable = new TableView<Product>(); // Table
            ObservableList<Product> productInventoryList = FXCollections.observableArrayList(); // Observable list to contain the Product objects
            productInventoryTable.setItems(productInventoryList); // Setting the elements of the list as the contents of the table
            productInventoryTable.setPrefSize(900, 700);

            Rectangle productsRec = new Rectangle(375,615); // Rectangle to be used as visual container.
            productsRec.setId("productsRec");

            TableColumn<Product, String> idSaleAdminInventory = new TableColumn("ID"); // Creating the columns
            idSaleAdminInventory.setCellValueFactory(new PropertyValueFactory("id")); // Specifying the attribute of the Product class to be displayed.
            idSaleAdminInventory.setMinWidth(300);

            TableColumn<Product, String> descriptionAdminInventory = new TableColumn("Descripción");
            descriptionAdminInventory.setCellValueFactory(new PropertyValueFactory("description"));
            descriptionAdminInventory.setMinWidth(350);

            TableColumn<Product, String> priceAdminInventory = new TableColumn("Precio");
            priceAdminInventory.setCellValueFactory(new PropertyValueFactory("price"));
            priceAdminInventory.setMinWidth(125);

            TableColumn<Product, String> existenceInventory = new TableColumn("Existencia");
            existenceInventory.setCellValueFactory(new PropertyValueFactory("existence"));
            existenceInventory.setMinWidth(125);

            // Adding the columns to the table
            productInventoryTable.getColumns().setAll(existenceInventory, idSaleAdminInventory, descriptionAdminInventory, priceAdminInventory);

            Statement productTable = conn.createStatement(); // Retrieving the information of every product from the database.
            ResultSet productsList = productTable.executeQuery("SELECT id, description, price, existence FROM Products");

            while(productsList.next()){ // For all the retrieved products
                    String idProd = productsList.getString(1);
                    String descriptionProd = productsList.getString(2);
                    String priceProd = productsList.getString(3);
                    String existenceProd = productsList.getString(4);

                    Product product = new Product(); // Create a new product object
                    product.setId(idProd);
                    product.setDescription(descriptionProd);
                    product.setPrice(priceProd);
                    product.setExistence(existenceProd);

                    productInventoryList.add(product); // Add the product to the list, so it will be be displayed.
            }

            // Creating the anchor pane of the products tab
            AnchorPane productParent = new AnchorPane(productsRec, productsMenuGrid, productInventoryTable);
            AnchorPane.setTopAnchor(productsRec, 15d);
            AnchorPane.setLeftAnchor(productsRec, 15d);
            AnchorPane.setTopAnchor(productsMenuGrid, 15d);
            AnchorPane.setLeftAnchor(productsMenuGrid, 15d);
            AnchorPane.setTopAnchor(productInventoryTable, 15d);
            AnchorPane.setRightAnchor(productInventoryTable, 15d);

            productParent.setPrefSize(1440, 750);

            productTab.setContent(productParent);
            productTable.close();

            // Creating the tab to display the products sold in a day of the clients that requested  ---------------------- TAB 4

            Tab daySalesTab = new Tab(); // Tab creation
            daySalesTab.setText("Ventas del dia");

            Button finishDay = new Button("Finalizar el día"); // Button to end the day

            TableView<Product> soldProductTable = new TableView<Product>(); // Creating the table to display the sold products
            ObservableList<Product> soldProductList = FXCollections.observableArrayList(); // Creating the list to store the Product objects
            soldProductTable.setItems(soldProductList); // Setting the elements of the list as the contents of the table
            soldProductTable.setPrefSize(900, 700);


            TableColumn<Product, String> idSold = new TableColumn("ID"); // Creating the columns of the table
            idSold.setCellValueFactory(new PropertyValueFactory("id")); // Specifying the Product attribute to display
            idSold.setMinWidth(200);


            TableColumn<Product, String> amountSold = new TableColumn("Cantidad");
            amountSold.setCellValueFactory(new PropertyValueFactory("existence"));
            amountSold.setMinWidth(125);

            TableColumn<Product, String> rfcClient = new TableColumn("RFC");
            rfcClient.setCellValueFactory(new PropertyValueFactory("rfc"));
            rfcClient.setMinWidth(150);

            soldProductTable.getColumns().setAll(amountSold, idSold, rfcClient); // Adding the columns to the table

            Statement tool = conn.createStatement(); // Retreiving the information of all the products in the database.

            ResultSet tool2 = tool.executeQuery("SELECT id, sold, rfc FROM DaySales");

            while(tool2.next()){ // For every product retrieved
                    String id1 = "";
                    String sold1 = "";
                    String rfc1 = "";
                    id1 = tool2.getString(1);
                    sold1 = tool2.getString(2);
                    rfc1 = tool2.getString(3);

                    Product product1 = new Product(); // create a new product object
                    product1.setId(id1);
                    product1.setExistence(sold1);
                    product1.setRfc(rfc1);

                    soldProductList.add(product1); // Add the product to the list
            }

            // Creating the anchor pane for the day sales tab
            AnchorPane daySalesParent = new AnchorPane(soldProductTable, finishDay);
            AnchorPane.setBottomAnchor(finishDay, 15d );
            AnchorPane.setLeftAnchor(finishDay, 650d);
            AnchorPane.setTopAnchor(soldProductTable, 15d);
            AnchorPane.setRightAnchor(soldProductTable,250d);

            daySalesTab.setContent(daySalesParent);
            tool2.close();

            // Adding all the tabs to the tab pane
            adminTabPane.getTabs().add(sales);
            adminTabPane.getTabs().add(clients);
            adminTabPane.getTabs().add(productTab);
            adminTabPane.getTabs().add(daySalesTab);

            // The tabs cannot be closed, so they are always displayed at the top
            adminTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

            //Creating the anchor pane for the whole admin scene
            AnchorPane adminParent = new AnchorPane(adminTabPane, adminHeader);
            AnchorPane.setTopAnchor(adminHeader, 10d);
            AnchorPane.setRightAnchor(adminHeader, 20d);
            AnchorPane.setTopAnchor(adminTabPane, 60d);
            AnchorPane.setRightAnchor(adminTabPane, 0d);

            Scene adminView = new Scene(adminParent); // Creating the admin scene
            adminView.getStylesheets().add(getClass().getResource("adminScene.css").toExternalForm()); // Loading the stylesheet


            //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*******************************************<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
            // EVENT HANDLERS FOR THE WHOLE GRAPHICAL INTERFACE: All the event handlers are implemented in this part of the code.
            // They are all implemented using lambda expressions, since this simplifies the process. Otherwise, functions for each
            // event handler would have to be written.


            // HANDLING THE LOG IN SCENE

            // Log in with button
            logInBtn.setOnAction(event -> {
                    try {
                            ApplicationLogic.logInClick(usernameInput, passwordInput, conn, primaryStage, adminView, staffView, mainAdminMenu, mainStaffMenu );
                    } catch (SQLException e1) {
                            e1.printStackTrace();
                    }

            });

            // Log in by pressing enter key
            logInScene.setOnKeyPressed(event -> {
                if(event.getCode().equals(ENTER)){
                    try {
                        ApplicationLogic.logInClick(usernameInput, passwordInput, conn, primaryStage, adminView, staffView, mainAdminMenu, mainStaffMenu );
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            });

            // HANDLING THE STAFF SCENE

            // Change user in the staff scene
            changeUser.setOnAction(e -> {
                try {
                    ApplicationLogic.changeUserClick(conn, primaryStage, logInScene);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            });

            // Click on the add product button in the staff scene
            add.setOnAction(e -> {
                    try {
                            ApplicationLogic.addClick(primaryStage, conn, id, totalField, products);
                    } catch (SQLException e1) {
                            e1.printStackTrace();
                    }
            });

            // Click on the new client button in the staff scene
            newClientButtonStaff.setOnAction(e -> {
                    try {
                            ApplicationLogic.newClientButtonClick(primaryStage, conn, nameFieldStaff, directFieldStaff, delegFieldStaff, cpFieldStaff, rfcFieldStaff, mailFieldStaff, clientList);
                    } catch (SQLException e1) {
                            e1.printStackTrace();
                    }
            });

            // Adding a product on the staff view with an enter
            staffView.setOnKeyPressed(e -> {
                    if (e.getCode().equals(ENTER)) {
                            try {
                                    ApplicationLogic.addClick(primaryStage, conn, id, totalField, products);
                            } catch (SQLException e1) {
                                    e1.printStackTrace();
                            }
                    }
            });

            // When you select to pay on cash staff scene
            cash.setOnAction(e -> ApplicationLogic.cashClick(paymentForm));

            // When you select to pay with card staff scene
            card.setOnAction(e -> ApplicationLogic.cardClick(paymentForm));

            // When you click on button to end a sale and charge for the products on the staff scene
            charge.setOnAction(e -> {
                try {
                    ApplicationLogic.chargeClick(client, invoiceTool, paymentForm, primaryStage, conn, products, totalField, soldProductList);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            });

            // HANDLING EVENTS ON THE ADMIN SCENE

            // Pay on cash on the admin scene
            cashAdmin.setOnAction(e -> ApplicationLogic.cashClick(paymentFormAdmin));

            // Pay with card on the admin scene
            cardAdmin.setOnAction(e -> ApplicationLogic.cardClick(paymentFormAdmin));

            // Add a product to the cart in the admin scene
            addAdmin.setOnAction( e -> {
                    try {
                            ApplicationLogic.addClick(primaryStage, conn, idAdmin, totalFieldAdmin, productsAdmin);
                    } catch (SQLException e1) {
                            e1.printStackTrace();
                    }
            });

            // End a sale and charge for the products in admin
            chargeAdmin.setOnAction(e -> {
                    try {
                            ApplicationLogic.chargeClick(clientAdmin, invoiceToolAdmin, paymentFormAdmin, primaryStage, conn, productsAdmin, totalFieldAdmin, soldProductList);
                    } catch (SQLException e1) {
                            e1.printStackTrace();
                    }
            });

            // CLick on the add client button in the admin scene
            newClientButtonAdmin.setOnAction(e -> {
                    try {
                            ApplicationLogic.newClientButtonClick(primaryStage, conn, nameFieldAdmin, directFieldAdmin, delegFieldAdmin, cpFieldAdmin, rfcFieldAdmin, mailFieldAdmin, clientList);
                    } catch (SQLException e1) {
                            e1.printStackTrace();
                    }
            });

            // Click on the delete client button
            deleteClientButton.setOnAction( e -> {
                    try {
                            ApplicationLogic.deleteClient(clientList, conn, deleteClientField, primaryStage);
                    } catch (SQLException e1) {
                            e1.printStackTrace();
                    }
            });

            // Click on the add new product button
            addNewProduct.setOnAction(e -> {
                    try {
                            ApplicationLogic.addProductInventoryClick(newIdField, newDescriptionField, newPriceField, newExistenceField, productInventoryList, conn, primaryStage);
                    } catch (SQLException e1) {
                            e1.printStackTrace();
                    }
            });

            // Click on the delete product button
            deleteProduct.setOnAction(e -> {
                    try {
                            ApplicationLogic.deleteProductClick(deleteId, productInventoryList, conn, primaryStage);
                    } catch (SQLException e1) {
                            e1.printStackTrace();
                    }
            });

            // CLick on the add stock button
            addExistenceButton.setOnAction(e -> {
                    try {
                            ApplicationLogic.updateExistence(addExistenceId, addExistenceAmount, conn, primaryStage, productInventoryList);
                    } catch (SQLException e1) {
                            e1.printStackTrace();
                    }
            });

            // Change user in the admin scene
            changeUserAdmin.setOnAction(e -> {
                    try {
                            ApplicationLogic.changeUserClick(conn, primaryStage, logInScene);
                    } catch (SQLException e1) {
                            e1.printStackTrace();
                    }
            });

            // Click on the finish day button
            finishDay.setOnAction(e -> {
                try {
                    ApplicationLogic.endDay(soldProductList, conn);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            });

            // Close button on either admin or staff scene
            close.setOnAction(e-> ApplicationLogic.closeClick(conn, primaryStage));
            closeAdmin.setOnAction(e-> ApplicationLogic.closeClick(conn, primaryStage));

            // When closing the application using the close button of the app window instead of the dedicate button
            primaryStage.setOnCloseRequest(e -> ApplicationLogic.closeClick(conn, primaryStage));
    }

    /**
     * This method changes the scene of the primary stage to the adminView.
     * @param primaryStage the primary Stage of the application
     * @param adminView the scene containing the elements of the administrator view.
     */
    public static void logAdmin(Stage primaryStage, Scene adminView){
        primaryStage.setScene(adminView);
        primaryStage.setTitle("Administrador");
        primaryStage.setFullScreen(false);
    }

    /**
     * This method changes the scene of the primary scene to the staff view.
     * @param primaryStage The primary Stage of the application
     * @param staffView The scene containing the elements of the staff view.
     */
    public static void logStaff(Stage primaryStage, Scene staffView){
        primaryStage.setScene(staffView);
        primaryStage.setTitle("Staff");
        primaryStage.setFullScreen(false);
    }
}