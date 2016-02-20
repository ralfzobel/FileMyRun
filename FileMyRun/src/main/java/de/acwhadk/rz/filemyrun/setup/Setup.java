package de.acwhadk.rz.filemyrun.setup;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;

/**
 * Create a simple setup for the program.
 * TODO make the setup editable by the user.
 * 
 * @author Ralf
 *
 */
public class Setup {

	private static final String APP_NAME = "FileMyRun";
	
	private static final String INI_FILE = APP_NAME + ".ini";
	private static final String ACTIVITIES_DIR = "Activities";
	private static final String IMPORTED_DIR = "imported";
	private static final String DOWNLOAD_DIR = "Downloads";

	private static final String PROP_DOWNLOAD_DIR = "download.dir";
	private static final String PROP_IMPORTED_DIR = "imported.dir";
	private static final String PROP_ACTIVITIES_DIR = "activities.dir";

	private static final String sep = File.separator;
	
	private static Setup instance;
	
	private Properties props;

	private IOException ioError;
	
	private Setup() {
		props = new java.util.Properties();
		try {
			loadProperties();
		} catch (IOException e) {
			ioError = e;
		}
	}
	
	public static Setup getInstance() {
		if (instance == null) {
			instance = new Setup();
		}
		return instance;
	}
	
	private void loadProperties() throws IOException {
		String dataFolder = System.getenv("LOCALAPPDATA");
		String iniFileName = dataFolder + sep + APP_NAME + sep + INI_FILE;
		File iniFile = new File(iniFileName);
		if (!iniFile.exists()) {
			setInitialData();
			checkDirs();
			createIniFile(iniFileName);
		} else {
			Reader reader = new BufferedReader(new FileReader(iniFile));
			props.load(reader);
			checkDirs();
		}
	}

	private File setInitialData() throws IOException {
		String dataFolder = System.getenv("LOCALAPPDATA");
		String userFolder = System.getenv("USERPROFILE");
		String appFolder = dataFolder + sep + APP_NAME;
		props.clear();
		props.setProperty(PROP_ACTIVITIES_DIR, appFolder + sep + ACTIVITIES_DIR);
		props.setProperty(PROP_IMPORTED_DIR, appFolder + sep + IMPORTED_DIR);
		props.setProperty(PROP_DOWNLOAD_DIR, userFolder + sep + DOWNLOAD_DIR);
		return null;
	}

	private void createIniFile(String iniFileName) throws IOException {
		Writer out = new BufferedWriter(new FileWriter(iniFileName));
		props.store(out , "Auto generates INI-File. Do NOT edit!");
		out.close();
	}
	
	private void checkDirs() throws IOException {
		String dataFolder = System.getenv("LOCALAPPDATA");
		String appFolder = dataFolder + sep + APP_NAME;
		checkFolder(appFolder);
		checkFolder(props.getProperty(PROP_ACTIVITIES_DIR));
		checkFolder(props.getProperty(PROP_IMPORTED_DIR));
		File downloadDir = new File(props.getProperty(PROP_DOWNLOAD_DIR));
		if (!downloadDir.isDirectory()) {
			throw new IOException(props.getProperty(PROP_DOWNLOAD_DIR) + " is no directory");
		}
	}
	
	private void checkFolder(String folder) throws IOException {
		File dir = new File(folder);
		if (!dir.exists()) {
			if (!dir.mkdir()) {
				throw new IOException("cannot create directory " + folder);
			}
		}
		if (!dir.isDirectory()) {
			throw new IOException(folder + " is no directory");
		}
	}

	public String getApplicationName() {
		return APP_NAME;
	}

	public String getActivitiesFolder() {
		return props.getProperty(PROP_ACTIVITIES_DIR);
	}

	public String getDownloadFolder() {
		return props.getProperty(PROP_DOWNLOAD_DIR);
	}

	public String getImportedFolder() {
		return props.getProperty(PROP_IMPORTED_DIR);
	}

	public IOException getIoError() {
		return ioError;
	}
}
