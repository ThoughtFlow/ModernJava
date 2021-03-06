package chap12;

public class MultiLineTest {

    private static String getAlignedMultiLine() {
        String textBlock = "";

        textBlock = """
                           Formatted text block text
                           Every line is aligned
                           But leading spaces used in program formatting are ignored
                           This is important in keeping everything aligned
                           """;

        return textBlock;
    }

    private static String getMisalignedMultiLine() {
        String textBlock = "";
        
        textBlock = """
                           Formatted text block text
                           Every line is aligned
                           This is important in keeping everything aligned
              As you can see, this misalignment changes everything!
                           """;

        return textBlock;
    }

    private static String getEmptyMultiLine() {
        String textBlock = "";
        
        textBlock = """
                    """;

        return textBlock;
    }

    private static String getHtmlCode() {
        String textBlock = "";
        
        textBlock = """
        <html>
          <body>
              <p>Welcome page</p>
          </body>
        </html>
        """;
        
        return textBlock;
    }

    private static String generateJavaCode() {
        String textBlock = "";
        
        // Mix and match text blocks with traditional code
        textBlock = "public void print(Object object) {\n" +
                    """
                      System.out.println(Objects.toString(object));
                    }
                    """;
        
        return textBlock;
    }

    private static String getWithoutLineFeed() {
        String textBlock = "";

        textBlock =  """
				line 1
				line 2
				line 3 """; // OK - No line feed on last line

        return textBlock + "|";
    }

    private static String getEmptyLine() {
        String emptyLine = "";
        
        emptyLine = """
                    """;      // OK - Empty line

        return emptyLine;
    }

    private static void otherExamples() {
        // These do not compile
//        String alpha = """""";       // No line terminator after first delimiter
//        String beta  = """ """;      // No line terminator after first delimiter
//        String cappa = """
//               ";            // No closing delimiter (text block continues to EOF)
//        String delta = """
//           abc \ def
//        """;          // unescaped backslash
    }

    private static String getNoNewLineWithSpace() {
        // \s translates into a single space instead of newline
        String colors = """
            red  
            green\s
            blue \s
            """;

        return colors;
    }

    private static String getNoNewLine() {
        //  suppresses the newline character
        String text = """
                Lorem ipsum dolor sit amet, consectetur adipiscing \
                elit, sed do eiusmod tempor incididunt ut labore \
                et dolore magna aliqua.\
                """;

        return text;
    }


    public static void main(String... args) {
        System.out.println("==================================================");
        System.out.println("Aligned text block");
        System.out.println("==================================================");
        System.out.println(getAlignedMultiLine());

        System.out.println("==================================================");
        System.out.println("Unaligned text block");
        System.out.println("==================================================");
        System.out.println(getMisalignedMultiLine());

        System.out.println("==================================================");
        System.out.println("Empty text block");
        System.out.println("==================================================");
        System.out.println(getEmptyMultiLine());

        System.out.println("==================================================");
        System.out.println("Html code");
        System.out.println("==================================================");
        System.out.println(getHtmlCode());

        System.out.println("==================================================");
        System.out.println("Generate Java code");
        System.out.println("==================================================");
        System.out.println(generateJavaCode());

        System.out.println("==================================================");
        System.out.println("Get without line feed");
        System.out.println("==================================================");
        System.out.println(getWithoutLineFeed());

        System.out.println("==================================================");
        System.out.println("Get empty line");
        System.out.println("==================================================");
        System.out.println(getEmptyLine());

        System.out.println("==================================================");
        System.out.println("Get no new line with spaces");
        System.out.println("==================================================");
        System.out.println(getNoNewLineWithSpace());

        System.out.println("==================================================");
        System.out.println("Get no new line");
        System.out.println("==================================================");
        System.out.println(getNoNewLine());
    }
}
