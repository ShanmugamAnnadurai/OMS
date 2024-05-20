package org.freshkart.oms.controller;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.freshkart.oms.entity.Customer;
import org.freshkart.oms.service.CustomerService;



import java.util.List;



@Path("/customers")
@Tag(name = "Customer REST EndPoint")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerController {

    @Inject
    CustomerService customerService;
    @POST
    @Operation(summary = "Create a New Customer")
    @Transactional
    public Response createCustomer(@Valid Customer customer) {
        try {
            Customer createdCustomer = customerService.createCustomer(customer);
            return Response.status(Response.Status.CREATED).entity(createdCustomer).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to create customer: " + e.getMessage()).build();
        }
    }

    @GET
    @Operation(summary = "Get All Customers")
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomer();
    }

    @GET
    @Operation(summary = "Get the Customer by Id")
    @Path("/{customerId}")
    public Response getCustomerById(@PathParam("customerId") Long customerId) {
        try {
            Customer customer = customerService.getCustomerById(customerId);
            if (customer != null) {
                return Response.ok(customer).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Customer with ID " + customerId + " not found").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to retrieve customer: " + e.getMessage()).build();
        }
    }

    @PUT
    @Operation(summary = "Update the Customer by Id")
    @Path("/{customerId}")
    @Transactional
    public Response updateCustomer(@PathParam("customerId") Long customerId, @Valid Customer updatedCustomer) {
        try {
            updatedCustomer.setCustomerId(customerId);
            Customer updated = customerService.updateCustomer(updatedCustomer);
            return Response.ok(updated).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to update customer: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Operation(summary = "Delete the Customer by Id")
    @Path("/{customerId}")
    @Transactional
    public Response deleteCustomer(@PathParam("customerId") Long customerId) {
        try {
            boolean deleted = customerService.deleteCustomer(customerId);
            if (deleted) {
                return Response.noContent().build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Customer with ID " + customerId + " not found").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to delete customer: " + e.getMessage()).build();
        }
    }





}
