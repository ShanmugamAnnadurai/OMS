package org.freshkart.oms.controller;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.freshkart.oms.entity.PurchaseOrderItem;
import org.freshkart.oms.service.PurchaseOrderItemService;

import java.util.List;

@Path("/purchaseOrderItem")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Purchase Order Item REST EndPoint")
public class PurchaseOrderItemController {

    @Inject
    PurchaseOrderItemService purchaseOrderItemService;

    @POST
    @Transactional
    @Operation(summary = "Create a New Purchase Order Item")
    public Response createPurchaseOrderItem(@Valid PurchaseOrderItem purchaseOrderItem) {
        try {
            PurchaseOrderItem createdPurchaseOrderItem = purchaseOrderItemService.createPurchaseOrderItem(purchaseOrderItem);
            return Response.status(Response.Status.CREATED).entity(createdPurchaseOrderItem).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to create Purchase Order Item: " + e.getMessage()).build();
        }
    }

    @GET
    @Operation(summary = "Get All the Purchase Order Item")
    public List<PurchaseOrderItem> getAllPurchaseOrderItem() {
        return purchaseOrderItemService.getAllPurchaseOrderItem();
    }

    @GET
    @Operation(summary = "Get the Purchase Order Item by Id")
    @Path("/{purchaseOrderItemId}")
    public Response getPurchaseOrderItemById(@PathParam("purchaseOrderItemId") Long purchaseOrderItemId) {
        try {
            PurchaseOrderItem purchaseOrderItem = purchaseOrderItemService.getPurchaseOrderItemById(purchaseOrderItemId);
            if (purchaseOrderItemId != null) {
                return Response.ok(purchaseOrderItem).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Purchase Order Item with ID " + purchaseOrderItemId + " not found").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to retrieve Purchase Order Item: " + e.getMessage()).build();
        }
    }

    @PUT
    @Operation(summary = "Update the Purchase Order Item by Id")
    @Path("/{purchaseOrderItemId}")
    @Transactional
    public Response updatePurchaseOrderItem(@PathParam("purchaseOrderItemId") Long purchaseOrderItemId, @Valid PurchaseOrderItem updatedPurchaseOrderItem) {
        try {
            updatedPurchaseOrderItem.setOrderDetailsId(purchaseOrderItemId);
            PurchaseOrderItem updated = purchaseOrderItemService.updatePurchaseOrderItem(updatedPurchaseOrderItem);
            return Response.ok(updated).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to update Purchase Order Item: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Operation(summary = "Delete the Purchase Order Item by Id")
    @Path("/{purchaseOrderItemId}")
    @Transactional
    public Response deletePurchaseOrderItem(@PathParam("purchaseOrderItemId") Long purchaseOrderItemId) {
        try {
            boolean deleted = purchaseOrderItemService.deletePurchaseOrderItem(purchaseOrderItemId);
            if (deleted) {
                return Response.noContent().build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Purchase Order Item with ID " + purchaseOrderItemId + " not found").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to delete Purchase Order Item: " + e.getMessage()).build();
        }
    }
}
