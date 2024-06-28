package billing;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import models.Product;

public class Bill {
    private String customerId;
    private String customerName;
    private List<Product> products;

    public Bill(String customerId, String customerName, List<Product> products) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.products = products;
    }

    public void generateBill() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String formattedDateTime = now.format(formatter);

        double totalAmount = 0;
        StringBuilder billContent = new StringBuilder();
        billContent.append("+-------------------------------------------+\n");
        billContent.append("|           VIRTUAL MART SYSTEM            |\n");
        billContent.append("+-------------------------------------------+\n");
        billContent.append("Customer ID: ").append(customerId).append("\n");
        billContent.append("Customer Name: ").append(customerName).append("\n");
        billContent.append("--------------------------------------------\n");

        for (Product product : products) {
            double productTotal = product.getPrice() * product.getQuantity();
            totalAmount += productTotal;
            billContent.append("Product ID: ").append(product.getId()).append("\n");
            billContent.append("Product Name: ").append(product.getName()).append("\n");
            billContent.append("Product Price: ").append(String.format("%.2f", product.getPrice())).append("\n");
            billContent.append("Quantity: ").append(product.getQuantity()).append("\n");
            billContent.append("Total: ").append(String.format("%.2f", productTotal)).append("\n");
            billContent.append("--------------------------------------------\n");
        }

        billContent.append("Total Amount: ").append(String.format("%.2f", totalAmount)).append("\n");
        billContent.append("--------------------------------------------\n");
        billContent.append("Date and Time: ").append(formattedDateTime).append("\n");
        billContent.append("+-------------------------------------------+\n");

        String filePath = "D:/" + "Bill_" + formattedDateTime + ".txt";

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(billContent.toString());
            System.out.println("Bill generated successfully at: " + filePath);
        } catch (IOException e) {
            System.err.println("Error generating bill: " + e.getMessage());
        }
    }
}
