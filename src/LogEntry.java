public class LogEntry {
    private int timeStep;
    private String status;
    private String bufferLevel;

    public LogEntry(int timeStep, String status, String bufferLevel) {
        this.timeStep = timeStep;
        this.status = status;
        this.bufferLevel = bufferLevel;
    }

    public int getTimeStep() {
        return timeStep;
    }

    public String getStatus() {
        return status;
    }

    public String getBufferLevel() {
        return bufferLevel;
    }
}
