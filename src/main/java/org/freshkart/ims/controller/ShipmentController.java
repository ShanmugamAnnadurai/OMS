package org.freshkart.ims.controller;


import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.freshkart.ims.entity.Shipment;
import org.freshkart.ims.service.ShipmentService;
import org.freshkart.oms.entity.Customer;
import org.freshkart.oms.entity.PurchaseOrder;
import org.freshkart.oms.service.PurchaseOrderService;

import java.time.Instant;
import java.util.List;

@Path("/shipment")
@Tag(name = "Shipment REST EndPoint")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ShipmentController {
    @Inject
    ShipmentService shipmentService;

    @Inject
    PurchaseOrderService purchaseOrderService;

    @POST
    @Operation(summary = "Create a New Shipment")
    @Transactional
    public Response createShipment(@Valid Shipment shipment) {
        try {
            if (shipment.getPurchaseOrder().getPurchaseOrderId() == null ) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Purchase Order ID are required to create a Shipment")
                        .build();
            }

            if (shipment.getPurchaseOrder().getPurchaseOrderId() != null) {
                PurchaseOrder purchaseOrder = purchaseOrderService.getPurchaseOrderById(shipment.getPurchaseOrder().getPurchaseOrderId());
                if (purchaseOrder == null) {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("Purchase Order with ID " + shipment.getPurchaseOrder().getPurchaseOrderId() + " not found")
                            .build();
                }
            }
            Shipment createdShipment= shipmentService.createShipment(shipment);
            purchaseOrderService.updatePurchaseOrderStatus(shipment.getPurchaseOrder().getPurchaseOrderId(),"Shipped");
            return Response.status(Response.Status.CREATED).entity(createdShipment).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to create Product: " + e.getMessage()).build();
        }
    }

    @GET
    @Operation(summary = "Get All the Shipments")
    public List<Shipment> getAllShipment() {
        return shipmentService.getAllShipment();
    }

    @GET
    @Operation(summary = "Get the Shipment by Id")
    @Path("/{shipmentId}")
    public Response getShipmentById(@PathParam("shipmentId") Long shipmentId) {
        try {
            Shipment shipment = shipmentService.getShipmentById(shipmentId);
            if (shipment != null) {
                return Response.ok(shipment).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Shipment with ID " + shipment + " not found").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to retrieve shipment: " + e.getMessage()).build();
        }
    }

    @PUT
    @Operation(summary = "Update the Shipment by Id")
    @Path("/{shipmentId}")
    @Transactional
    public Response updateShipment(@PathParam("shipmentId") Long shipmentId, @Valid Shipment updatedShipment) {
        try {
            updatedShipment.setShipmentId(shipmentId);
            Shipment updated = shipmentService.updateShipment(updatedShipment);
            return Response.ok(updated).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to update shipment: " + e.getMessage())
                    .build();
        }
    }


    @PUT
    @Operation(summary = "Update the Shipment by Id to Change the Status to 'Delivered'")
    @Path("forDelivery/{shipmentId}")
    @Transactional
    public Response updateShipmentForDelivery(@PathParam("shipmentId") Long shipmentId, Shipment updatedShipment) {
        try {
            updatedShipment.setShipmentId(shipmentId);
            Shipment updated = shipmentService.updateShipmentForDelivery(updatedShipment);
            return Response.ok(updated).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to update shipment: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Operation(summary = "Delete the Shipment by Id")
    @Path("/{shipmentId}")
    @Transactional
    public Response deleteShipment(@PathParam("shipmentId") Long shipmentId) {
        try {
            boolean deleted = shipmentService.deleteShipment(shipmentId);
            if (deleted) {
                return Response.noContent().build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("shipment with ID " + shipmentId + " not found").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to delete shipment: " + e.getMessage()).build();
        }
    }
}
