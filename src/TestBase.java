import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import org.junit.Before;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.requestSpecification;
import static io.restassured.RestAssured.responseSpecification;

public class TestBase {

    @Before
    public void setUp() {

        configureRestAssured();
    }

    public void configureRestAssured() {

        RestAssured.baseURI = "https://petstore.swagger.io/v2";
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setContentType("application/json");
        requestSpecification = requestSpecBuilder.build();

        ResponseSpecBuilder builder = new ResponseSpecBuilder();
        responseSpecification = builder.build();
    }

    public Map<String, Object> createNewPet(int id, Map<String, String> category, String name, List<String> photoUrls, List<Map<String, String>> tagNameList, String status) {

        Map<String, Object> result = new HashMap<>();
        result.put("id", String.valueOf(id));
        result.put("category", category);
        result.put("name", name);
        result.put("photoUrls", photoUrls);
        result.put("tags", tagNameList);
        result.put("status", status);

        return result;
    }
}
