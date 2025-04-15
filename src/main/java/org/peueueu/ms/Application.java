package org.peueueu.ms;

import org.peueueu.ms.model.Board;
import org.peueueu.ms.view.BoardView;

public class Application {
    public static void main(String[] args) {
        Board board = new Board(3, 3, 1);
        new BoardView(board);
    }
}