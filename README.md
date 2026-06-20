# Stationary Management System

A Java desktop application for managing a stationary store inventory and related records. The project uses Swing for the user interface, JDBC for database access, and MySQL as the backend database.

## Features

- Manage records for:
  - Items
  - Suppliers
  - Customers
  - Purchases
  - Sales
  - Sales details
  - Supplier purchase data
- Add, update, delete, and refresh records from the GUI
- Search records by keyword
- Toggle between dark and light themes
- View customer records using a separate console utility

## Tech Stack

- Java
- Swing (GUI)
- JDBC
- MySQL
- Maven
- FlatLaf

## Project Structure

- `src/main/java/MainApp.java`  
  Simple launcher with a menu-style interface.
- `src/main/java/StationaryManagerFinal.java`  
  Main application for CRUD operations, table switching, search, and theme toggle.
- `src/main/java/DBConnect.java`  
  Utility for checking the MySQL connection.
- `src/main/java/ViewCustomers.java`  
  Console-based tool to display customer information.

## Prerequisites

- Java JDK 17 or higher
- Apache Maven
- MySQL Server
- JDBC and FlatLaf dependencies available in the project setup

## Database Setup

1. Create a MySQL database named `stationary`.
2. Ensure the required tables exist in the database.
3. Update the database credentials in the Java files if needed:
   - URL: `jdbc:mysql://localhost:3306/stationary`
   - Username: `root`
   - Password: `gaurang31`

> Note: The credentials are currently hardcoded in the code, so you may need to change them to match your local MySQL setup.

## Running the Project

### 1. Compile the project

```bash
mvn compile
```

### 2. Run the main desktop application

From the IDE or using the command line:

```bash
java -cp target/classes StationaryManagerFinal
```

### 3. Run the simple starter window

```bash
java -cp target/classes MainApp
```

## Notes

- `StationaryManagerFinal` is the main application for complete database management.
- `MainApp` is a lightweight starter screen.
- Make sure your MySQL server is running before launching the program.

## Future Improvements

- Add schema creation scripts
- Store DB credentials in environment variables
- Improve validation and error handling
- Add data export/import support
