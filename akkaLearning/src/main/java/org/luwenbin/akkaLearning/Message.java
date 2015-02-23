package org.luwenbin.akkaLearning;

import scala.concurrent.duration.Duration;

public class Message {
	/**
	 * @author luwenbin888 Job message to Master actor to start computation
	 */
	static class JobMessage {
		private int num;

		public JobMessage(int num) {
			super();
			this.num = num;
		}

		public int getNum() {
			return num;
		}

		public void setNum(int num) {
			this.num = num;
		}

	}

	/**
	 * @author luwenbin888 Message to Worker actor to do actual computation
	 */
	static class WorkerMessage {
		private int workerId;
		private int start;
		private int end;

		public WorkerMessage(int workerId, int start, int end) {
			super();
			this.workerId = workerId;
			this.start = start;
			this.end = end;
		}

		public int getWorkerId() {
			return workerId;
		}

		public void setWorkerId(int workerId) {
			this.workerId = workerId;
		}

		public int getStart() {
			return start;
		}

		public void setStart(int start) {
			this.start = start;
		}

		public int getEnd() {
			return end;
		}

		public void setEnd(int end) {
			this.end = end;
		}

	}

	/**
	 * @author luwenbin888 Result message from Worker actor to Master actor
	 */
	static class ComputeResultMessage {
		private int workerId;
		private int start;
		private int end;
		private long result;

		public ComputeResultMessage(int workerId, int start, int end,
				long result) {
			super();
			this.workerId = workerId;
			this.start = start;
			this.end = end;
			this.result = result;
		}

		public int getStart() {
			return start;
		}

		public void setStart(int start) {
			this.start = start;
		}

		public int getEnd() {
			return end;
		}

		public void setEnd(int end) {
			this.end = end;
		}

		public int getWorkerId() {
			return workerId;
		}

		public void setWorkerId(int workerId) {
			this.workerId = workerId;
		}

		public long getResult() {
			return result;
		}

		public void setResult(long result) {
			this.result = result;
		}

	}

	static class CompletedMessage {
		private long result;
		private Duration duration;

		public CompletedMessage(long result, Duration duration) {
			super();
			this.result = result;
			this.duration = duration;
		}

		public long getResult() {
			return result;
		}

		public void setResult(long result) {
			this.result = result;
		}

		public Duration getDuration() {
			return duration;
		}

		public void setDuration(Duration duration) {
			this.duration = duration;
		}

	}
}
