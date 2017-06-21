package co.m800.assgnt.akka.actors;

import akka.actor.Props;
import org.apache.commons.lang3.StringUtils;

/**
 * Created on 6/19/2017.
 * By Mike.
 * First Akka
 * Copyright (c) 2017 Tulasoft Creative Studio <code@tulasoftcreative.com>
 * All Rights Reserved.
 * <p>
 * The Aggregator actor split words in the lines by the space “ “ character based on the “line” event
 * The Aggregator actor counts the number of words in a file
 * The Aggregator actor prints the number of words in a file in the console when it receives the “end-of-file” event
 */
public class Aggregator extends BaseActor {
    static long wordCount = 0;

    static public Props props() {
        return Props.create(Aggregator.class, () -> new Aggregator());
    }

    /**
     * This protocol listens to a start of file event message from the FileParser.
     */
    static public class StartOfFileEvent {
        public final String firstLine;

        public StartOfFileEvent(String firstLine) {
            this.firstLine = firstLine;
        }
    }

    /**
     * This protocol listens to an end of file event message from the FileParser.
     */
    static public class EndOfFileEvent {
        public final String endLine;

        public EndOfFileEvent(String endLine) {
            this.endLine = endLine;
        }
    }

    /**
     * This protocol listens to a line event message from the FileParser.
     */
    static public class LineEvent {
        public final String line;
        public LineEvent(String line) {
            this.line = line;
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(StartOfFileEvent.class, startOfFileEvent -> {
            if (!StringUtils.isEmpty(startOfFileEvent.firstLine)) {
                String firstLine = startOfFileEvent.firstLine;
                this.wordCount = firstLine.split(" ").length; //initialise the count
            }
        }).match(EndOfFileEvent.class, endOfFileEvent -> {
            if (!StringUtils.isEmpty(endOfFileEvent.endLine)) {
                this.wordCount = wordCount + endOfFileEvent.endLine.split(" ").length; //initialise the count
                System.out.print("\nWords\t" + wordCount + "\n"); //print out results
                getContext().stop(self()); //Poison pill in child actor called when done to stop explicitly
            }
        }).match(LineEvent.class, lineEvent -> {
            if (!StringUtils.isEmpty(lineEvent.line)) {
                log.info("Aggregator Actor");
                wordCount = wordCount + lineEvent.line.split(" ").length;
            }
        }).build();
    }

    /**
     * Returns the wordCount of the file
     *
     * @return
     */
    public long testWordCount() {
        return wordCount;
    }
}
