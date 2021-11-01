package ru.annachemic.tests;

import okhttp3.ResponseBody;
import ru.annachemic.Services.CategoryService;
import ru.annachemic.Services.ProductService;
import com.github.javafaker.Faker;
import ru.annachemic.db.dao.ProductsMapper;
import ru.annachemic.db.model.Products;
import ru.annachemic.dto.Category;
import ru.annachemic.dto.Product;
import ru.annachemic.enums.CategoryType;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import retrofit2.Response;
import retrofit2.Retrofit;
import ru.annachemic.utils.DbUtils;
import ru.annachemic.utils.PrettyLogger;
import ru.annachemic.utils.RetrofitUtils;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.MatcherAssert.*;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductTests {
    static Retrofit client;
    static ProductService productService;
    static CategoryService categoryService;

    static ProductsMapper productsMapper;

    static int productId;




    Faker faker = new Faker();
    Product product;
    static Product product1 = new Product()

          //  .withId(13552) //удалить после тестов


            .withTitle("newProduct")
            .withCategoryTitle(CategoryType.FOOD.getTitle())
            .withPrice(-1000);



    PrettyLogger prettyLogger = new PrettyLogger();

    @BeforeAll
    static void beforeAll(){
        client = RetrofitUtils.getRetrofit();
        productService = client.create(ProductService.class);
        categoryService = client.create(CategoryService.class);
        productsMapper = DbUtils.getProductsMapper();


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
        Integer countProductsBefore = DbUtils.countProducts(productsMapper);
        System.out.println(countProductsBefore);
        Response<Product> response = productService.createProduct(product.withId(1)).execute();
        Integer countProductsAfter = DbUtils.countProducts(productsMapper);
        System.out.println(countProductsAfter);
        assertThat(response.raw().code(), equalTo(400));
        assertThat(countProductsAfter, equalTo(countProductsBefore)); // проверяем, что продукт не создался и количество продуктов не изменилось
    }

    @Order(2)
    @Test
    void postProductTest2() throws IOException{
        Integer countProductsBefore = DbUtils.countProducts(productsMapper);
        Response<Product> response = productService.createProduct(product).execute();
        Integer countProductsAfter = DbUtils.countProducts(productsMapper);
        prettyLogger.log(response.body().toString());
        assertThat(countProductsAfter, equalTo(countProductsBefore+1)); //проверка - количество товаров увеличилось
        assertThat(response.body().getTitle(),equalTo(product.getTitle()));
        assertThat(response.body().getPrice(),equalTo(product.getPrice()));
        assertThat(response.body().getCategoryTitle(),equalTo(product.getCategoryTitle()));

        product1.setId(response.body().getId());

    }

    @Order(3)
    @SneakyThrows
    @Test
    void putProductWithNoIdTest3() throws IOException{ //тест негативный и не относится ни к какому товару (попытка изменить товар без id), поэтому ничего измениться не должно, проверку не добавлял
        Response<Product> response = productService.putProduct(product).execute();

        assertThat(response.errorBody().string().contains("Id must be not null for new entity"), is (true));
        assertThat(response.code(), equalTo(400));


    }

    @Order(4)
    @Test
    void putProductTest4() throws IOException{
        Response<Product> response = productService.putProduct(product1).execute();
        Products example = DbUtils.selectProductById(productsMapper,product1.getId()); //проверка через selectById
        assertThat(response.body().getTitle(),equalTo(product1.getTitle()));
        assertThat(response.body().getPrice(),equalTo(product1.getPrice()));
        assertThat(response.body().getCategoryTitle(),equalTo(product1.getCategoryTitle()));
        assertThat(response.body().getTitle(),equalTo(example.getTitle()));
        assertThat(response.body().getPrice(),equalTo(example.getPrice()));

    }


    @Order(5)
    @Test
    void getProductTest5() throws IOException{
        Response<Product> response = productService.getProduct(product1.getId()).execute();
        Products example = DbUtils.selectProductById(productsMapper,product1.getId()); //проверка через selectById
        assertThat(response.body().getTitle(),equalTo(product1.getTitle()));
        assertThat(response.body().getPrice(),equalTo(product1.getPrice()));
        assertThat(response.body().getCategoryTitle(),equalTo(product1.getCategoryTitle()));
        assertThat(response.body().getTitle(),equalTo(example.getTitle()));
        assertThat(response.body().getPrice(),equalTo(example.getPrice()));

    }


    @Order(6)
    @Test
    void getProductWithIncorrectIdTest6() throws IOException{
        Response<Product> response = productService.getProduct(1111111111).execute();
        Products example = DbUtils.selectProductById(productsMapper,1111111111); //проверка через selectById
        assertThat(response.errorBody().string().contains("Unable to find product with id"), is (true));
        assertThat(response.code(), equalTo(404));
        assertThat(example, equalTo(null));


    }
    @Order(7)
    @Test
    void deleteProductWithIncorrectIdTest7() throws IOException{
        Response<ResponseBody> response = productService.deleteProduct(1111111111).execute();
        Products example = DbUtils.selectProductById(productsMapper,1111111111); //проверка через selectById
        assertThat(response.code(), equalTo(500));
        assertThat(example, equalTo(null));
    }
    @Order(8)
    @Test
    void deleteProductTest8() throws IOException{
        Integer countProductsBefore = DbUtils.countProducts(productsMapper);
        Response<ResponseBody> response = productService.deleteProduct(product1.getId()).execute();
        Integer countProductsAfter = DbUtils.countProducts(productsMapper);
        assertThat(response.code(), equalTo(200));
        assertThat(countProductsAfter,equalTo(countProductsBefore-1)); // количество продуктов должно уменьшитьс
    }
/*

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
    */
}
