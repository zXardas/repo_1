package Services;

import retrofit2.Call;
import dto.Category;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CategoryService {
    //получает все продукты определенной категории
    @GET("categories/{id}")
    Call<Category> getCategory(@Path("id") int id);
}
