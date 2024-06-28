package ui;

import database.Database;
import models.Admin;
import models.Customer;
import models.Producer;
import models.Product;
import models.User;
import models.ProductRequest;
import services.ProductService;
import billing.Bill;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Database database = new Database();
        ProductService productService = new ProductService();

        while (true) {
            System.out.println("Welcome to Virtual Mart Management System");
            System.out.println("1. Login");
            System.out.println("2. Sign Up");
            System.out.println("3. Exit");

            int choice = getChoice(scanner, 1, 3);

            try {
                switch (choice) {
                    case 1:
                        login(scanner, database, productService);
                        break;
                    case 2:
                        signUp(scanner, database);
                        break;
                    case 3:
                        System.out.println("Exiting...");
                        return;
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }

    private static void login(Scanner scanner, Database database, ProductService productService) {
        System.out.println("1. Login as Admin");
        System.out.println("2. Login as Producer");
        System.out.println("3. Login as Customer");

        int choice = getChoice(scanner, 1, 3);

        System.out.print("Enter User ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        try {
            User user = database.getUser(id);
            if (user != null && user.getPassword().equals(password)) {
                System.out.println(user.getClass().getSimpleName() + " logged in successfully.");
                switch (choice) {
                    case 1:
                        handleAdmin(scanner, database, productService, (Admin) user);
                        break;
                    case 2:
                        handleProducer(scanner, database, productService, (Producer) user);
                        break;
                    case 3:
                        handleCustomer(scanner, database, productService, (Customer) user);
                        break;
                }
            } else {
                System.out.println("Invalid credentials.");
            }
        } catch (Exception e) {
            System.out.println("Login failed. An error occurred: " + e.getMessage());
        }
    }

    private static void signUp(Scanner scanner, Database database) {
        System.out.println("1. Sign Up as Admin");
        System.out.println("2. Sign Up as Producer");
        System.out.println("3. Sign Up as Customer");

        int choice = getChoice(scanner, 1, 3);

        System.out.print("Enter User ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        try {
            if (id.length() != 0 && name.length() != 0 && password.length() != 0) {
                switch (choice) {
                    case 1:
                        Admin admin = new Admin(id, name, password);
                        database.addUser(admin);
                        System.out.println("Admin registered successfully.");
                        break;
                    case 2:
                        Producer producer = new Producer(id, name, password);
                        database.addUser(producer);
                        System.out.println("Producer registered successfully.");
                        break;
                    case 3:
                        Customer customer = new Customer(id, name, password);
                        database.addUser(customer);
                        System.out.println("Customer registered successfully.");
                        break;
                    default:
                        System.out.println("Invalid choice.");
                        return;
                }
                System.out.println();
            } else {
                System.out.println("Invalid input. Please enter valid values.");
            }

        } catch (Exception e) {
            System.out.println("Sign up failed. An error occurred: " + e.getMessage());
        }
    }

    private static void handleAdmin(Scanner scanner, Database database, ProductService productService, Admin admin) {
        while (true) {
            System.out.println();
            System.out.println("1. View Product Requests");
            System.out.println("2. Accept Product Request");
            System.out.println("3. Reject Product Request");
            System.out.println("4. Logout");
            System.out.println();

            int choice = getChoice(scanner, 1, 4);

            try {
                switch (choice) {
                    case 1:
                        viewProductRequests(database);
                        break;
                    case 2:
                        if (!database.getProductRequests().isEmpty()) {
                            processProductRequest(scanner, database, productService, true);
                        } else {
                            System.out.println("Empty database ....");
                        }
                        break;
                    case 3:
                        if (!database.getProductRequests().isEmpty()) {
                            processProductRequest(scanner, database, productService, false);
                        } else {
                            System.out.println("Empty database ....");

                        }
                        break;
                    case 4:
                        return;
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }

    private static void handleProducer(Scanner scanner, Database database, ProductService productService,
            Producer producer) {
        while (true) {
            System.out.println();
            System.out.println("1. Add Product Request");
            System.out.println("2. Logout");
            System.out.println();

            int choice = getChoice(scanner, 1, 2);

            try {
                switch (choice) {
                    case 1:
                        addProductRequest(scanner, database, producer);
                        break;
                    case 2:
                        return;
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }

    private static void handleCustomer(Scanner scanner, Database database, ProductService productService,
            Customer customer) {
        while (true) {
            System.out.println();
            System.out.println("1. View Products");
            System.out.println("2. Buy Product");
            System.out.println("3. Logout");
            System.out.println();

            int choice = getChoice(scanner, 1, 3);

            try {
                switch (choice) {
                    case 1:
                        viewProducts(productService);
                        break;
                    case 2:
                        buyProduct(scanner, productService, customer);
                        break;
                    case 3:
                        return;
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }

    private static void addProductRequest(Scanner scanner, Database database, Producer producer) {
        System.out.print("Enter Product ID: ");
        String productId = scanner.nextLine().trim();
        if (productId.isEmpty()) {
            System.out.println("Product ID cannot be empty. Please enter a valid Product ID.");
            return; // Exit method if productId is empty
        }
        System.out.print("Enter Product Name: ");
        String productName = scanner.nextLine();
        if (productName.isEmpty()) {
            System.out.println("Product Name cannot be empty. Please enter a valid Product Name.");
            return;
        }
        System.out.print("Enter Product Price: ");
        double productPrice = scanner.nextDouble();
        System.out.print("Enter Product Quantity: ");
        int productQuantity = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        try {
            Product product = new Product(productId, productName, productPrice, productQuantity);
            ProductRequest request = new ProductRequest(producer.getId(), product);
            database.addProductRequest(request);
            System.out.println("Product request added successfully. Awaiting admin approval.");
        } catch (Exception e) {
            System.out.println("Adding product request failed. An error occurred: " + e.getMessage());
        }
    }

    private static void viewProductRequests(Database database) {
        try {
            boolean foundPendingRequests = false;

            for (ProductRequest request : database.getProductRequests()) {
                if ("pending".equals(request.getStatus())) {
                    foundPendingRequests = true;
                    System.out.println("Producer ID: " + request.getProducerId());
                    System.out.println("Product ID: " + request.getProduct().getId());
                    System.out.println("Product Name: " + request.getProduct().getName());
                    System.out.println("Product Price: " + request.getProduct().getPrice());
                    System.out.println("Product Quantity: " + request.getProduct().getQuantity());
                    System.out.println("Status: " + request.getStatus());
                    System.out.println("----------------------");
                }
            }

            if (!foundPendingRequests) {
                System.out.println("No Pending Product Requests:");
            }
        } catch (Exception e) {
            System.out.println("Error viewing product requests: " + e.getMessage());
        }
    }

    private static void processProductRequest(Scanner scanner, Database database, ProductService productService,
            boolean accept) {
        System.out.print("Enter Product ID to " + (accept ? "Accept" : "Reject") + ": ");
        String productId = scanner.nextLine();
        ProductRequest requestToProcess = null;

        try {
            for (ProductRequest request : database.getProductRequests()) {
                if (request.getProduct().getId().equals(productId) && "pending".equals(request.getStatus())) {
                    requestToProcess = request;
                    break;
                }
            }

            if (requestToProcess != null) {
                if (accept) {
                    requestToProcess.setStatus("accepted");
                    productService.addProduct(requestToProcess.getProduct());
                    System.out.println("Product request accepted and product added successfully.");
                } else {
                    requestToProcess.setStatus("rejected");
                    System.out.println("Product request rejected.");
                }
            } else {
                System.out.println("Product request not found or already processed.");
            }
        } catch (Exception e) {
            System.out.println("Processing product request failed. An error occurred: " + e.getMessage());
        }
    }

    private static void viewProducts(ProductService productService) {
        System.out.println("Available Products:");
        try {
            Iterable<Product> products = productService.getProducts();
            for (Product product : products) {
                System.out.println("ID: " + product.getId());
                System.out.println("Name: " + product.getName());
                System.out.println("Price: " + product.getPrice());
                System.out.println("Quantity: " + product.getQuantity());
                System.out.println("----------------------");
            }
        } catch (Exception e) {
            System.out.println("Error viewing products: " + e.getMessage());
        }
    }

    private static void buyProduct(Scanner scanner, ProductService productService, Customer customer) {
        List<Product> cart = new ArrayList<>();
        while (true) {
            System.out.print("Enter Product ID to Buy: ");
            String productId = scanner.nextLine();
            Product product = productService.searchProduct(productId);
            if (product != null) {
                System.out.print("Enter Quantity: ");
                int quantity = scanner.nextInt();
                scanner.nextLine();
                if (quantity <= product.getQuantity()) {
                    cart.add(new Product(product.getId(), product.getName(), product.getPrice(), quantity));
                    System.out.println("Product added to cart.");
                } else {
                    System.out.println("Insufficient quantity.");
                }
            } else {
                System.out.println("Product not found.");
            }

            System.out.println("Do you want to buy another product? (yes/no)");
            String response = scanner.nextLine().trim().toLowerCase();
            if (!response.equals("yes")) {
                break;
            }
        }

        if (!cart.isEmpty()) {
            System.out.print("Do you want to generate the bill? (yes/no): ");
            String response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("yes")) {
                boolean allProductsAvailable = true;
                for (Product cartProduct : cart) {
                    Product product = productService.searchProduct(cartProduct.getId());
                    if (product == null || cartProduct.getQuantity() > product.getQuantity()) {
                        allProductsAvailable = false;
                        System.out.println("Insufficient quantity for product: " + cartProduct.getName());
                    }
                }

                if (allProductsAvailable) {
                    for (Product cartProduct : cart) {
                        Product product = productService.searchProduct(cartProduct.getId());
                        product.setQuantity(product.getQuantity() - cartProduct.getQuantity());
                    }
                    Bill bill = new Bill(customer.getId(), customer.getName(), cart);
                    bill.generateBill();
                    System.out.println("Purchase successful. Bill generated.");
                } else {
                    System.out.println("Purchase aborted due to insufficient quantities.");
                }
            } else {
                System.out.println("Bill not generated. Purchase aborted.");
            }
        }
    }

    private static int getChoice(Scanner scanner, int min, int max) {
        int choice;
        while (true) {
            try {

                System.out.print("Enter your choice (" + min + "-" + max + "): ");
                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    if (choice >= min && choice <= max) {
                        break;
                    } else {
                        System.out.println("Invalid choice. Please try again.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.nextLine(); // Consume invalid input
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
        return choice;
    }
}
