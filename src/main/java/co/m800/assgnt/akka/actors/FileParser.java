package co.m800.assgnt.akka.actors;

import akka.actor.ActorRef;
import akka.actor.Props;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created on 6/19/2017.
 * By Mike.
 * First Akka
 * Copyright (c) 2017 Tulasoft Creative Studio <code@tulasoftcreative.com>
 * All Rights Reserved.
 * The FileParser actor sends different events (“start-of-file”, “line”, “end-of-file”) to an Aggregator actor,
 * depending on the parser state
 */
public class FileParser extends BaseActor {
    String parserState;

    private final ActorRef aggregatorRef;
    /**
     * Create Props for an actor of this type.
     *
     * @return a Props for creating this actor
     */
    static public Props props() {
        return Props.create(FileParser.class, () -> new FileParser());
    }

    /**
     * Protocal to handles signal sent to FileParser
     */
    public static class ParseMessageEvent {
        public final Path file;

        public ParseMessageEvent(Path file) {
            this.file = file;
        }
    }

    /**
     * Constructor for Actor
     */
    FileParser() {
        aggregatorRef = getContext().actorOf(Aggregator.props(), "aggregatorRef");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(ParseMessageEvent.class, this::onParseMessage).build();
    }

    /**
     * Check if there is any file in a predefined directory
     *
     * @param parseMessageEvent
     */
    private void onParseMessage(ParseMessageEvent parseMessageEvent) {
        //check if there is any file in predefined directory
        try (Stream<String> stream = Files.lines(parseMessageEvent.file)) {
            ArrayList<String> list = stream.collect(Collectors.toCollection(ArrayList<String>::new));
            log.info("FileParser actor");
            for (String line : list) {
                if (line.trim().equals(list.get(0).trim())) {
                    parserState = "START-OF-FILE";
                    log.info("start-of-file Event");
                    aggregatorRef.tell(new Aggregator.StartOfFileEvent(line), getSelf());//depending on state
                } else if (line.trim().equals(list.get(list.size() - 1).trim())) {
                    parserState = "END-OF-FILE";
                    log.info("end-of-file Event");
                    aggregatorRef.tell(new Aggregator.EndOfFileEvent(line), getSelf());//depending on state
                } else {
                    parserState = "LINE";
                    log.info("line Event");
                    aggregatorRef.tell(new Aggregator.LineEvent(line), getSelf());//depending on state
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
