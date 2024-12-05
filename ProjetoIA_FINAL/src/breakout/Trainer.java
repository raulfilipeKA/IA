package breakout;

import java.util.Scanner;

public class Trainer {

	public static void main(String[] args) {
		int hiddenDim = 20; // Dimensão da camada oculta da rede neural

		// Cria uma população inicial com indivíduos aleatórios
		GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(hiddenDim);
		double[] individuo = geneticAlgorithm.startSearch();
		FeedforwardNeuralNetwork trainedNetwork = new FeedforwardNeuralNetwork(hiddenDim, individuo);
		Scanner scanner = new Scanner(System.in);

		System.out.println("Press ENTER to start the game...");
		scanner.nextLine();

		Breakout breakoutGame = new Breakout(trainedNetwork, geneticAlgorithm.getSeed());

		scanner.close();

	}
}