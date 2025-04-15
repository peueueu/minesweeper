package org.peueueu.ms.model;

import org.peueueu.ms.exception.MineExplosionException;

import java.util.ArrayList;
import java.util.List;

public class BoardCell {
    private final int row;
    private final int column;
    private boolean isOpen = false;
    private boolean hasMine;
    private boolean isFlagged = false;

    private List<BoardCell> neighbors = new ArrayList<>();

    BoardCell(int row, int column) {
        this.row = row;
        this.column = column;
    }

    boolean addNeighbor(BoardCell neighbor) {
        boolean differentRow = row != neighbor.row;
        boolean differentColumn = column != neighbor.column;
        boolean isOnDiagonal = differentRow && differentColumn;

        int deltaRow = Math.abs(row - neighbor.row);
        int deltaColumn = Math.abs(column - neighbor.column);
        int deltaSum = deltaRow + deltaColumn;

        if(deltaSum == 1) {
            neighbors.add(neighbor);
            return true;
        } else if(deltaSum == 2 && isOnDiagonal) {
            neighbors.add(neighbor);
            return true;
        } else {
            return false;
        }
    }

    boolean safeNeighbors() {
        return neighbors.stream().noneMatch(neighbor -> neighbor.hasMine);
    }

    void toggleFlag() {
        if(!isOpen) {
            isFlagged = !isFlagged;
        }
    }

    boolean open() {
        if(!isOpen && !isFlagged) {
            isOpen = true;
            if(hasMine) {
                throw new MineExplosionException();
            }
            if(safeNeighbors()) {
                neighbors.forEach(BoardCell::open);
            }

            return true;
        }
        return false;
    }

    boolean objectiveAccomplished() {
        boolean discoveredBoardCell = !hasMine && isOpen;
        boolean protectedBoardCell = hasMine && isFlagged;
        return discoveredBoardCell || protectedBoardCell;
    }

    long minesOnTheNeighborhood() {
        return neighbors.stream().filter(neighbor -> neighbor.hasMine).count();
    }

    void restart() {
        isOpen = false;
        hasMine = false;
        isFlagged = false;
    }

    @Override
    public String toString() {
        if(isFlagged) return "🚩";
        if(isOpen && hasMine) return "💣";
        if(isOpen && minesOnTheNeighborhood() > 0) {
            return String.valueOf(minesOnTheNeighborhood());
        }
        if(isOpen) return " ";
        return "❔";
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public boolean hasMine() {
        return hasMine;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    void setMine() {
        if(!hasMine) {
            hasMine = true;
        }
    }
}