package org.freshkart.oms.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.freshkart.ims.entity.Product;
import org.freshkart.ims.service.ProductService;
import org.freshkart.oms.entity.Customer;
import org.freshkart.oms.entity.PurchaseOrder;
import org.freshkart.oms.entity.PurchaseOrderItem;
import org.freshkart.oms.repository.CustomerRepository;
import org.freshkart.oms.repository.PurchaseOrderRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PurchaseOrderService {
    @Inject
    PurchaseOrderRepository purchaseOrderRepository;

    @Inject
    CustomerService customerService;

    @Inject
    ProductService productService;

    @Inject
    PurchaseOrderItemService purchaseOrderItemService;

    public List<PurchaseOrder> getAllPurchaseOrders() {
        return purchaseOrderRepository.listAll();
    }

    @Transactional
    public PurchaseOrder getPurchaseOrderById(Long purchaseOrderId) {
        return purchaseOrderRepository.findById(purchaseOrderId);
    }

    @Transactional
    public PurchaseOrder createPurchaseOrder(PurchaseOrder request) {
        if (request.getCustomer().getCustomerId() == null && request.getOrderItems() == null ) {
            throw new RuntimeException("Customer ID or Order Items are required to create a purchase order");
        }

        if (request.getCustomer().getCustomerId() != null) {
            Customer customer = customerService.getCustomerById(request.getCustomer().getCustomerId());
            if (customer == null) {
                throw new RuntimeException("Customer with ID " + request.getCustomer().getCustomerId() + " not found");

            }
        }

        if (request.getOrderItems() != null) {
            boolean allProductsAvailable = request.getOrderItems().stream()
                    .allMatch(orderItem -> {
                        Product product = productService.getProductById(orderItem.getProduct().getProductId());
                        return product != null && product.getQuantityAvailable() >= orderItem.getQuantity();
                    });

            if (!allProductsAvailable) {
               throw new RuntimeException("One or more products are out of stock or not found");
            }



            for (PurchaseOrderItem orderItem : request.getOrderItems()) {
                Product product = productService.getProductById(orderItem.getProduct().getProductId());
                int newQuantity = product.getQuantityAvailable() - orderItem.getQuantity();
                if (newQuantity < 0) {
                    throw new RuntimeException("Product " + product.getName() + " is out of stock");

                }
                product.setQuantityAvailable(newQuantity);
                productService.updateProduct(product);
            }
        }

        Double totalPrice = request.getOrderItems().stream()
                .mapToDouble(orderItem -> {
                    Product product = productService.getProductById(orderItem.getProduct().getProductId());
                    return product.getPrice() * orderItem.getQuantity();
                })
                .sum();


        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setOrderDate(Instant.now());
        purchaseOrder.setCustomer(request.getCustomer().getCustomerId() != null ? customerService.getCustomerById(request.getCustomer().getCustomerId()) : null);
        purchaseOrder.setOrderItems(request.getOrderItems());
        purchaseOrder.setDeliveryDate(request.getDeliveryDate());
        purchaseOrder.setPaymentMethod(request.getPaymentMethod());
        purchaseOrder.setTotalPrice(totalPrice);
        purchaseOrder.setStatus("Pending");

        purchaseOrderRepository.persist(purchaseOrder);

        if (request.getOrderItems() != null) {
            for (PurchaseOrderItem orderItem : request.getOrderItems()) {
                Product product = productService.getProductById(orderItem.getProduct().getProductId());
                orderItem.setPrice(product.getPrice() * orderItem.getQuantity());
                orderItem.setPurchaseOrder(purchaseOrder);
                purchaseOrderItemService.createPurchaseOrderItem(orderItem);
            }
        }



        return purchaseOrder;

    }

    @Transactional
    public void updatePurchaseOrderStatus(Long purchaseOrderId, String shipmentStatus) {
        PurchaseOrder purchaseOrder = getPurchaseOrderById(purchaseOrderId);
        if (purchaseOrder != null) {
            if (shipmentStatus.equals("Shipped")) {
                purchaseOrder.setStatus("Shipped");
            } else if (shipmentStatus.equals("Delivered")) {
                purchaseOrder.setStatus("Delivered");
                purchaseOrder.setDeliveryDate(Instant.now());
            }
            // Update the purchase order status
            updatePurchaseOrder(purchaseOrder);
        }
    }



    @Transactional
    public PurchaseOrder updatePurchaseOrder(PurchaseOrder updatedPurchaseOrder) {
        Optional<PurchaseOrder> existingPurchaseOrder = Optional.ofNullable(getPurchaseOrderById(updatedPurchaseOrder.getPurchaseOrderId()));
        if (existingPurchaseOrder.isPresent()) {
            return purchaseOrderRepository.getEntityManager().merge(updatedPurchaseOrder);
        } else {
            throw new IllegalArgumentException("Purchase Order with ID " + updatedPurchaseOrder.getPurchaseOrderId() + " does not exist");
        }
    }

    @Transactional
    public boolean deletePurchaseOrder(Long purchaseOrderId) {
        return purchaseOrderRepository.deleteById(purchaseOrderId);
    }
}
