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
        boolean differentRow = this.row != neighbor.row;
        boolean differentColumn = this.column != neighbor.column;
        boolean isOnDiagonal = differentRow && differentColumn;

        int deltaRow = Math.abs(this.row - neighbor.row);
        int deltaColumn = Math.abs(this.column - neighbor.column);
        int deltaSum = deltaRow + deltaColumn;

        if(deltaSum == 1) {
            this.neighbors.add(neighbor);
            return true;
        } else if(deltaSum == 2 && isOnDiagonal) {
            this.neighbors.add(neighbor);
            return true;
        } else {
            return false;
        }
    }

    void toggleFlag() {
        if(!this.isOpen) {
            this.isFlagged = !this.isFlagged;
        }
    }

    boolean open() {
        if(!this.isOpen && !this.isFlagged) {
            this.isOpen = true;
            if(this.hasMine) {
                throw new MineExplosionException();
            }
            if(this.safeNeighbors()) {
                this.neighbors.forEach(BoardCell::open);
            }

            return true;
        }
      return false;
    }

    boolean safeNeighbors() {
        return this.neighbors.stream().noneMatch(neighbor -> neighbor.hasMine);
    }

    boolean getIsFlagged() {
        return this.isFlagged;
    }

    void setMine() {
        if(!this.hasMine) {
            this.hasMine = true;
        }
    }

}