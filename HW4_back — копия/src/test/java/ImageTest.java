import dto.PostImageResponse;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static Endpoints.Endpoints.*;
import static io.restassured.RestAssured.given;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ImageTest extends BaseTest{





@Order(1)
    @Test //1
void test1UploadFileImageTestWithNoInfo(){
        postImageResponse = given (requestSpecificationWithAuthAndMultipartImage, positiveResponseSpecification)
                .post(UPLOAD_IMG)
                .prettyPeek()
                .then()
                .extract()
                .body()
                .as(PostImageResponse.class);
        uploadedImageDeleteHash = postImageResponse.getData().getDeletehash();
    }

    @After
    void tearDown1(){
        given(requestSpecificationWithAuth, responsePositiveSpecification)

                .delete(DEL_IMG, username, uploadedImageDeleteHash)

                .prettyPeek();
    }

    @Order(2)
@Test //2
    void test2UploadFileImageTestWithNoInfoCheckRegEx(){
        postImageResponse = given (requestSpecificationWithAuthAndMultipartImage, responseSpecificationRegEx)
                .post(UPLOAD_IMG)
                .prettyPeek()
                .then()
                .extract()
                .body()
                .as(PostImageResponse.class);
        uploadedImageDeleteHash = postImageResponse.getData().getDeletehash();

    }

    @After
    void tearDown2(){
        given(requestSpecificationWithAuth, responsePositiveSpecification)
                .delete(DEL_IMG, username, uploadedImageDeleteHash)
                .prettyPeek();
    }

    @Order(3)
    @Test //3
    void test3UploadFileImageTestWithInfo(){
        postImageResponse = given (requestSpecificationWithAuthAndMultipartImageInfo, responseSpecificationWithInfo)
                .post(UPLOAD_IMG)
                .prettyPeek()
                .then()
                .extract()
                .body()
                .as(PostImageResponse.class);
        uploadedImageDeleteHash = postImageResponse.getData().getDeletehash();

    }

    @After
    void tearDown3(){
        given(requestSpecificationWithAuth, responsePositiveSpecification)
                .delete(DEL_IMG, username, uploadedImageDeleteHash)
                .prettyPeek();
    }

    @Order(4)
@Test //4
    void test4UploadFileBaseImageTestWithNoInfo(){

            postImageResponse = given (requestSpecificationWithAuthWithBase64, positiveResponseSpecification)
                    .post(UPLOAD_IMG)
                    .prettyPeek()
                    .then()
                    .extract()
                    .body()
                    .as(PostImageResponse.class);
        uploadedImageDeleteHash = postImageResponse.getData().getDeletehash();
        }

    void tearDown4(){
        given(requestSpecificationWithAuth, responsePositiveSpecification)
                .delete(DEL_IMG, username, uploadedImageDeleteHash)
                .prettyPeek();
    }
    @Order(5)
    @Test //5
    void test5UploadFileBaseImageTestWithNoInfoCheckRegEx(){
        postImageResponse = given (requestSpecificationWithAuthWithBase64, responseSpecificationRegEx)
                .post(UPLOAD_IMG)
                .prettyPeek()
                .then()
                .extract()
                .body()
                .as(PostImageResponse.class);
        uploadedImageDeleteHash = postImageResponse.getData().getDeletehash();
    }

  /*  void tearDown5(){
        given(requestSpecificationWithAuth, responseSpecificationChange)
                .delete(DEL_IMG, username, uploadedImageDeleteHash)
                .prettyPeek();
    }*/
    @Order(6)
    @Test //6
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

    }

    /* @After
     void tearDown6(){
         given(requestSpecificationWithAuth, responseSpecificationChange)
                 .delete(DEL_IMG, username, uploadedImageDeleteHash)
                 .prettyPeek();
     }*/
    @Order(7)
    @Test //7
    void test7FavoriteImage(){
        Response response = given(requestSpecificationWithAuth, responseSpecificationFavorite)
                .post("https://api.imgur.com/3/image/{imageHash}/favorite", uploadedImageHash)
                .prettyPeek();



    }

  /*  @After
    void tearDown10(){
        given()
                .header("Authorization",token)
                .when()
                .delete("https://api.imgur.com/3/account/{username}/image/{deletehash}", username, uploadedImageDeleteHash)
                .prettyPeek()
                .then()
                .statusCode(200);
    }*/


}




