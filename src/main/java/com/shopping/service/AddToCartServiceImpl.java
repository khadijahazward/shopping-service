package com.shopping.service;

import com.shopping.client.ProductClient;
import com.shopping.db.Product;
import com.shopping.stubs.addtocart.AddToCartRequest;
import com.shopping.stubs.addtocart.AddToCartResponse;
import com.shopping.stubs.addtocart.AddToCartServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.TimeUnit;

public class AddToCartServiceImpl extends AddToCartServiceGrpc.AddToCartServiceImplBase {
    @Override
    public void isProductAvailable(AddToCartRequest request, StreamObserver<AddToCartResponse> responseObserver) {
        // call the product client and get the quantity
        Product product = getProduct(request);

        // check if there is enough stocks
        boolean is_available = true;
        String message = "Product " + product.getId() + " is available.";
        if (request.getQuantity() > product.getQuantity()){
            is_available = false;
            message = "Product " + product.getId() + " = Out of Stock";
        }

        // build the response object
        AddToCartResponse.Builder addToCartResponseBuilder =
                AddToCartResponse.newBuilder()
                        .setCurrentStock(product.getQuantity())
                        .setIsAvailable(is_available)
                        .setMessage(message);
        AddToCartResponse addToCartResponse = addToCartResponseBuilder.build();
        responseObserver.onNext(addToCartResponse);
        responseObserver.onCompleted();
    }

    private Product getProduct(AddToCartRequest request) {
        ManagedChannel managedChannel = ManagedChannelBuilder.forTarget("localhost:50053").usePlaintext().build();
        ProductClient productClient = new ProductClient(managedChannel);
        Product product = productClient.getProduct(request.getProductId());
        try {
            managedChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return product;
    }
}
