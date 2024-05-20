package org.freshkart.wms.controller;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.freshkart.wms.entity.WarehouseLocation;
import org.freshkart.wms.service.WarehouseLocationService;

import java.util.List;

@Path("/warehouseLocation")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Warehouse Location REST EndPoint")
public class WarehouseLocationController {

    @Inject
    WarehouseLocationService warehouseLocationService;

    @POST
    @Operation(summary = "Create a New Warehouse Location")
    @Transactional
    public Response createWarehouseLocation(@Valid WarehouseLocation warehouseLocation) {
        try {
            WarehouseLocation createdWarehouseLocation = warehouseLocationService.createWarehouseLocation(warehouseLocation);
            return Response.status(Response.Status.CREATED).entity(createdWarehouseLocation).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to create Warehouse Location: " + e.getMessage()).build();
        }
    }

    @GET
    @Operation(summary = "Get All Warehouse Locations")
    public List<WarehouseLocation> getAllWarehouseLocation() {
        return warehouseLocationService.getAllWarehouseLocation();
    }

    @GET
    @Operation(summary = "Get the Warehouse Location by Id")
    @Path("/{warehouseLocationId}")
    public Response getWarehouseLocationById(@PathParam("warehouseLocationId") Long warehouseLocationId) {
        try {
            WarehouseLocation warehouseLocation = warehouseLocationService.getWarehouseLocationById(warehouseLocationId);
            if (warehouseLocation != null) {
                return Response.ok(warehouseLocation).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Warehouse Location with ID " + warehouseLocationId + " not found").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to retrieve Warehouse Location: " + e.getMessage()).build();
        }
    }

    @PUT
    @Operation(summary = "Update the Warehouse Location by Id")
    @Path("/{warehouseLocationId}")
    @Transactional
    public Response updateWarehouseLocation(@PathParam("warehouseLocationId") Long warehouseLocationId,
                                            @Valid WarehouseLocation updatedWarehouseLocation) {
        try {
            updatedWarehouseLocation.setWarehouseId(warehouseLocationId);
            WarehouseLocation updated = warehouseLocationService.updateWarehouseLocation(updatedWarehouseLocation);
            return Response.ok(updated).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to update Warehouse Location: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Operation(summary = "Delete the Warehouse Location by Id")
    @Path("/{warehouseLocationId}")
    @Transactional
    public Response deleteWarehouseLocation(@PathParam("warehouseLocationId") Long warehouseLocationId) {
        try {
            boolean deleted = warehouseLocationService.deleteWarehouseLocation(warehouseLocationId);
            if (deleted) {
                return Response.noContent().build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Warehouse Location with ID " + warehouseLocationId + " not found").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to delete Warehouse Location: " + e.getMessage()).build();
        }
    }

}

