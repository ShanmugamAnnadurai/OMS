package org.freshkart.ims.controller;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.freshkart.ims.entity.ShipmentDetail;
import org.freshkart.ims.service.ShipmentDetailService;

import java.util.List;

@Path("/shipmentDetail")
@Tag(name = "Shipment Detail REST EndPoint")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ShipmentDetailController {
    @Inject
    ShipmentDetailService shipmentDetailService;

    @POST
    @Operation(summary = "Create a New Shipment Detail")
    @Transactional
    public Response createShipmentDetail(@Valid ShipmentDetail shipmentDetail) {
        try {
            ShipmentDetail createdShipmentDetail = shipmentDetailService.createShipmentDetail(shipmentDetail);
            return Response.status(Response.Status.CREATED).entity(createdShipmentDetail).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to create Shipment Detail: " + e.getMessage()).build();
        }
    }

    @GET
    @Operation(summary = "Get All Shipment Details")
    public List<ShipmentDetail> getAllShipmentDetail() {
        return shipmentDetailService.getAllShipmentDetail();
    }

    @GET
    @Operation(summary = "Get the Shipment Detail by Id")
    @Path("/{shipmentDetailId}")
    public Response getShipmentDetailById(@PathParam("shipmentDetailId") Long shipmentDetailId) {
        try {
            ShipmentDetail shipmentDetail = shipmentDetailService.getShipmentDetailById(shipmentDetailId);
            if (shipmentDetail != null) {
                return Response.ok(shipmentDetail).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Shipment Detail with ID " + shipmentDetail + " not found").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to retrieve Shipment Detail: " + e.getMessage()).build();
        }
    }

    @PUT
    @Operation(summary = "Update the Shipment Detail by Id")
    @Path("/{shipmentDetailId}")
    @Transactional
    public Response updateShipmentDetail(@PathParam("shipmentDetailId") Long shipmentDetailId, @Valid ShipmentDetail updatedShipmentDetail) {
        try {
            updatedShipmentDetail.setShipmentDetailsId(shipmentDetailId);
            ShipmentDetail updated = shipmentDetailService.updateShipmentDetail(updatedShipmentDetail);
            return Response.ok(updated).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to update Shipment Detail: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Operation(summary = "Delete the Shipment Detail by Id")
    @Path("/{shipmentDetailId}")
    @Transactional
    public Response deleteShipmentDetail(@PathParam("shipmentDetailId") Long shipmentDetailId) {
        try {
            boolean deleted = shipmentDetailService.deleteShipmentDetail(shipmentDetailId);
            if (deleted) {
                return Response.noContent().build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Shipment Detail with ID " + shipmentDetailId + " not found").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to delete Shipment Detail: " + e.getMessage()).build();
        }
    }

}
