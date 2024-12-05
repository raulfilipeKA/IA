package breakout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import utils.Commons;

public class GeneticAlgorithm {
	private List<double[]> population;

	private static final int NUM_POPULATION = 100;
	private static final double MUTATION_PROBABILITY = 0.5; // ALTERAR CONFORME NECESSARIO
	private static final int NUM_GENERATIONS = 500;
	private static int SIZE = 0;
	static int seed = 6; // seed fixa neste caso, pode alterar para outro int (random ou nao)
	private int hiddenDim;

	public GeneticAlgorithm(int hiddenDim) {
		this.hiddenDim = hiddenDim;
		SIZE = (hiddenDim * 7 + 2 * hiddenDim + hiddenDim + 2);
		population = new ArrayList<double[]>(SIZE);
		createPopulation();
	}

	public void createPopulation() {
		for (int i = 0; i < NUM_POPULATION; i++) {
			population.add(createIndividual(SIZE));
		}
	}

	int getSeed() {
		return seed;
	}

	private double[] createIndividual(int individuoSize) {
		double[] individuo = new double[individuoSize];
		for (int i = 0; i < individuoSize; i++) {
			individuo[i] = Math.random() * 2 - 1;
		}
		return individuo;
	}

	public double calculateFitness(double[] individual) {
		FeedforwardNeuralNetwork nn = new FeedforwardNeuralNetwork(hiddenDim, individual);
		BreakoutBoard b = new BreakoutBoard(nn, false, seed);
		b.setSeed(seed);
		b.runSimulation();
		double fitness = b.getFitness();
		return fitness;
	}

	public double[] torneio(List<double[]> population) {
		int index1 = (int) (Math.random() * population.size());
		int index2 = (int) (Math.random() * population.size());

		double[] individual1 = population.get(index1);
		double[] individual2 = population.get(index2);

		double fitnessIndividual1 = calculateFitness(individual1);
		double fitnessIndividual2 = calculateFitness(individual2);

		if (fitnessIndividual1 > fitnessIndividual2) {
			return individual1;
		} else {
			return individual2;
		}

	}

	private double[] crossover(double[] parent1, double[] parent2) {
		int point = (int) (Math.random() * parent1.length);
		double[] child = new double[parent1.length];

		for (int i = 0; i < point; i++) {
			child[i] = parent1[i];
		}

		for (int i = point; i < parent1.length; i++) {
			child[i] = parent2[i];
		}

		return child;
	}

	private double[] mutate(double[] individual) {
		int ponto = (int) (Math.random() * individual.length);
		double value = Math.random() * 2 - 1;
		individual[ponto] = value;
		return individual;
	}

	public double[] getBestIndividual() {
		double bestFitness = 0;
		double[] bestIndividual = null;

		for (int i = 0; i < population.size(); i++) {
			double[] individual = population.get(i);
			double fitness = calculateFitness(individual);
			if (fitness > bestFitness) {
				bestFitness = fitness;
				bestIndividual = individual;
			}
		}
		return bestIndividual;
	}

	public double getBestFitness() {
		double bestFitness = 0;

		for (int i = 0; i < population.size(); i++) {
			double fitness = calculateFitness(population.get(i));
			if (fitness > bestFitness) {
				bestFitness = fitness;
			}
		}
		return bestFitness;
	}

	public double[] startSearch() {
		for (int i = 0; i < NUM_GENERATIONS; i++) {
			List<double[]> newPopulation = new ArrayList<>(NUM_POPULATION);
			for (int j = 0; j < NUM_POPULATION; j++) {
				double[] parente1 = torneio(population);
				double[] parente2 = torneio(population);
				double[] filho = crossover(parente1, parente2);
				if (Math.random() < MUTATION_PROBABILITY) {
					filho = mutate(filho);
				}
				newPopulation.add(filho);
			}
			population.clear();
			population.addAll(newPopulation);
//			if (calculateFitness(getBestIndividual()) > 200000) { // foi utilizado apenas para mostrar que ele aprendeu
//				break;
//			}
			System.out.println(i);
			System.out.println(getBestFitness());
			// System.out.println(calculateFitness(getBestIndividual()));

		}
		return getBestIndividual();
	}
}