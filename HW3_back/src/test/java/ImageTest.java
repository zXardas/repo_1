import io.restassured.response.Response;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.*;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ImageTest extends BaseTest{
    private final String PATH_TO_IMAGE="src/test/resources/test_img.jpg";
    static String encodedFile;
    static String uploadedImageDeleteHash;
    static String uploadedImageHash;
    @BeforeEach
    void beforeTest(){
        byte[] byteArray=getFileContent();
        encodedFile= Base64.getEncoder().encodeToString(byteArray);

    }

/*@Test //1
    void uploadFileImageTestWithNoInfo(){
        uploadedImageDeleteHash=given()
                .header("Authorization",token)
                .multiPart("image", new File("src/test/resources/test_img.jpg"))
                .expect()
                .statusCode(200)
                .when()
                .post("https://api.imgur.com/3/image")
                .prettyPeek()
                .then()
                .extract().response().jsonPath().getString("data.deletehash");

}*/

@Order(1)
    @Test //1
    void test1UploadFileImageTestWithNoInfo(){
        Response response =given()
                .header("Authorization",token)
                .multiPart("image", new File("src/test/resources/test_img.jpg"))
                .expect()
                .statusCode(200)
                .when()
                .post("https://api.imgur.com/3/image")
                .prettyPeek();
        uploadedImageDeleteHash= response.jsonPath().getString("data.deletehash");
    }
    @After
    void tearDown1(){
        given()
                .header("Authorization",token)
                .when()
                .delete("https://api.imgur.com/3/account/{username}/image/{deletehash}", username, uploadedImageDeleteHash)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    @Order(2)
@Test //2
    void test2UploadFileImageTestWithNoInfoCheckRegEx(){
        Response response =given()
                .header("Authorization",token)
                .multiPart("image", new File("src/test/resources/test_img.jpg"))
                .expect()
                .statusCode(200)
                .when()
                .post("https://api.imgur.com/3/image")
                .prettyPeek();
        uploadedImageDeleteHash= response.jsonPath().getString("data.deletehash");
        uploadedImageHash = response.jsonPath().getString("data.id");
        assertThat(uploadedImageHash,matchesPattern("[A-Za-z0-9]{7}"));
        assertThat(uploadedImageDeleteHash,matchesPattern("[A-Za-z0-9]{15}"));
    }

    @After
    void tearDown2(){
        given()
                .header("Authorization",token)
                .when()
                .delete("https://api.imgur.com/3/account/{username}/image/{deletehash}", username, uploadedImageDeleteHash)
                .prettyPeek()
                .then()
                .statusCode(200);
    }
    @Order(3)
    @Test //3
    void test3UploadFileImageTestWithInfo(){
        Response response =given()
                .header("Authorization",token)
                .multiPart("image", new File("src/test/resources/test_img.jpg"))
                .formParam("title","titleCheck")
                .formParam("name","nameCheck.jpg")
                .formParam("description","descriptionCheck")
                .expect()
                .body("data.name",is("nameCheck.jpg"))
                .body("data.title",is("titleCheck"))
                .body("data.description",is("descriptionCheck"))
                .statusCode(200)
                .when()
                .post("https://api.imgur.com/3/image")
                .prettyPeek();
        uploadedImageDeleteHash= response.jsonPath().getString("data.deletehash");
    }

    @After
    void tearDown3(){
        given()
                .header("Authorization",token)
                .when()
                .delete("https://api.imgur.com/3/account/{username}/image/{deletehash}", username, uploadedImageDeleteHash)
                .prettyPeek()
                .then()
                .statusCode(200);
    }
    @Order(4)
@Test //4
    void test4UploadFileBaseImageTestWithNoInfo(){
        uploadedImageDeleteHash=given()
                .header("Authorization", token)
                .multiPart("image",encodedFile)
                .expect()
                .body("success",is(true))
                .when()
                .post("https://api.imgur.com/3/image")
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract().response().jsonPath().getString("data.deletehash");
    }
    @After
    void tearDown4(){
        given()
                .header("Authorization",token)
                .when()
                .delete("https://api.imgur.com/3/account/{username}/image/{deletehash}", username, uploadedImageDeleteHash)
                .prettyPeek()
                .then()
                .statusCode(200);
    }
    @Order(5)
    @Test //5
    void test5UploadFileBaseImageTestWithNoInfoCheckRegEx(){
        Response response =given()
                .header("Authorization",token)
                .multiPart("image", encodedFile)
                .expect()
                .body("success",is(true))
                .statusCode(200)
                .when()
                .post("https://api.imgur.com/3/image")
                .prettyPeek();
        uploadedImageDeleteHash= response.jsonPath().getString("data.deletehash");
        uploadedImageHash = response.jsonPath().getString("data.id");
        assertThat(uploadedImageHash,matchesPattern("[A-Za-z0-9]{7}"));
        assertThat(uploadedImageDeleteHash,matchesPattern("[A-Za-z0-9]{15}"));


    }
    @After
    void tearDown5(){
        given()
                .header("Authorization",token)
                .when()
                .delete("https://api.imgur.com/3/account/{username}/image/{deletehash}", username, uploadedImageDeleteHash)
                .prettyPeek()
                .then()
                .statusCode(200);
    }
    @Order(6)
    @Test //6
    void test6UploadFileBaseImageTestWithInfo(){
        Response response =given()
                .header("Authorization",token)
                .multiPart("image", encodedFile)
                .formParam("title","titleCheck")
                .formParam("name","nameCheck.jpg")
                .formParam("description","descriptionCheck")
                .expect()
                .body("data.name",is("nameCheck.jpg"))
                .body("data.title",is("titleCheck"))
                .body("data.description",is("descriptionCheck"))
                .when()
                .post("https://api.imgur.com/3/image")
                .prettyPeek();
        uploadedImageHash = response.jsonPath().getString("data.id");
        uploadedImageDeleteHash= response.jsonPath().getString("data.deletehash");



    }
    /*@After
    void tearDown6(){
        given()
                .header("Authorization",token)
                .when()
                .delete("https://api.imgur.com/3/account/{username}/image/{deletehash}", username, uploadedImageDeleteHash)
                .prettyPeek()
                .then()
                .statusCode(200);
    }*/
    @Order(7)
    @Test //7
    void test7FavoriteImage(){
        Response response =given()
                .header("Authorization",token)
                .expect()
                .body("data",is("favorited"))
                .when()
                .post("https://api.imgur.com/3/image/{imageHash}/favorite", uploadedImageHash)
                .prettyPeek();



    }
 /*   @After
    void tearDown7(){
        given()
                .header("Authorization",token)
                .when()
                .delete("https://api.imgur.com/3/account/{username}/image/{deletehash}", username, uploadedImageDeleteHash)
                .prettyPeek()
                .then()
                .statusCode(200);
    }*/
 @Order(8)
    @Test //8
    void test8UFavoriteImage(){
        Response response =given()
                .header("Authorization",token)
                .expect()
                .body("data",is("unfavorited"))
                .when()
                .post("https://api.imgur.com/3/image/{imageHash}/favorite", uploadedImageHash)
                .prettyPeek();

    }
  /*  @After
    void tearDown8(){
        given()
                .header("Authorization",token)
                .when()
                .delete("https://api.imgur.com/3/account/{username}/image/{deletehash}", username, uploadedImageDeleteHash)
                .prettyPeek()
                .then()
                .statusCode(200);
    }*/
  @Order(9)
  @Test //9
  void test9UpdateImage(){
      Response response =given()
              .header("Authorization",token)
              .formParam("title","titleNewCheck")
              .formParam("description","descriptionNewCheck")
              .expect()
              .body("data",is(true))

              .when()
              .post("https://api.imgur.com/3/image/{imageDeleteHash}", uploadedImageDeleteHash)
              .prettyPeek();

  }
  /*  @After
    void tearDown9(){
        given()
                .header("Authorization",token)
                .when()
                .delete("https://api.imgur.com/3/account/{username}/image/{deletehash}", username, uploadedImageDeleteHash)
                .prettyPeek()
                .then()
                .statusCode(200);
    }*/
  @Order(10)
    @Test //10
    void test10GetImage(){
        Response response =given()
                .header("Authorization",token)

                .expect()
                .body("data.title",is("titleNewCheck"))
                .body("data.description",is("descriptionNewCheck"))
                .when()
                .get("https://api.imgur.com/3/image/{imageHash}", uploadedImageHash)
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

    private byte[] getFileContent(){
        byte[] byteArray=new byte[0];
        try {byteArray = FileUtils.readFileToByteArray(new File(PATH_TO_IMAGE));}
        catch (IOException e){e.printStackTrace();}
    return byteArray;
    }
}
