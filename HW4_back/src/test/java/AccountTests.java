import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class AccountTests extends BaseTest{
    @Test
    void getAccountInfoTest(){
        given()
                .header ("Authorization",token)
                .when()
                .get("https://api.imgur.com/3/account/{username}",username)
                .then()
                .statusCode(200);
    }
    @Test
    void getAccountInfoWithLoggingTest(){
        given()
                .header("Authorization", token)
                .log() //логгирование следующей строки
                .method()
                .log()
                .uri()
                .when()
                .get("https://api.imgur.com/3/account/{username}", username)
                .prettyPeek()
                .then()
                .statusCode(200);
    }
@Test
    void getAccountInfoWithAssertionsInGivenTest(){
        given()
                .header("Authorization", token) //можно вместо token вписывать его значение
                .log()
                .method()
                .expect() //проверки ответа с использованием рестассуре
                .statusCode(200)
                .body("data.url",equalTo(username))
                .body("success",equalTo(true))
                .body("status",equalTo(200))
                .contentType("application/json")
                .when()
                .get("https://api.imgur.com/3/account/{username}",username)
                .prettyPeek();
}
@Test
    void getAccountInfoWithAssertionsAfterTest(){
        Response response=given() //записываем в респонс ответ и далее проверяем его через junit
                .header("Authorization",token)
                .log()
                .method()
                .when()
                .get("https://api.imgur.com/3/account/3/{username}",username)
                .prettyPeek();
        assertThat(response.jsonPath().get("data.url"),equalTo(username));
}

}
