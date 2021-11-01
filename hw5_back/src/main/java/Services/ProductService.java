package Services;

import dto.Product;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.ArrayList;

public interface ProductService {
    @GET("products") //список всех прдукт
    Call<ArrayList<Product>> getProducts();
    @GET("products/{id}") //получение конкретного продукта
    Call<Product> getProduct (@Path("id") Integer id);
    @POST("products") //указываем новый
    Call<Product> createProduct(@Body Product product);
    @DELETE("products/{id}")
    Call<ResponseBody> deleteProduct (@Path("id") Integer id); //ResponseBody тк не важно что в ответе
    @PUT("products")
    Call<Product> putProduct(@Body Product product);
}
