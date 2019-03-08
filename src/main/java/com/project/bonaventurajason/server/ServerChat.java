package com.project.bonaventurajason.server;

import com.project.bonaventurajason.ChatServiceGrpc;
import com.project.bonaventurajason.JoinArgument;
import com.project.bonaventurajason.MessageChat;
import com.project.bonaventurajason.Response;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ServerChat {
    private final int port;
    private final ArrayList<String> arrUsername = new ArrayList<>();
    private final ArrayList<String> arrPeer = new ArrayList<>();
    private final ArrayList<String> arrStatus = new ArrayList<>();
    private int index = 0;


    public ServerChat(int port) {
        this.port = port;

    }

    void start() throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(port)
                .addService(new ChatServiceImpl())
                .build();
        server.start();
        System.out.println("Server started at port " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("Shutdown server");
                server.shutdown();
                System.out.println("Server shutdown!");
            }
        });
        server.awaitTermination();
    }

    public class ChatServiceImpl extends ChatServiceGrpc.ChatServiceImplBase {
        private ArrayList<Integer> arrObserver = new ArrayList<>();
        private Set<StreamObserver<MessageChat>> observers1 = ConcurrentHashMap.newKeySet();
        private Set<StreamObserver<MessageChat>> observers2 = ConcurrentHashMap.newKeySet();
        private Set<StreamObserver<MessageChat>> observers3 = ConcurrentHashMap.newKeySet();
        private Set<StreamObserver<MessageChat>> observers4 = ConcurrentHashMap.newKeySet();
        private Set<StreamObserver<MessageChat>> observers5 = ConcurrentHashMap.newKeySet();
        private int max = 0;


        @Override
        public void joinChat(JoinArgument request, StreamObserver<Response> responseObserver) {
            try {
                String sign = "masuk";
                for (int i = 0; i < arrUsername.size(); i++) {
                    //System.out.println("Hai");
                    if (request.getUsername().equals(arrUsername.get(i))) {
                        sign = "keluar";
                        responseObserver.onNext(Response.newBuilder().setStatus("User name has been taken, Disconnect from the server").build());
                        responseObserver.onCompleted();

                    }
                }
                if (sign.equals("masuk")) {
                    System.out.println(sign);
                    arrUsername.add(request.getUsername());
                    arrPeer.add(request.getPeer());
                    arrStatus.add(request.getStatus());
                    index = arrUsername.size() - 1;
                    System.out.println("Index" + index);
                    int temp = 0;
                    for (int i = 0; i < arrUsername.size(); i++) {
                        if (arrUsername.get(index).equals(arrPeer.get(i))) {
                            responseObserver.onNext(Response.newBuilder().setStatus("Connected").build());
                            System.out.println("Match");
                            arrObserver.add(arrObserver.get(i));
                            temp = 1;
                            responseObserver.onCompleted();
                            break;
                        }

                        System.out.println("i = " + i);

                    }

                    System.out.println(temp);
                    if (temp < 1) {
                        System.out.println("Next observer");
                        max++;
                        arrObserver.add(max);

                    }

                    System.out.println("Observer" + arrObserver.get(index));
                    System.out.println(arrUsername.get(index) + " " + arrPeer.get(index) + " " + arrStatus.get(index));
                    responseObserver.onNext(Response.newBuilder().setStatus("Waiting for Connection...").build());
                    responseObserver.onCompleted();


                }

            }
            catch (IllegalStateException e){
        }
    }

    @Override
    public StreamObserver<MessageChat> chatting(StreamObserver<MessageChat> responseObserver) {
        if (arrObserver.get(index) == 1) {
            observers1.add(responseObserver);
            return new StreamObserver<MessageChat>() {
                @Override
                public void onNext(MessageChat messageChat) {
                    System.out.println("Masuk on next" + messageChat);
                    String nama = arrUsername.get(index)+" has left";
                    if(messageChat.toString().equals(nama)){
                        System.out.println(messageChat);
                        for(int i =0;i<arrPeer.size();i++){
                            if(arrUsername.get(i).equals(arrPeer.get(index))){
                                System.out.println(arrUsername.get(i));
                                arrUsername.remove(i);
                                arrPeer.remove(i);
                                arrStatus.remove(i);
                                break;
                            }
                        }
                        System.out.println(arrUsername.get(index));
                        arrUsername.remove(index);
                        arrPeer.remove(index);
                        arrStatus.remove(index);
                    }
                    System.out.println(messageChat);
                    MessageChat.newBuilder()
                            .setMessage(messageChat.toString())
                            .setUsername(messageChat.toString())
                            .build();
                    for (StreamObserver<MessageChat> observer : observers1) {
                        observer.onNext(messageChat);
                        //System.out.println("Pesan terkirim");
                    }
                }

                @Override
                public void onError(Throwable t) {

                }

                @Override
                public void onCompleted() {

                }
            };
        }
        if (arrObserver.get(index) == 2) {
            observers2.add(responseObserver);
            return new StreamObserver<MessageChat>() {
                @Override
                public void onNext(MessageChat messageChat) {
                    System.out.println(messageChat);
                    MessageChat.newBuilder()
                            .setMessage(messageChat.toString())
                            .setUsername(messageChat.toString())
                            .build();

                    for (StreamObserver<MessageChat> observer : observers2) {
                        observer.onNext(messageChat);
                    }
                }

                @Override
                public void onError(Throwable t) {

                }

                @Override
                public void onCompleted() {

                }
            };
        }
        if (arrObserver.get(index) == 3) {
            observers3.add(responseObserver);
            return new StreamObserver<MessageChat>() {
                @Override
                public void onNext(MessageChat messageChat) {
                    System.out.println(messageChat);
                    MessageChat.newBuilder()
                            .setMessage(messageChat.toString())
                            .setUsername(messageChat.toString())
                            .build();

                    for (StreamObserver<MessageChat> observer : observers3) {
                        observer.onNext(messageChat);
                    }
                }

                @Override
                public void onError(Throwable t) {

                }

                @Override
                public void onCompleted() {

                }
            };
        }
        if (arrObserver.get(index) == 4) {
            observers4.add(responseObserver);
            return new StreamObserver<MessageChat>() {
                @Override
                public void onNext(MessageChat messageChat) {
                    System.out.println(messageChat);
                    MessageChat.newBuilder()
                            .setMessage(messageChat.toString())
                            .setUsername(messageChat.toString())
                            .build();

                    for (StreamObserver<MessageChat> observer : observers4) {
                        observer.onNext(messageChat);
                    }
                }

                @Override
                public void onError(Throwable t) {

                }

                @Override
                public void onCompleted() {

                }
            };
        }
        if (arrObserver.get(index) == 5) {
            observers5.add(responseObserver);
            return new StreamObserver<MessageChat>() {
                @Override
                public void onNext(MessageChat messageChat) {
                    System.out.println(messageChat);
                    MessageChat.newBuilder()
                            .setMessage(messageChat.toString())
                            .setUsername(messageChat.toString())
                            .build();

                    for (StreamObserver<MessageChat> observer : observers5) {
                        observer.onNext(messageChat);
                    }
                }

                @Override
                public void onError(Throwable t) {

                }

                @Override
                public void onCompleted() {

                }
            };
        }
//            int i = 0;
//            int j = 0;
//            Map data = new HashMap();
//            while (i < arrUsername.size()) {
//                while (j < arrPeer.size()) {
//                    if (arrUsername.get(i).equals(arrPeer.get(j))) {
//                        System.out.println("Berhasil masuk kode equals");
//                        Set<StreamObserver<MessageChat>> observers = ConcurrentHashMap.newKeySet();
//                        data.put(arrUsername.get(i), observers);
//                        System.out.println("Data " + data.size());
//                        observers.add(responseObserver);
//                        return new StreamObserver<MessageChat>() {
//                            @Override
//                            public void onNext(MessageChat messageChat) {
//                                System.out.println(messageChat);
//                                MessageChat.newBuilder()
//                                        .setMessage(messageChat.toString())
//                                        .setUsername(messageChat.toString())
//                                        .build();
//
//                                for (StreamObserver<MessageChat> observer : observers) {
//                                    observer.onNext(messageChat);
//                                }
//                            }
//
//                            @Override
//                            public void onError(Throwable t) {
//
//                            }
//
//                            @Override
//                            public void onCompleted() {
//
//                            }
//                        };
//
//
//                    } else {
//                        System.out.println("Masuk ke j++");
//                        j++;
//                    }
//                }
//
//            }
        //observers.add(responseObserver);
        return new StreamObserver<MessageChat>() {
            @Override
            public void onNext(MessageChat messageChat) {
//                   System.out.println(messageChat);
//                   MessageChat.newBuilder()
//                           .setMessage(messageChat.toString())
//                           .setUsername(messageChat.toString())
//                           .build();
//
//                   for(StreamObserver<MessageChat> observer : observers){
//                       observer.onNext(messageChat);
//                   }
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("Masuk error");
                System.out.println(t);
            }

            @Override
            public void onCompleted() {


            }
        };
    }


}

}
