import dto.PostImageResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static Endpoints.Endpoints.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)


public class ChangeTest extends BaseTest{
//тесты на изменение параметров изображений
static         String uploadedImageDeleteHash1  ;

    @Order(1)
    @Test
    void test6UploadFileBaseImageTestWithInfo(){
        postImageResponse = given (requestSpecificationWithAuthWithBase64Info, responseSpecificationWithInfo)
                .post(UPLOAD_IMG)
                .prettyPeek()
                .then()
                .extract()
                .body()
                .as(PostImageResponse.class);
        uploadedImageDeleteHash = postImageResponse.getData().getDeletehash();
        uploadedImageHash = postImageResponse.getData().getId();
        uploadedImageDeleteHash1 = uploadedImageDeleteHash;

    }

    @Order(2)
    @Test
        //2
    void test7FavoriteImage(){
        Response response = given(requestSpecificationWithAuth, responseSpecificationFavorite)
                .post("https://api.imgur.com/3/image/{imageHash}/favorite", uploadedImageHash)
                .prettyPeek();



    }
    @Order(3)
    @Test //3
    void test8UFavoriteImage(){
        Response response = given(requestSpecificationWithAuth)
                .pathParam("imageHash",uploadedImageHash)
                .expect()
                .body("data",is("unfavorited"))
                .when()
                .post("https://api.imgur.com/3/image/{imageHash}/favorite")
                .prettyPeek();

    }

    @Order(4)
    @Test //4
    void test9UpdateImage(){
        Response response =  given(requestSpecificationWithAuthAndMultipartImageInfoChange, positiveResponseSpecification)
                .post(UPDATE, uploadedImageDeleteHash)
                .prettyPeek();

    }

    @Order(5)
    @Test //5
    void test10GetImage(){
        Response response =  given(requestSpecificationWithAuth, responseSpecificationWithInfoChange)
                .get("https://api.imgur.com/3/image/"+uploadedImageHash)
                .prettyPeek();
    }
}

