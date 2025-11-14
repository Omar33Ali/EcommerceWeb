package testPackage;

import base.TestBase;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.Api;
import utils.DataReader;
import utils.Log;

import static io.qameta.allure.Allure.addAttachment;
import static utils.PageBase.*;

public class ApiE2EScenariosTest extends TestBase {

    Api api = new Api(driver);
    SoftAssert softassert ;
    DataReader dataReader = new DataReader();

    String name = generateUsername("user");
    String email = generateRandomEmail();
    String password = generateRandomPassword();
    @Description("Register a new user via API and verify creation")
    @Story("User Registration and Password Update via API")
    @Test()
    public void registerNewUserUsingApi() {
        softassert = new SoftAssert();
        // Generate random user data

        // Register user via API
        Log.info("Registering user via API with Name: " + name + ", Email: " + email);
        Allure.step("Registering user via API with Name: " + name + ", Email: " + email);
        Response response = api.registerUserViaApi(name, email, password);
        // Log response to Allure report
        Log.info("API Response: " + response.getBody().asPrettyString());
        Allure.step("API Response: " + response.getBody().asPrettyString());
        addAttachment("API Response", "application/json", response.getBody().asPrettyString());
        // Verify user is created
        Log.info("Verifying user creation via API response");
        Allure.step("Verifying user creation via API response");
        softassert.assertTrue(response.getStatusCode() == 201,
                "Expected status code 201 , but got: " + response.getStatusCode());
        softassert.assertEquals(response.jsonPath().getString("data.name"), name, "Response should contain the correct username");
        softassert.assertEquals(response.jsonPath().getString("data.email"), email, "Response should contain the correct email");
        softassert.assertAll();
    }

    @Test( dependsOnMethods = {"registerNewUserUsingApi"})
    @Description("Update password for existing user via API and verify update")
    @Story("User Registration and Password Update via API")
    public void updatePasswordForExistingUser() {
        softassert = new SoftAssert();
        // Existing user data
        String newPassword =generateRandomPassword();
        String token = api.loginAndGetToken(email, password);
        Log.info("Token received for password update: " + token);
        Allure.step("Token received for password update: " + token);

        // Update password via API
        Log.info("Updating password for user: " + email);
        Response response = api.updatePasswordViaApi(token, password, newPassword);
        Log.info("API Response: " + response.getBody().asPrettyString());
        addAttachment("API Password Update Response", "application/json", response.getBody().asPrettyString());

        // Verify password is updated
        Log.info("Verifying password update via API response");
        softassert.assertTrue(response.getStatusCode() == 200,
                "Expected status code 200, but got: " + response.getStatusCode());
        softassert.assertTrue(response.getBody().asString().toLowerCase().contains("success"), "Response should indicate success");
        softassert.assertAll();
    }

    @Test( dependsOnMethods = {"registerNewUserUsingApi"})
    @Description("Create, update, and delete a note via API")
    @Story("Note Creation, Update, and Deletion via API")
    public void createUpdateAndDeleteNoteViaApi() {
        softassert = new SoftAssert();

        Log.info("Logging in to get token for note operations for user: " + email);
        Allure.step("Logging in to get token for note operations for user: " + email);

        String token = api.loginAndGetToken(email, password);

        Log.info("Token received for note operations: " + token);
        Allure.step("Token received for note operations: " + token);

        Log.info("**** Creating a new note via API ****");
        Allure.step("**** Creating a new note via API ****");
        Response createResp = api.createNoteViaApi(token,
                (String) testData.get("initialTitle"),
                (String) testData.get("initialDescription"),
                (String) testData.get("category"));

        softassert.assertEquals(createResp.getStatusCode(), 200);
        String noteId = createResp.jsonPath().getString("data.id");

        Log.info("Note created with ID: " + noteId);
        Allure.step("Note created with ID: " + noteId);

        String updatedTitle = "Updated Note Title";
        String updatedDescription = "This is the updated content.";
        Log.info("**** Updating the note via API ****");
        Allure.step("**** Updating the note via API ****");
        Response updateResp = api.updateNoteViaApi(token, noteId,
                (String) testData.get("updatedTitle"),
                (String) testData.get("updatedDescription"),
                (String) testData.get("category"),
                false);
        softassert.assertEquals(updateResp.getStatusCode(), 200," Note update should return status 200");

        Log.info("**** Verifying the updated note via API ****");
        Allure.step("**** Verifying the updated note via API ****");
        Response getResp = api.getNoteById(token, noteId);
        softassert.assertEquals(getResp.getStatusCode(), 200," Fetching updated note should return status 200");
        softassert.assertEquals(getResp.jsonPath().getString("data.title"), testData.get("updatedTitle")," Note title should be updated");
        softassert.assertEquals(getResp.jsonPath().getString("data.description"), testData.get("updatedDescription")," Note description should be updated");

        Log.info("**** Deleting the note via API ****");
        Allure.step("**** Deleting the note via API ****");
        // Delete note
        Response deleteResp = api.deleteNoteViaApi(token, noteId);
        softassert.assertEquals(deleteResp.getStatusCode(), 200, "Note deletion should return status 200");
        // Verify note is deleted
        Response getAfterDeleteResp = api.getNoteById(token, noteId);
        softassert.assertTrue(getAfterDeleteResp.getStatusCode() == 404 || getAfterDeleteResp.jsonPath().getString("data") == null,
                "Note should not be found after deletion");

        softassert.assertAll();
    }

}
