package chap12;

import java.util.EnumSet;

/**
 * These may not yet be supported by your IDE. Uncomment the switch expressions and use runSwitchTests in a bash shell
 * to compile and run these examples.
 */
public class SwitchTests {

    enum Day {MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY};

    private static String getDayType(Day day) {
        String type = "?";

        // Java 12 switch expressions
//        type = switch (day) {
//            case FRIDAY, SATURDAY, SUNDAY -> "Fun day";
//            case WEDNESDAY -> "Hump day";
//            case MONDAY, TUESDAY, THURSDAY -> "Blah day";
//            default -> "Unknown day";
//        };

        return type;
    }

    private static void printDayType(Day day) {
        // Java 12 switch statements need not return values.
//        switch (day) {
//            case FRIDAY, SATURDAY, SUNDAY  -> System.out.println(day + " is Fun day");
//            case WEDNESDAY                 -> System.out.println(day + " is Hump day");
//            case MONDAY, TUESDAY, THURSDAY -> System.out.println(day + " is Blah day");
//            default -> System.out.println(day + "is Unknown day");
//        };
    }

    private static String getDayTypeWithBreak(Day day) {
        String type = "?";

        // Java 12 switch statements can also be combined with classic-style break. But then, each case must have a
        // break statement.
//        type = switch (day) {
//            case FRIDAY, SATURDAY, SUNDAY:
//                break "Fun day";
//            case WEDNESDAY:
//                break "Hump day";
//            case MONDAY, TUESDAY, THURSDAY:
//                break "Blah day";
//            default:
//                break "Unknown value";
//        };

        return type;
    }

    public static void main (String... args) {

        System.out.println("GetDayType=======");
        EnumSet.allOf(Day.class).forEach(day -> System.out.println(day + " is " + getDayType(day)));
        System.out.println();

        System.out.println("PrintDayType=======");
        EnumSet.allOf(Day.class).forEach(SwitchTests::printDayType);
        System.out.println();

        System.out.println("GetDayTypeWithBreak=======");
        EnumSet.allOf(Day.class).forEach(day -> System.out.println(day + " is " + getDayTypeWithBreak(day)));
        System.out.println();
    }
}
