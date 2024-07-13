# 📦 Warehouse Management GUI

Welcome to the Warehouse Management GUI! This Java application, using a MySQL database, helps you manage product entries and exits with ease.

## 📋 Table of Contents

- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

## 🌟 Features

- **View Suppliers**: Load and display supplier names from the database.
- **Add Products**: Insert new products with supplier selection.
- **Add Suppliers**: Insert new suppliers into the database.
- **Stock Check**: View current stock levels filtered by product name.
- **Record Movements**: Log product entries and exits.
- **User-Friendly Interface**: Simple and intuitive UI.

## 🔧 Prerequisites

- Java Development Kit (JDK) 8 or later
- MySQL Database
- JDBC Driver for MySQL

## 🚀 Installation

1. **Clone the Repository**:
    ```sh
    git clone https://github.com/NinoZullo05/java-mysql-gui.git
    cd java-mysql-gui
    ```

2. **Set Up MySQL Database**:
    - Create a MySQL database named `GestioneMagazzinoGUI`.
    - Import the provided SQL file to set up the required tables.
    - Update the database credentials in the code if necessary.

3. **Compile and Run the Application**:
    - Open the project in your favorite IDE (e.g., NetBeans).
    - Compile and run the `Main` class.

## 💡 Usage

1. **Navigate Through Tabs**: Use the tabs to switch between different functionalities (Home, Products, Suppliers, Movements, Stock).
2. **Add New Entries**:
    - **Products**: Fill in the product details and select a supplier.
    - **Suppliers**: Fill in the supplier details.
3. **View and Manage Stock**: Check current stock levels by entering a product name.
4. **Record Movements**: Log product entries or exits by filling in the details.
5. **Messages**: Informative messages will guide you through the actions and any errors.

## 🤝 Contributing

Contributions are welcome! Feel free to open issues or submit pull requests.

## 📄 License

This project is licensed under the MIT License.
