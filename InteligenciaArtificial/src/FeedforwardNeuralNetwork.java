import java.security.Signature;
import java.util.Random;

public class FeedforwardNeuralNetwork {

	//	Os atributos da classe serão os seguintes:
	private int inputDim;
	private int hiddenDim;
	private int outputDim;
	private double[][] hiddenWeights;
	private double[] hiddenBiases;
	private double[][] outputWeights;
	private double[] outputBiases;

	//	O construtor FeedforwardNeuralNetwork cria uma nova instância da rede com as dimensões especificadas e inicializa os pesos e os biases com valores aleatórios.

	public FeedforwardNeuralNetwork(int inputDim, int hiddenDim, int outputDim) {
		this.inputDim = inputDim;
		this.hiddenDim = hiddenDim;
		this.outputDim = outputDim;

		hiddenWeights = new double[inputDim][hiddenDim];
		outputWeights = new double[hiddenDim][outputDim];
		hiddenBiases = new double[hiddenDim];
		outputBiases = new double[outputDim];

		initializeParameters();
	}

	//	O método initializeParameters inicializa os pesos e os biases da rede com valores aleatórios. Utilize valores entre 0 e 0.5, para os parâmetros.

	private void initializeParameters() {
		Random random = new Random();

		for(int i = 0; i < inputDim; i++) {
			this.hiddenBiases[i] = random.nextDouble() * 0.5;
		}

		for(int i = 0; i < hiddenDim; i++) {
			for(int j = 0; j < hiddenDim; j++) {
				this.hiddenWeights[i][j] = random.nextDouble() * 0.5;	
			}
		}

		for(int i = 0; i < outputDim; i++) {
			this.outputBiases[i] = random.nextDouble() * 0.5;
		}

		for(int i = 0; i < hiddenDim; i++) {
			for(int j = 0; j < outputDim; j++) {
				this.outputWeights[i][j] = random.nextDouble() * 0.5;	
			}
		}
	}

	//	Um segundo construtor, para inicializar a rede com um conjunto especificado de valores, armazenados num vetor.
	//	Por exemplo, o vetor com os valores da rede neuronal da figura seria o seguinte:
	//	[w1,1; w1,2; w2,1; w2,2; B1; B2; w1,o; w2,o; Bo]

	public FeedforwardNeuralNetwork(int inputDim, int hiddenDim, int outputDim, double[] values) {
		this.inputDim = inputDim;
		this.hiddenDim = hiddenDim;
		this.outputDim = outputDim;

		if(values.length != inputDim * hiddenDim + hiddenDim + hiddenDim * outputDim + outputDim) {
			throw new IllegalArgumentException();
		}

		int contador = 0;

		hiddenWeights = new double[inputDim][hiddenDim];	
		for(int i = 0; i < inputDim; i++) {
			for(int j = 0; j < hiddenDim; j++) {
				this.hiddenWeights[i][j] = values[contador];
				contador++;
			}
		}

		hiddenBiases = new double[hiddenDim];
		for(int i = 0; i < hiddenDim; i++) {
			this.hiddenBiases[i] = values[contador];
			contador++;
		}

		outputWeights = new double[hiddenDim][outputDim];
		for(int i = 0; i < hiddenDim; i++) {
			for(int j = 0; j < outputDim; j++) {
				this.outputWeights[i][j] = values[contador];
				contador++;
			}
		}

		outputBiases = new double[outputDim];
		for(int i = 0; i < outputDim; i++) {
			this.outputBiases[i] = values[contador];
			contador++;
		}
	}

	//	O método forward calcula a saída da rede para um determinado vetor de entrada. Ele usa os pesos e os biases da rede para propagar o sinal de entrada pela rede e calcular a saída. 
	//	Utilize como função de ativação nas camadas escondidadas e nas de output a função sigmoid.

	public double[] forward(double[] inputValues) {
		double[] hiddenOutputs = new double[hiddenDim];
		double[] output = new double[outputDim];

		for(int i = 0; i < hiddenDim; i++) {
			double soma = 0.0;
			for(int j = 0; j < inputDim; j++) {
				soma += (inputValues[j] * hiddenWeights[j][i]);
			}	
			hiddenOutputs[i] = sigmoid(soma + hiddenBiases[i]);
		}

		for(int i = 0; i < outputDim; i++) {
			double soma = 0.0;
			for(int j = 0; j < hiddenDim; j++) {
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

	//	O método getNeuralNetwork() retorna um vetor que representa o conjunto de pesos e biases da rede em um único vetor unidimensional. Como o que é recebido no segundo constrtutor.

	public double[] getNeuralNetwork() {
		int total = inputDim * hiddenDim + hiddenDim + hiddenDim * outputDim + outputDim;
		double[] neuralNetwork = new double[total];

		int contador = 0;

		for(int i = 0; i < inputDim; i++) {
			for(int j = 0; j < hiddenDim; j++) {
				neuralNetwork[contador] = hiddenWeights[i][j];
				contador++;
			}
		}

		for(int i = 0; i < inputDim; i++) {
			neuralNetwork[contador] = hiddenBiases[i];
			contador++;
		}

		for(int i = 0; i < hiddenDim; i++) {
			for(int j = 0; j < outputDim; j++) {
				neuralNetwork[contador] = outputWeights[i][j];
				contador++;
			}
		}

		for(int i = 0; i < outputDim; i++) {
			neuralNetwork[contador] = outputBiases[i];
			contador++;
		}
		return neuralNetwork;
	}

	//	Para efeitos de teste use o seguinte toString():	
	@Override
	public String toString() {
		String result = "Neural Network: \nNumber of inputs: " + inputDim + "\n" + "Weights between input and hidden layer with " + hiddenDim + " neurons: \n";
		String hidden = "";
		for (int input = 0; input < inputDim; input++) {
			for (int i = 0; i < hiddenDim; i++) {
				hidden+= " w"+(input+1) + (i+1) +": " + hiddenWeights[input][i] + "\n";
			}
		}
		result += hidden;
		String biasHidden = "Hidden biases: \n";
		for (int i = 0; i < hiddenDim; i++) {
			biasHidden += " b "+(i+1)+": " + hiddenBiases[i] +"\n";
		}
		result+= biasHidden;
		String output = "Weights between hidden and output layer with "	+ outputDim +" neurons: \n";
		for (int hiddenw = 0; hiddenw < hiddenDim; hiddenw++) {
			for (int i = 0; i < outputDim; i++) {
				output+= " w"+(hiddenw+1) +"o"+(i+1)+": " + outputWeights[hiddenw][i] + "\n";
			}
		}
		result += output;
		String biasOutput = "Ouput biases: \n";
		for (int i = 0; i < outputDim; i++) {
			biasOutput += " bo"+(i+1)+": " + outputBiases[i] + "\n";
		} 
		result+= biasOutput;
		return result;
	}



	public static void main(String[] args) {
		double[] values = { 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.2, 0.1, 0.3, 0.3, 0.5 };
		FeedforwardNeuralNetwork nn_values = new FeedforwardNeuralNetwork(3, 2, 1, values);
		System.out.println(nn_values);
		System.out.println("Forward result:");
		double[] input = { 0.0, 0.0, 1.0 };
		double[] result = nn_values.forward(input);
		for (int i = 0; i < result.length; i++) {
			System.out.println(" Result for neuron ouput" + (i + 1) + "= " + result[i]);
		}
	}



}