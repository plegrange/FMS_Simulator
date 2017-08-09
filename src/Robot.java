import javafx.beans.property.SimpleStringProperty;
import javafx.css.SimpleStyleableStringProperty;

import java.util.*;

public class Robot {
    private List<LogEntry> statusLog;
    private String robotName;
    private String status;
    private String bufferLevel;
    private int bufferCapacity;
    private Queue<Part> inBufferQueue;
    private Part partInProcessing;
    protected String busy = "BUSY", ready = "READY", blocked = "BLOCKED", waiting = "WAITING", failure = "FAILURE";
    // private SimpleStringProperty status = new SimpleStringProperty();
    //private SimpleStringProperty bufferLevel = new SimpleStringProperty();
    private int currentTimeStep;
    private int timeStepStartedOnPart;
    private int cycleTimeInSeconds;
    private Robot nextRobotInLine;
    private double failureRate;

    public Robot(String robotName, int bufferCapacity, int cycleTimeInSeconds, double failureRate, int meanRepairTime) {
        statusLog = new ArrayList<>();
        this.robotName = robotName;
        this.failureRate = failureRate;
        this.meanRepairTime = meanRepairTime;
        inBufferQueue = new LinkedList<>();
        this.cycleTimeInSeconds = cycleTimeInSeconds;
        this.partInProcessing = null;
        this.bufferCapacity = bufferCapacity;
        status = ready;
        currentTimeStep = 0;
    }

    public String doTimeStep() {
        bufferLevel = inBufferQueue.size() + "/" + bufferCapacity;
        statusLog.add(new LogEntry(currentTimeStep, status, bufferLevel));
        switch (status) {
            case "FAILURE":
                repair();
                break;
            case "READY":
            case "WAITING":
                takePartFromBuffer();
                processPart();
                break;
            case "BUSY":
                throwDice();
                if (status != failure)
                    processPart();
                timeRemaining--;
                break;
            case "BLOCKED":
                givePartToNextRobotInLine();
                break;
            default:
                break;
        }
        currentTimeStep++;

        return status;
    }

    private int meanRepairTime;

    private void repair() {
        if (currentTimeStep - timeStepOfFailure >= meanRepairTime)
            status = busy;
    }

    private void throwDice() {
        if (new Random().nextDouble() < failureRate * ((currentTimeStep * 1.0) / (24 * 3600 * 1.0)))
            fail();
    }

    private int timeStepOfFailure;
    private int timeRemaining;

    private void fail() {
        status = failure;
        timeStepOfFailure = currentTimeStep;
        // timeRemaining = cycleTimeInSeconds - (currentTimeStep - timeStepStartedOnPart);
    }

    public void setNextRobotInLine(Robot nextRobotInLine) {
        this.nextRobotInLine = nextRobotInLine;
    }

    public int volume = 0;

    private void givePartToNextRobotInLine() {
        if (nextRobotInLine == null) {
            partInProcessing = null;
            status = ready;
            volume++;
            return;
        }
        partInProcessing = nextRobotInLine.receivePartIntoBuffer(partInProcessing);
        if (partInProcessing == null) {
            status = ready;
            volume++;
        } else {
            status = blocked;
        }
    }

    public Part receivePartIntoBuffer(Part newPart) {
        if (inBufferQueue.size() < bufferCapacity) {
            inBufferQueue.add(newPart);
            return null;
        }
        return newPart;
    }

    private void takePartFromBuffer() {
        if (inBufferQueue.size() >= 1) {
            partInProcessing = inBufferQueue.remove();
            bufferLevel = inBufferQueue.size() + "/" + bufferCapacity;
            status = busy;
            timeStepStartedOnPart = currentTimeStep;
            timeRemaining = cycleTimeInSeconds;
        } else
            status = waiting;
    }

    private void processPart() {
        if (partInProcessing == null) return;
        if (timeRemaining <= 0) {
            givePartToNextRobotInLine();
        }// else timeRemaining--;
    }

    public String bufferLevel() {
        return this.bufferLevel;
    }

    public int getBufferCapacity() {
        return bufferCapacity;
    }

    public void displayStatus() {
        System.out.println(robotName + " " + status + " || BufferLevel: " + bufferLevel);
    }

    public List<LogEntry> getStatusLog() {
        return statusLog;
    }

    public String getRobotName() {
        return robotName;
    }

    public int bufferIntLevel() {
        return inBufferQueue.size();
    }
}
