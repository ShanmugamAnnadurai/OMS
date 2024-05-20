package org.freshkart.ims.controller;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.freshkart.ims.entity.Supplier;
import org.freshkart.ims.service.SupplierService;
import org.freshkart.oms.service.PurchaseOrderService;

import java.util.List;

@Path("/supplier")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Supplier REST EndPoint")
public class SupplierController {
    @Inject
    SupplierService supplierService;

    @POST
    @Operation(summary = "Create a New Supplier")
    @Transactional
    public Response createSupplier(@Valid Supplier supplier) {

        try {
            Supplier createdSupplier = supplierService.createSupplier(supplier);

            return Response.status(Response.Status.CREATED).entity(createdSupplier).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to create Product: " + e.getMessage()).build();
        }
    }

    @GET
    @Operation(summary = "Get All Suppliers")
    public List<Supplier> getAllSupplier() {
        return supplierService.getAllSupplier();
    }

    @GET
    @Operation(summary = "Get the Supplier by Id")
    @Path("/{supplierId}")
    public Response getSupplierById(@PathParam("supplierId") Long supplierId) {
        try {
            Supplier supplier = supplierService.getSupplierById(supplierId);
            if (supplier != null) {
                return Response.ok(supplier).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Supplier with ID " + supplier + " not found").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to retrieve supplier: " + e.getMessage()).build();
        }
    }

    @PUT
    @Operation(summary = "Update the Supplier by Id")
    @Path("/{supplierId}")
    @Transactional
    public Response updateSupplier(@PathParam("supplierId") Long supplierId, @Valid Supplier updatedSupplier) {
        try {
            updatedSupplier.setSupplierId(supplierId);
            Supplier updated = supplierService.updateSupplier(updatedSupplier);
            return Response.ok(updated).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to update Supplier: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Operation(summary = "Delete the Supplier by Id")
    @Path("/{supplierId}")
    @Transactional
    public Response deleteSupplier(@PathParam("supplierId") Long supplierId) {
        try {
            boolean deleted = supplierService.deleteSupplier(supplierId);
            if (deleted) {
                return Response.noContent().build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Supplier with ID " + supplierId + " not found").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to delete Supplier: " + e.getMessage()).build();
        }
    }
}
