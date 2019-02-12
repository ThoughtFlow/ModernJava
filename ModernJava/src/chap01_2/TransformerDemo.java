package chap01_2;

public class TransformerDemo {

    public static void main(String... args) {

        {
            Transformer transformer = String::toLowerCase;
            transformer.printTransform("This Is My String");
        }

        {
            Transformer transformer = Transformer::capitalize;
            transformer.printTransform("this is my string");
        }

        {
            Transformer transformer = Transformer::removeSpaces;
            transformer.printTransform("This is my string");
        }
        
        {
        	System.out.println(Transformer.capitalize("capitalize this"));
        	System.out.println(Transformer.removeSpaces("r e m o v e - s p a c e s"));
        }
    }
}
