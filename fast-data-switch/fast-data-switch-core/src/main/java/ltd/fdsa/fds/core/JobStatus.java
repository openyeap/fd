package ltd.fdsa.fds.core;

public enum JobStatus {

	SUCCESS(0), FAILED(1);

	private int status;

	JobStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return this.status;
	}
}
