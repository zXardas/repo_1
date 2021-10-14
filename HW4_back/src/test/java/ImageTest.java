import dto.PostImageResponse;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import static Endpoints.Endpoints.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.matchesPattern;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ImageTest extends BaseTest{
    private final String PATH_TO_IMAGE="src/test/resources/test_img.jpg";
    static String encodedFile;
    static String uploadedImageDeleteHash;
    static String uploadedImageHash;
    MultiPartSpecification multiPartSpecificationSpecificationWithBase64;
    MultiPartSpecification multiPartSpecificationWithFile;

    static RequestSpecification requestSpecificationWithAuthAndMultipartImageInfo;
    static RequestSpecification requestSpecificationWithAuthAndMultipartImage;
    static RequestSpecification requestSpecificationWithAuthWithBase64;
    static RequestSpecification requestSpecificationWithAuthWithBase64Info;
    static RequestSpecification requestSpecificationWithAuthAndMultipartImageInfoChange;

    static ResponseSpecification responseSpecificationWithInfo;
    static ResponseSpecification responseSpecificationChange;
    static ResponseSpecification responseSpecificationRegEx;
    static  ResponseSpecification responseSpecificationFavorite;
    static  ResponseSpecification responseSpecificationUFavorite;
    static  ResponseSpecification responseSpecificationWithInfoChange;


    PostImageResponse postImageResponse;

    @BeforeEach
    void beforeTest(){
        byte[] byteArray=getFileContent();
        encodedFile= Base64.getEncoder().encodeToString(byteArray);

        multiPartSpecificationSpecificationWithBase64 = new MultiPartSpecBuilder(encodedFile)
                .controlName("image")
                .build();

        multiPartSpecificationWithFile = new MultiPartSpecBuilder(new File("src/test/resources/test_img.jpg"))
                .controlName("image")
                .build();


        requestSpecificationWithAuthAndMultipartImageInfo = new RequestSpecBuilder()
                .addRequestSpecification(requestSpecificationWithAuth) //header с токеном для авторизации
                .addFormParam("title","titleCheck")
                .addFormParam("name","nameCheck.jpg")
                .addFormParam("description","descriptionCheck")
                .addMultiPart(multiPartSpecificationWithFile)
                .build();

        requestSpecificationWithAuthAndMultipartImageInfoChange = new RequestSpecBuilder()
                .addRequestSpecification(requestSpecificationWithAuth) //header с токеном для авторизации
                .addFormParam("title","titleNewCheck")
                .addFormParam("description","descriptionNewCheck")
                .addMultiPart(multiPartSpecificationWithFile)
                .build();


        requestSpecificationWithAuthAndMultipartImage = new RequestSpecBuilder()
                .addRequestSpecification(requestSpecificationWithAuth) //header с токеном для авторизации
                .addMultiPart(multiPartSpecificationWithFile)
                .build();

        requestSpecificationWithAuthWithBase64Info = new RequestSpecBuilder()
                .addRequestSpecification(requestSpecificationWithAuth) //header с токеном для авторизации
                .addFormParam("title","titleCheck")
                .addFormParam("name","nameCheck.jpg")
                .addFormParam("description","descriptionCheck")
                .addMultiPart(multiPartSpecificationSpecificationWithBase64)
                .build();

        requestSpecificationWithAuthWithBase64 = new RequestSpecBuilder()
                .addRequestSpecification(requestSpecificationWithAuth)
                .addMultiPart(multiPartSpecificationSpecificationWithBase64)
                .build();


        responseSpecificationWithInfo = new ResponseSpecBuilder()
                .expectBody("status", equalTo(200))
                .expectBody("success", is(true))
                .expectBody("data.name",is("nameCheck.jpg"))
                .expectBody("data.title",is("titleCheck"))
                .expectBody("data.description",is("descriptionCheck"))
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200)
                .build();

        responseSpecificationWithInfoChange = new ResponseSpecBuilder()
                .expectBody("status", equalTo(200))
                .expectBody("success", is(true))
                .expectBody("data.title",is("titleNewCheck"))
                .expectBody("data.description",is("descriptionNewCheck"))
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200)
                .build();


        responseSpecificationChange = new ResponseSpecBuilder()
                .expectBody("status", equalTo(200))
                .expectBody("success", is(true))
                .expectBody("data",is(true))
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200)
                .build();

        responseSpecificationRegEx = new ResponseSpecBuilder()
                .expectBody("status", equalTo(200))
                .expectBody("success", is(true))
                .expectBody("data.id",matchesPattern("[A-Za-z0-9]{7}"))
                .expectBody("data.deletehash",matchesPattern("[A-Za-z0-9]{15}"))
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200)
                .build();


        responseSpecificationFavorite = new ResponseSpecBuilder()
                .expectBody("status", equalTo(200))
                .expectBody("success", is(true))
                .expectBody("data",is("favorited"))
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200)
                .build();

        responseSpecificationUFavorite = new ResponseSpecBuilder()
                .expectBody("status", equalTo(200))
                .expectBody("success", is(true))
                .expectBody("data",is("unfavorited"))
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200)
                .build();

    }

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
        given(requestSpecificationWithAuth, responseSpecificationChange)
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
        given(requestSpecificationWithAuth, responseSpecificationChange)
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
        given(requestSpecificationWithAuth, responseSpecificationChange)
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
        given(requestSpecificationWithAuth, responseSpecificationChange)
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
 /*   @After
    void tearDown7(){
         given(requestSpecificationWithAuth, responseSpecificationChange)
                 .delete(DEL_IMG, username, uploadedImageDeleteHash)
                 .prettyPeek();
     }*/
 @Order(8)
    @Test //8
    void test8UFavoriteImage(){
     Response response = given(requestSpecificationWithAuth, responseSpecificationUFavorite)

             .post("https://api.imgur.com/3/image/{imageHash}/favorite", uploadedImageHash)
             .prettyPeek();

    }
  /*  @After
    void tearDown8(){
         given(requestSpecificationWithAuth, responseSpecificationChange)
                 .delete(DEL_IMG, username, uploadedImageDeleteHash)
                 .prettyPeek();
     }*/
  @Order(9)
  @Test //9
  void test9UpdateImage(){
      Response response =  given(requestSpecificationWithAuthAndMultipartImageInfoChange, responseSpecificationChange)
              .post(UPDATE, uploadedImageDeleteHash)
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
      postImageResponse =given(requestSpecificationWithAuth, responseSpecificationWithInfoChange)

                .get(GET_IMG, uploadedImageHash)
                .prettyPeek()
                .then()
              .extract()
              .body()
              .as(PostImageResponse.class);
      uploadedImageDeleteHash = postImageResponse.getData().getDeletehash();

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




