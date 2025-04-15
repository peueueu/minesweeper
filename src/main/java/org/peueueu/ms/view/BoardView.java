package org.peueueu.ms.view;

import org.peueueu.ms.exception.ExitGameException;
import org.peueueu.ms.exception.MineExplosionException;
import org.peueueu.ms.model.Board;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class BoardView {
    private Board board;
    private final Scanner userInput = new Scanner(System.in);
    private boolean continueGame = true;
    boolean mineExploded = false;


    public BoardView(Board board) {
        this.board = board;

        start();
    }

    public void start() {
        try {
            while (continueGame) {
                playSingleGame();
                askToContinue();
            }
        } catch (ExitGameException e) {
            System.out.println("I find your lack of faith disturbing.");
        } finally {
            userInput.close();
        }
    }

    private void playSingleGame() {
        try {
            while (!board.objectiveAccomplished()) {
                displayGameState();
                List<Integer> coordinates = promptForCoordinates();
                String action = promptForAction();
                handlePlayerAction(action, coordinates);
            }
            System.out.println(getVictoryMessage());
        } catch (MineExplosionException e) {
            mineExploded = true;
            System.out.println(getDefeatMessage());
        }
    }

    private void displayGameState() {
        System.out.println(board);
        System.out.println("""
               The Force reveals the board...
               Your move, young Minewalker.
               """);
    }

    private List<Integer> promptForCoordinates() {
        while (true) {
            String input = promptUser("Select a cell, you will (x,y): ");

            if (shouldExit(input)) {
                throw new ExitGameException(exitGameMessage());
            }

            try {
                return Arrays.stream(input.split(","))
                        .filter(inputString -> !inputString.isEmpty())
                        .map(Integer::parseInt)
                        .toList();
            } catch (NumberFormatException e) {
                System.out.println("Numbers, you must enter. Try again.");
            }
        }
    }

    private String promptForAction() {
        while (true) {
            String input = promptUser("""
                Choose your path:
                '1' to open the cell, or
                '2' to flag your suspicion?
                Your choice: """);

            if (shouldExit(input)) {
                throw new ExitGameException(exitGameMessage());
            }

            if (input.equals("1") || input.equals("2")) {
                return input.toLowerCase();
            }
            System.out.println("Clouded, your choice is. '1' or '2' you must choose.");
        }
    }

    private void handlePlayerAction(String action, List<Integer> coordinates) {
        if (coordinates.size() != 2) {
            System.out.println("Two numbers, you must provide. Hmmm...");
            return;
        }

        int row = coordinates.get(0);
        int column = coordinates.get(1);

        switch (action) {
            case "1" -> board.openCell(row, column);
            case "2" -> board.flagCell(row, column);
        }
    }

    private void askToContinue() {
        String input = promptUser(
            """
            Another game, you desire?
            (Yes - 'Y', No - 'N' answer you must):
            """);

        if (shouldExit(input)) {
            throw new ExitGameException(exitGameMessage());
        }

        continueGame = input.equalsIgnoreCase("y");
        if (continueGame) {
            board.restart();
        }
    }

    private String promptUser(String message) {
        System.out.print(message);
        return userInput.nextLine().trim();
    }

    private boolean shouldExit(String input) {
        return input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("n");
    }

    private String getVictoryMessage() {
        return """
           Victory, you have achieved!
           
           Clear your mind was,
           Sharp your instincts,
           Patient your approach.
           
           The Force of Logic, you have mastered.
           
           Danger, you saw coming.
           Traps, you avoided.
           A true Minewalker, you have become.
           
           Rest now, or face greater challenges?
           Decide, you must...
           Hmmm...
           """;
    }

    private String getDefeatMessage() {
        return """
           Failed, you have. But learn, you did.
           
           Too quick, your movements were,
           Or perhaps too hesitant.
           The dark side of recklessness, you faced.
           
           But strong with the Force still, you are.
           
           Another attempt, you will make?
           Grow stronger, you must.
           Hmmm...
           """;
    }

    private String exitGameMessage() {
        return "Abandon the mines today, fear will rule you tomorrow.";
    }
}
