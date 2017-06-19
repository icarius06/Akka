package co.m800.assgnt.akka.actors;

import akka.actor.AbstractActor;
import akka.actor.Props;

/**
 * Created by tulasoft on 6/19/17.
 */
public class Aggregator extends AbstractActor {
    long wordCount;

    static public Props props() {
        return Props.create(Aggregator.class, () -> new Aggregator());
    }

    static public class StartOfFileEvent {
        public final String firstLine;

        public StartOfFileEvent(String firstLine) {
            this.firstLine = firstLine;
        }

    }

    static public class EndOfFileEvent {
        public final String endLine;

        public EndOfFileEvent(String endLine) {
            this.endLine = endLine;
        }
    }

    static public class LineEvent {
        public final String line;

        public LineEvent(String line) {
            this.line = line;
        }

    }


    @Override
    public Receive createReceive() {
        return receiveBuilder().match(StartOfFileEvent.class, startOfFileEvent -> {
            this.wordCount = startOfFileEvent.firstLine.split(" ").length; //initialise the count
        })
                .match(EndOfFileEvent.class, endOfFileEvent -> {
                    this.wordCount = wordCount + endOfFileEvent.endLine.split(" ").length; //initialise the count
                }).match(LineEvent.class, lineEvent -> {
                    wordCount = wordCount + lineEvent.line.split(" ").length;
                }).build();
    }

}
