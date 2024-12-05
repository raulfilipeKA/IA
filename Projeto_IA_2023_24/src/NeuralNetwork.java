
import utils.GameController;
import java.util.Random;
import utils.Commons;

public class NeuralNetwork implements GameController {
	// Constantes definindo o tamanho da entrada e saÃ­da
	private static final int INPUT_DIM = Commons.BREAKOUT_STATE_SIZE;
	private static final int OUTPUT_DIM = Commons.BREAKOUT_NUM_ACTIONS;

	private int hiddenDim;
	private int chromosomeLength;
	private double[][] hiddenWeights;
	private double[] hiddenBiases;
	private double[][] outputWeights;
	private double[] outputBiases;

	public NeuralNetwork(int hiddenDim) {
		this.hiddenDim = hiddenDim;
		chromosomeLength = (INPUT_DIM * hiddenDim) + hiddenDim + (hiddenDim * OUTPUT_DIM) + OUTPUT_DIM;
		this.hiddenWeights = new double[INPUT_DIM][hiddenDim];
		this.hiddenBiases = new double[hiddenDim];
		this.outputWeights = new double[hiddenDim][OUTPUT_DIM];
		this.outputBiases = new double[OUTPUT_DIM];

		initializeParameters();
	}

	private void initializeParameters() {
		Random random = new Random();

		// Inicializar pesos e biases da camada oculta
		for (int i = 0; i < INPUT_DIM; i++) {
			for (int j = 0; j < hiddenDim; j++) {
				hiddenWeights[i][j] = (random.nextDouble() - 0.5) * 2; // Valores entre -1 e 1
			}
		}

		for (int i = 0; i < hiddenDim; i++) {
			hiddenBiases[i] = (random.nextDouble() - 0.5) * 2;
		}

		// Inicializar pesos e biases da camada de saÃ­da
		for (int i = 0; i < hiddenDim; i++) {
			for (int j = 0; j < OUTPUT_DIM; j++) {
				outputWeights[i][j] = (random.nextDouble() - 0.5) * 2;
			}
		}

		for (int i = 0; i < OUTPUT_DIM; i++) {
			outputBiases[i] = (random.nextDouble() - 0.5) * 2;
		}
	}

	@Override
	public int nextMove(int[] currentState) {
		double[] hiddenLayerOutputs = new double[hiddenDim];
		double[] outputLayerOutputs = new double[OUTPUT_DIM];

		// PropagaÃ§Ã£o da entrada atÃ© a camada oculta
		for (int i = 0; i < hiddenDim; i++) {
			hiddenLayerOutputs[i] = 0;
			for (int j = 0; j < INPUT_DIM; j++) {
				hiddenLayerOutputs[i] += currentState[j] * hiddenWeights[j][i];
			}
			hiddenLayerOutputs[i] += hiddenBiases[i];
			hiddenLayerOutputs[i] = sigmoid(hiddenLayerOutputs[i]);
		}

		// PropagaÃ§Ã£o da camada oculta atÃ© a saÃ­da
		for (int i = 0; i < OUTPUT_DIM; i++) {
			outputLayerOutputs[i] = 0;
			for (int j = 0; j < hiddenDim; j++) {
				outputLayerOutputs[i] += hiddenLayerOutputs[j] * outputWeights[j][i];
			}
			outputLayerOutputs[i] += outputBiases[i];
			outputLayerOutputs[i] = sigmoid(outputLayerOutputs[i]);
		}

		// DecisÃ£o de qual aÃ§Ã£o tomar baseada na saÃ­da da rede
		if (outputLayerOutputs[0] > outputLayerOutputs[1]) {
			return 1;
		}
		return 2;
	}

	private double sigmoid(double x) {
		return 1.0 / (1.0 + Math.exp(-x));
	}

	public void updateParameters(double[] newWeights) {
		int index = 0;

		// Atualizar pesos da camada oculta
		for (int i = 0; i < INPUT_DIM; i++) {
			for (int j = 0; j < hiddenDim; j++) {
				hiddenWeights[i][j] = newWeights[index++];
			}
		}

		// Atualizar biases da camada oculta
		for (int i = 0; i < hiddenDim; i++) {
			hiddenBiases[i] = newWeights[index++];
		}

		// Atualizar pesos da camada de saÃ­da
		for (int i = 0; i < hiddenDim; i++) {
			for (int j = 0; j < OUTPUT_DIM; j++) {
				outputWeights[i][j] = newWeights[index++];
			}
		}

		// Atualizar biases da camada de saÃ­da
		for (int i = 0; i < OUTPUT_DIM; i++) {
			outputBiases[i] = newWeights[index++];
		}
	}

	public int getChromoLen() {
		return chromosomeLength;
	}

	public int getHiddenDim() {
		return hiddenDim;
	}

	public double[] getParameters() {

		double[] params = new double[(INPUT_DIM * hiddenDim) + hiddenDim + (hiddenDim * OUTPUT_DIM) + OUTPUT_DIM];
		int index = 0;

		// Extrair pesos da camada oculta
		for (int i = 0; i < INPUT_DIM; i++) {
			for (int j = 0; j < hiddenDim; j++) {
				params[index++] = hiddenWeights[i][j];
			}
		}

		// Extrair biases da camada oculta
		for (int i = 0; i < hiddenDim; i++) {
			params[index++] = hiddenBiases[i];
		}

		// Extrair pesos da camada de saÃ­da
		for (int i = 0; i < hiddenDim; i++) {
			for (int j = 0; j < OUTPUT_DIM; j++) {
				params[index++] = outputWeights[i][j];
			}
		}

		// Extrair biases da camada de saÃ­da
		for (int i = 0; i < OUTPUT_DIM; i++) {
			params[index++] = outputBiases[i];
		}

		return params;
	}

}