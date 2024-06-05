package api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ApiDeliveryTest {
    public static final String BASE_URL = "https://backend.tallinn-learning.ee";
    public static final String BASE_PATH = "/test-orders/";
    // Homework_10
    //PUT _problem
    @Test
    public void changeOrderDetails() {
        String requestBodyChangeOldMethod = "{\n" +
                "  \"status\": \"OPEN\",\n" +
                "  \"courierId\": 0,\n" +
                "  \"customerName\": \"TATA\",\n" +
                "  \"customerPhone\": \"string\",\n" +
                "  \"comment\": \"string\",\n" +
                "  \"id\": 0\n" +
                "} ";
        String reciveCustomerName = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(requestBodyChangeOldMethod)
                .log()
                .all()
                .header("api_key", "1111222233334444")
                .put(BASE_URL + BASE_PATH + "10")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .extract()
                .path("customerNames");
        Assertions.assertEquals("TATA", reciveCustomerName);
    }

    // DELETE _is OK
    public void deleteOrderId() {
        RestAssured
                .given()
                .log()
                .all()
                .header("api_key", "3333114455667788")
                .delete(BASE_URL + BASE_PATH + "4")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    // GET with query params
    @Test
    public void authenticateUser() {
        RestAssured
                .given()
                .queryParam("username", "Tata")
                .queryParam("password", "123456")
                .log()
                .all()
                .get(BASE_URL + BASE_PATH)
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
    }

    // Lesson_10

    @Test
    public void checkOrderDetailsWithCorrectOrderId() {
        RestAssured
                .given()
                .log()
                .all()
                .get(BASE_URL + BASE_PATH + "1")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void checkErrorStatusCodeWithIncorrectOrderId() {
        RestAssured
                .given()
                .log()
                .all()
                .get(BASE_URL + BASE_PATH + "20")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void checkErrorStatusCodeWithNegativeNumberOrderId() {
        RestAssured
                .given()
                .log()
                .all()
                .get(BASE_URL + BASE_PATH + "-20")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void checkOrderDetailsWithCorrectOrderIdAndCheckOrderStatus() {
        String receiveOrderStatus = RestAssured
                .given()
                .log()
                .all()
                .get(BASE_URL + BASE_PATH + "4")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .extract()
                .path("status");

        Assertions.assertEquals("OPEN", receiveOrderStatus);
    }

    @Test
    public void checkOrderIdInResponse() {
        int orderIdRequested = 4;
        int receivedOrderID = RestAssured
                .given()
                .log()
                .all()
                .get(BASE_URL + BASE_PATH + orderIdRequested)
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .extract()
                .path("id");

        Assertions.assertEquals(orderIdRequested, receivedOrderID);
    }

    @Test
    public void checkOrderCreation() {
        String requestBodyUglyWay = "{\n" +
                "  \"status\": \"OPEN\",\n" +
                "  \"courierId\": 0,\n" +
                "  \"customerName\": \"Tata\",\n" +
                "  \"customerPhone\": \"11223344\",\n" +
                "  \"comment\": \"hello\",\n" +
                "  \"id\": 0\n" +
                "} ";

        String receivedStatus = RestAssured
                .given()
                .contentType(ContentType.JSON)
                //.queryParam("username", "Olga")
                //.queryParam("password", "123456")
                .body(requestBodyUglyWay)
                .log()
                .all()
                .post(BASE_URL + BASE_PATH)
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .extract()
                .path("status");
        Assertions.assertEquals("OPEN", receivedStatus);
    }

    @Test
    public void createOrderWithIncorrectStatusAndCheckResponseMessage() {
        String requestBodyUglyWay = "{\n" +
                "  \"status\": \"CLOSED\",\n" +
                "  \"courierId\": 0,\n" +
                "  \"customerName\": \"Tata\",\n" +
                "  \"customerPhone\": \"11223344\",\n" +
                "  \"comment\": \"hello\",\n" +
                "  \"id\": 0\n" +
                "} ";

        String responseBody = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(requestBodyUglyWay)
                .log()
                .all()
                .post(BASE_URL + BASE_PATH)
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .extract()
                .asString();
        Assertions.assertEquals("Incorrect query", responseBody);
    }


}
