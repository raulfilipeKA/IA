package breakout;

public class GameSimulator {
	public static void main(String[] args) {
		// Parâmetros do jogo e da rede neural
		int seed = 13; // Exemplo de semente
		boolean withGui = true; // Rodar sem interface gráfica para acelerar a simulação
		int hiddenDim = 10; // Dimensão da camada oculta da rede neural

		// Criação da rede neural e configuração inicial
		FeedforwardNeuralNetwork neuralNetwork = new FeedforwardNeuralNetwork(hiddenDim);

		// Criação do BreakoutBoard com um GameController
		BreakoutBoard breakoutBoard = new BreakoutBoard(neuralNetwork, withGui, seed);

		// Simulação do jogo
		breakoutBoard.runSimulation();

		// Aqui você pode acessar a fitness após a simulação, se necessário
		System.out.println("Fitness Score: " + breakoutBoard.getFitness());
	}
}
