import Services.CategoryService;
import Services.ProductService;
import com.github.javafaker.Faker;
import dto.Category;
import dto.Product;
import enums.CategoryType;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.junit.Before;
import org.junit.jupiter.api.*;
import retrofit2.Response;
import retrofit2.Retrofit;
import utils.PrettyLogger;
import utils.RetrofitUtils;
import org.junit.Assert.*;
import org.hamcrest.CoreMatchers.*;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.MatcherAssert.*;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductTests {
    static Retrofit client;
    static ProductService productService;
    static CategoryService categoryService;
    static int productId;




    Faker faker = new Faker();
    Product product;
    static Product product1 = new Product()
            .withTitle("newProduct")
            .withCategoryTitle(CategoryType.FOOD.getTitle())
            .withPrice(-1000);



    PrettyLogger prettyLogger = new PrettyLogger();

    @BeforeAll
    static void beforeAll(){
        client = RetrofitUtils.getRetrofit();
        productService = client.create(ProductService.class);
        categoryService = client.create(CategoryService.class);


    }

   @BeforeEach
    void setUp() {
        product = new Product()
                .withTitle(faker.food().dish())
                .withPrice((int)((Math.random()+1)*100))
                .withCategoryTitle(CategoryType.FOOD.getTitle());

    }
    @Order(1)
    @SneakyThrows
    @Test
    void postProductWithIdTest1() throws IOException{
        Response<Product> response = productService.createProduct(product.withId(1)).execute();
        assertThat(response.raw().code(), equalTo(400));
    }
    @Order(2)
    @Test
    void postProductTest2() throws IOException{
        Response<Product> response = productService.createProduct(product).execute();
        prettyLogger.log(response.body().toString());
        assertThat(response.body().getTitle(),equalTo(product.getTitle()));
        assertThat(response.body().getPrice(),equalTo(product.getPrice()));
        assertThat(response.body().getCategoryTitle(),equalTo(product.getCategoryTitle()));
        product1.setId(response.body().getId());

    }

    @Order(3)
    @SneakyThrows
    @Test
    void putProductWithNoIdTest3() throws IOException{
        Response<Product> response = productService.putProduct(product).execute();
       // prettyLogger.log(response.errorBody().string().contains("Id must be not null for new entity"));
        assertThat(response.errorBody().string().contains("Id must be not null for new entity"), is (true));
        assertThat(response.code(), equalTo(400));
    }
    @Order(4)
    @Test
    void putProductTest4() throws IOException{
        Response<Product> response = productService.putProduct(product1).execute();
        assertThat(response.body().getTitle(),equalTo(product1.getTitle()));
        assertThat(response.body().getPrice(),equalTo(product1.getPrice()));
        assertThat(response.body().getCategoryTitle(),equalTo(product1.getCategoryTitle()));
    }
    @Order(5)
    @Test
    void getProductTest5() throws IOException{
        Response<Product> response = productService.getProduct(product1.getId()).execute();
        assertThat(response.body().getTitle(),equalTo(product1.getTitle()));
        assertThat(response.body().getPrice(),equalTo(product1.getPrice()));
        assertThat(response.body().getCategoryTitle(),equalTo(product1.getCategoryTitle()));
    }
    @Order(6)
    @Test
    void getProductWithIncorrectIdTest6() throws IOException{
        Response<Product> response = productService.getProduct(1111111111).execute();

        assertThat(response.errorBody().string().contains("Unable to find product with id"), is (true));
        assertThat(response.code(), equalTo(404));


    }
    @Order(7)
    @Test
    void deleteProductWithIncorrectIdTest7() throws IOException{
        Response<ResponseBody> response = productService.deleteProduct(1111111111).execute();
        assertThat(response.code(), equalTo(500));
    }
    @Order(8)
    @Test
    void deleteProductTest8() throws IOException{
        Response<ResponseBody> response = productService.deleteProduct(product1.getId()).execute();
        assertThat(response.code(), equalTo(200));
    }


    @Test
    void getCategoryByIdTest() throws IOException{
        Integer id=CategoryType.FOOD.getId();
        Response<Category> response = categoryService
                .getCategory(id)
                .execute();
        prettyLogger.log(response.body().toString());
        assertThat(response.body().getTitle(),equalTo(CategoryType.FOOD.getTitle()));
        assertThat(response.body().getId(),equalTo(id));
    }
}
