
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
//import utils.Commons;

public class Trainer {
	private GeneticAlgorithm geneticAlgorithm;
	private NeuralNetwork neuralNetwork;
	private double bestOverallFitness;
	private NeuralNetwork bestGen;
	private int hiddenDim;
	private int seed;

	public Trainer(int populationSize, double mutationRate, int hiddenDim, int seed) {
//		int inputDim = Commons.BREAKOUT_STATE_SIZE;
//		int outputDim = Commons.BREAKOUT_NUM_ACTIONS;
		this.seed = seed;
		this.hiddenDim = hiddenDim;
		neuralNetwork = new NeuralNetwork(hiddenDim);
		int chromosomeLength = neuralNetwork.getChromoLen();
		geneticAlgorithm = new GeneticAlgorithm(populationSize, chromosomeLength, mutationRate);
		bestGen = new NeuralNetwork(hiddenDim);
	}

	public void train(int numGenerations) {
		for (int generation = 0; generation < numGenerations; generation++) {
			System.out.println("Generation: " + generation);

			// Avaliar o fitness da populaÃ§Ã£o atual
			geneticAlgorithm.evaluateFitness(neuralNetwork, seed);

			// Obter o melhor fitness da geraÃ§Ã£o atual
			double bestFitness = geneticAlgorithm.getBestFitness();
			System.out.println("Best Fitness: " + bestFitness);

			bestOverallFitness = calculateBestOverallFitness(bestFitness);

			// Obter os melhores parÃ¢metros da populaÃ§Ã£o atual
			double[] bestParams = geneticAlgorithm.getBestParameters();

			// Atualizar a rede neural com os melhores parÃ¢metros
			neuralNetwork.updateParameters(bestParams);

			// Evoluir para a prÃ³xima geraÃ§Ã£o da populaÃ§Ã£o
			geneticAlgorithm.evolve();
			// Verificar se o fitness atual é melhor que o melhor fitness geral
			if (geneticAlgorithm.getBestFitness() > bestOverallFitness) {
				// Obter os parâmetros da rede neural atual com melhor fitness
				double[] bestNetworkParameters = neuralNetwork.getParameters();
				// Atualizar os parâmetros da melhor rede neural (bestGen) com os novos
				// parâmetros
				bestGen.updateParameters(bestNetworkParameters);

				bestOverallFitness = geneticAlgorithm.getBestFitness(); // Atualizar o melhor fitness geral
			}
		}
		System.out.println("Best Overall Fitness: " + bestOverallFitness);
	}

	public NeuralNetwork getTrainedNeuralNetwork() {
		return neuralNetwork;
	}

	public NeuralNetwork getBestGen() {
		return bestGen;
	}

	// Método para clonar uma rede neural
	private NeuralNetwork cloneNeuralNetwork(NeuralNetwork originalNetwork) {
		NeuralNetwork clonedNetwork = new NeuralNetwork(originalNetwork.getHiddenDim());
		double[] originalParams = originalNetwork.getParameters();
		clonedNetwork.updateParameters(originalParams);
		return clonedNetwork;
	}

	// Método para calcular o melhor fitness geral
	public double calculateBestOverallFitness(double currentBestFitness) {
		// Atualizar o melhor fitness geral se o atual for maior
		if (currentBestFitness > bestOverallFitness) {
			bestOverallFitness = currentBestFitness;
		}

		return bestOverallFitness;
	}

	private static void escreverMelhorFitnessEmArquivo(double bestFitness) {
		try (FileWriter fw = new FileWriter("melhor_fitness.txt");
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
			out.println("Melhor valor de fitness: " + bestFitness);
		} catch (IOException e) {
			System.err.println("Erro ao escrever o melhor fitness no arquivo.");
			e.printStackTrace();
		}
	}

	NeuralNetwork getNN() {
		return neuralNetwork;
	}

	public static void main(String[] args) {
		int populationSize = 100;
		double mutationRate = 0.05;
		int hiddenDim = 50;
		int seed = 3;
		int numGenerations = 500;
		Trainer trainer = new Trainer(populationSize, mutationRate, hiddenDim, seed);
		BreakoutBoard bb = new BreakoutBoard(trainer.getNN(), false, seed);
		bb.setSeed(seed);
		bb.runSimulation();
		trainer.train(numGenerations);

		// Após o treino, obter a melhor rede neural encontrada, clonando-a
		NeuralNetwork bestGen = trainer.cloneNeuralNetwork(trainer.getTrainedNeuralNetwork());
		double bestOverallFitness = trainer.bestOverallFitness;

		// Escrever o melhor fitness em um arquivo
		escreverMelhorFitnessEmArquivo(bestOverallFitness);

		// Criar um novo jogo Breakout com a melhor rede neural e outra rede neural para
		// comparação
		NeuralNetwork feed = new NeuralNetwork(30);
		Breakout a = new Breakout(bestGen, seed);
		Breakout b = new Breakout(feed, seed);
	}

}