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
    private BoardCell minedBoardCell;

    @BeforeEach
    void initBoardCellValue() {
        boardCell = new BoardCell(3, 3);

        adjacentNeighbor = new BoardCell(2, 3);
        neighborFromAdjacentNeighbor = new BoardCell(1, 3);
        diagonalNeighbor = new BoardCell(2, 2);
        notNeighbor = new BoardCell(5, 1);
        minedBoardCell = new BoardCell(4, 4);
        minedBoardCell.setMine();
    }

    @Test
    @DisplayName("Should return the BoardCell row")
    void getRow_BoardCellRow_rowNumber() {
        int boardCellRowNumber = 3;
        assertEquals(boardCellRowNumber, boardCell.getRow());
    }

    @Test
    @DisplayName("Should return the BoardCell row")
    void getColumn_BoardCellColumn_columnNumber() {
        int boardCellColumnNumber = 3;
        assertEquals(boardCellColumnNumber, boardCell.getColumn());
    }

    @Test
    @DisplayName("should add adjacent neighbors")
    void addNeighbor_AdjacentNeighbor_True() {
        boolean isNeighbor = boardCell.addNeighbor(adjacentNeighbor);

        assertTrue(isNeighbor);
    }

    @Test
    @DisplayName("should add diagonal neighbors")
    void addNeighbor_DiagonalNeighbor_True() {
        boolean isNeighbor = boardCell.addNeighbor(diagonalNeighbor);

        assertTrue(isNeighbor);
    }

    @Test
    @DisplayName("should not add invalid neighbors")
    void addNeighbor_NotNeighbor_False() {
        boolean isNeighbor = boardCell.addNeighbor(notNeighbor);

        assertFalse(isNeighbor);
    }

    @Test
    @DisplayName("Should return true when all neighbors are without mines")
    void safeNeighbors_AllNeighborsAreSafe_True() {
        boardCell.addNeighbor(adjacentNeighbor);
        assertTrue(boardCell.safeNeighbors());
    }

    @Test
    @DisplayName("Should return false if some neighbor has mines")
    void safeNeighbors_HasUnsafeNeighbor_False() {
        adjacentNeighbor.setMine();
        boardCell.addNeighbor(adjacentNeighbor);
        assertFalse(boardCell.safeNeighbors());
    }

    @Test
    @DisplayName("Should have isFlagged property value false as default")
    void getIsFlagged_DefaultValue_False() {
        assertFalse(boardCell.isFlagged());
    }

    @Test
    @DisplayName("Should change property isFlagged to true")
    void toggleFlag_isFlaggedTrue_BoardCellIsNotOpen() {
        boardCell.toggleFlag();
        assertTrue(boardCell.isFlagged());
    }

    @Test
    @DisplayName("Should return true when opening a BoardCell without flag or mine")
    void open_BoardCellWithoutFlagOrMine_True() {
        assertTrue(boardCell.open());
    }

    @Test
    @DisplayName("Should not open BoardCell if isFlagged")
    void open_BoardCellWithFlag_False() {
        boardCell.toggleFlag();
        assertFalse(boardCell.open());
    }

    @Test
    @DisplayName("Should throw MineExplosionException")
    void open_BoardCellWithMine_ThrowsMineExplosionException() {
        boardCell.setMine();
        assertThrows(MineExplosionException.class, () -> boardCell.open());
    }

    @Test
    @DisplayName("Should open safe neighbors recursively")
    void open_OpenAllSafeNeighbors_True() {
        boardCell.addNeighbor(adjacentNeighbor);
        adjacentNeighbor.addNeighbor(neighborFromAdjacentNeighbor);
        boardCell.open();
        assertTrue(adjacentNeighbor.isOpen() && neighborFromAdjacentNeighbor.isOpen());
    }

    @Test
    @DisplayName("Should stop when neighbor is not safe to open")
    void open_NotOpenUnsafeNeighbor_False() {
        boardCell.addNeighbor(adjacentNeighbor);
        adjacentNeighbor.addNeighbor(neighborFromAdjacentNeighbor);
        neighborFromAdjacentNeighbor.setMine();

        System.out.println(boardCell);

        boardCell.open();
        assertTrue(adjacentNeighbor.isOpen() && !neighborFromAdjacentNeighbor.isOpen());
    }

    @Test
    @DisplayName("Should set isOpen to false on restart")
    void restart_BoardCell_IsOpen_False() {
        boardCell.open();
        boardCell.restart();
        assertFalse(boardCell.isOpen());
    }

    @Test
    @DisplayName("Should set isFlagged to false on restart")
    void restart_BoardCell_IsFlagged_False() {
        boardCell.toggleFlag();
        boardCell.restart();
        assertFalse(boardCell.isFlagged());
    }

    @Test
    @DisplayName("Should set hasMine to false on restart")
    void restart_BoardCell_HasMine_False() {
        boardCell.setMine();
        boardCell.restart();
        assertFalse(boardCell.hasMine());
    }

    @Test
    @DisplayName("Should return true when opened BoardCell without mines")
    void objectiveAccomplished_DiscoveredBoardCell_True() {
        boardCell.open();
        assertTrue(boardCell.objectiveAccomplished());
    }

    @Test
    @DisplayName("Should return true when protected BoardCell with mine")
    void objectiveAccomplished_ProtectedBoardCell_True() {
        minedBoardCell.toggleFlag();
        assertTrue(minedBoardCell.objectiveAccomplished());
    }

    @Test
    @DisplayName("Should return false if opened mined BoardCell")
    void objectiveAccomplished_OpenedMinedBoardCell_False() {
        assertThrows(MineExplosionException.class, () -> minedBoardCell.open());
        assertFalse(minedBoardCell.objectiveAccomplished());
    }

    @Test
    @DisplayName("Should show the number of mines on the neighborhood")
    void minesOnTheNeighborhood_ShowTheNumberOfMines() {
        boardCell.addNeighbor(adjacentNeighbor);
        boardCell.addNeighbor(diagonalNeighbor);
        adjacentNeighbor.setMine();
        diagonalNeighbor.setMine();
        assertEquals(2, boardCell.minesOnTheNeighborhood());
    }

    @Test
    @DisplayName("Should return red flag emoji if a boardCell is flagged")
    void toString_flaggedCell_returnsFlagEmoji() {
        boardCell.toggleFlag();
        assertEquals("🚩", boardCell.toString());
    }

    @Test
    @DisplayName("Should return bomb emoji if opened boardCell with mine")
    void toString_openMinedCell_returnsBombEmoji() {
        boardCell.setMine();
        assertThrows(MineExplosionException.class, () -> boardCell.open());
        assertEquals("💣", boardCell.toString());
    }

    @Test
    @DisplayName("Should return the number of mines in the neighborhood if open a boardCell where neighbors have mines")
    void toString_openCellNeighborsWithMines_returnsMineCount() {
        adjacentNeighbor.setMine();
        boardCell.addNeighbor(adjacentNeighbor);
        boardCell.open();
        assertEquals("1", boardCell.toString());
    }

    @Test
    @DisplayName("Should return a square emoji if opened a safe boardCell")
    void toString_openSafeCell_returnsEmptySquare() {
        boardCell.open();
        assertEquals(" ", boardCell.toString());
    }

    @Test
    @DisplayName("Should return a question mark emoji when boardCell is closed")
    void toString_closedCell_returnsQuestionMark() {
        assertEquals("❔", boardCell.toString());
    }
}
