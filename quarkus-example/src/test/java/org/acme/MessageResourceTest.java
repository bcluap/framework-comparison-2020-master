package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class MessageResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/hello/John")
          .then()
             .statusCode(200)
             .body(is("Hello John"));
    }
    
    @Test
    public void testHelloEndpoint2() {
        given()
          .when().get("/hello2/John")
          .then()
             .statusCode(200)
             .body(is("Hello John"));
    }

}