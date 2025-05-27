package com.shopping.service;

import com.shopping.client.OrderClient;
import com.shopping.db.User;
import com.shopping.db.UserDao;
import com.shopping.stubs.order.Order;
import com.shopping.stubs.user.Gender;
import com.shopping.stubs.user.UserRequest;
import com.shopping.stubs.user.UserResponse;
import com.shopping.stubs.user.UserServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {
    public static Logger logger = Logger.getLogger(UserServiceImpl.class.getName());
    private UserDao userDao = new UserDao();

    @Override
    public void getUserDetails(UserRequest request, StreamObserver<UserResponse> responseObserver) {
        User user = userDao.getDetails(request.getUsername());

        // use the builders to construct the objects corresponding to the message types in proto file.
        UserResponse.Builder userResponseBuilder =
                // transfers data from User object to proto object
                UserResponse.newBuilder()
                        .setId(user.getId())
                        .setAge(user.getAge())
                        .setGender(Gender.valueOf(user.getGender()))
                        .setUsername(user.getUsername())
                        .setName(user.getName());

        List<Order> orderList = getOrderList(userResponseBuilder);

        userResponseBuilder.setNoOfOrders(orderList.size());
        UserResponse userResponse = userResponseBuilder.build();

        // returns UserResponse to the client.
        responseObserver.onNext(userResponse);
        responseObserver.onCompleted();
    }

    private static List<Order> getOrderList(UserResponse.Builder userResponseBuilder) {
        logger.info("Creating a channel and calling the OrderClient");

        // invokes the order client and gets the list of orders for a user
        ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:50052").usePlaintext().build();
        OrderClient orderClient = new OrderClient(channel);
        List<Order> orderList = orderClient.getOrders(userResponseBuilder.getId());

        try {
            channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Could not shutdown the Http/2 channel", e);
        }
        return orderList;
    }
}
