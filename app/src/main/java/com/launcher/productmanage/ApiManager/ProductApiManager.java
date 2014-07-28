package com.launcher.productmanage.ApiManager;
import com.launcher.productmanage.Model.ProductData;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.concurrency.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * Created by chen on 14-7-19.
 */
public class ProductApiManager extends MainApiManager {
//    private interface InformNetWorkService {
//        @GET("/informs")
//        List<InformData> getIforms();
//    }


    private static final ProductAPiInterface.GetProducts informsNetwork = restAdapter.create(ProductAPiInterface.GetProducts.class);
    public static Observable<List<ProductData>> getProductsData() {
        return Observable.create(new Observable.OnSubscribeFunc<List<ProductData>>() {
            @Override
            public Subscription onSubscribe(Observer<? super List<ProductData>> observer) {
                try {
                    observer.onNext(informsNetwork.getProducts());
                    observer.onCompleted();
                } catch (Exception e) {
                    observer.onError(e);
                }

                return Subscriptions.empty();
            }
        }).subscribeOn(Schedulers.threadPoolForIO());
    }

    private static final ProductAPiInterface.GetProduct informNetwork = restAdapter.create(ProductAPiInterface.GetProduct.class);

    public static Observable<ProductData> getProductData(final String id) {
        return Observable.create(new Observable.OnSubscribeFunc<ProductData>() {
            @Override
            public Subscription onSubscribe(Observer<? super ProductData> observer) {
                try {
                    observer.onNext(informNetwork.getProduct(id));
                    observer.onCompleted();
                } catch (Exception e) {
                    observer.onError(e);
                }

                return Subscriptions.empty();
            }
        }).subscribeOn(Schedulers.threadPoolForIO());
    }
}
