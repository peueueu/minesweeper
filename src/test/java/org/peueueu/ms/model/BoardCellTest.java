package org.peueueu.ms.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardCellTest {
    private BoardCell boardCell;
    @BeforeEach
    void initBoardCellValue() {
        this.boardCell = new BoardCell(3, 3);
    }

    @Test
    void addNeighbor_AdjacentNeighbor_True() {
        BoardCell neighbor = new BoardCell(2, 3);
        boolean isNeighbor = boardCell.addNeighbor(neighbor);

        assertTrue(isNeighbor);
    }

    @Test
    void addNeighbor_DiagonalNeighbor_True() {
        BoardCell neighbor = new BoardCell(2, 2);
        boolean isNeighbor = boardCell.addNeighbor(neighbor);

        assertTrue(isNeighbor);
    }
    @Test
    void addNeighbor_NotNeighbor_False() {
        BoardCell neighbor = new BoardCell(2, 1);
        boolean isNeighbor = boardCell.addNeighbor(neighbor);

        assertFalse(isNeighbor);
    }
}
