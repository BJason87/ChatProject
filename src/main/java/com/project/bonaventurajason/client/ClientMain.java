package com.project.bonaventurajason.client;

import com.project.bonaventurajason.MessageChat;
import com.project.bonaventurajason.Response;
import io.grpc.stub.StreamObserver;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientMain {
    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 9999;
        String username = "default";
        String peer = "default";
        int narg = args.length;
        if (narg > 0) {
            String arg, opt;
            for (int i = 0; i < narg; i++) {
                arg = args[i];
                if ("-h".equals(arg)) {
                    if (i + 1 < narg) {
                        opt = args[++i];
                        if (opt.charAt(0) != '-') {
                            host = opt;
                        } else {
                            System.err.println("-h requires host name , found : '" + opt + '\'');
                            return;
                        }
                    } else {
                        System.err.println("-h requires host name");
                    }
                } else if ("-p".equals(arg)) {
                    if (i + 1 < narg) {
                        opt = args[++i];
                        if (opt.charAt(0) != '-') {
                            try {
                                port = Integer.parseInt(opt);
                            } catch (NumberFormatException e) {
                                System.err.println("-p requires port number, found :'" + opt + '\'');
                                return;
                            }
                        } else {
                            System.err.println("-p requires port number, found : '" + opt + '\'');
                        }
                    } else {
                        System.err.println("-p requires port number");
                        return;
                    }
                } else if ("-u".equals(arg)) {
                    if (i + 1 < narg) {
                        opt = args[++i];
                        if (opt.charAt(0) != '-') {
                            username = opt;
                        } else {
                            System.err.println("-u requires username , found : '" + opt + '\'');
                            return;
                        }
                    } else {
                        System.err.println("-u requires username");
                    }
                } else if ("-r".equals(arg)) {
                    if (i + 1 < narg) {
                        opt = args[++i];
                        if (opt.charAt(0) != '-') {
                            peer = opt;
                        } else {
                            System.err.println("-r requires peer , found : '" + opt + '\'');
                            return;
                        }
                    } else {
                        System.err.println("-r requires username");
                    }
                }

            }
        }


        ClientChat client = new ClientChat(host, port, username, peer);

        AtomicBoolean finished = new AtomicBoolean(false);

        StreamObserver<MessageChat> inbound = new StreamObserver<MessageChat>() {
            @Override
            public void onNext(MessageChat message) {
                System.out.println(message.getUsername() + " > " + message.getMessage());
            }

            @Override
            public void onError(Throwable t) {
                System.err.println(t);
                finished.set(true);

            }

            @Override
            public void onCompleted() {
                //System.out.println("finished");
                finished.set(true);

            }
        };
        Response check = client.saveData(username, peer);
        if (check.getStatus().equals("User name has been taken, Disconnect from the server")) {
            inbound.onCompleted();
        } else {
            StreamObserver<MessageChat> outbound = client.chatting(inbound);
            Scanner in = new Scanner(System.in);
            while (in.hasNextLine() && !finished.get()) {
                String line = in.nextLine();
                if ("q!".equals(line)) {
                    MessageChat messageChat = sendMessage(username + " has left", peer);
                    outbound.onNext(messageChat);
                    break;
                } else {
                    MessageChat messageChat = sendMessage(line, username);
                    outbound.onNext(messageChat);
                }

            }


        }

        //System.out.println("Client connect to " + host + " : " + port + " : " + username + " : " + peer);
    }

    private static MessageChat sendMessage(String line, String username) {
        MessageChat message = MessageChat.newBuilder()
                .setMessage(line)
                .setUsername(username)
                .build();
        return message;

    }

}
