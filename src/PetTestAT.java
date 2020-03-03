import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.requestSpecification;

public class PetTestAT extends TestBase {

    @Test
    public void testPet() {

        // step 1 - create and return a new pet with some attributes

        Map<String, String> category = new HashMap<>();
        category.put("id", "12345");
        category.put("name", "doggie");

        List<String> photoUrls = new ArrayList();
        photoUrls.add("testPhotoUrl12345");

        List<Map<String, String>> tagNameList = new ArrayList<Map<String, String>>();

        Map<String, String> tagName = new HashMap<>();
        tagName.put("id", "54321");
        tagName.put("name", "goodDog");

        tagNameList.add(tagName);

        // Assemble request payload for creating new pet
        Map<String, Object> newPetPayload = createNewPet(111, category, "Friday", photoUrls, tagNameList, "available");

        // Send API call to create a new pet into pet store
        Response newPetCreationresponse = given().spec(requestSpecification).
                body(newPetPayload).
                accept("application/json").
                contentType("application/json").
                when().
                post("/pet");

        Assert.assertEquals(200, newPetCreationresponse.statusCode());


        // step 2 - Verify The pet was created with correct data
        // Send API call to retrieve the pet created at step 1

        Response getPetResponse = given().spec(requestSpecification).
                accept("application/json").
                contentType("application/json").
                when().
                get("/pet/111");

        Assert.assertEquals(200, getPetResponse.statusCode());


        // Validate the values of new record created
        JsonPath jsonPathEvaluator = getPetResponse.jsonPath();
        Assert.assertEquals(jsonPathEvaluator.get("id").toString(), "111");

        Map<String, String> returnedCategory = jsonPathEvaluator.get("category");
        Assert.assertEquals("12345", String.valueOf(returnedCategory.get("id")));

        Assert.assertEquals("doggie", returnedCategory.get("name"));

        List<String> returnedPhotoUrls = jsonPathEvaluator.get("photoUrls");
        Assert.assertEquals("testPhotoUrl12345", returnedPhotoUrls.get(0));

        List<Map<String, String>> returnedTags = jsonPathEvaluator.get("tags");

        Map<String, String> firstTag = returnedTags.get(0);
        Assert.assertEquals("54321", String.valueOf(firstTag.get("id")));
        Assert.assertEquals("goodDog", firstTag.get("name"));

        Assert.assertEquals(jsonPathEvaluator.get("status"), "available");


        // step 3 - Update this pet name , verify the update and return the record

        Map<String, Object> updatedPetNamePayload = createNewPet(111, category, "UpdatedFriday", photoUrls, tagNameList, "available");

        // Send API call to update an existing pet in pet store
        Response updatedPetResponse = given().spec(requestSpecification).
                body(updatedPetNamePayload).
                accept("application/json").
                contentType("application/json").
                when().
                put("/pet");

        Assert.assertEquals(200, updatedPetResponse.statusCode());

        JsonPath updatedPetJsonPathEvaluator = updatedPetResponse.jsonPath();
        Assert.assertEquals("UpdatedFriday", updatedPetJsonPathEvaluator.get("name"));


        // step 4 - Delete the Pet and demonstrate the pet now deleted

        Response deletedPetResponse = given().spec(requestSpecification).
                accept("application/json").
                contentType("application/json").
                when().
                delete("/pet/111");

        // http status 404 should return as pet with id 111 was just being deleted
        Response newGetPetResponse = given().
                accept("application/json").
                contentType("application/json").
                when().
                delete("/pet/111");

        Assert.assertEquals(404, newGetPetResponse.statusCode());
    }
}
