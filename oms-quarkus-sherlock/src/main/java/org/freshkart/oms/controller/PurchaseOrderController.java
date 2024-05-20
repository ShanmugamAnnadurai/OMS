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
    public Response createPurchaseOrder(@Valid PurchaseOrder purchaseOrder) {

        try {
            PurchaseOrder createdPurchaseOrder = purchaseOrderService.createPurchaseOrder(purchaseOrder);
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
