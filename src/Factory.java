import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Factory {
    private List<Cell> cells;
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
        new Output(cells, timesteps, volume);
    }

    private List<Integer> timesteps, volume;

    private boolean producing() {
        for (Cell cell : cells) {
            if (!cell.queueEmpty())
                return true;
        }
        return false;
    }

    private int totalDownTime = 0;

    private void simulate() {
        timesteps = new ArrayList<>();
        volume = new ArrayList<>();
        for (int p = 0; p < 0.1 * numberOfParts; p++) {
            cells.get(1 + new Random().nextInt(numberOfCells - 1)).receivePartIntoBuffer(new Part(p));
        }
        while (producing()) {
            if (parts.size() > 0) {
                if (cells.get(0).bufferIntLevel() < cells.get(0).getBufferCapacity()) {
                    cells.get(0).receivePartIntoBuffer(parts.remove(0));
                }
            }
            for (Cell cell : cells) {
                cell.doTimeStep();
            }
            timesteps.add(Integer.valueOf(timeStep));
            volume.add(Integer.valueOf((cells.get(cells.size() - 1)).volume));
            System.out.println("Timestep: " + timeStep + " | Parts Remaining: " + parts.size() + " | Volume: " + (cells.get(cells.size() - 1)).volume);
            System.out.flush();
            // displayLineStatus();
            timeStep++;
        }
        for (Cell cell : cells) {
            totalDownTime += cell.getDownTime();
            System.out.println(cell.getCellName() + " Downtime : " + cell.getDownTime());
        }
        System.out.println("Average Downtime: " + (totalDownTime / numberOfCells));
        System.out.println("Average Volume: " + (Double.valueOf(cells.get(cells.size() - 1).volume) / (Double.valueOf(timeStep))));
    }

    Scanner userInput = new Scanner(System.in);

    private void displayLineStatus() {
        while (userInput.nextLine().isEmpty()) {

        }
        System.out.println("Timestep: " + timeStep);
        for (Cell cell : cells) {
            cell.displayStatus();
        }
        System.out.print("\b\b\b\b\b");
    }

    private int numberOfParts = 10000;

    private void createParts() {
        parts = new ArrayList<>();
        for (int i = 0; i < numberOfParts; i++) {
            parts.add(new Part(i));
        }
    }

    private void linkRobots() {
        for (int i = 0; i < numberOfCells - 1; i++) {
            cells.get(i).setNextCellInLine(cells.get(i + 1));
        }
    }

    private int numberOfCells = 50;

    private void createRobots() {
        cells = new ArrayList<>();
        Random random;
        for (int i = 1; i <= numberOfCells; i++) {
            random = new Random();
            cells.add(new Cell("RB" + i,
                    10 + random.nextInt(40),
                    1,
                    0.05 * random.nextDouble(),
                    5 + random.nextInt(50)));
        }

    }
}
