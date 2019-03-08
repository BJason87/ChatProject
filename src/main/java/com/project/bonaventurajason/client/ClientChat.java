package com.project.bonaventurajason.client;

import com.project.bonaventurajason.ChatServiceGrpc;
import com.project.bonaventurajason.JoinArgument;
import com.project.bonaventurajason.MessageChat;
import com.project.bonaventurajason.Response;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class ClientChat {
    private final String host;
    private final int port;
    private final String username;
    private final String peer;
    
    public ClientChat(String host, int port, String username, String peer) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.peer = peer;
    }

    public Response saveData(String username, String peer) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        ChatServiceGrpc.ChatServiceBlockingStub stub = ChatServiceGrpc.newBlockingStub(channel);

        JoinArgument req = JoinArgument.newBuilder()
                .setUsername(username)
                .setPeer(peer)
                .build();
        Response resp = stub.joinChat(req);
        if(resp.getStatus().equals("User name has been taken, Disconnect from the server")){
            System.out.println(resp);
            return resp;

        }
        else{
            System.out.println(resp);
            return resp;
        }
    }

    StreamObserver<MessageChat> chatting(StreamObserver<MessageChat> responseObserver) {
        ManagedChannel ch = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        ChatServiceGrpc.ChatServiceStub asyncStub = ChatServiceGrpc.newStub(ch);
        return asyncStub.chatting(responseObserver);
    }
}
