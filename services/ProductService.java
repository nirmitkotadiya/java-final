package services;

import models.Product;
import java.util.HashMap;
import java.util.Map;

public class ProductService {
    private Map<String, Product> productMap;

    public ProductService() {
        this.productMap = new HashMap<>();
    }

    public void addProduct(Product product) {
        productMap.put(product.getId(), product);
    }

    public void removeProduct(String productId) {
        productMap.remove(productId);
    }

    public Product searchProduct(String productId) {
        return productMap.get(productId);
    }

    public Iterable<Product> getProducts() {
        return productMap.values();
    }
}
