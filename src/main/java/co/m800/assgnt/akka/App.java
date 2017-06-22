package co.m800.assgnt.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import co.m800.assgnt.akka.actors.FileScanner;
import co.m800.assgnt.akka.utils.Helper;
import co.m800.assgnt.akka.utils.LogMessages;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private static final Logger log = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        final ActorSystem actorSystem = ActorSystem.create("first-akka");
        try {
            String directory = Helper.getLogsFolder();

            if (!StringUtils.isEmpty(directory)) {
                log.info("Processing log files in \"" + directory + "\"");
                //#create-file-scanner-actor
                final ActorRef fileScanner = actorSystem.actorOf(FileScanner.props(directory), "fileScanner");
                //#sending message to FileScanner with dir to scan
                fileScanner.tell(new FileScanner.ScanMessageEvent(), ActorRef.noSender());
                log.info(">>> Press ENTER to exit <<<");
                System.in.read();
            } else {
                log.log(Level.SEVERE, LogMessages.EMPTY_LOGS_DIR);
            }
        } catch (Exception e) {
            if (e instanceof NoSuchFileException)
                log.log(Level.SEVERE, LogMessages.MISSING_LOGS_DIR);//handle accordingly
            if (e instanceof NoSuchFieldException)
                System.out.print(e.getMessage()); //handle accordingly
            if (e instanceof IOException)
                System.out.print(e.getMessage()); //handle accordingly
        } finally {
            actorSystem.terminate();
        }
    }
}
