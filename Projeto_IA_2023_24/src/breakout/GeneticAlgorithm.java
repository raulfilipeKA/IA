package breakout;

import java.util.Random;

public class GeneticAlgorithm {
	private double[][] population;
	private double[] fitnessScores;
	private int populationSize = 100;
	private int chromosomeLength;
	private double mutationRate;// = 0.05;
	private Random random;
	private int bestIndividualIndex; // Índice do melhor indivíduo na população

	public GeneticAlgorithm(int populationSize, int chromosomeLength, double mutationRate) {
		this.populationSize = populationSize;
		this.chromosomeLength = chromosomeLength;
		this.mutationRate = mutationRate;
		this.population = new double[populationSize][chromosomeLength];
		this.fitnessScores = new double[populationSize];
		this.random = new Random();
		this.bestIndividualIndex = 0; // Inicialmente, o melhor indivíduo é o primeiro da população
		initializePopulation();
	}

	private void initializePopulation() {
		for (int i = 0; i < populationSize; i++) {
			for (int j = 0; j < chromosomeLength; j++) {
				population[i][j] = random.nextDouble();
			}
		}
	}

	public void evaluateFitness(FeedforwardNeuralNetwork nn, int seed) {
		for (int i = 0; i < populationSize; i++) {

			nn.updateParameters(population[i]); // Atualiza a rede neural com os parâmetros da população

			// Cria um novo BreakoutBoard passando a rede neural como controller
			BreakoutBoard board = new BreakoutBoard(nn, false, seed);
			board.setSeed(seed);
			board.runSimulation(); // Roda a simulação do jogo

			fitnessScores[i] = board.getFitness(); // Obtém o fitness após a simulação
		}
	}

	public double[] getBestParameters() {
		double bestFitness = -Double.MAX_VALUE;
		int bestIndex = 0;

		for (int i = 0; i < populationSize; i++) {
			if (fitnessScores[i] > bestFitness) {
				bestFitness = fitnessScores[i];
				bestIndex = i;
			}
		}

		return population[bestIndex];
	}

	public void evolve() {
		double[][] newPopulation = new double[populationSize][chromosomeLength];

		for (int i = 0; i < populationSize; i++) {
			int parent1 = selectParent();
			int parent2 = selectParent();
			double[] child = crossover(population[parent1], population[parent2]);
			child = mutate(child);
			newPopulation[i] = child;
		}

		population = newPopulation;
	}

	private int selectParent() {
		int tournamentSize = 5;
		int bestIndex = random.nextInt(populationSize);
		double bestFitness = fitnessScores[bestIndex];

		for (int i = 1; i < tournamentSize; i++) {
			int idx = random.nextInt(populationSize);
			if (fitnessScores[idx] > bestFitness) {
				bestIndex = idx;
				bestFitness = fitnessScores[idx];
			}
		}
		return bestIndex;
	}

	private double[] crossover(double[] parent1, double[] parent2) {
		double[] child = new double[chromosomeLength];
		int crossoverPoint = random.nextInt(chromosomeLength);
		for (int i = 0; i < crossoverPoint; i++) {
			child[i] = parent1[i];
		}
		for (int i = crossoverPoint; i < chromosomeLength; i++) {
			child[i] = parent2[i];
		}
		return child;
	}

	private double[] mutate(double[] chromosome) {
		for (int i = 0; i < chromosomeLength; i++) {
			if (random.nextDouble() < mutationRate) {
				chromosome[i] += random.nextDouble() * 0.1;
			}
		}
		return chromosome;
	}

	public double getBestFitnessPop() {
		double bestFitness = 0;

		for (int i = 0; i < populationSize; i++) {
			if (fitnessScores[i] > bestFitness) {
				bestFitness = fitnessScores[i];
			}
		}

		return bestFitness;
	}

	public static void main(String[] args) {
		// Parâmetros para o algoritmo genético
		int populationSize = 50;
		double mutationRate = 0.05;
		int hiddenDim = 50;
		int seed = 123; // Seed para reprodutibilidade dos resultados

		// Criação da rede neural que será otimizada pelo algoritmo genético
		FeedforwardNeuralNetwork neuralNetwork = new FeedforwardNeuralNetwork(hiddenDim);

		// Criação do algoritmo genético com os parâmetros definidos
		GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(populationSize, neuralNetwork.getChromoLen(),
				mutationRate);

		// Avaliação inicial da população (primeira geração)
		geneticAlgorithm.evaluateFitness(neuralNetwork, seed);

		// Execução das gerações seguintes e evolução do algoritmo genético
		int numGenerations = 50;
		for (int generation = 1; generation <= numGenerations; generation++) {
			// Evolução da população (seleção, crossover, mutação)
			geneticAlgorithm.evolve();

			// Avaliação da nova população com a rede neural atualizada
			geneticAlgorithm.evaluateFitness(neuralNetwork, seed);

			// Obtenção do melhor fitness e parâmetros da melhor solução encontrada
			double bestFitness = geneticAlgorithm.getBestFitnessPop();
			double[] bestParameters = geneticAlgorithm.getBestParameters();

			// Exibição dos resultados da geração atual
			System.out.println("Geração " + generation + ": Melhor Fitness = " + bestFitness);

			// Atualização dos parâmetros da rede neural com os melhores parâmetros
			// encontrados
			neuralNetwork.updateParameters(bestParameters);
		}

		// Após as iterações, exibir o melhor fitness final e os parâmetros finais da
		// rede neural
		double finalBestFitness = geneticAlgorithm.getBestFitnessPop();
		double[] finalBestParameters = geneticAlgorithm.getBestParameters();

		System.out.println("Melhor Fitness Final: " + finalBestFitness);
		System.out.println("Parâmetros Finais da Rede Neural: " + java.util.Arrays.toString(finalBestParameters));
	}
}
