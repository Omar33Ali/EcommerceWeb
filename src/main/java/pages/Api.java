package pages;

import io.qameta.allure.Allure;
import utils.ConfigReader;
import utils.Log;
import utils.PageBase;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.config.EncoderConfig;
import io.restassured.RestAssured;

import java.nio.charset.StandardCharsets;

import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class Api extends PageBase {

    ConfigReader configReader = new ConfigReader();

    public Api(WebDriver driver) {
        super(driver);
    }

    public Response registerUserViaApi(String name, String email, String password) {
        String url = configReader.getProperty("registerURL");
        Log.info("Register URL: " + url);
        Map<String, String> payload = new HashMap<>();
        payload.put("name", name);
        payload.put("email", email);
        payload.put("password", password);
        Response resp = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(payload)
                .when()
                .post(url)
                .andReturn();
        System.out.println("Status: " + resp.getStatusCode());
        System.out.println("Response Body: " + resp.getBody().asPrettyString());
        return resp;
    }

    public Response updatePasswordViaApi(String token, String currentPassword, String newPassword) {

        String url = configReader.getProperty("updatePasswordURL");
        Log.info("Update Password URL: " + url);
        Map<String, String> jsonPayload = new HashMap<>();
        jsonPayload.put("currentPassword", currentPassword);
        jsonPayload.put("newPassword", newPassword);

        Response resp = given()
                .log().all()
                .config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig()
                        .defaultContentCharset(StandardCharsets.UTF_8)
                        .appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .accept("application/json")
                .header("x-auth-token", token)
                .formParam("currentPassword", currentPassword)
                .formParam("newPassword", newPassword)
                .post(url)
                .then()
                .extract()
                .response();

        Log.info("Update Password Status: " + resp.getStatusCode());
        Log.info("Update Password Response Body: " + resp.getBody().asPrettyString());
        Allure.addAttachment("Update Password Response", "application/json", resp.getBody().asPrettyString());
        return resp;
    }


    public String loginAndGetToken(String email, String password) {
        String url = configReader.getProperty("loginURL");
        Log.info("Login URL: " + url);
        Map<String, String> payload = new HashMap<>();
        payload.put("email", email);
        payload.put("password", password);
        Response resp = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(payload)
                .when()
                .post(url)
                .andReturn();
        Log.info("Login Status: " + resp.getStatusCode());
        Log.info("Login Response Body: " + resp.getBody().asPrettyString());
        Allure.addAttachment("Login Response", "application/json", resp.getBody().asPrettyString());
        String token = resp.jsonPath().getString("data.token");
        Log.info("Extracted token: " + token);
        return token;
    }

    public Response updateNoteViaApi(String token, String noteId, String newTitle, String newDescription, String category, boolean completed) {
        String url = configReader.getProperty("noteURL") + noteId;
        Log.info("Update Note URL: " + url);
        Map<String, Object> payload = new HashMap<>();
        payload.put("title", newTitle);
        payload.put("description", newDescription);
        payload.put("category", category);
        payload.put("completed", completed);

        Response resp = given()
                .log().all()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("x-auth-token", token)
                .body(payload)
                .put(url)
                .then()
                .extract()
                .response();

        Log.info("Update Note Status: " + resp.getStatusCode());
        Log.info("Update Note Response Body: " + resp.getBody().asPrettyString());
        Allure.addAttachment("Update Note Response", "application/json", resp.getBody().asPrettyString());

        return resp;
    }


    public Response createNoteViaApi(String token, String title, String description, String category) {
        String url = configReader.getProperty("noteURL");
        Log.info("Create Note URL: " + url);
        Map<String, String> payload = new HashMap<>();
        payload.put("title", title);
        payload.put("description", description);
        payload.put("category", category);

        Response resp = given()
                .log().all()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("x-auth-token", token)
                .body(payload)
                .post(url)
                .then()
                .extract()
                .response();

        Log.info("Create Note Status: " + resp.getStatusCode());
        Log.info("Create Note Response Body: " + resp.getBody().asPrettyString());
        Allure.addAttachment("Create Note Response", "application/json", resp.getBody().asPrettyString());
        return resp;
    }

    public Response getNoteById(String token, String noteId) {
        String url = configReader.getProperty("noteURL") + noteId;
        Log.info("Get Note URL: " + url);
        Response resp = given()
                .log().all()
                .accept(ContentType.JSON)
                .header("x-auth-token", token)
                .get(url)
                .then()
                .log().all()
                .extract()
                .response();

        Log.info("Get Note Status: " + resp.getStatusCode());
        Log.info("Get Note Response Body: " + resp.getBody().asPrettyString());
        Allure.addAttachment("Get Note Response", "application/json", resp.getBody().asPrettyString());
        return resp;
    }

    public Response deleteNoteViaApi(String token, String noteId) {
        String url = configReader.getProperty("noteURL") + noteId;
        Log.info("Delete Note URL: " + url);
        Response resp = given()
                .log().all()
                .accept(ContentType.JSON)
                .header("x-auth-token", token)
                .delete(url)
                .then()
                .log().all()
                .extract()
                .response();
        Log.info("Delete Note Status: " + resp.getStatusCode());
        Log.info("Delete Note Response Body: " + resp.getBody().asPrettyString());
        Allure.addAttachment("Delete Note Response", "application/json", resp.getBody().asPrettyString());

        return resp;
    }

}
