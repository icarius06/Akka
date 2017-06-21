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
        final TestActorRef<FileScanner> testActorRef1 = TestActorRef.create(system, props, "fileScanner1");
        final FileScanner fileScannerActor = testActorRef1.underlyingActor();
        assertTrue(fileScannerActor.testMe());
    }

    /**
     * Prove that we can load an actor Ref
     */
    @Test
    public void demonstrateFileParserActorRef() {
        final Props props = FileParser.props(); //test folder
        final TestActorRef<FileParser> testActor2Ref = TestActorRef.create(system, props, "fileParser1");
        final FileParser fileParserActor = testActor2Ref.underlyingActor();
        assertTrue(fileParserActor.testMe());
    }

    /**
     * Prove that we can load an actor Ref
     */
    @Test
    public void demonstrateAggregatorActorRef() {
        final Props props = Aggregator.props(); //test folder
        final TestActorRef<Aggregator> testActor3Ref = TestActorRef.create(system, props, "aggregator1");
        final Aggregator aggregatorActor = testActor3Ref.underlyingActor();
        assertTrue(aggregatorActor.testMe());
    }

    /**
     * We test the Aggregator Actor for inconsistencies in file word count
     */
    @Test
    public void testAggregatorWordCount() {
        final Props props = Aggregator.props();
        final TestActorRef<Aggregator> aggregatorTestActorRef = TestActorRef.create(system, props, "aggregatorWordCountTest");

        //line items in file
        List<String> items = new ArrayList<>();
        items.add("1 s");
        items.add("2");
        items.add("3 g");
        items.add("4 5 6 g");
        final Aggregator aggregatorWordCountActor = aggregatorTestActorRef.underlyingActor();
        items.stream().forEach(item -> {
            if (item.trim().equals(items.get(0).trim())) {
                aggregatorTestActorRef.tell(new Aggregator.StartOfFileEvent(item), aggregatorTestActorRef);
            } else if (item.trim().equals(items.get(items.size() - 1).trim())) {
                aggregatorTestActorRef.tell(new Aggregator.EndOfFileEvent(item), aggregatorTestActorRef);
            } else {
                aggregatorTestActorRef.tell(new Aggregator.LineEvent(item), aggregatorTestActorRef);
            }
        });

        //expect 9 words in the 'file'
        assertEquals(9, aggregatorWordCountActor.getWordCount());
    }

    /**
     * Count the number of files in a directory visited
     */
    @Test
    public void testFileScannerFilesInDirCount() {
        final Props props = FileScanner.props("/home/tulasoft/Documents/sampls"); //test folder
        final TestActorRef<FileScanner> ref = TestActorRef.create(system, props, "fileScannerFilesInDirCount");
        final FileScanner actor = ref.underlyingActor();
        ref.tell(new FileScanner.ScanMessageEvent(), ActorRef.noSender());
        assertEquals(2, actor.getFilesCount());
    }
}
