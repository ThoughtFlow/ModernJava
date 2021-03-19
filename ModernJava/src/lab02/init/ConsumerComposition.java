package lab02.init;

import java.util.function.Consumer;

public class ConsumerComposition {

	public static void main(String... args) {
		
		// todo Define functions here
		
		// todo Compose here
		Consumer<String> superPrint = s -> {}; // implement consumer composition
		
		superPrint.accept("This is a normal line and will only be printed to stdout");
		superPrint.accept("Exception occurred - line will be printed to stdout and stderr");
		
	}
}
