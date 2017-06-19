package co.m800.assgnt.akka.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

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
public class FileParser extends AbstractActor {
    private final ActorRef aggregatorRef;

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

    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

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
            //check if the file has any content
            if (list.size() > 0) {
                for (String line : list) {
                    if (line.trim().equals(list.get(0).trim())) {
                        log.info("start-of-file Event");
                        aggregatorRef.tell(new Aggregator.StartOfFileEvent(line), getSelf());//depending on state
                    } else if (line.trim().equals(list.get(list.size() - 1).trim())) {
                        log.info("end-of-file Event");
                        aggregatorRef.tell(new Aggregator.EndOfFileEvent(line), getSelf());//depending on state
                    } else {
                        log.info("line Event");
                        aggregatorRef.tell(new Aggregator.LineEvent(line), getSelf());//depending on state
                    }
                }
            } else {
                aggregatorRef.tell(new Aggregator.EndOfFileEvent(" "), getSelf());//depending on state
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}