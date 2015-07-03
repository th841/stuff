package hu.th.dynorgchart.data;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 
 * Listener to read data on servlet context init
 * 
 * @author th
 */
public class DataInitializer implements ServletContextListener {

    private static final String DATA_FILE_NAME = "directory.json";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String realPath = sce.getServletContext().getRealPath("");
        File dataFile = new File(realPath, DATA_FILE_NAME);
        if (dataFile.isFile() == true) {
            if (dataFile.canRead() == true) {
                try {
                    DataFileReader.getInstance().processDataFile(dataFile);
                } catch (Throwable e) {
                    // catch everything
                    System.err.println("Failure processing data file: " + e.getMessage());
                }
            } else {
                System.out.println("Cannot read data file [" + dataFile.getAbsolutePath() + "]. No permission.");
            }
        } else {
            System.out.println("Data file [" + dataFile.getAbsolutePath() + "] not found.");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // TODO Auto-generated method stub

    }

}
