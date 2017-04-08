public class Cell {
	private int number;
	private int row;
	private int column;
	
	public Cell(int number, int row, int column) {
		this.number = number;
		this.row = row;
		this.column = column;
	}
	
	public int getNumber() {
		return this.number;
	}
	
	public void setNumber(int number) {
		this.number = number;
	}
	
	public int getRow() {
		return this.row;
	}
	
	public int getColumn() {
		return this.column;
	}	
}
