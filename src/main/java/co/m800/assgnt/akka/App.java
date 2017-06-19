package co.m800.assgnt.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import co.m800.assgnt.akka.actors.FileScanner;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created on 6/19/2017.
 * By Mike.
 * First Akka
 * Copyright (c) 2017 Tulasoft Creative Studio <code@tulasoftcreative.com>
 * All Rights Reserved.
 * <p>
 * <p>
 * On application startup (main), you’d create your ActorSystem and eventual actors you need
 * The application (main), sends a scan message to a FileScanner actor which will check if
 * there is any file in predefined directory
 */
public class App {
    public static void main(String[] args) {
        final ActorSystem actorSystem = ActorSystem.create("first-akka");
        try {
            System.out.print("\nEnter logs directory.");
            Scanner sc = new Scanner(System.in);
            String directory = sc.next();

            if (!StringUtils.isEmpty(directory)) {
                //#create-actors
                final ActorRef fileScanner = actorSystem.actorOf(FileScanner.props(directory), "fileScanner");
                fileScanner.tell(new FileScanner.ScanMessageEvent(), ActorRef.noSender());

                //#sending message to FileScanner with dir to scan
                System.out.println("Counting words in log files in \"" + directory + "\"\n");
                System.out.println(">>> Press ENTER to exit <<<");
                System.in.read();
            } else {
                System.out.print("\n Directory value cant be empty.");
            }

        } catch (Exception e) {
            if (e instanceof NoSuchFieldException)
                System.out.print(e.getMessage()); //handle accordingly
            if (e instanceof IOException)
                System.out.print(e.getMessage()); //handle accordingly
        } finally {
            actorSystem.terminate();
        }
    }
}
