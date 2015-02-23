package org.luwenbin.akkaLearning;

import org.luwenbin.akkaLearning.Message.ComputeResultMessage;
import org.luwenbin.akkaLearning.Message.WorkerMessage;

import akka.actor.UntypedActor;

/**
 * @author luwenbin888 This is Worker actor, computes sum from i to j assigned
 *         by Master actor
 */
public class WorkerActor extends UntypedActor {

	@Override
	public void onReceive(Object msg) throws Exception {
		if (msg instanceof WorkerMessage) {
			WorkerMessage task = (WorkerMessage) msg;
			int start = task.getStart();
			int end = task.getEnd();
			long result = 0L;
			for (; start <= end; start++) {
				result += start;
			}
			ComputeResultMessage resultMessage = new ComputeResultMessage(
					task.getWorkerId(), task.getStart(), end, result);

			getSender().tell(resultMessage, getSelf());
		} else {
			unhandled(msg);
		}
	}

}
