import java.util.ArrayList;
import java.util.Arrays;

public class SudokuGrid {
	private ArrayList<Cell> sudokuGrid;
	
	public SudokuGrid(ArrayList<Integer> numbers) {
		this.sudokuGrid = new ArrayList<Cell>();
		fillGrid(numbers);
	}
	
	private void fillGrid(ArrayList<Integer> numbers) {
		int count = 0;
		int row = 0;
		int col = 0;
		while (count < numbers.size()) {
			Cell c = new Cell(numbers.get(count), row, col);
			sudokuGrid.add(c);
			if (col == 8) {
				row++;
			}
			col = (col+1)%9;
			count++;
		}
		if (count != 81) {
			throw new IndexOutOfBoundsException(count + " numbers inputted. Please have exactly 81 numbers.");
		}
	}
	
	public boolean solved() {
		boolean result = true;
		for(Cell c : sudokuGrid) {
			if (c.getNumber() == 0) {
				result = false;
			}
		}
		return result;
	}
	
	public int getNumber(int row, int col) {
		return sudokuGrid.get(9*row+col).getNumber();
	}
	
	public void setNumber(int row, int col, int num) {
		sudokuGrid.get(9*row+col).setNumber(num);
	}
	
	/**
	 * Filters out the possible numbers that can be in the same 3 x 3 box as the given row and column
	 * @param possibleNumbers
	 * @param row
	 * @param col
	 * @return
	 */
	public ArrayList<Integer> filterBox(ArrayList<Integer> possibleNumbers, int row, int col) {
		int x = row%3;
		int y = col%3;
		int firstRowToRemove = 1;
		int secondRowToRemove = 2;
		int firstColToRemove = 1;
		int secondColToRemove = 2;
		if (x == 1) {
			firstRowToRemove = -1;
			secondRowToRemove = 1;
		}
		if (x == 2) {
			firstRowToRemove = -2;
			secondRowToRemove = -1;
		}
		if (y == 1) {
			firstColToRemove = -1;
			secondColToRemove = 1;
		}
		if (y == 2) {
			firstColToRemove = -2;
			secondColToRemove = -1;
		}
		possibleNumbers.remove((Object)getNumber(row, col+firstColToRemove));
		possibleNumbers.remove((Object)getNumber(row, col+secondColToRemove));
		possibleNumbers.remove((Object)getNumber(row+firstRowToRemove, col));
		possibleNumbers.remove((Object)getNumber(row+firstRowToRemove, col+firstColToRemove));
		possibleNumbers.remove((Object)getNumber(row+firstRowToRemove, col+secondColToRemove));
		possibleNumbers.remove((Object)getNumber(row+secondRowToRemove, col));
		possibleNumbers.remove((Object)getNumber(row+secondRowToRemove, col+firstColToRemove));
		possibleNumbers.remove((Object)getNumber(row+secondRowToRemove, col+secondColToRemove));
		return possibleNumbers;
	}

	/**
	 * Filters out the possible numbers that can be in the same column as the given row and column
	 * @param possibleNumbers
	 * @param row
	 * @param col
	 * @return
	 */
	public ArrayList<Integer> filterCol(ArrayList<Integer> possibleNumbers, int row, int col) {
		int i = 0;
		while (i < 9) {
			if (i != row) {
				int numberToRemove = getNumber(i, col);
				if (numberToRemove != 0) {
					possibleNumbers.remove((Object)numberToRemove);
				}
			}
			i++;
		}
		return possibleNumbers;
	}

	/**
	 * Filters out the possible numbers that can be in the same row as the given row and column
	 * @param possibleNumbers
	 * @param row
	 * @param col
	 * @return
	 */
	public ArrayList<Integer> filterRow(ArrayList<Integer> possibleNumbers, int row, int col) {
		int i = 0;
		while (i < 9) {
			if (i != col) {
				int numberToRemove = getNumber(row, i);
				if (numberToRemove != 0) {
					possibleNumbers.remove((Object)numberToRemove);
				}
			}
			i++;
		}
		return possibleNumbers;
	}

	public void print() {
		for(Cell c : sudokuGrid) {
			if (c.getColumn() == 0 && c.getRow() % 3 == 0) {
				System.out.println("-----------------------");
			}
			if (c.getColumn() % 3 == 0) {
				System.out.print("| ");
			}
			System.out.print(c.getNumber() + " ");
			if (c.getColumn() == 8) {
				System.out.print("|");
				System.out.println();
				if (c.getRow() == 8) {
					System.out.println("-----------------------");
				}
			}
		}
		System.out.println("");
		System.out.println("");
	}

	public boolean onlyPositionInRow(int num, int row, int col) {
		// if any row, col or box already contains the number, it is not a valid position for the number
		if (rowContains(num, row) || colContains(num, col) || boxContains(num, row, col)) {
			return false;
		}
		// mark the entire row as possible positions at the beginning
		ArrayList<Boolean> possiblePositions = new ArrayList<>(Arrays.asList(true, true, true, true, true, true, true, true, true));
		int i = 0;
		while (i < 9) {
			if (i == col) {
				// automatically set this position as false for easier checking later on
				possiblePositions.set(i, false);
			} else if (getNumber(row, i) != 0) {
				// if there already is a number there, the position is not available
				possiblePositions.set(i, false);
			} else if (colContains(num, i)) {
				possiblePositions.set(i, false);
			} else if (boxContains(num, row, i)) {
				possiblePositions.set(i, false);
			}
			i++;
		}
		if (possiblePositions.contains(true)) {
			return false;
		}
		return true;
	}

	public boolean onlyPositionInCol(int num, int row, int col) {
		// if any row, col or box already contains the number, it is not a valid position for the number
		if (rowContains(num, row) || colContains(num, col) || boxContains(num, row, col)) {
			return false;
		}
		// mark the entire col as possible positions at the beginning
		ArrayList<Boolean> possiblePositions = new ArrayList<>(Arrays.asList(true, true, true, true, true, true, true, true, true));
		int i = 0;
		while (i < 9) {
			if (i == row) {
				// automatically set this position as false for easier checking later on
				possiblePositions.set(i, false);
			} else if (getNumber(i, col) != 0) {
				// if there already is a number there, the position is not available
				possiblePositions.set(i, false);
			} else if (rowContains(num, i)) {
				possiblePositions.set(i, false);
			} else if (boxContains(num, i, col)) {
				possiblePositions.set(i, false);
			}
			i++;
		}
		if (possiblePositions.contains(true)) {
			return false;
		}
		return true;
	}
	
	public boolean onlyPositionInBox(int num, int row, int col) {
		// if any row, col or box already contains the number, it is not a valid position for the number
		if (rowContains(num, row) || colContains(num, col) || boxContains(num, row, col)) {
			return false;
		}
		int x = row%3;
		int y = col%3;
		int firstRowToCheck = 1;
		int secondRowToCheck = 2;
		int firstColToCheck = 1;
		int secondColToCheck = 2;
		if (x == 1) {
			firstRowToCheck = -1;
			secondRowToCheck = 1;
		}
		if (x == 2) {
			firstRowToCheck = -2;
			secondRowToCheck = -1;
		}
		if (y == 1) {
			firstColToCheck = -1;
			secondColToCheck = 1;
		}
		if (y == 2) {
			firstColToCheck = -2;
			secondColToCheck = -1;
		}
		if (colContains(num, col+firstColToCheck) && colContains(num, col+secondColToCheck)
				&& rowContains(num, row+firstRowToCheck) && rowContains(num, row+secondRowToCheck)) {
			return true;
		}
		return false;
	}
	
	public boolean rowContains(int num, int row) {
		int i = 0;
		while (i < 9) {
			if (getNumber(row, i) == num) {
				return true;
			}			
			i++;
		}
		return false;
	}
	
	public boolean colContains(int num, int col) {
		int i = 0;
		while (i < 9) {
			if (getNumber(i, col) == num) {
				return true;
			}			
			i++;
		}
		return false;
	}
	
	public boolean boxContains(int num, int row, int col) {
		int x = row%3;
		int y = col%3;
		int firstRowToCheck = 1;
		int secondRowToCheck = 2;
		int firstColToCheck = 1;
		int secondColToCheck = 2;
		if (x == 1) {
			firstRowToCheck = -1;
			secondRowToCheck = 1;
		}
		if (x == 2) {
			firstRowToCheck = -2;
			secondRowToCheck = -1;
		}
		if (y == 1) {
			firstColToCheck = -1;
			secondColToCheck = 1;
		}
		if (y == 2) {
			firstColToCheck = -2;
			secondColToCheck = -1;
		}
		if (getNumber(row, col+firstColToCheck) == num) {
			return true;
		}
		if (getNumber(row, col+secondColToCheck) == num) {
			return true;
		}
		if (getNumber(row+firstRowToCheck, col) == num) {
			return true;
		}
		if (getNumber(row+firstRowToCheck, col+firstColToCheck) == num) {
			return true;
		}
		if (getNumber(row+firstRowToCheck, col+secondColToCheck) == num) {
			return true;
		}
		if (getNumber(row+secondRowToCheck, col) == num) {
			return true;
		}
		if (getNumber(row+secondRowToCheck, col+firstColToCheck) == num) {
			return true;
		}
		if (getNumber(row+secondRowToCheck, col+secondColToCheck) == num) {
			return true;
		}
		return false;
	}
}
