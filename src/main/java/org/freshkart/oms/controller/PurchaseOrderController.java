package org.freshkart.oms.controller;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.freshkart.ims.entity.Product;
import org.freshkart.ims.service.ProductService;
import org.freshkart.oms.entity.Customer;
import org.freshkart.oms.entity.PurchaseOrder;
import org.freshkart.oms.entity.PurchaseOrderItem;
import org.freshkart.oms.service.CustomerService;
import org.freshkart.oms.service.PurchaseOrderItemService;
import org.freshkart.oms.service.PurchaseOrderService;

import java.time.Instant;
import java.util.List;

@Path("/purchaseOrder")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Purchase Order REST EndPoint")
public class PurchaseOrderController  {

    @Inject
    PurchaseOrderService purchaseOrderService;

    @Inject
    ProductService productService;

    @Inject
    CustomerService customerService;

    @Inject
    PurchaseOrderItemService purchaseOrderItemService;




    @POST
    @Operation(summary = "Create a New Purchase Order")
    @Transactional
    public Response createPurchaseOrder(@Valid PurchaseOrder request) {

        try {
            if (request.getCustomer().getCustomerId() == null && request.getOrderItems() == null ) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Customer ID or Order Items are required to create a purchase order")
                        .build();
            }

            if (request.getCustomer().getCustomerId() != null) {
                System.out.print("Customer Id is:"+ request.getCustomer().getCustomerId());
                Customer customer = customerService.getCustomerById(request.getCustomer().getCustomerId());
                if (customer == null) {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("Customer with ID " + request.getCustomer().getCustomerId() + " not found")
                            .build();
                }
            }

            if (request.getOrderItems() != null && !request.getOrderItems().isEmpty()) {
                System.out.print(request.getOrderItems());
                boolean allProductsAvailable = request.getOrderItems().stream()
                        .allMatch(orderItem -> {
                            Product product = productService.getProductById(orderItem.getProduct().getProductId());
                            return product != null && product.getQuantityAvailable() >= orderItem.getQuantity();
                        });

                if (!allProductsAvailable) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("One or more products are out of stock or not found")
                            .build();
                }



                for (PurchaseOrderItem orderItem : request.getOrderItems()) {
                    Product product = productService.getProductById(orderItem.getProduct().getProductId());
                    int newQuantity = product.getQuantityAvailable() - orderItem.getQuantity();
                    if (newQuantity < 0) {
                        return Response.status(Response.Status.BAD_REQUEST)
                                .entity("Product " + product.getName() + " is out of stock")
                                .build();
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

            PurchaseOrder createdPurchaseOrder = purchaseOrderService.createPurchaseOrder(purchaseOrder);


            if (request.getOrderItems() != null && !request.getOrderItems().isEmpty()) {
                for (PurchaseOrderItem orderItem : request.getOrderItems()) {
                    Product product = productService.getProductById(orderItem.getProduct().getProductId());
                    orderItem.setPrice(product.getPrice() * orderItem.getQuantity());
                    orderItem.setPurchaseOrder(createdPurchaseOrder);
                    purchaseOrderItemService.createPurchaseOrderItem(orderItem);
                }
            }




            return Response.status(Response.Status.CREATED).entity(createdPurchaseOrder).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to create purchase order: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Operation(summary = "Get All the Purchase Order")
    public List<PurchaseOrder> getAllPurchaseOrder() {
        return purchaseOrderService.getAllPurchaseOrders();
    }

    @GET
    @Operation(summary = "Get the Purchase Order by Id")
    @Path("/{purchaseOrderId}")
    public Response getPurchaseOrderById(@PathParam("purchaseOrderId") Long purchaseOrderId) {
        try {
            PurchaseOrder purchaseOrder = purchaseOrderService.getPurchaseOrderById(purchaseOrderId);
            if (purchaseOrder != null) {
                return Response.ok(purchaseOrder).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Purchase Order with ID " + purchaseOrderId + " not found").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to retrieve Purchase Order: " + e.getMessage()).build();
        }
    }

    @PUT
    @Operation(summary = "Update the Purchase Order by Id")
    @Path("/{purchaseOrderId}")
    @Transactional
    public Response updatePurchaseOrder(@PathParam("purchaseOrderId") Long purchaseOrderId, @Valid PurchaseOrder updatedPurchaseOrder) {
        try {
             updatedPurchaseOrder.setPurchaseOrderId(purchaseOrderId);
            PurchaseOrder updated = purchaseOrderService.updatePurchaseOrder(updatedPurchaseOrder);
            return Response.ok(updated).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to update Purchase Order: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Operation(summary = "Delete the Purchase Order by Id")
    @Path("/{purchaseOrderId}")
    @Transactional
    public Response deletePurchaseOrder(@PathParam("purchaseOrderId") Long purchaseOrderId) {
        try {
            boolean deleted = purchaseOrderService.deletePurchaseOrder(purchaseOrderId);
            if (deleted) {
                return Response.noContent().build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Purchase Order with ID " + purchaseOrderId + " not found").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to delete Purchase Order: " + e.getMessage()).build();
        }
    }

}
