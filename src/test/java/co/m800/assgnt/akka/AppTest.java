package co.m800.assgnt.akka;

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

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
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
     * We test the Aggregator Actor for inconsitencies in file word count
     */
    @Test
    public void testAggregatorWordCount() {
        final Props props = Aggregator.props();
        final TestActorRef<Aggregator> ref = TestActorRef.create(system, props, "aggregator");

        //line items in file
        List<String> items = new ArrayList<>();
        items.add("one two three");
        items.add("moja mbili tatu nne");
        items.add("moja mbili tatu nne tano");
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

        assertEquals(12, actor.testWordCount());
    }


}
