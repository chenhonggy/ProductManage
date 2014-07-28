package com.launcher.productmanage.ApiManager;
import com.launcher.productmanage.Model.ProductData;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by chen on 14-7-26.
 */
public class ProductAPiInterface {
    public interface GetProducts {
        @GET("/products")
        List<ProductData> getProducts();
    }

    public interface GetProduct {
        @GET("/products/{id}")
        ProductData getProduct(@Path("id") String id);
    }

}
