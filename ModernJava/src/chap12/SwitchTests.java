package chap12;

import java.util.EnumSet;

/**
 * These may not yet be supported by your IDE. Uncomment the switch expressions and use runSwitchTests in a bash shell
 * to compile and run these examples.
 */
@SuppressWarnings({"UnnecessaryDefault", "EnhancedSwitchMigration"})
public class SwitchTests {

    enum Day {MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY};

    private static String getDayTypeClassicJava(Day day) {
        String type;

        switch (day) {
            case FRIDAY:
            case SATURDAY:
            case SUNDAY:
                type = "Fun day";
                break;
            case WEDNESDAY:
                type = "Hump day";
                break;
            case MONDAY:
            case TUESDAY:
            case THURSDAY:
                type = "Blah day";
                break;
            default:
                throw new IllegalStateException("Unknown" + day);
        }

        return type;
    }

    private static String getDayType(Day day) {
        // Java 13 switch expressions
        return switch (day) {
            case FRIDAY, SATURDAY, SUNDAY -> "Fun day";
            case WEDNESDAY -> "Hump day";
            case MONDAY, TUESDAY, THURSDAY -> "Blah day";
            default -> "Unknown day";
        };
    }

    private static void printDayType(Day day) {
        // Java 13 switch statements need not return values.
        switch (day) {
            case FRIDAY, SATURDAY, SUNDAY  -> System.out.println(day + " is Fun day");
            case WEDNESDAY                 -> System.out.println(day + " is Hump day");
            case MONDAY, TUESDAY, THURSDAY -> System.out.println(day + " is Blah day");
            default -> System.out.println(day + "is Unknown day");
        };
    }

    private static String getDayTypeWithYield(Day day) {
        String dayType = "";

        // Java 13 switch statements also introduce yield statement that "break" out of each case statement.
        // Yields must return a value and each case must return a yield.
        dayType = switch (day) {
            case FRIDAY, SATURDAY, SUNDAY:
                yield "Fun day";
            case WEDNESDAY:
                yield "Hump day";
            case MONDAY, TUESDAY, THURSDAY:
                yield "Blah day";
            default:
                yield "Unknown value";
        };

        return dayType;
    }

     public static void main (String... args) {

        System.out.println("getDayTypeWithClassicJava=======");
        EnumSet.allOf(Day.class).forEach(day -> System.out.println(day + " is " + getDayTypeClassicJava(day)));
        System.out.println();

        System.out.println("GetDayType=======");
        EnumSet.allOf(Day.class).forEach(day -> System.out.println(day + " is " + getDayType(day)));
        System.out.println();

        System.out.println("PrintDayType=======");
        EnumSet.allOf(Day.class).forEach(SwitchTests::printDayType);
        System.out.println();

        System.out.println("GetDayTypeWithBreak=======");
        EnumSet.allOf(Day.class).forEach(day -> System.out.println(day + " is " + getDayTypeWithYield(day)));
        System.out.println();
    }
}
