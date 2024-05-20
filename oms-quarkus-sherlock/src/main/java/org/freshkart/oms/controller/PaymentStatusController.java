package org.freshkart.oms.controller;


import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.freshkart.oms.entity.PaymentStatus;
import org.freshkart.oms.service.PaymentStatusService;

import java.util.List;

@Path("/paymentStatus")
@Tag(name = "Payment Status REST EndPoint")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PaymentStatusController {

    @Inject
    PaymentStatusService paymentStatusService;

    @POST
    @Operation(summary = "Create a New Payment Status")
    @Transactional
    public Response createPaymentStatus(@Valid PaymentStatus paymentStatus) {
        try {
            PaymentStatus createdPaymentStatus = paymentStatusService.createPaymentStatus(paymentStatus);
            return Response.status(Response.Status.CREATED).entity(createdPaymentStatus).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to create paymentStatus: " + e.getMessage()).build();
        }
    }

    @GET
    @Operation(summary = "Get All the Payment Status")
    public List<PaymentStatus> getAllPaymentStatus() {
        return paymentStatusService.getAllPaymentStatus();
    }

    @GET
    @Path("/{paymentStatusId}")
    @Operation(summary = "Get the Payment Status by Id")
    public Response getPaymentStatusById(@PathParam("paymentStatusId") Long paymentStatusId) {
        try {
            PaymentStatus paymentStatus = paymentStatusService.getPaymentStatusById(paymentStatusId);
            if (paymentStatus != null) {
                return Response.ok(paymentStatus).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Payment Status with ID " + paymentStatusId + " not found").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to retrieve Payment Status: " + e.getMessage()).build();
        }
    }

    @PUT
    @Operation(summary = "Update the Payment Status by Id")
    @Path("/{paymentStatusId}")
    @Transactional
    public Response updatePaymentStatus(@PathParam("paymentStatusId") Long paymentStatusId, @Valid PaymentStatus updatedPaymentStatus) {
        try {
            updatedPaymentStatus.setPaymentId(paymentStatusId);
            PaymentStatus updated = paymentStatusService.updatePaymentStatus(updatedPaymentStatus);
            return Response.ok(updated).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to update Payment Status: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Operation(summary = "Delete the Payment Status by Id")
    @Path("/{paymentStatusId}")
    @Transactional
    public Response deletePaymentStatus(@PathParam("paymentStatusId") Long paymentStatusId) {
        try {
            boolean deleted = paymentStatusService.deletePaymentStatus(paymentStatusId);
            if (deleted) {
                return Response.noContent().build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Payment Status with ID " + paymentStatusId + " not found").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to delete Payment Status: " + e.getMessage()).build();
        }
    }
}
