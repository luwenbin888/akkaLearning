package org.luwenbin.akkaLearning;

import org.luwenbin.akkaLearning.Message.JobMessage;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

/**
 * @author luwenbin888 Driver class to trigger the Akka system and send message
 *         to Master actor
 */
public class MainTest {

	public static void main(String[] args) {
		JobMessage job = new JobMessage(123123123);
		ActorSystem system = ActorSystem.create("ComputationSystem");
		ActorRef masterActor = system.actorOf(Props.create(MasterActor.class),
				"MasterActor");
		masterActor.tell(job, ActorRef.noSender());

		/*
		 * system.awaitTermination();
		 * System.out.println("Akka system shutdown complete");
		 */
	}
}
