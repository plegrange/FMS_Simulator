public class Brain {
    private double productionRate, failureRate;
    private double consumptionRate;
    private int inBufferLevel, outBufferLevel;
    private double starvationProbability;
    private double blockageProbability;

    public Brain() {

    }

    public String think(int inBufferLevel,
                        int nextBufferLevel,
                        double failureRate,
                        double consumptionRate,
                        double blockageProbability,
                        double starvationProbability) {
        this.inBufferLevel = inBufferLevel;
        this.outBufferLevel = nextBufferLevel;
        this.failureRate = failureRate;
        this.consumptionRate = consumptionRate;
        this.blockageProbability = blockageProbability;
        this.starvationProbability = starvationProbability;
        return makeDecision();
    }

    private String makeDecision() {
return"";
    }
}
