package org.freshkart.wms.controller;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.freshkart.wms.entity.InventoryTransaction;
import org.freshkart.wms.service.InventoryTransactionService;


import java.util.List;

@Path("/inventoryTransaction")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Inventory Transaction REST EndPoint")
public class InventoryTransactionController {
    @Inject
    InventoryTransactionService inventoryTransactionService;

    @POST
    @Operation(summary = "Create a New Inventory Transaction")
    @Transactional
    public Response createInventoryTransaction(@Valid InventoryTransaction inventoryTransaction) {
        try {
            InventoryTransaction createdInventoryTransaction = inventoryTransactionService.createInventoryTransaction(inventoryTransaction);
            return Response.status(Response.Status.CREATED).entity(createdInventoryTransaction).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to create Inventory Transaction: " + e.getMessage()).build();
        }
    }


    @GET
    @Operation(summary = "Get All Inventory Transactions")
    public List<InventoryTransaction> getAllInventoryTransaction() {
        return inventoryTransactionService.getAllInventoryTransaction();
    }

    @GET
    @Operation(summary = "Get the Inventory Transaction by Id")
    @Path("/{inventoryTransactionId}")
    public Response getInventoryTransactionById(@PathParam("inventoryTransactionId") Long inventoryTransactionId) {
        try {
            InventoryTransaction inventoryTransaction = inventoryTransactionService.getInventoryTransactionById(inventoryTransactionId);
            if (inventoryTransaction != null) {
                return Response.ok(inventoryTransaction).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Inventory Transaction with ID " + inventoryTransactionId + " not found").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to retrieve Inventory Transaction: " + e.getMessage()).build();
        }
    }

    @PUT
    @Operation(summary = "Update the Inventory Transaction by Id")
    @Path("/{inventoryTransactionId}")
    @Transactional
    public Response updateInventoryTransaction(@PathParam("inventoryTransactionId") Long inventoryTransactionId, @Valid InventoryTransaction updatedInventoryTransaction) {
        try {
            updatedInventoryTransaction.setTransactionId(inventoryTransactionId);
            InventoryTransaction updated = inventoryTransactionService.updateInventoryTransaction(updatedInventoryTransaction);
            return Response.ok(updated).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to update Inventory Transaction: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Operation(summary = "Delete the Inventory Transaction by Id")
    @Path("/{inventoryTransactionId}")
    @Transactional
    public Response deleteInventoryTransaction(@PathParam("inventoryTransactionId") Long inventoryTransactionId) {
        try {
            boolean deleted = inventoryTransactionService.deleteInventoryTransaction(inventoryTransactionId);
            if (deleted) {
                return Response.noContent().build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Inventory Transaction with ID " + inventoryTransactionId + " not found").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to delete Inventory Transaction: " + e.getMessage()).build();
        }
    }

}
