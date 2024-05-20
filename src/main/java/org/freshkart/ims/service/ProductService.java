package org.freshkart.ims.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.freshkart.ims.entity.Product;
import org.freshkart.ims.repository.ProductRepository;


import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ProductService {

    @Inject
    ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.listAll();
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId);
    }

    @Transactional
    public Product createProduct(Product product) {
        productRepository.persist(product);
        return product;
    }

    @Transactional
    public Product updateProduct(Product updatedProduct) {
        Optional<Product> existingProduct = Optional.ofNullable(getProductById(updatedProduct.getProductId()));
        if (existingProduct.isPresent()) {
            return productRepository.getEntityManager().merge(updatedProduct);
        } else {
            throw new IllegalArgumentException("Product with ID " + updatedProduct.getProductId() + " does not exist");
        }
    }

    @Transactional
    public boolean deleteProduct(Long productId) {
        return productRepository.deleteById(productId);
    }
}
