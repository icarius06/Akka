package co.m800.assgnt.akka.actors;

import akka.actor.ActorRef;
import akka.actor.Props;

import java.io.IOException;
import java.nio.file.*;

/**
 * Created on 6/19/2017.
 * By Mike.
 * First Akka
 * Copyright (c) 2017 Tulasoft Creative Studio <code@tulasoftcreative.com>
 * All Rights Reserved.
 * <p>
 * The FileScanner actor sends a parse message to a FileParser actor in order to initiate the parsing
 */
public class FileScanner extends BaseActor {
    public Path path;

    static public Props props(String dir) {
        return Props.create(FileScanner.class, dir);
    }

    private final ActorRef fileParser;

    private final String dir;

    /**
     * Constructor for Actor
     *
     * @param dir directory possibly containing log files
     */
    public FileScanner(String dir) {
        this.dir = dir;
        fileParser = getContext().actorOf(FileParser.props(), "fileParser");
    }

    //protocol
    public static class ScanMessageEvent {
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(ScanMessageEvent.class, this::onScanMessage).build();
    }

    /**
     * Check if there is any file in a predefined directory
     *
     * @param scanMessageEvent
     */
    private void onScanMessage(ScanMessageEvent scanMessageEvent) {
        this.path = Paths.get(dir);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            for (Path file : stream) {
                fileParser.tell(new FileParser.ParseMessageEvent(file), getSelf());
            }
        } catch (IOException | DirectoryIteratorException exception) {
            System.err.println(exception);
        }

        log.info("FileScanner Actor");
    }
}
