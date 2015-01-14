package eggtimer;

public class AudioPlaybackFailureException extends Exception {

	public AudioPlaybackFailureException() {
		super();
	}

	public AudioPlaybackFailureException(String message) {
		super(message);
	}

	public AudioPlaybackFailureException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
