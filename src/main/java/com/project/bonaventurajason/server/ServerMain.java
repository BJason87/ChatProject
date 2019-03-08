package com.project.bonaventurajason.server;

import java.io.IOException;

public class ServerMain {
    public static void main(String[] args) throws IOException, InterruptedException {
        ServerChat server =new ServerChat(9998);
        server.start();

    }
}
