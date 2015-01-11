package munakello_public;

import java.io.File;
import java.util.Timer;

/**
 * A simple egg timer that plays a random mp3 file from a directory at set intervals.
 * 
 * 1st argument - Wanted playback period as minutes.
 * 2nd argument - Wanted playback directory (relative path, defaults to the current folder).
 * 
 * @author Sampo Osmonen
 */
public class EggTimer {

	private Timer timer = new Timer();
	private long period = -1;
	private RandomPlayerTask playTask;

	public void setPeriod(long period) {
		this.period = period * 60000;
	}

	public void setDirectory(String playDirStr) throws BadDirectoryException {
		String finalDir;
		if (playDirStr == null || playDirStr.equals(".") || playDirStr.equals("")) {
			finalDir = ".";
		}
		else {
			String sep = File.separator;
			finalDir = "." + sep + playDirStr;
		}
		File dir = new File(finalDir);
		this.playTask = new RandomPlayerTask(dir);
	}


	public EggTimer(long period, String dirStr) throws BadDirectoryException {
		setPeriod(period);
		setDirectory(dirStr);
	}

	public EggTimer(long period) throws BadDirectoryException {
		this(period, ".");
	}

	public EggTimer() {
	}


	public void start() throws Exception {
		if (this.period < 0) {
			throw new Exception("First define the period");
		}
		this.timer.schedule(this.playTask, this.period, this.period);
	}

	public void close() {
		this.timer.cancel();
	}


	public static void main(String[] args) {
		if (args.length <= 0) {
			System.err.println("Define at least the period (minutes)");
			return;
		}
		long period;
		try {
			period = Long.parseLong(args[0]);
		}
		catch (NumberFormatException e) {
			System.err.println("Input the period as an integer (minutes)");
			return;
		}
		EggTimer eggTimer = new EggTimer();
		eggTimer.setPeriod(period);
		try {
			if (args.length > 1) {
				eggTimer.setDirectory(args[1]);
			}
			else {
				eggTimer.setDirectory(".");
			}
		}
		catch (BadDirectoryException e) {
			eggTimer.close();
			System.err.println(e.getMessage());
			return;
		}
		try {
			eggTimer.start();
		}
		catch (Exception e) {
			eggTimer.close();
			e.printStackTrace();
			return;
		}
	}

}
