package org.peueueu.ms.model;

import org.peueueu.ms.exception.MineExplosionException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Board {
    private final int rows;
    private final int columns;
    private final int mines;

    private final List<BoardCell> cellList = new ArrayList<>();

    public Board(int rows, int columns, int mines) {
        this.rows = rows;
        this.columns = columns;
        this.mines = mines;
        generateCells();
        attachNeighbors();
        shuffleMines();
    }

    public void openCell(int row, int column) {
        Predicate<BoardCell> predicate = cell -> cell.getRow() == row && (cell.getColumn() == column);
        cellList.stream().filter(predicate).findFirst().ifPresent(BoardCell::open);
    }

    public void flagCell(int row, int column) {
        Predicate<BoardCell> predicate = cell -> cell.getRow() == row && (cell.getColumn() == column);
        cellList.stream().filter(predicate).findFirst().ifPresent(BoardCell::toggleFlag);
    }

    private void generateCells() {
        for(int row = 0; row < rows; row++) {
            for(int column = 0; column < columns; column++) {
                cellList.add(new BoardCell(row, column));
            }
        }
    }

    private void attachNeighbors() {
        for(BoardCell c1 : cellList) {
            for(BoardCell c2 : cellList) {
                c1.addNeighbor(c2);
            }
        }
    }

    private void shuffleMines() {
        long setupMines = 0;
        Predicate<BoardCell> cellWithMine = BoardCell::hasMine;
        while(setupMines < mines) {
            setupMines = cellList.stream().filter(cellWithMine).count();
            int randomCellNumber = (int) (Math.random() * cellList.size());
            cellList.get(randomCellNumber).setMine();
        }
    }

    public boolean objectiveAccomplished() {
        return cellList.stream().allMatch(BoardCell::objectiveAccomplished);
    }

    public void restart() {
        cellList.forEach(BoardCell::restart);
        shuffleMines();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        int idx = 0;
        for(int row = 0; row < rows; row++) {
            for(int column = 0; column < columns; column++) {
                sb.append(" ");
                sb.append(cellList.get(idx));
                sb.append(" ");
                idx++;
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
