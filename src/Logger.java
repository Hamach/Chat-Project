import java.io.FileWriter;
import java.io.IOException;

public class Logger {
    public static final Logger INSTANCE = new Logger("logfile.txt");
    private FileWriter log;

    public Logger(String fileName) {
        try {
            log = new FileWriter(fileName);
        } catch (IOException e) {
            System.err.println("Error with logger");
        }
    }

    public void add(String type, String message) {
        try {
            log.write(type + ": " + message + "\r\n");
            log.flush();
        } catch (IOException e) {
            System.err.println("Error with logger" + e.getMessage());
        }
    }
}
