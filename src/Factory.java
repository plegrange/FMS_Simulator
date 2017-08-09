import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Factory {
    private List<Robot> robots;
    private List<Part> parts;
    private int timeStep;
    private Stage primaryStage;
    private TableView table = new TableView();

    public Factory() {
        //this.primaryStage = primaryStage;
        createRobots();
        linkRobots();
        createParts();
        timeStep = 0;
        //setupBindings();
        simulate();
        new Output(robots);
    }

    private void simulate() {
        while (robots.get(robots.size()-1).volume < numberOfParts) {
            if (parts.size() > 0) {
                if (robots.get(0).bufferIntLevel() < robots.get(0).getBufferCapacity()) {
                    robots.get(0).receivePartIntoBuffer(parts.remove(0));
                }
            }
            for (Robot robot : robots) {
                robot.doTimeStep();
            }
            System.out.println("Timestep: " + timeStep + " | Parts Remaining: " + parts.size() + " | Volume: " + (robots.get(robots.size() - 1)).volume);
            System.out.flush();
            // displayLineStatus();
            timeStep++;
        }
    }

    Scanner userInput = new Scanner(System.in);

    private void displayLineStatus() {
        while (userInput.nextLine().isEmpty()) {

        }
        System.out.println("Timestep: " + timeStep);
        for (Robot robot : robots) {
            robot.displayStatus();
        }
        System.out.print("\b\b\b\b\b");
    }

    private int numberOfParts = 500;

    private void createParts() {
        parts = new ArrayList<>();
        for (int i = 0; i < numberOfParts; i++) {
            parts.add(new Part(i));
        }
    }

    private void linkRobots() {
        for (int i = 0; i < numberOfRobots-1; i++) {
            robots.get(i).setNextRobotInLine(robots.get(i + 1));
        }
    }
private int numberOfRobots = 100;
    private void createRobots() {
        robots = new ArrayList<>();
        Random random;
        for(int i = 1; i <= numberOfRobots; i++){
            random = new Random();
            robots.add(new Robot("RB"+i,10+random.nextInt(10),10+random.nextInt(10),0.05*random.nextDouble(),1000+random.nextInt(1000)));
        }

    }
}
