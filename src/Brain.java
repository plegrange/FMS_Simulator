import java.util.List;

public class Brain {
    private double productionRate, failureRate;
    private double consumptionRate;
    private int inBufferLevel, outBufferLevel;
    private double starvationProbability;
    private double blockageProbability;
    private List<double[][]> weightMatrices; //[neuron][input]
    private int numberOfHiddenNeurons;

    public Brain(List<double[][]> weightMatrices) {
        this.weightMatrices = weightMatrices;
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
        double[] inputs = new double[6];
        double[] outputs = inputs;
        for (int i = 0; i < weightMatrices.size(); i++) {
            outputs = inputToLayer(weightMatrices.get(i), outputs);
        }
        return makeDecision(outputs);
    }

    private String[] decisionOptions = new String[]{"CONTINUE", "CHILL", "BLAZE"};

    private String makeDecision(double[] outputs) {
        int maxIndex = -1;
        double maxValue = 0;
        for (int i = 0; i < outputs.length; i++) {
            if (outputs[i] > maxValue) {
                maxValue = outputs[i];
                maxIndex = i;
            }
        }
        return decisionOptions[maxIndex];
    }

    private double[] inputToLayer(double[][] weightMatrix, double[] inputs) {
        double[] layerOutput = new double[weightMatrix.length];
        for (int n = 0; n < weightMatrix.length; n++) {
            layerOutput[n] = 0;
            for (int i = 0; i < inputs.length; i++) {
                layerOutput[n] += weightMatrix[n][i] * inputs[i];
            }
        }
        return layerOutput;
    }
}
