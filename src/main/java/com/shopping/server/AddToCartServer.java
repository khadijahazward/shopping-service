package com.shopping.server;

import com.shopping.service.AddToCartServiceImpl;
import com.shopping.service.ProductServiceImpl;
import com.shopping.stubs.user.UserResponse;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddToCartServer {
    private static Logger logger = Logger.getLogger(UserResponse.class.getName());
    private Server server;

    // hosts the UserServiceImpl class on port 50051.
    public void startServer(){
        int port = 50054;
        try {
            server = ServerBuilder.forPort(port).addService(new AddToCartServiceImpl()).build().start();
            logger.info("Add to Cart server started on port 50054");
            Runtime.getRuntime().addShutdownHook(
                    new Thread(() -> {
                        logger.info("Clean server shutdown in case JVM was shutdown");
                        try {
                            AddToCartServer.this.shutdownServer();
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
        AddToCartServer addToCartServer = new AddToCartServer();
        addToCartServer.startServer();
        addToCartServer.blockUntilShutdown();
    }
}
