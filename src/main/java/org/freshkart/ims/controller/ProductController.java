package org.freshkart.ims.controller;


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


import java.util.List;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Product REST EndPoint")
public class ProductController {

    @Inject
    ProductService productService;

    @POST
    @Transactional
    @Operation(summary = "Create a New Product")
    public Response createProduct(@Valid Product product) {
        try {
            Product createdProduct = productService.createProduct(product);
            return Response.status(Response.Status.CREATED).entity(createdProduct).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to create Product: " + e.getMessage()).build();
        }
    }

    @GET
    @Operation(summary = "Get All Product")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GET
    @Operation(summary = "Get the Product by Id")
    @Path("/{productId}")
    public Response getProductById(@PathParam("productId") Long productId) {
        try {
            Product product = productService.getProductById(productId);
            if (product != null) {
                return Response.ok(product).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Product with ID " + productId + " not found").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to retrieve Product: " + e.getMessage()).build();
        }
    }

    @PUT
    @Operation(summary = "Update the Product by Id")
    @Path("/{productId}")
    @Transactional
    public Response updateProduct(@PathParam("productId") Long productId, @Valid Product updatedProduct) {
        try {
            updatedProduct.setProductId(productId);
            Product updated = productService.updateProduct(updatedProduct);
            return Response.ok(updated).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to update product: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Operation(summary = "Delete the Product by Id")
    @Path("/{productId}")
    @Transactional
    public Response deleteProduct(@PathParam("productId") Long productId) {
        try {
            boolean deleted = productService.deleteProduct(productId);
            if (deleted) {
                return Response.noContent().build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Product with ID " + productId + " not found").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to delete product: " + e.getMessage()).build();
        }
    }
}
