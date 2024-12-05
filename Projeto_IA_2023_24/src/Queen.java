import java.util.Random;

public class Queen implements Comparable<Queen> {
	private static final int BOARD_SIZE = 8;
	private int[] rows;
	private int fitness;
	private static Random random = new Random();

	public Queen(boolean initialize) {
		this.rows = new int[BOARD_SIZE];
		if (initialize) {
			for (int i = 0; i < BOARD_SIZE; i++) {
				setRow(i, random.nextInt(BOARD_SIZE));
			}
		}
		calculateFitness();
	}

	public int[] getRows() {
		return rows;
	}

	public void setRow(int row, int col) {
		rows[row] = col;
		calculateFitness();
	}

	public int getFitness() {
		return fitness;
	}

	private void calculateFitness() {
		fitness = 0; // Começamos com o fitness máximo
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = i + 1; j < BOARD_SIZE; j++) {
				if (rows[i] == rows[j] || Math.abs(rows[i] - rows[j]) == Math.abs(i - j)) {
					fitness++; // Diminuímos o fitness se as rainhas se atacam
				}
			}
		}
	}

	@Override
	public int compareTo(Queen other) {
		return Integer.compare(this.fitness, other.fitness);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				if (rows[i] == j) {
					sb.append("Q ");
				} else {
					sb.append(". ");
				}
			}
			sb.append("\n");
		}
		sb.append("Fitness: ").append(fitness);
		return sb.toString();
	}
}
