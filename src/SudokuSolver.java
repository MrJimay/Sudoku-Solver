import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class SudokuSolver {
	private SudokuGrid sudokuGrid;
	
	public static void main(String[] args) {
		SudokuSolver solver = new SudokuSolver();
		Scanner sc = null;
		
		try {
			// read input file
			sc = new Scanner(new FileReader(args[0]));
			// process the input until end of file	
			solver.createGrid(sc);
			solver.solve();
			solver.printSolution();
		}
		catch (FileNotFoundException e) {
			System.out.println("Input must be a single line of 81 numbers, except separated by spaces.");
			System.out.println("Empty boxes should be represented by a 0.");
		}
		finally {
			// close scanner when done
		    if (sc != null) sc.close();
		}
	}

	public void printSolution() {
		sudokuGrid.print();
	}

	public void createGrid(Scanner sc) {
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		while (sc.hasNextInt()) {
			numbers.add(sc.nextInt());
		}
		this.sudokuGrid = new SudokuGrid(numbers);
	}

	public void solve() {
		int row = 0;
		int col = 0;
		while (!sudokuGrid.solved()) {
			// only set number if not already set
			if (sudokuGrid.getNumber(row, col) == 0) {
				setOnlyPossibleNumber(row, col);
			}
			if (sudokuGrid.getNumber(row, col) == 0) {
				setOnlyPossiblePosition(row, col);
			}
			if (col == 8) {
				row = (row+1)%9;
			}
			col = (col+1)%9;
		}
	}
	
	// Fills the cell at the given row and column if that is the only possible position for a number
	private boolean setOnlyPossiblePosition(int row, int col) {
		int num = 1;
		while (num <= 9) {
			if (onlyPossiblePosition(num, row, col)) {
				sudokuGrid.setNumber(row, col, num);
				return true;
			}
			num++;
		}
		return false;
	}

	// Fills the cell at the given row and column if there is only one possible number for it
	private boolean setOnlyPossibleNumber(int row, int col) {
		ArrayList<Integer> possibleNumbers = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9));
		possibleNumbers = sudokuGrid.filterRow(possibleNumbers, row, col);
		possibleNumbers = sudokuGrid.filterCol(possibleNumbers, row, col);
		possibleNumbers = sudokuGrid.filterBox(possibleNumbers, row, col);
		if (possibleNumbers.size() == 1) {
			sudokuGrid.setNumber(row, col, possibleNumbers.get(0));
			return true;
		}
		return false;
	}
	
	// Check if the given row and column is the only possible position for that number
	private boolean onlyPossiblePosition(int num, int row, int col) {
		if (sudokuGrid.onlyPositionInRow(num, row, col) || sudokuGrid.onlyPositionInCol(num, row, col) || sudokuGrid.onlyPositionInBox(num, row, col)) {
			return true;
		}
		return false;
	}
}
