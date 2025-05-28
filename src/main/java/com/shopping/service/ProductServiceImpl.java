package com.shopping.service;

import com.shopping.db.Product;
import com.shopping.db.ProductDao;
import com.shopping.stubs.product.ProductRequest;
import com.shopping.stubs.product.ProductResponse;
import com.shopping.stubs.product.ProductServiceGrpc;
import io.grpc.stub.StreamObserver;

import java.util.logging.Logger;

public class ProductServiceImpl extends ProductServiceGrpc.ProductServiceImplBase {
    ProductDao productDao = new ProductDao();
    Product product = new Product();
    private Logger logger = Logger.getLogger(ProductServiceImpl.class.getName());

    @Override
    public void getProductDetails(ProductRequest request, StreamObserver<ProductResponse> responseObserver) {
        product =  productDao.getProduct(request.getId());

        logger.info("Creating a Product proto object");

        // create the proto object from the domain object
        ProductResponse.Builder productResponseBuilder = ProductResponse
                .newBuilder()
                .setProductName(product.getName())
                .setId(product.getId())
                .setPrice(product.getPrice())
                .setStock(product.getQuantity());
        ProductResponse productResponse = productResponseBuilder.build();

        responseObserver.onNext(productResponse);
        responseObserver.onCompleted();
    }
}
