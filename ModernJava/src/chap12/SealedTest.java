package chap12;

import java.util.Random;
import java.util.stream.IntStream;

public class SealedTest {

    private static ChessPiece[][] placePieces(PieceColor color) {
        ChessPiece[][] pieces = new ChessPiece[8][8];

        pieces[0][0] = new Rook(color);
        pieces[1][0] = new Knight(color);
        pieces[2][0] = new Bishop(color);
        pieces[3][0] = new Queen(color);
        pieces[4][0] = new King(color);
        pieces[5][0] = new Bishop(color);
        pieces[6][0] = new Knight(color);
        pieces[7][0] = new Rook(color);
        pieces[0][1] = new Pawn(color);
        pieces[1][1] = new Pawn(color);
        pieces[2][1] = new Pawn(color);
        pieces[3][1] = new Pawn(color);
        pieces[4][1] = new Pawn(color);
        pieces[5][1] = new Pawn(color);
        pieces[6][1] = new Pawn(color);
        pieces[7][1] = new Pawn(color);
        
        return pieces;
    }

    private static void simulateGame(ChessPiece[][] myPieces, ChessPiece[][] opponentPieces) {

        Random rand = new Random();
        int newXPosition = rand.nextInt(8);
        int newYPosition = rand.nextInt(8);
        int thisXPosition = rand.nextInt(8);
        int thisYPosition = rand.nextInt(8);

        if (myPieces[thisXPosition][thisYPosition] != null &&
            myPieces[thisXPosition][thisYPosition].canMove(newXPosition, newYPosition, myPieces, opponentPieces)) {

            myPieces[newXPosition][newYPosition] = myPieces[thisXPosition][thisYPosition];
            System.out.println("Moved " + myPieces[thisXPosition][thisYPosition] +
                    " to " + newXPosition + ", " + newYPosition);
        }

    }

    public static void main(String[] args) {
        ChessPiece[][] myPieces = placePieces(PieceColor.Black);
        ChessPiece[][] opponentPieces = placePieces(PieceColor.White);

        IntStream.range(0, 100).forEach((i) -> {
            simulateGame(myPieces, opponentPieces);
            simulateGame(opponentPieces, myPieces);
        });
    }

    private enum PieceColor {White, Black};

    private static sealed abstract class ChessPiece permits King, Queen, Rook, Bishop, Knight, Pawn {
        private PieceColor color;

        ChessPiece(PieceColor color) {
            this.color = color;
        }

        PieceColor getColor() {
            return color;
        }

        abstract String getName();

        public String toString() {
            return getColor() + " " + getName();
        }

        abstract boolean canMove(int newXPosition, int newYPosition,
                                 ChessPiece[][] pieces, ChessPiece[][] opponentPieces);
    }

    private static final class King extends ChessPiece
    {
        public King(PieceColor color)
        {
            super(color);
        }

        String getName() {
            return "King";
        }

        @Override
        boolean canMove(int newXPosition, int newYPosition,
                        ChessPiece[][] pieces, ChessPiece[][] opponentPieces) {

            return newXPosition < 8 && newXPosition >= 0 && newYPosition < 8 && newYPosition >= 0;
        }
    }

    private static final class Queen extends ChessPiece {

        public Queen(PieceColor color)
        {
            super(color);
        }

        @Override
        String getName() {
           return "Queen";
        }

        @Override
        boolean canMove(int newXPosition, int newYPosition,
                        ChessPiece[][] pieces, ChessPiece[][] opponentPieces) {

            return newXPosition < 8 && newXPosition >= 0 && newYPosition < 8 && newYPosition >= 0;
        }
    }

    private static final class Rook extends ChessPiece {

        public Rook(PieceColor color)
        {
            super(color);
        }

        @Override
        String getName() {
            return "Rook";
        }

        @Override
        boolean canMove(int newXPosition, int newYPosition,
                        ChessPiece[][] pieces, ChessPiece[][] opponentPieces) {
            return newXPosition < 8 && newXPosition >= 0 && newYPosition < 8 && newYPosition >= 0;
        }
    }

    private static final class Bishop extends ChessPiece {

        public Bishop(PieceColor color)
        {
            super(color);
        }

        @Override
        String getName() {
            return "Bishop";
        }

        @Override
        boolean canMove(int newXPosition, int newYPosition,
                        ChessPiece[][] pieces, ChessPiece[][] opponentPieces) {
            return newXPosition < 8 && newXPosition >= 0 && newYPosition < 8 && newYPosition >= 0;
        }
    }

    private static final class Knight extends ChessPiece {

        public Knight(PieceColor color)
        {
            super(color);
        }

        @Override
        String getName() {
            return "Knight";
        }

        @Override
        boolean canMove(int newXPosition, int newYPosition,
                        ChessPiece[][] pieces, ChessPiece[][] opponentPieces) {
            return newXPosition < 8 && newXPosition >= 0 && newYPosition < 8 && newYPosition >= 0;
        }
    }

    private static final class Pawn extends ChessPiece {

        public Pawn(PieceColor color)
        {
            super(color);
        }

        @Override
        String getName() {
            return "Pawn";
        }
        @Override
        boolean canMove(int newXPosition, int newYPosition,
                        ChessPiece[][] pieces, ChessPiece[][] opponentPieces) {
            return newXPosition < 8 && newXPosition >= 0 && newYPosition < 8 && newYPosition >= 0;
        }
    }
}
