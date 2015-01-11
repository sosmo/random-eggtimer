package munakello_public;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.util.Random;

import javazoom.jl.player.Player;

public class AudioPlayer implements AudioPlayerInterface {

	private File dir;
	private String type = "";
	private static Random random = new Random();
	//private File[] mp3Files;  // It's a good idea to check the files every time when starting playback, so no need to put them to memory.

	@Override
	public void setDir(File dir) throws BadDirectoryException {
		// These two checks aren't required, really (will be checked later), but it's a good idea to do an extra iteration to make sure invalid dir doesn't get set before starting.
		File[] files = listFilesWithType(dir, type);  // throws BadDirectoryEx with a custom message if not a directory
		if (files.length <= 0) {
			throw new BadDirectoryException("The directory must contain at least one file of the type of your choice");
		}
		this.dir = dir;
	}


	public AudioPlayer(String type) {
		this.type = type;
	}


	public static File[] listFilesWithType(File dir, String type) throws BadDirectoryException {
		if (dir.isDirectory()) {
			throw new BadDirectoryException("Give an address to a directory");
		}
		File[] files;
		if (type == null || type.equals("")) {
			files = dir.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File file, String name) {
					return file.isDirectory();
				}
			});
		}
		else {
			files = dir.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File file, String name) {
					return name.matches(".*\\.(" + type + ")");
				}
			});
		}
		return files;
	}

	public static File[] listFiles(File dir) throws BadDirectoryException {
		return listFilesWithType(dir, null);
	}

	public static File playRandomAudio(File dir, String type) throws AudioPlaybackFailureException, BadDirectoryException {
		File[] files = listFilesWithType(dir, type);
		File filePlayed = playRandomAudio(files);
		if (filePlayed == null) {
			throw new BadDirectoryException("The directory must contain at least one file of the type of your choice");
		}
		return filePlayed;
	}

	public static File playRandomAudio(File[] files) throws AudioPlaybackFailureException {
		if (files.length <= 0) {
			return null;
		}
		File next = files[random.nextInt(files.length)];
		playAudio(next);
		return next;
	}

	public File playRandomAudioFromDir() throws AudioPlaybackFailureException, BadDirectoryException {
		if (dir == null) {
			throw new IllegalStateException("First set the playback directory");  // More specific error message for bad usage
		}
		return playRandomAudio(dir, type);
	}

	public static File playAudio(File file) throws AudioPlaybackFailureException {
		String fileName = file.getName();
		if (fileName.endsWith(".mp3")) {
			try (FileInputStream fis = new FileInputStream(file)) {
				Player player = new Player(fis);
				player.play();
			}
			catch (Exception e) {
				throw new AudioPlaybackFailureException("Playback failure" + System.lineSeparator() + e.getMessage(), e.getCause());  // javazoom ei kylläkään heitä exceptionia mut ihan sama
			}
		}
		else {
			throw new AudioPlaybackFailureException("Unsupported filetype");
		}
		return file;
	}

}
