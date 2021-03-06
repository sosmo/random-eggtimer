package eggtimer;

import java.io.File;
import java.util.TimerTask;

public class RandomPlayerTask extends TimerTask {

	private final Mp3Player mp3Player;


	public RandomPlayerTask(File dir) throws BadDirectoryException {
		this.mp3Player = new Mp3Player();
		this.mp3Player.setDir(dir);
	}


	@Override
	public void run() {
		try {
			File playedFile = mp3Player.playRandomAudioFromDir();
			System.out.println(playedFile.getName());
		}
		catch (AudioPlaybackFailureException | BadDirectoryException e) {
			System.err.println("Playback failure!");
			System.err.println(e.getMessage() + ".");
			System.exit(1);  // Exit rather than keep repeating.
		}
	}

}
