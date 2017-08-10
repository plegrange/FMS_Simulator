import javafx.application.Platform;

import java.io.FileWriter;
import java.util.List;

public class Output {


    public Output(List<Cell> cells, List<Integer> timesteps, List<Integer> volume) {
        try {

            FileWriter writer = new FileWriter("volume.csv");
            for (int i = 0; i < timesteps.size(); i++) {
                writer.append(timesteps.get(i).toString() + ',');
                writer.append(volume.get(i).toString());
                writer.append('\n');
            }
            writer.flush();
            writer.close();

           /* for (Cell robot : cells) {
                writer = new FileWriter(robot.getCellName() + ".csv");
                List<LogEntry> logEntries = robot.getStatusLog();
                for (LogEntry entry : logEntries) {
                    writer.append(String.valueOf(entry.getTimeStep()) + ',');
                    writer.append(entry.getStatus() + ',');
                    writer.append(String.valueOf(entry.getBufferLevel()));
                    writer.append('\n');
                }
                writer.flush();
                writer.close();
                System.out.println(robot.getCellName() + ".csv written");

            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        Platform.exit();
    }
}
