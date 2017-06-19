package co.m800.assgnt.akka.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.io.IOException;
import java.nio.file.*;

/**
 * Created by tulasoft on 6/19/17.
 */
public class FileScanner extends AbstractActor {
    static public Props props(String dir) {
        return Props.create(FileScanner.class, dir);
    }
    
    private final ActorRef fileParser;
    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private final String dir;

    public FileScanner(String dir) {
        this.dir = dir;
        fileParser = getContext().actorOf(FileParser.props(), "fileParser");
    }

    //protocol
    public static class ScanMessage {
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(ScanMessage.class, this::onScanMessage).build();
    }

    /**
     * Check if there is any file in a predefined directory
     *
     * @param checkFile
     */
    private void onScanMessage(ScanMessage checkFile) {
        //check if there is any file in predefined directory
        Path p1 = Paths.get(dir);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(p1)) {
            for (Path file : stream) {
                fileParser.tell(new FileParser.ParseMessage(file), getSelf());
            }
        } catch (IOException | DirectoryIteratorException x) {
            // IOException can never be thrown by the iteration.
            // In this snippet, it can only be thrown by newDirectoryStream.
            System.err.println(x);
        }

        log.info("find file");
    }
}
