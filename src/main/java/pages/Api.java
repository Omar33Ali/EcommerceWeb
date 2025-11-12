package pages;
import utils.PageBase;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class Api extends PageBase {

    public Api(WebDriver driver) {
        super(driver);
    }

    public String getToken() {

        Cookie cookie = driver.manage().getCookieNamed("orangehrm");
        if (cookie == null) {
            System.out.println("Cookie 'token' not found.");
        }
        String value = cookie.getValue();
        System.out.println("Extracted token: " + value);

        return value;
    }

    public Response addNewEmployeeUsingApi() {

        String baseUrl = "https://opensource-demo.orangehrmlive.com/web/index.php/api/v2/";
        String endpoint = "pim/employees";
        String id = randomString(5);

        Map<String, Object> payLoad = new HashMap<>();
        payLoad.put("firstName", "Ayman");
        payLoad.put("middleName", "Weal");
        payLoad.put("lastName", "Ali");
        payLoad.put("empPicture", null);
        payLoad.put("employeeId", id);

        Response resp = given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Origin", "https://opensource-demo.orangehrmlive.com")
                .header("Referer", "https://opensource-demo.orangehrmlive.com/web/index.php/pim/addEmployee")
                .header("User-Agent", "Mozilla/5.0")
                .cookie("orangehrm", getToken())
                .body(payLoad)
                .when()
                .post(endpoint)
                .andReturn();

        System.out.println("Status: " + resp.getStatusCode());
        System.out.println("Response Body: " + resp.getBody().asPrettyString());
        String empNumber = resp.path("data.empNumber") == null ? null : resp.path("data.empNumber").toString();
        String employeeId = resp.path("data.employeeId") == null ? null : resp.path("data.employeeId").toString();

        System.out.println("empNumber = " + empNumber);
        System.out.println("employeeId = " + employeeId);

        return resp;

    }

    public static List<String> extractIds(Response resp) {
        Object empNumberObj = resp.path("data.empNumber");
        Object employeeIdObj = resp.path("data.employeeId");

        String empNumber = empNumberObj != null ? empNumberObj.toString() : null;
        String employeeId = employeeIdObj != null ? employeeIdObj.toString() : null;

        return Arrays.asList(empNumber, employeeId);
    }

    public void updateEmployeeDataUsingApi(String empNumber, String employeeId) {

        String baseUrl = "https://opensource-demo.orangehrmlive.com/web/index.php/api/v2/";

        Map<String, Object> payload = new HashMap<>();
        payload.put("lastName", "QC");
        payload.put("firstName", "Mostafa");
        payload.put("middleName", "Weal");
        payload.put("employeeId", employeeId);
        payload.put("otherId", "5");
        payload.put("drivingLicenseNo", "5845");
        payload.put("drivingLicenseExpiredDate", "2025-08-31");
        payload.put("gender", "1");
        payload.put("maritalStatus", "Single");
        payload.put("birthday", "1998-11-30");
        payload.put("nationalityId", 55);



        Response resp = given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Origin", "https://opensource-demo.orangehrmlive.com")
                .header("Referer", "https://opensource-demo.orangehrmlive.com/web/index.php/pim/addEmployee")
                .header("User-Agent", "Mozilla/5.0")
                .cookie("orangehrm", getToken())
                .body(payload)
                .when()
                .put("pim/employees/" + empNumber + "/personal-details")
                .andReturn();


        System.out.println("Status Code: " + resp.getStatusCode());
        System.out.println("Response Body: " + resp.getBody().asPrettyString());

    }


}
