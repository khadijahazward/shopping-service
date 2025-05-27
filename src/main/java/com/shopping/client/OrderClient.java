package com.shopping.client;

import com.shopping.stubs.order.Order;
import com.shopping.stubs.order.OrderRequest;
import com.shopping.stubs.order.OrderResponse;
import com.shopping.stubs.order.OrderServiceGrpc;
import io.grpc.Channel;

import java.util.List;
import java.util.logging.Logger;

public class OrderClient {
    private Logger logger = Logger.getLogger(OrderClient.class.getName());

    // get the stub (blocking or non-blocking call)
    private OrderServiceGrpc.OrderServiceBlockingStub orderServiceBlockingStub;

    // open the channel with http/2
    public OrderClient(Channel channel){
        orderServiceBlockingStub = OrderServiceGrpc.newBlockingStub(channel);
    }

    // call the service method
    public List<Order> getOrders(int userId){
        logger.info("Order Client calling the Order Service Method");

        // build the request
        OrderRequest orderRequest = OrderRequest.newBuilder().setUserId(userId).build();
        OrderResponse orderResponse = orderServiceBlockingStub.getOrderForUser(orderRequest);
        return orderResponse.getOrderList();
    }
}
