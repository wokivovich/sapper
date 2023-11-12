package com.wokivovich.sapper;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ControllerIT {

    @BeforeAll
    static void beforeAll(){
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 5000;
    }

    @Test
    void startGame_returnsBaseField() {
        //Given
        String expected = "[[null, null, null, null, null, null, null, null], " +
                "[null, null, null, null, null, null, null, null], " +
                "[null, null, null, null, null, null, null, null], " +
                "[null, null, null, null, null, null, null, null], " +
                "[null, null, null, null, null, null, null, null], " +
                "[null, null, null, null, null, null, null, null], " +
                "[null, null, null, null, null, null, null, null], " +
                "[null, null, null, null, null, null, null, null]]";

        //When
        String actual = given()
                .get("/api/start/8")
                .jsonPath()
                .get("field")
                .toString();

        //Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void startGame_returnsFieldWithMediumSize() {
        //Given
        int expected = 12;

        //When
        int actual = given()
                .get("/api/start/12")
                .jsonPath()
                .get("field[0]")
                .toString()
                .split(",")
                .length;

        //Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void startGame_hasDefaultStatus() {

        //When-Then
        given()
                .when()
                .get("/api/start/8")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("status", equalTo("EMPTY"));
    }

    @Test
    void postFlag_OneBlockDisabled() {

        //Given
        String expected = "disable";

        //When
        int sessionId = given().when().get("/api/start/8").jsonPath().get("sessionId");
        String request = "[" + sessionId + ", 0, 0]";

        String actual = given()
                .contentType(ContentType.JSON)
                .body(request)
                .post("/api/flag")
                .jsonPath()
                .get("field[0][0]")
                .toString();

        //Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void chooseBlock_HttpStatusOk() {

        //When
        int sessionId = given().when().get("/api/start/8").jsonPath().get("sessionId");
        String request1 = "[" + sessionId + ", 3, 5]";

        //Then
        given()
                .contentType(ContentType.JSON)
                .body(request1)
                .post("/api/play")
                .then()
                .statusCode(200);
    }
}
