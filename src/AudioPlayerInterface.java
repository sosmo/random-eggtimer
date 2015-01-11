package munakello_public;

import java.io.File;

public interface AudioPlayerInterface {

	public void setDir(File dir) throws BadDirectoryException;
	
}
