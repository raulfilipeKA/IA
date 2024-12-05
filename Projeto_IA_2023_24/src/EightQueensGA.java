import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class EightQueensGA {
	private static final int POPULATION_SIZE = 100;
	private static final int NUM_GENERATIONS = 1000;
	private static final double MUTATION_RATE = 0.05;

	private static final int BOARD_SIZE = 8;
	private static Random random = new Random();

	public EightQueensGA() {
		Queen[] population = new Queen[POPULATION_SIZE];

		// Inicializa a população
		for (int i = 0; i < POPULATION_SIZE; i++) {
			population[i] = new Queen(true);
		}

		// Processo de evolução
		for (int generation = 0; generation < NUM_GENERATIONS; generation++) {
			Arrays.sort(population);

			System.out.println("Generation " + generation + ": " + population[0]);

			// Verifica se uma solução ideal foi encontrada
			if (population[0].getFitness() == 0) {
				break;
			}

			Queen[] newPopulation = new Queen[POPULATION_SIZE];
			for (int i = 0; i < POPULATION_SIZE; i++) {
				Queen parent1 = selectParent(population);
				Queen parent2 = selectParent(population);
				Queen child = crossover(parent1, parent2);
				mutate(child);
				newPopulation[i] = child;
			}

			population = newPopulation;
		}

		// Exibe a melhor solução encontrada
		Arrays.sort(population);
		System.out.println("Melhor solução encontrada: \n" + population[0]);
	}

	private Queen selectParent(Queen[] population) {
		Queen[] tournament = new Queen[10];
		for (int i = 0; i < tournament.length; i++) {
			tournament[i] = population[random.nextInt(POPULATION_SIZE)];
		}
		Arrays.sort(tournament);
		return tournament[0];
	}

	private Queen crossover(Queen parent1, Queen parent2) {
		Queen child = new Queen(false); // Não inicializar com posições aleatórias
		for (int i = 0; i < BOARD_SIZE; i++) {
			child.setRow(i, random.nextBoolean() ? parent1.getRows()[i] : parent2.getRows()[i]);
		}
		return child;
	}

	private void mutate(Queen queen) {
		if (random.nextDouble() < MUTATION_RATE) {
			int row = random.nextInt(BOARD_SIZE);
			int col = random.nextInt(BOARD_SIZE);
			queen.setRow(row, col); // Introduz uma nova posição
		}
	}
}
