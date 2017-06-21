package co.m800.assgnt.akka.actors;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * Created on 6/21/2017.
 * By Mike.
 * First Akka
 * Copyright (c) 2017 Tulasoft Creative Studio <code@tulasoftcreative.com>
 * All Rights Reserved.
 * <p>
 * Base Actor for DRY code
 */
public class BaseActor extends AbstractActor {

    //Use this for logging actor stuff
    public LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public Receive createReceive() {
        return null;
    }

    /**
     * Use this method to test if Ref is loaded
     *
     * @return true/false depending on whether ActorRef is visible to test
     */
    public boolean testMe() {
        return true;
    }
}
