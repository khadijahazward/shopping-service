package com.shopping.service;

import com.google.protobuf.util.Timestamps;
import com.shopping.db.Order;
import com.shopping.db.OrderDao;
import com.shopping.stubs.order.OrderRequest;
import com.shopping.stubs.order.OrderResponse;
import com.shopping.stubs.order.OrderServiceGrpc;
import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class OrderServiceImpl extends OrderServiceGrpc.OrderServiceImplBase {
    private Logger logger = Logger.getLogger(OrderServiceImpl.class.getName());
    private OrderDao orderDao = new OrderDao();

    @Override
    public void getOrderForUser(OrderRequest request, StreamObserver<OrderResponse> responseObserver) {
        List<Order> orders = orderDao.getOrders(request.getUserId());

        logger.info("Converting Order domain objects to OrderResponse Proto objects");

        // convert domain object to proto objects
        List<com.shopping.stubs.order.Order> orderList = orders.stream().map(order -> com.shopping.stubs.order.Order.newBuilder()
                .setOrderId(order.getOrderId())
                .setUserId(order.getUserId())
                .setOrderDate(Timestamps.fromMillis(order.getOrderDate().getTime()))
                .setNoOfItems(order.getNoOfItems())
                .setTotalAmount(order.getTotalAmount())
                .build())
                .collect(Collectors.toList());

        OrderResponse orderResponse = OrderResponse.newBuilder().addAllOrder(orderList).build();
        responseObserver.onNext(orderResponse);
        responseObserver.onCompleted();
    }
}
