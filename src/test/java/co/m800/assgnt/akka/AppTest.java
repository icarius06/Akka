package co.m800.assgnt.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.javadsl.TestKit;
import co.m800.assgnt.akka.actors.Aggregator;
import co.m800.assgnt.akka.actors.FileParser;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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

    @Test
    public void TheAggregatorClass() {
        new TestKit(system) {{
            final ActorRef service =
                    system.actorOf(Props.create(FileParser.class));
            final ActorRef probe = getRef();

            List<String> items = new ArrayList<>();
            items.add("one two three");
            items.add("moja mbili tatu nne");

            items.stream().forEach(item -> {
                if (item.trim().equals(items.get(0).trim())) {
                    service.tell(new Aggregator.StartOfFileEvent(item), probe);
                } else if (item.trim().equals(items.get(items.size() - 1).trim())) {
                    service.tell(new Aggregator.EndOfFileEvent(item), probe);
                } else {
                    service.tell(new Aggregator.LineEvent(item), probe);
                }
                expectNoMsg();
            });
        }};
    }
}
