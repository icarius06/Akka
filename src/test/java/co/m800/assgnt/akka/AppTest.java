package co.m800.assgnt.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;
import akka.testkit.javadsl.TestKit;
import co.m800.assgnt.akka.actors.Aggregator;
import co.m800.assgnt.akka.actors.FileParser;
import co.m800.assgnt.akka.actors.FileScanner;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AppTest {
    static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    /**
     * Prove that we can load an actor Ref
     */
    @Test
    public void demonstrateFileScannerActorRef() {
        final Props props = FileScanner.props("/home/tulasoft/Documents/sampls"); //test folder
        final TestActorRef<FileScanner> ref = TestActorRef.create(system, props, "fileScanner");
        final FileScanner actor = ref.underlyingActor();
        assertTrue(actor.testMe());
    }

    /**
     * Prove that we can load an actor Ref
     */
    @Test
    public void demonstrateFileParserActorRef() {
        final Props props = FileParser.props(); //test folder
        final TestActorRef<FileParser> ref = TestActorRef.create(system, props, "fileParser");
        final FileParser actor = ref.underlyingActor();
        assertTrue(actor.testMe());
    }

    /**
     * Prove that we can load an actor Ref
     */
    @Test
    public void demonstrateAggregatorActorRef() {
        final Props props = Aggregator.props(); //test folder
        final TestActorRef<Aggregator> ref = TestActorRef.create(system, props, "aggregator");
        final Aggregator actor = ref.underlyingActor();
        assertTrue(actor.testMe());
    }

    /**
     * We test the Aggregator Actor for inconsistencies in file word count
     */
    @Test
    public void testAggregatorWordCount() {
        final Props props = Aggregator.props();
        final TestActorRef<Aggregator> ref = TestActorRef.create(system, props, "aggregator");

        //line items in file
        List<String> items = new ArrayList<>();
        items.add("1 s");
        items.add("2");
        items.add("3 g");
        items.add("4 5 6 g");
        final Aggregator actor = ref.underlyingActor();
        items.stream().forEach(item -> {
            if (item.trim().equals(items.get(0).trim())) {
                ref.tell(new Aggregator.StartOfFileEvent(item), ref);
            } else if (item.trim().equals(items.get(items.size() - 1).trim())) {
                ref.tell(new Aggregator.EndOfFileEvent(item), ref);
            } else {
                ref.tell(new Aggregator.LineEvent(item), ref);
            }
        });

        //expect 12 words in the 'file'
        assertEquals(9, actor.testWordCount());
    }

    /**
     * Count the number of files in a directory visited
     */
    @Test
    public void testFileScannerFilesInDirCount() {
        final Props props = FileScanner.props("/home/tulasoft/Documents/sampls"); //test folder
        final TestActorRef<FileScanner> ref = TestActorRef.create(system, props, "fileScanner");
        final FileScanner actor = ref.underlyingActor();
        ref.tell(new FileScanner.ScanMessageEvent(), ActorRef.noSender());
        assertEquals(2, actor.testFileCount());
    }
}
