package org.luwenbin.akkaLearning;

import org.luwenbin.akkaLearning.Message.ComputeResultMessage;
import org.luwenbin.akkaLearning.Message.WorkerMessage;

import scala.Option;
import akka.actor.UntypedActor;

/**
 * @author luwenbin888 This is Worker actor, computes sum from i to j assigned
 *         by Master actor
 */
public class WorkerActor extends UntypedActor {
	
	private static int RetryCount = 1;
	
	private final String RestartFormat = "Restart worker %s, start %s, end %s";
	private final String ExceptionFormat = "Thrown exception from Worker %s";
	private final String UnknownExceptionFormat = "Unknown exception %s, bypass it";
	
	@Override
	public void preRestart(Throwable cause, Option<Object> msg) {
		if (cause instanceof MyException && msg.nonEmpty()) {
			WorkerMessage task = (WorkerMessage)msg.get();
			System.out.println(String.format(RestartFormat, task.getWorkerId(), task.getStart(), task.getEnd()));
			getSelf().tell(task, getSelf());
		}
		else {
			System.out.println(String.format(UnknownExceptionFormat, cause.toString()));
		}
	}

	@Override
	public void onReceive(Object msg) throws Exception {
		if (msg instanceof WorkerMessage) {
			
			WorkerMessage task = (WorkerMessage) msg;
			if((RetryCount--) >=1) throw new MyException(String.format(ExceptionFormat, task.getWorkerId()));
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
