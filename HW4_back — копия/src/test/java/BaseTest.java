
import dto.PostImageResponse;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.matchesPattern;

public abstract class BaseTest {
    static Properties properties = new Properties();
    static String token;
    static String username;

static ResponseSpecification positiveResponseSpecification;
static RequestSpecification requestSpecificationWithAuth;


    private static final String PATH_TO_IMAGE="src/test/resources/test_img.jpg";
    static String encodedFile;
    static MultiPartSpecification multiPartSpecificationSpecificationWithBase64;
    static MultiPartSpecification multiPartSpecificationWithFile;
    static RequestSpecification requestSpecificationWithAuthAndMultipartImageInfo;
    static RequestSpecification requestSpecificationWithAuthAndMultipartImage;
    static RequestSpecification requestSpecificationWithAuthWithBase64;
    static RequestSpecification requestSpecificationWithAuthWithBase64Info;
    static RequestSpecification requestSpecificationWithAuthAndMultipartImageInfoChange;
    static RequestSpecification requestSpecificationInfo;

    static ResponseSpecification responseSpecificationWithInfo;
    static ResponseSpecification responsePositiveSpecificationChange;
    static ResponseSpecification responsePositiveSpecification;
    static ResponseSpecification responseSpecificationRegEx;
    static  ResponseSpecification responseSpecificationFavorite;
    static  ResponseSpecification responseSpecificationUFavorite;
    static  ResponseSpecification responseSpecificationWithInfoChange;
    static String uploadedImageDeleteHash;
    static String uploadedImageHash;
    static PostImageResponse postImageResponse;
    @BeforeAll
    static void beforeAll(){



        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.filters(new AllureRestAssured());
        RestAssured.baseURI = "https://api.imgur.com/3";
        getProperties();
        token=properties.getProperty("token");
        username=properties.getProperty("username");

        byte[] byteArray=getFileContent();
        encodedFile= Base64.getEncoder().encodeToString(byteArray);

        multiPartSpecificationSpecificationWithBase64 = new MultiPartSpecBuilder(encodedFile)
                .controlName("image")
                .build();

        multiPartSpecificationWithFile = new MultiPartSpecBuilder(new File("src/test/resources/test_img.jpg"))
                .controlName("image")
                .build();

        positiveResponseSpecification = new ResponseSpecBuilder()
                .expectBody("status", equalTo(200))
                .expectBody("success", is(true))
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200)
                .build();

        requestSpecificationWithAuth = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .build();

        requestSpecificationInfo = new RequestSpecBuilder()
                .addFormParam("title","titleCheck")
                .addFormParam("name","nameCheck.jpg")
                .addFormParam("description","descriptionCheck")
                .build();


        requestSpecificationWithAuthAndMultipartImageInfo = new RequestSpecBuilder()
                .addRequestSpecification(requestSpecificationWithAuth) //header с токеном для авторизации
                .addRequestSpecification(requestSpecificationInfo)
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
                .addRequestSpecification(requestSpecificationInfo)
                .addMultiPart(multiPartSpecificationSpecificationWithBase64)
                .build();

        requestSpecificationWithAuthWithBase64 = new RequestSpecBuilder()
                .addRequestSpecification(requestSpecificationWithAuth)
                .addMultiPart(multiPartSpecificationSpecificationWithBase64)
                .build();



        responsePositiveSpecificationChange = new ResponseSpecBuilder()
                .addResponseSpecification(positiveResponseSpecification)
                .expectBody("data",is(true))
                .build();

        responseSpecificationWithInfo = new ResponseSpecBuilder()
                .addResponseSpecification(positiveResponseSpecification)
                .expectBody("data.name",is("nameCheck.jpg"))
                .expectBody("data.title",is("titleCheck"))
                .expectBody("data.description",is("descriptionCheck"))

                .build();

        responseSpecificationWithInfoChange = new ResponseSpecBuilder()
                .addResponseSpecification(positiveResponseSpecification)
                .expectBody("data.title",is("titleNewCheck"))
                .expectBody("data.description",is("descriptionNewCheck"))

                .build();


        responseSpecificationRegEx = new ResponseSpecBuilder()
                .addResponseSpecification(positiveResponseSpecification)
                .expectBody("data.id",matchesPattern("[A-Za-z0-9]{7}"))
                .expectBody("data.deletehash",matchesPattern("[A-Za-z0-9]{15}"))

                .build();


        responseSpecificationFavorite = new ResponseSpecBuilder()
                .addResponseSpecification(positiveResponseSpecification)
                .expectBody("data",is("favorited"))

                .build();

        responseSpecificationUFavorite = new ResponseSpecBuilder()
                .addResponseSpecification(positiveResponseSpecification)
                .expectBody("data",is("unfavorited"))

                .build();

    }

    @BeforeEach
    void beforeTest(){




    }

    public static void getProperties(){
        try(InputStream output=new FileInputStream("src/test/resources/application.properties"))
        {properties.load(output);}
        catch(IOException e) {e.printStackTrace();}

    }
    private static byte[] getFileContent(){
        byte[] byteArray=new byte[0];
        try {byteArray = FileUtils.readFileToByteArray(new File(PATH_TO_IMAGE));}
        catch (IOException e){e.printStackTrace();}
        return byteArray;
    }

}
