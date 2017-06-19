package co.m800.assgnt.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import co.m800.assgnt.akka.actors.FileScanner;

import java.io.IOException;

public class App {
    public static void main(String[] args) {
        final ActorSystem system = ActorSystem.create("first-akka");
        try {
            //#create-actors
            final ActorRef fileScanner = system.actorOf(FileScanner.props("/home/tulasoft/Documents/sampls"), "fileScanner");
            fileScanner.tell(new FileScanner.ScanMessage(), ActorRef.noSender());
            //#main-send-messages
            System.out.println(">>> Press ENTER to exit <<<");
            System.in.read();
        } catch (IOException ioe) {
        } finally {
            system.terminate();
        }
    }
}
