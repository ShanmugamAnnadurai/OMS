package org.freshkart.wms.controller;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.freshkart.wms.entity.StorageLocation;
import org.freshkart.wms.service.StorageLocationService;

import java.util.List;

@Path("/storageLocation")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Storage Location REST EndPoint")
public class StorageLocationController {
    @Inject
    StorageLocationService storageLocationService;

    @POST
    @Operation(summary = "Create a New Storage Location")
    @Transactional
    public Response createStorageLocation(@Valid StorageLocation storageLocation) {
        try {
            StorageLocation createdStorageLocation = storageLocationService.createStorageLocation(storageLocation);
            return Response.status(Response.Status.CREATED).entity(createdStorageLocation).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to create Storage Location: " + e.getMessage()).build();
        }
    }

    @GET
    @Operation(summary = "Get All the Storage Locations")
    public List<StorageLocation> getAllStorageLocation() {
        return storageLocationService.getAllStorageLocation();
    }

    @GET
    @Operation(summary = "Get the Storage Location by Id")
    @Path("/{storageLocationId}")
    public Response getStorageLocationById(@PathParam("storageLocationId") Long storageLocationId) {
        try {
            StorageLocation storageLocation = storageLocationService.getStorageLocationById(storageLocationId);
            if (storageLocation != null) {
                return Response.ok(storageLocation).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Storage Location with ID " + storageLocationId + " not found").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to retrieve Storage Location: " + e.getMessage()).build();
        }
    }

    @PUT
    @Operation(summary = "Update the Storage Location by Id")
    @Path("/{storageLocationId}")
    @Transactional
    public Response updateStorageLocation(@PathParam("storageLocationId") Long storageLocationId, @Valid StorageLocation updatedStorageLocation) {
        try {
            updatedStorageLocation.setLocationId(storageLocationId);
            StorageLocation updated = storageLocationService.updateStorageLocation(updatedStorageLocation);
            return Response.ok(updated).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to update Storage Location: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Operation(summary = "Delete the Storage Location by Id")
    @Path("/{storageLocationId}")
    @Transactional
    public Response deleteStorageLocation(@PathParam("storageLocationId") Long storageLocationId) {
        try {
            boolean deleted = storageLocationService.deleteStorageLocation(storageLocationId);
            if (deleted) {
                return Response.noContent().build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Storage Location with ID " + storageLocationId + " not found").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to delete Storage Location: " + e.getMessage()).build();
        }
    }

}
