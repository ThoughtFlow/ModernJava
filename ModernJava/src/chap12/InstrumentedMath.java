package chap12;


public class InstrumentedMath {

    public static int factorial(int count) {
        if (count == 1) {
            StackWalkerInstrumenter.getInstance().captureStackFrame();
        }

        return count > 0 ? count * factorial(--count) : 1;
    }

    public static int square(int toSquare) {
        StackWalkerInstrumenter.getInstance().captureStackFrame();
        return toSquare * toSquare;
    }

    public static double squareRoot(int toSquareRoot) {
        StackWalkerInstrumenter.getInstance().captureStackFrame();
        return Math.sqrt(toSquareRoot);
    }

}
