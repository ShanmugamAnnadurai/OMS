package org.freshkart.oms;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.freshkart.oms.entity.Customer;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;



public class CustomerControllerTest {
    @Test
    @Transactional
    public void testCreateCustomer() {
        Customer customer = new Customer();
        customer.setName("Shanmugam");
        customer.setContactInfo("+91 8124777042");
        customer.setAddress("Big Street, Panapakkam");

        given()
                .contentType(ContentType.JSON)
                .body(customer)
                .when()
                .post("/customers")
                .then()
                .statusCode(201)
                .body("name", equalTo("Shanmugam"));
    }

    @Test
    public void testGetAllCustomers() {
        given()
                .when()
                .get("/customers")
                .then()
                .statusCode(200)
                .body(not(empty()));
    }

    @Test
    public void testGetCustomerById() {
        Long existingCustomerId = 1L;

        given()
                .pathParam("customerId", existingCustomerId)
                .when()
                .get("/customers/{customerId}")
                .then()
                .statusCode(200)
                .body("customerId", equalTo(existingCustomerId.intValue()));
    }

    @Test
    @Transactional
    public void testUpdateCustomer() {
        Long existingCustomerId = 1L;
        Customer updatedCustomer = new Customer();
        updatedCustomer.setCustomerId(existingCustomerId);
        updatedCustomer.setName("Updated Name");

        given()
                .pathParam("customerId", existingCustomerId)
                .contentType(ContentType.JSON)
                .body(updatedCustomer)
                .when()
                .put("/customers/{customerId}")
                .then()
                .statusCode(200)
                .body("name", equalTo("Updated Name"));
    }

    @Test
    @Transactional
    public void testDeleteCustomer() {
        Long existingCustomerId = 1L;

        given()
                .pathParam("customerId", existingCustomerId)
                .when()
                .delete("/customers/{customerId}")
                .then()
                .statusCode(204);

        // Verify the customer is deleted
        given()
                .pathParam("customerId", existingCustomerId)
                .when()
                .get("/customers/{customerId}")
                .then()
                .statusCode(404);
    }
}
