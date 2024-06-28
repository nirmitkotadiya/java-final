package database;

import models.User;
import models.Admin;
import models.Customer;
import models.Producer;
import models.Product;
import models.ProductRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private Map<String, User> userMap;
    private Map<String, Product> productMap;
    private List<ProductRequest> productRequests;

    public Database() {
        this.userMap = new HashMap<>();
        this.productMap = new HashMap<>();
        this.productRequests = new ArrayList<>();

        // Add default users
        addUser(new Admin("admin", "admin", "admin"));
        addUser(new Producer("producer", "producer", "producer"));
        addUser(new Customer("customer", "customer", "customer"));
    }

    public void addUser(User user) {
        userMap.put(user.getId(), user);
    }

    public User getUser(String id) {
        return userMap.get(id);
    }

    public void removeUser(String id) {
        userMap.remove(id);
    }

    public void addProduct(Product product) {
        productMap.put(product.getId(), product);
    }

    public void removeProduct(String productId) {
        productMap.remove(productId);
    }

    public List<ProductRequest> getProductRequests() {
        return productRequests;
    }

    public void addProductRequest(ProductRequest request) {
        productRequests.add(request);
    }

    public void removeProductRequest(ProductRequest request) {
        productRequests.remove(request);
    }
}
