package chap12;

public class PatternMatcher {

    @SuppressWarnings("PatternVariableCanBeUsed")
    private static String getFormattedValueOldWay(Object object) {
        String returnValue;

        // This is the classic way of pattern matching
        if (object instanceof String) {
            String s = (String) object;
            returnValue = "From a String: " + s.toLowerCase();
        }
        else if (object instanceof Integer) {
            Integer i = (Integer) object;
            returnValue = "From an integer: " + i.toString();
        }
        else if (object instanceof Long) {
            Long l = (Long) object;
            returnValue = "From a Long: " + l.toString();
        }
        else {
            throw new IllegalArgumentException("Unsupported type: " + object.getClass().toString());
        }

        return returnValue;
    }

    private static String getFormattedValue(Object object) {
        String returnValue;

        // Now with pattern matching
        if (object instanceof String s) {
            returnValue = "From a String: " + s.toLowerCase();
            }
        else if (object instanceof Integer i) {
            returnValue = "From an integer: " + i.toString();
        }
        else if (object instanceof Long l) {
            returnValue = "From a Long: " + l.toString();
        }
        else {
            throw new IllegalArgumentException("Unsupported type: " + object.getClass().toString());
        }

        return returnValue;
    }

    public static void main(String... args) {
        System.out.println(getFormattedValue("Java SE 14"));
        System.out.println(getFormattedValue(14));
        System.out.println(getFormattedValue(14L));

        try {
            System.out.println(getFormattedValue(14.0));
        }
        catch (IllegalArgumentException e) {
            System.out.println("As expected, unsupported type: (" + e.getMessage() + ")");
        }
    }
}
