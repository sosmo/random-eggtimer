package eggtimer;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.zip.DataFormatException;

/**
 * A simple egg timer that plays a random mp3 file from a directory at set intervals. Requires the JavaZOOM JLayer library. (http://www.javazoom.net/).
 *
 * 1st argument - Wanted playback period as minutes.
 * 2nd argument (optional) - Wanted playback directory (relative path, defaults to the current folder).
 *
 * @author Sampo Osmonen
 */
public class EggTimer {

	public static String nl = System.lineSeparator();

	private final Timer timer = new Timer();
	private long period = -1;
	private RandomPlayerTask playTask = null;

	public void setPeriod(long period) throws DataFormatException {
		if (period <= 0) {
			throw new DataFormatException("The period has to be at least 1 (minute)");
		}
		this.period = period * 60000;
	}

	public File setDirectory(String playDirStr) throws BadDirectoryException {
		String finalDir;
		if (playDirStr == null || playDirStr.equals(".") || playDirStr.equals("")) {
			finalDir = ".";
		}
		else {
			String sep = File.separator;
			finalDir = "." + sep + playDirStr;
		}
		File dir = new File(finalDir);
		playTask = new RandomPlayerTask(dir);
		return dir;
	}


	public EggTimer(long period, String dirStr) throws BadDirectoryException, DataFormatException {
		setPeriod(period);
		setDirectory(dirStr);
	}

	public EggTimer(long period) throws BadDirectoryException, DataFormatException {
		this(period, ".");
	}

	public EggTimer() {
	}


	public void start() throws IllegalStateException {
		if (period < 0) {
			throw new IllegalStateException("First define the period");
		}
		if (playTask == null) {
			throw new IllegalStateException("First explicitly set the directory to play from");
		}
		timer.schedule(playTask, period, period);
	}

	public void close() {
		timer.cancel();
	}

	private static void handleError(Exception e) {
		System.err.println(e.getMessage() + "!");
	}


	public static void main(String[] args) {
		System.out.println("Starting random-eggtimer..." + nl);
		if (args.length <= 0) {
			System.err.println("Define at least the period (minutes)!");
			System.exit(1);
		}
		long period = 0;
		try {
			period = Long.parseLong(args[0]);
		}
		catch (NumberFormatException e) {
			System.err.println("Input the period as an integer (minutes)!");
			System.exit(1);
		}
		EggTimer eggTimer = new EggTimer();
		try {
			eggTimer.setPeriod(period);
		}
		catch (DataFormatException e) {
			eggTimer.close();
			System.err.println("Input a period greater than 0!");
			System.exit(1);
		}
		try {
			File dir = null;
			if (args.length > 1) {
				dir = eggTimer.setDirectory(args[1]);
			}
			else {
				dir = eggTimer.setDirectory(".");
			}
			try {
				System.out.println("Directory: " + dir.getCanonicalPath());
			}
			catch (IOException e) {
				// No need to react
			}
		}
		catch (BadDirectoryException e) {
			eggTimer.close();
			handleError(e);
			System.exit(1);
		}
		eggTimer.start();
		System.out.println("Period: " + period + " minutes" + nl + "Counting down now!" + nl);
	}

}
