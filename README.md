# point-of-sale-system
This project is a Point of Sale System designed for a pharmacy. It handles the inventory of Products, the Client's information, and the sales of the day. The documentation is divided in two sections, the first one is for users of the application, and the second one is for developers. 

# Information for users

## Log In Window
The Point Of Sale system supports two different kind of users: administrators, and staff. Each one of them has access to a different set of operations and information. The first step when using the application is to log in, given a username and a password, which already has some privileges associated with it. When you first open the software, you must type your log in information in this window: 

[![Log-In-Window.png](https://i.postimg.cc/7ht8jG18/Log-In-Window.png)](https://postimg.cc/9w9n7fNJ)


## Staff View
If the user had staff privileges, then the Staff view of the system will be shown. The staff is only able to carry out sales. Products are added to the cart by typing the product code in the "Add prouct" textbox and clicking the button. The total price of the sale is shown at the right-down corner, and it is automatically modified when a new product is added. In order to keep track of the clients who want to get an invoice, the staff user is also able to add a new Client to the database system. The user can select whether to generate an invoice, by clicking on the checkbox and typing the RFC of the client. To end a sale, the user must select a payment method from the dropdown menu and click on the "Finalizar compra" (End sale) button. There is a dropdown menu in the upper right corner, designed to close the application or change the user. 

[![Staff-View.png](https://i.postimg.cc/pL84J1St/Staff-View.png)](https://postimg.cc/VJfKY417)

## Admin View 
When Logging In with and administrator account, the user is able to perform additional operations and view the information of the Clients, Inventory, and Day Sales. The Admin view is divided in four different tabs: sales, clients, products, and day sales. There is a dropdown menu in the upper right corner, designed to close the application or change the user. 

### Sales Tab
In the first tab, the administrator is able to perform sales, along with their invoices. This tab is pretty similar to the Staff View, but it doesn't have a menu to add a new client, since there is a dedicated tab for that.

[![Admin-Sale.png](https://i.postimg.cc/wxQGMmm9/Admin-Sale.png)](https://postimg.cc/bddTFdg5)

### Clients Tab
The second tab is designed to view and manage the information of the clients. There is a table in which the Name, Zip code, RFC, and email of the registered Clients are displayed. There is also a menu to add new clients by typing the necessary information adn clicking on the "Agregar cliente" (Add client) button. Additionally, the administrator is able to delete a client from the database by typying her RFC and clicking the "Eliminar" (Delete) button. 

[![Clients-Table.png](https://i.postimg.cc/Kc15S3D9/Clients-Table.png)](https://postimg.cc/z31hTBMW)

### Products Tab 
The third tab includes all the information regarding the products in the inventory. There is a table that includes the stock, ID, description, and Price of each registered product. There is also a menu that allows to perform three different actions. The first one is to add a new product to the inventory, by specifying all the necessary information and clicking on the "Agregar producto" (Add product) buutton. The second action is to delete a product form the inventory by typing its ID and clicking on the "Eliminar producto" (Delete product) button. Finally, the administrator can also add stock to a partiuclar product, by typing the ID, and the amount to be added, and clicking on the "Agregar" (Add) button. 

[![Products-Table.png](https://i.postimg.cc/Vkn9h3gQ/Products-Table.png)](https://postimg.cc/75PTGtjm)

### Day Sales tab
The fourth and last tab is designed to show the sales of a day, such that the client asked for and invoice. There is a table that shows the product's ID, the amount bought, and the rfc of the client who made the purchase. When the pharmacy is about to close, the administrator can click on the "End Sales" button, in order to delete all the sales information and start a new day. This tab requires more functionalities, which will be added later on. 

[![Screen-Shot-2020-10-18-at-11-07-34.png](https://i.postimg.cc/Y0wKkcgc/Screen-Shot-2020-10-18-at-11-07-34.png)](https://postimg.cc/Vr4HBVD4)

# Information for developers 

## Required libraries
In order to work with the project and run it, the developer must download two libraries which are used in the code: JavaFX and MySQL-Java connector. The JavaFX library was used for the Graphical interface, and it can be downloaded [here](https://gluonhq.com/products/javafx/). The project was developed with JavaFX 11, since it is the first long-term support version. Write down the path to the lib folder, and this will be referred to as {PATH_TO_FX}. The MySQL-Java connector can be downloaded [here](https://downloads.mysql.com/archives/c-j/). For the project, the 8.0.13 version was used. Add both libraries to the project in your IDE. 

## Creating the MySQL database
To run the project, you must have a copy of the MySQL database "GlafiraPharmacy", which was created to store the pharmacie's information. This can be done easily by exporting the "DatabaseStructure.sql" file to MySQL Workbench, and then creating a new User with a password, and give the user permission to access the "GlafiraPharmacy" database. In the "GraphicalComponents" file, you must modify the connection statement in line 24, by specifying the port, the user, and the password. 

## Adding JavaFX arguments 
To compile the project, you must first add the JavaFX modules to the run configurations. In your IDE, add the following JVM arguments (change the {PATH_TO_FX} as appropriate: 

        --module-path {PATH_TO_FX} --add-modules javafx.controls 
        --module-path {PATH_TO_FX} --add-modules javafx.base 
        --module-path {PATH_TO_FX} --add-modules javafx.graphics 
        --module-path {PATH_TO_FX} --add-modules javafx.media
        
With this configurations, you will be able to run the project from your IDE. 






