import javafx.application.Platform;
import sun.rmi.runtime.Log;

import java.io.FileWriter;
import java.util.List;

public class Output {


    public Output(List<Robot> robots) {
        try {

            for (Robot robot : robots) {
                FileWriter writer = new FileWriter(robot.getRobotName() + ".csv");
                List<LogEntry> logEntries = robot.getStatusLog();
                for (LogEntry entry : logEntries) {
                    writer.append(String.valueOf(entry.getTimeStep())+',');
                    writer.append(entry.getStatus()+',');
                    writer.append(String.valueOf(entry.getBufferLevel()));
                    writer.append('\n');
                }
                writer.flush();
                writer.close();
                System.out.println(robot.getRobotName()+".csv written");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Platform.exit();
    }
}
