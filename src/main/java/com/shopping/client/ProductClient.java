package com.shopping.client;

import com.shopping.db.Product;
import com.shopping.stubs.product.ProductRequest;
import com.shopping.stubs.product.ProductResponse;
import com.shopping.stubs.product.ProductServiceGrpc;
import io.grpc.Channel;

import java.util.logging.Logger;

public class ProductClient {
    private Logger logger = Logger.getLogger(ProductClient.class.getName());
    private ProductServiceGrpc.ProductServiceBlockingStub productServiceBlockingStub;

    public ProductClient(Channel channel){
        productServiceBlockingStub = ProductServiceGrpc.newBlockingStub(channel);
    }

    // call the service
    public Product getProduct(int id){
        // create the request
        ProductRequest productRequest = ProductRequest.newBuilder().setId(id).build();

        // send and get the response
        logger.info("Product Client calling the Product Service Method");
        ProductResponse productResponse = productServiceBlockingStub.getProductDetails(productRequest);
        Product product = new Product();

        product.setQuantity(productResponse.getStock());
        product.setPrice(productResponse.getPrice());
        product.setId(productResponse.getId());
        product.setName(productResponse.getProductName());

        return product;
    }
}
