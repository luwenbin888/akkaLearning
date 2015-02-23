package org.luwenbin.akkaLearning;

import java.util.concurrent.TimeUnit;

import org.luwenbin.akkaLearning.Message.CompletedMessage;
import org.luwenbin.akkaLearning.Message.ComputeResultMessage;
import org.luwenbin.akkaLearning.Message.JobMessage;
import org.luwenbin.akkaLearning.Message.WorkerMessage;

import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.actor.SupervisorStrategy.Directive;
import akka.actor.UntypedActor;
import akka.japi.Function;
import akka.routing.RoundRobinRouter;

/**
 * 
 * @author luwenbin888 This is master actor receive job from main and divide the
 *         job and assign to ComputeActor
 */
@SuppressWarnings("deprecation")
public class MasterActor extends UntypedActor {

	private final String resultFormat = "Worker %s completed computation from %s to %s, result %s.";

	ActorRef router;
	ActorRef listener;

	int numOfComplete = 0;
	int numOfWorkers = 0;
	long totalResult = 0;

	long startTimestamp;

	private static SupervisorStrategy strategy = new OneForOneStrategy(10,
			Duration.create(1, TimeUnit.MINUTES),
			new Function<Throwable, Directive>() {

				public Directive apply(Throwable exception) throws Exception {
					if (exception instanceof MyException) {
						return SupervisorStrategy.restart();
					} else {
						return SupervisorStrategy.escalate();
					}
				}

			});

	@Override
	public SupervisorStrategy supervisorStrategy() {
		return strategy;
	}

	public MasterActor() {
		router = getContext().actorOf(
				Props.create(WorkerActor.class).withRouter(
						new RoundRobinRouter(Constants.NumOfWorkers)));
		listener = getContext().actorOf(Props.create(ListenerActor.class),
				"listener");
	}

	@Override
	public void onReceive(Object msg) throws Exception {
		if (msg instanceof JobMessage) {
			startTimestamp = System.currentTimeMillis();
			JobMessage job = (JobMessage) msg;
			int num = job.getNum();

			numOfWorkers = num / Constants.WorkerPayload;
			int remainder = num % Constants.WorkerPayload;

			if (remainder > 0) {
				numOfWorkers++;
			}

			// assign tasks to workers
			int payload = Constants.WorkerPayload;
			int start = 1;
			for (int i = 1; i <= numOfWorkers; i++) {
				if (i == numOfWorkers && remainder > 0) {
					payload = remainder;
				}
				WorkerMessage task = new WorkerMessage(i, start, start
						+ payload - 1);
				router.tell(task, getSelf());
				start = start + payload;
			}
		} else if (msg instanceof ComputeResultMessage) {
			// collect result from workers and INC total
			ComputeResultMessage resultMsg = (ComputeResultMessage) msg;
			System.out.println(String.format(resultFormat,
					resultMsg.getWorkerId(), resultMsg.getStart(),
					resultMsg.getEnd(), resultMsg.getResult()));
			numOfComplete++;
			totalResult += resultMsg.getResult();
			if (numOfComplete == numOfWorkers) {
				// System.out.println("All workers completed...Result: " +
				// totalResult);
				// getContext().system().shutdown();
				long endTimestamp = System.currentTimeMillis();
				Duration timeElapsed = Duration.create(endTimestamp
						- startTimestamp, TimeUnit.MILLISECONDS);
				CompletedMessage completedMessage = new CompletedMessage(
						totalResult, timeElapsed);
				listener.tell(completedMessage, getSender());
			}
		} else {
			unhandled(msg);
		}
	}
}
