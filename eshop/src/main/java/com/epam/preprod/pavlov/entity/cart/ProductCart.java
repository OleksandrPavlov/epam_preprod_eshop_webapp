package com.epam.preprod.pavlov.entity.cart;

import com.epam.preprod.pavlov.entity.Product;
import com.epam.preprod.pavlov.entity.cart.ajax.UpdateCartRequest;
import com.epam.preprod.pavlov.exception.ValidationException;
import com.epam.preprod.pavlov.service.product.ProductService;

import java.util.*;
import java.util.function.Consumer;

public class ProductCart {
    private Map<Product, Integer> productMap = new HashMap<>();
    private ProductService productService;
    private final Consumer<UpdateCartRequest> addOperator = (element) -> {
        Product product = getProductById(element);
        addProduct(product);
    };

    private final Consumer<UpdateCartRequest> remove = (element) -> {
        Product product = getProductById(element);
        removeProduct(product);
    };

    private final Consumer<UpdateCartRequest> removeAll = (element) -> {
        removeAllProducts();
    };

    private final Consumer<UpdateCartRequest> reassignCount = (element) -> {
        Product product = getProductById(element);
        updateProductCount(product, element.getCount());
    };
    protected Map<String, Consumer<UpdateCartRequest>> operationMap = new HashMap<>();

    public Map<Product, Integer> getProductMap() {
        return productMap;
    }

    public ProductCart(ProductService productService) {
        this.productService = productService;
        initOperatorMap();
    }

    public ProductCart(Map<Product, Integer> productMap, ProductService productService) {
        this(productService);
        this.productMap = productMap;
    }

    public void addProduct(Product product) {
        Product modifiedProduct = setDefaultValuesForUnnecessaryFields(product);
        if (productMap.containsKey(modifiedProduct)) {
            productMap.put(modifiedProduct, productMap.get(modifiedProduct) + 1);
            return;
        }
        productMap.put(product, 1);
    }

    public void updateProductCount(Product product, int count) {
        productMap.put(getModifiedProduct(product), count);
    }

    public void removeProduct(Product product) {
        productMap.remove(getModifiedProduct(product));
    }

    public List<Product> getProductList() {
        return new ArrayList<>(productMap.keySet());
    }

    public double calculateTotalPrice() {
        return productMap.entrySet().stream().map((entry) -> entry.getKey().getPrice() * entry.getValue()).reduce((double) 0, Double::sum);
    }

    public void removeAllProducts() {
        productMap.clear();
    }

    public void updateProductCart(UpdateCartRequest request) throws ValidationException {
        String updateOperation = request.getOperation();
        Consumer<UpdateCartRequest> operation = operationMap.get(updateOperation);
        if (Objects.isNull(operation)) {
            throw new ValidationException("Unsupported update operation!");
        }
        operation.accept(request);
    }

    private Product setDefaultValuesForUnnecessaryFields(Product product) {
        Product newProduct = new Product();
        newProduct.setProductId(product.getProductId());
        newProduct.setPrice(product.getPrice());
        newProduct.setProductName(product.getProductName());
        return newProduct;
    }

    protected void initOperatorMap() {
        operationMap.put(UpdateCartRequest.ADD_OPERATION, addOperator);
        operationMap.put(UpdateCartRequest.REMOVE_OPERATION, remove);
        operationMap.put(UpdateCartRequest.REMOVE_ALL_OPERATION, removeAll);
        operationMap.put(UpdateCartRequest.UPDATE_OPERATION, reassignCount);
    }

    private Product getProductById(UpdateCartRequest request) {
        int productId = request.getProductId();
        Product product = productService.getProductById(productId);
        if (Objects.isNull(product)) {
            throw new ValidationException("Nonexistent product!");
        }
        return product;
    }

    private Product getModifiedProduct(Product product) {
        Product modifiedProduct = setDefaultValuesForUnnecessaryFields(product);
        if (!productMap.containsKey(modifiedProduct)) {
            throw new ValidationException("Nonexistent product!");
        }
        return modifiedProduct;
    }

}
