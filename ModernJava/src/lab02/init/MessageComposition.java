package lab02.init;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class MessageComposition {

	/**
	 * Encrypts the given list of strings and returns a new list with their encrypted equivalents. Use a simple "shift one up UTF value" encryption.  
	 * 
	 * @param toEncryptStrings List of strings to encrypt.
	 * @return A list of encrypted strings.
	 */
	private static List<String> encrypt(List<String> toEncryptStrings) {
		List<String> output = new ArrayList<>();
		
		for (String nextLine : toEncryptStrings) {
			
			char[] stringArray = nextLine.toCharArray();
			for (int i = 0; i < stringArray.length; ++i) {
				stringArray[i] = (char) (stringArray[i] + 1);
			}
			
			output.add(new String(stringArray));
		}
		
		return output;
	};
	
	/**
	 * Encodes the given list of strings using BASE64 encoding.
	 * 
	 * @param toEncodeStrings List of strings to encode.
	 * @return A list of encoded strings.
	 */
	private static List<String> encode(List<String> toEncodeStrings) {
		List<String> output = new ArrayList<>();
		
		for (String nextLine : toEncodeStrings) {
			output.add(Base64.getEncoder().encodeToString(nextLine.getBytes()));
		}
		
		return output;
	}
	
	/**
	 * Obfuscates credit card numbers.
	 * 
	 * @param toObfuscateStrings List of strings to obfuscate.
	 * @return A list of obfuscated strings.
	 */
	private static List<String> obfuscate(List<String> toObfuscateStrings) {
		List<String> output = new ArrayList<>();
		
		for (String nextLine : toObfuscateStrings) {
			output.add(nextLine.replaceFirst("[0-9][0-9][0-9][0-9]\\-[0-9][0-9][0-9][0-9]\\-[0-9][0-9][0-9][0-9]", "****-****-****"));
		}
		
		return output;
	}
	
	/**
	 * Decrypts the list of strings encrypted with encrypt().
	 * 
	 * @param toDecryptStrings List of strings to decrypt.
	 * @return A list of decrypted strings.
	 */
	private static List<String> decrypt(List<String> toDecryptStrings) {
		
		List<String> output = new ArrayList<>();
		for (String nextLine : toDecryptStrings) {
			
			char[] stringArray = nextLine.toCharArray();
			for (int i = 0; i < stringArray.length; ++i) {
				stringArray[i] = (char) (stringArray[i] - 1);
			}
			
			output.add(new String(stringArray));
		}
		
		return output;
	};
	
	/**
	 * Decodes a list of strings encoded with encode().
	 * 
	 * @param stringsToDecode List of strings to decode.
	 * @return A list of decoded strings.
	 */
	private static List<String> decode(List<String> stringsToDecode) {
		List<String> output = new ArrayList<>();
		
		for (String nextLine : stringsToDecode) {
			output.add(new String(Base64.getDecoder().decode(nextLine)));
		}
		
		return output;
	}

	/**
	 * Prints the list of strings.
	 * 
	 * @param stage  The stage of the message to print.
	 * @param messageToPrint The message to print
	 */
	private static void printMessage(String stage, List<String> messageToPrint) {
		
		System.out.println(stage);
		for (String nextLine : messageToPrint) {
			System.out.println("  " + nextLine);
		}
	}
	
	/**
	 * Passes a list of strings through the writing and reading function and prints out message.
	 * 
	 * @param writingFunction Function to use to write the list of strings.
	 * @param readingFunction Function to use to read the list of strings.
	 */
	private static void writeAndRead(Supplier<Function<List<String>, List<String>>> writingFunction, Supplier<Function<List<String>, List<String>>> readingFunction) {
		List<String> rawMessage = Arrays.asList("Dear Value Account Holder,",
		        "  This is your Visa credit card statement for account 1234-5678-9012-3456.",
		        "  The balance due is $3,256.12.",
		        "  Thank you for your business.");
		
		printMessage("Initial:", rawMessage);
		List<String> messageToSend = writingFunction.get().apply(rawMessage);
		
		printMessage("Encoded:", messageToSend);
		List<String> receivedMessage = readingFunction.get().apply(messageToSend);
		
		printMessage("Decoded:", receivedMessage);
	}

	public static void main(String[] args) throws IOException {
		
		// Implement the writingFunction that will obfuscate, compress and encrypt.
		Supplier<Function<List<String>, List<String>>> writingFunction = () -> Function.identity();
		
		// Implement the readingFunction that will decode and decrypt.
		Supplier<Function<List<String>, List<String>>> readingFunction = () -> Function.identity();

		writeAndRead(writingFunction, readingFunction);
	}
}