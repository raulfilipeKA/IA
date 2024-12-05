
import java.util.Random;

public class GeneticAlgorithm {
	private double[][] population;
	private double[] fitnessScores;
	private int populationSize = 100;
	private int chromosomeLength;
	private double mutationRate;// = 0.05;
	private double crossoverRate;// = 0.2;
	private Random random;
	private int bestIndividualIndex; // Ãndice do melhor indivÃ­duo na populaÃ§Ã£o

	public GeneticAlgorithm(int populationSize, int chromosomeLength, double mutationRate) {
		this.populationSize = populationSize;
		this.chromosomeLength = chromosomeLength;
		this.mutationRate = mutationRate;
		this.crossoverRate = crossoverRate;
		this.population = new double[populationSize][chromosomeLength];
		this.fitnessScores = new double[populationSize];
		this.random = new Random();
		this.bestIndividualIndex = 0; // Inicialmente, o melhor indivÃ­duo Ã© o primeiro da populaÃ§Ã£o
		initializePopulation();
	}

	private void initializePopulation() {
		for (int i = 0; i < populationSize; i++) {
			for (int j = 0; j < chromosomeLength; j++) {
				population[i][j] = random.nextDouble();
			}
		}
	}

	public void evaluateFitness(NeuralNetwork neuralNetwork, int seed) {
		for (int i = 0; i < populationSize; i++) {
			// Cria uma nova instÃ¢ncia da rede neural com a dimensÃ£o oculta definida
			NeuralNetwork nn = new NeuralNetwork(20); // Supondo dimensÃ£o oculta de 20
			nn.updateParameters(population[i]); // Atualiza a rede neural com os parÃ¢metros da populaÃ§Ã£o

			// Cria um novo BreakoutBoard passando a rede neural como controller
			BreakoutBoard board = new BreakoutBoard(nn, false, seed); // false e uma semente
			board.setSeed(seed);
			board.runSimulation(); // aleatÃ³ria

			board.runSimulation(); // Roda a simulaÃ§Ã£o do jogo

			fitnessScores[i] = board.getFitness(); // ObtÃ©m o fitness apÃ³s a simulaÃ§Ã£o
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
				chromosome[i] += random.nextGaussian() * 0.1; // Small mutation based on Gaussian distribution
			}
		}
		return chromosome;
	}

	public double getBestFitness() {
		double bestFitness = -Double.MAX_VALUE;

		for (int i = 0; i < populationSize; i++) {
			if (fitnessScores[i] > bestFitness) {
				bestFitness = fitnessScores[i];
			}
		}

		return bestFitness;
	}
}