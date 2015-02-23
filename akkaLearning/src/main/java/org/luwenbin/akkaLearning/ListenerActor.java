package org.luwenbin.akkaLearning;

import org.luwenbin.akkaLearning.Message.CompletedMessage;

import akka.actor.UntypedActor;

/**
 * @author luwenbin888 Listen for complete message and prints result and then
 *         shut down Akka system.
 */
public class ListenerActor extends UntypedActor {

	private final String Format = "Computation Elapsed %s milliseconds, Result: %s";

	@Override
	public void onReceive(Object msg) throws Exception {
		if (msg instanceof CompletedMessage) {
			CompletedMessage completedMessage = (CompletedMessage) msg;
			System.out.println(String.format(Format, completedMessage
					.getDuration(), completedMessage.getResult()));
			getContext().system().shutdown();
		} else {
			unhandled(msg);
		}
	}

}
