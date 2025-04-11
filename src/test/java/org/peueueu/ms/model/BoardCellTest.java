package org.peueueu.ms.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.peueueu.ms.exception.MineExplosionException;

import static org.junit.jupiter.api.Assertions.*;

public class BoardCellTest {
    private BoardCell boardCell;
    private BoardCell notNeighbor;
    private BoardCell adjacentNeighbor;
    private BoardCell diagonalNeighbor;
    private BoardCell neighborFromAdjacentNeighbor;
    @BeforeEach
    void initBoardCellValue() {
        this.boardCell = new BoardCell(3, 3);
        this.adjacentNeighbor = new BoardCell(2, 3);
        this.neighborFromAdjacentNeighbor = new BoardCell(1, 3);
        this.diagonalNeighbor = new BoardCell(2, 2);
        this.notNeighbor = new BoardCell(5, 1);
    }

    @Test
    @DisplayName("should add adjacent neighbors")
    void addNeighbor_AdjacentNeighbor_True() {
        boolean isNeighbor = this.boardCell.addNeighbor(this.adjacentNeighbor);

        assertTrue(isNeighbor);
    }

    @Test
    @DisplayName("should add diagonal neighbors")
    void addNeighbor_DiagonalNeighbor_True() {
        boolean isNeighbor = this.boardCell.addNeighbor(this.diagonalNeighbor);

        assertTrue(isNeighbor);
    }
    @Test
    @DisplayName("should not add invalid neighbors")
    void addNeighbor_NotNeighbor_False() {
        boolean isNeighbor = this.boardCell.addNeighbor(this.notNeighbor);

        assertFalse(isNeighbor);
    }

    @Test
    @DisplayName("Should return true when all neighbors are without mines")
    void safeNeighbors_AllNeighborsAreSafe_True() {
        this.boardCell.addNeighbor(this.adjacentNeighbor);
        assertTrue(this.boardCell.safeNeighbors());
    }

    @Test
    @DisplayName("Should return false if some neighbor has mines")
    void safeNeighbors_HasUnsafeNeighbor_False() {
        this.adjacentNeighbor.setMine();
        this.boardCell.addNeighbor(this.adjacentNeighbor);
        assertFalse(this.boardCell.safeNeighbors());
    }

    @Test
    @DisplayName("Should have isFlagged property value false as default")
    void getIsFlagged_DefaultValue_False() {
        assertFalse(this.boardCell.getIsFlagged());
    }

    @Test
    @DisplayName("Should change property isFlagged to true")
    void toggleFlag_isFlaggedTrue_BoardCellIsNotOpen() {
        this.boardCell.toggleFlag();
        assertTrue(this.boardCell.getIsFlagged());
    }

    @Test
    @DisplayName("Should return true when opening a BoardCell without flag or mine")
    void open_BoardCellWithoutFlagOrMine_True() {
        assertTrue(this.boardCell.open());
    }

    @Test
    @DisplayName("Should not open BoardCell if isFlagged")
    void open_BoardCellWithFlag_False() {
        this.boardCell.toggleFlag();
        assertFalse(this.boardCell.open());
    }
    @Test
    @DisplayName("Should throw MineExplosionException")
    void open_BoardCellWithMine_ThrowsMineExplosionException() {
        this.boardCell.setMine();
        assertThrows(MineExplosionException.class, () -> {
            this.boardCell.open();
        });
    }

    @Test
    @DisplayName("Should open safe neighbors recursively")
    void open_OpenAllSafeNeighbors_True() {
        this.boardCell.addNeighbor(this.adjacentNeighbor);
        this.adjacentNeighbor.addNeighbor(this.neighborFromAdjacentNeighbor);
        this.boardCell.open();
        assertTrue(this.adjacentNeighbor.getIsOpen() && this.neighborFromAdjacentNeighbor.getIsOpen());
    }

    @Test
    @DisplayName("Should stop when neighbor is not safe to open")
    void open_NotOpenUnsafeNeighbor_False() {
        this.boardCell.addNeighbor(this.adjacentNeighbor);
        this.adjacentNeighbor.addNeighbor(this.neighborFromAdjacentNeighbor);
        this.neighborFromAdjacentNeighbor.setMine();

        this.boardCell.open();
        assertTrue(this.adjacentNeighbor.getIsOpen() && !this.neighborFromAdjacentNeighbor.getIsOpen());
    }
}
