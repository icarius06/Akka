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
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by tulasoft on 6/19/17.
 */
public class FileParser extends AbstractActor {
    private final ActorRef aggregator;

    static public Props props() {
        return Props.create(FileParser.class, () -> new FileParser());
    }

    public static class ParseMessage {
        public final Path file;

        public ParseMessage(Path file) {
            this.file = file;
        }
    }

    FileParser() {
        aggregator = getContext().actorOf(Props.create(Aggregator.class), "aggregator");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(ParseMessage.class, this::onParseMessage).build();
    }

    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    /**
     * Check if there is any file in a predefined directory
     *
     * @param parseMessage
     */
    private void onParseMessage(ParseMessage parseMessage) {
        //check if there is any file in predefined directory
        try (Stream<String> stream = Files.lines(parseMessage.file)) {
            ArrayList<String> list = stream.collect(Collectors.toCollection(ArrayList<String>::new));
            log.info("Read lines");
            for (String line : list) {
                if (line.trim().equals(list.get(0).trim())) {
                    log.info("FirstLine");
                    aggregator.tell(new Aggregator.StartOfFileEvent(line), getSelf());//depending on state
                } else if (line.trim().equals(list.get(list.size() - 1).trim())) {
                    log.info("LastLine");
                    aggregator.tell(new Aggregator.EndOfFileEvent(line), getSelf());//depending on state
                } else {
                    log.info("Line");
                    aggregator.tell(new Aggregator.LineEvent(line), getSelf());//depending on state
                }
            }
            // forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
