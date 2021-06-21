package lab04.fin;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class CurriedGrading
{
    private static final Function<GradeCalcType, Function<List<Double>, Double>> curryingFunction = method -> {
        Function<List<Double>, Double> func;
        switch (method)
        {
            case AVERAGE :
                func = list -> list.stream().reduce(0d, Double::sum) / list.size();
                break;

            case WORST :
                func = list -> list.stream().min(Double::compareTo).orElseGet(() -> 0d);
                break;

            case BEST :
                func = list -> list.stream().max(Double::compareTo).orElseGet(() -> 0d);
                break;

            default:
                func = list -> 0d;
        }

        return func;
    };

    public static void main(String... args)
    {
    	List<Double> scores = Arrays.asList(.65, .75, .85);
    	
        System.out.println(curryingFunction.apply(GradeCalcType.AVERAGE).apply(scores));
        System.out.println(curryingFunction.apply(GradeCalcType.BEST).apply(scores));
        System.out.println(curryingFunction.apply(GradeCalcType.WORST).apply(scores));
    }

    private enum GradeCalcType
    {
        AVERAGE,
        WORST,
        BEST
    }
}


