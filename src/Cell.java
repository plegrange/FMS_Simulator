import java.util.*;

public class Cell {
    private List<LogEntry> statusLog;
    private String cellName;
    private String status;
    private String bufferLevel;
    private int bufferCapacity;
    private Queue<Part> inBufferQueue;
    private Part partInProcessing;
    protected String busy = "BUSY", ready = "READY", blocked = "BLOCKED", waiting = "WAITING", failure = "FAILURE", chill = "CHILL", blaze = "BLAZE";
    // private SimpleStringProperty status = new SimpleStringProperty();
    //private SimpleStringProperty bufferLevel = new SimpleStringProperty();
    private int currentTimeStep;
    private int timeStepStartedOnPart;
    private int cycleTimeInSeconds;
    private Cell nextCellInLine;
    private double failureRate;
    private int downTime = 0;
    private Brain brain = new Brain();

    public Cell(String cellName, int bufferCapacity, int cycleTimeInSeconds, double failureRate, int meanRepairTime) {
        statusLog = new ArrayList<>();
        this.cellName = cellName;
        this.failureRate = failureRate;
        this.meanRepairTime = meanRepairTime;
        inBufferQueue = new LinkedList<>();
        this.cycleTimeInSeconds = cycleTimeInSeconds;
        this.partInProcessing = null;
        this.bufferCapacity = bufferCapacity;
        status = ready;
        currentTimeStep = 0;
    }

    public boolean queueEmpty() {
        if (inBufferQueue.size() == 0)
            return true;
        return false;
    }

    private double consumptionRate, bloackageProbability, waitingProbability;

    private void think() {
        String decision = brain.think(bufferIntLevel(), nextCellInLine.bufferIntLevel(), failureRate, consumptionRate, bloackageProbability, waitingProbability);
        switch (decision) {
            case "CONTINUE":
                break;
            case "CHILL":
                status = chill;
                break;
            case "BLAZE":
                status = blaze;
                break;
            default:
                break;
        }
    }

    int bufferTarget = bufferCapacity / 2;

    public String doTimeStep() {
        bufferLevel = inBufferQueue.size() + "/" + bufferCapacity;
        //statusLog.add(new LogEntry(currentTimeStep, status, bufferLevel));
        think();
        switch (status) {
            case "CHILL":
                bufferTarget = 0;
                break;
            case "BLAZE":
                bufferTarget = nextCellInLine.bufferCapacity;
                break;
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
        else downTime++;
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

    public void setNextCellInLine(Cell nextCellInLine) {
        this.nextCellInLine = nextCellInLine;
    }

    public int volume = 0;

    private void givePartToNextRobotInLine() {
        if (nextCellInLine == null) {
            partInProcessing = null;
            status = ready;
            volume++;
            return;
        }
        partInProcessing = nextCellInLine.receivePartIntoBuffer(partInProcessing);
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
        if (inBufferQueue.size() >= 1 && nextCellInLine.bufferIntLevel() < nextCellInLine.bufferTarget) {
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
        System.out.println(cellName + " " + status + " || BufferLevel: " + bufferLevel);
    }

    public List<LogEntry> getStatusLog() {
        return statusLog;
    }

    public String getCellName() {
        return cellName;
    }

    public int bufferIntLevel() {
        return inBufferQueue.size();
    }

    public int getDownTime() {
        return downTime;
    }
}
