package breakout;

import java.util.List;
import java.util.Random;

import utils.Commons;
import utils.GameController;

public class FeedforwardNeuralNetwork implements GameController {

//		Os atributos da classe serão os seguintes:
	private final int outputDim = Commons.BREAKOUT_NUM_ACTIONS;
	private final int inputDim = Commons.BREAKOUT_STATE_SIZE;
	private int INDIVIDUO_SIZE;

	public int hiddenDim;
	private double[][] hiddenWeights;
	private double[] hiddenBiases;
	private double[][] outputWeights;
	private double[] outputBiases;

//		Um segundo construtor, para inicializar a rede com um conjunto especificado de valores, armazenados num vetor.
//		Por exemplo, o vetor com os valores da rede neuronal da figura seria o seguinte:
//		[w1,1; w1,2; w2,1; w2,2; B1; B2; w1,o; w2,o; Bo]

	public FeedforwardNeuralNetwork(int hiddenDim, double[] values) {
		this.hiddenDim = hiddenDim;
		int contador = 0;

		hiddenWeights = new double[inputDim][hiddenDim];
		for (int i = 0; i < inputDim; i++) {
			for (int j = 0; j < hiddenDim; j++) {
				this.hiddenWeights[i][j] = values[contador];
				contador++;
			}
		}

		hiddenBiases = new double[hiddenDim];
		for (int i = 0; i < hiddenDim; i++) {
			this.hiddenBiases[i] = values[contador];
			contador++;
		}

		outputWeights = new double[hiddenDim][outputDim];
		for (int i = 0; i < hiddenDim; i++) {
			for (int j = 0; j < outputDim; j++) {
				this.outputWeights[i][j] = values[contador];
				contador++;
			}
		}
		outputBiases = new double[outputDim];
		for (int i = 0; i < outputDim; i++) {
			this.outputBiases[i] = values[contador];
			contador++;
		}
		INDIVIDUO_SIZE = (hiddenDim * inputDim + outputDim * hiddenDim + hiddenDim + outputDim);
	}

//		O método forward calcula a saída da rede para um determinado vetor de entrada. Ele usa os pesos e os biases da rede para propagar o sinal de entrada pela rede e calcular a saída. 
//		Utilize como função de ativação nas camadas escondidadas e nas de output a função sigmoid.

	public double[] forward(int[] inputValues) {
		double[] hiddenOutputs = new double[hiddenDim];
		double[] output = new double[outputDim];

		for (int i = 0; i < hiddenDim; i++) {
			double soma = 0.0;
			for (int j = 0; j < inputDim; j++) {
				soma += (inputValues[j] * hiddenWeights[j][i]);
			}
			hiddenOutputs[i] = sigmoid(soma + hiddenBiases[i]);
		}

		for (int i = 0; i < outputDim; i++) {
			double soma = 0.0;
			for (int j = 0; j < hiddenDim; j++) {
				soma += (hiddenOutputs[j] * outputWeights[j][i]);
			}
			output[i] = sigmoid(soma + outputBiases[i]);
		}
		return output;
	}

	private double sigmoid(double d) {
		double funcaoSigmoid = (1 / (1 + Math.exp(-d)));
		return funcaoSigmoid;
	}

//		O método getNeuralNetwork() retorna um vetor que representa o conjunto de pesos e biases da rede em um único vetor unidimensional. Como o que é recebido no segundo constrtutor.

	public double[] getNeuralNetwork() {
		int total = inputDim * hiddenDim + hiddenDim + hiddenDim * outputDim + outputDim;
		double[] neuralNetwork = new double[total];

		int contador = 0;

		for (int i = 0; i < inputDim; i++) {
			for (int j = 0; j < hiddenDim; j++) {
				neuralNetwork[contador] = hiddenWeights[i][j];
				contador++;
			}
		}

		for (int i = 0; i < inputDim; i++) {
			neuralNetwork[contador] = hiddenBiases[i];
			contador++;
		}

		for (int i = 0; i < hiddenDim; i++) {
			for (int j = 0; j < outputDim; j++) {
				neuralNetwork[contador] = outputWeights[i][j];
				contador++;
			}
		}

		for (int i = 0; i < outputDim; i++) {
			neuralNetwork[contador] = outputBiases[i];
			contador++;
		}
		return neuralNetwork;
	}

	int getIndividuoSize() {
		return INDIVIDUO_SIZE;
	}

	@Override
	public int nextMove(int[] currentState) {
		double[] output = forward(currentState);
		if (output[0] > output[1]) {
			return BreakoutBoard.LEFT;
		}
		return BreakoutBoard.RIGHT;
	}

}