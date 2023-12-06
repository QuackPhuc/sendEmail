package com.example.project.server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Server{
    private final String key = keyGenerator();
    static ArrayList<String> reqList = new ArrayList<String>();
    static ArrayList<String> mailList = new ArrayList<String>();

    static ArrayList<String> numberList = new ArrayList<String>();

    private String keyGenerator(){
        Random r = new Random();
        String s = "";
        for (int i=0;i<8;i++) {
            s += (char) (r.nextInt(25) + 65);
        }
        return s;
    }
    public String getKey(){
        return key;
    }
    public void run(){


        System.out.println("CODE: " + key);
    }
    public static void main(String[]args) throws IOException {
        Server server = new Server();
        server.run();
        getRequest gr = new getRequest(server.key);
        sendResponse sr = new sendResponse(server.key);
        while (true){
            System.out.println(Server.mailList.toString()+Server.reqList.toString()+Server.numberList.toString());
            gr.run();
            sr.run();
        }

    }
}

