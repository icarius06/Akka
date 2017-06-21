package co.m800.assgnt.akka.actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import co.m800.assgnt.akka.utils.LogMessages;

import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    /**
     * Create Props for an actor of this type.
     *
     * @param dir The directory to be passed to this actor’s constructor.
     * @return a Props for creating this actor
     */
    static public Props props(String dir) {
        return Props.create(FileScanner.class, dir);
    }

    private final ActorRef fileParser;

    private final String dir;

    private long filesCount;

    /**
     * Constructor for Actor
     *
     * @param dir directory possibly containing log files
     */
    public FileScanner(String dir) {
        this.dir = dir;
        fileParser = getContext().actorOf(FileParser.props(), "fileParser");
    }

    //Protocol
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
        try {
            Files.list(path).forEach(file -> {
                log.info(LogMessages.PROCESSING + file.getFileName().toString());
                fileParser.tell(new FileParser.ParseMessageEvent(file), getSelf());
                filesCount = filesCount + 1;
            });
        } catch (IOException | DirectoryIteratorException exception) {
            System.err.println(exception);
        }
        log.info(LogMessages.FILE_SCANNER_ACTOR);
    }

    /**
     * Returns the fileCount of the directory
     *
     * @return
     */
    public long testFileCount() {
        return filesCount;
    }
}
