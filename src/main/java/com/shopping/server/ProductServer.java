package com.shopping.server;

import com.shopping.service.OrderServiceImpl;
import com.shopping.service.ProductServiceImpl;
import com.shopping.stubs.user.UserResponse;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductServer {
    private static Logger logger = Logger.getLogger(UserResponse.class.getName());
    private Server server;

    // hosts the UserServiceImpl class on port 50051.
    public void startServer(){
        int port = 50053;
        try {
            server = ServerBuilder.forPort(port).addService(new ProductServiceImpl()).build().start();
            logger.info("Product server started on port 50053");
            Runtime.getRuntime().addShutdownHook(
                    new Thread(() -> {
                        logger.info("Clean server shutdown in case JVM was shutdown");
                        try {
                            ProductServer.this.shutdownServer();
                        } catch (InterruptedException e) {
                            logger.log(Level.SEVERE, "server shutdown interrupted", e);
                        }
                    }));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Server did not start", e);
        }
    }

    // stops the server
    public void shutdownServer() throws InterruptedException{
        if (server!=null){
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    // keeps running without termination
    public void blockUntilShutdown() throws InterruptedException{
        if (server!=null){
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ProductServer productServer = new ProductServer();
        productServer.startServer();
        productServer.blockUntilShutdown();
    }
}
