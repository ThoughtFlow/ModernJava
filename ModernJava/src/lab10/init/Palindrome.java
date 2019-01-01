package lab10.init;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Algorithm to find a palindrome:
 * - Strip off spaces in between words
 * - Convert all characters to same case (using lowercase in these examples)
 * - Filter out any word that is 2 characters or less
 * - Find the half-way point in the word and compare each character on the lower half to their counterpart on the upper half 
 */
public class Palindrome
{

    private static List<String> findPalindrome(List<String> list)
    {
    	// Implement this
    	return Collections.emptyList();
    }
    
    private static List<String> findPalindromeKeepOriginal(List<String> list)
    {
    	// Implement this
    	return Collections.emptyList();
    }
    
    public static void main(String... args) throws IOException
    {
        List<String> palindromes = Arrays.asList(
        "A man a plan a cat a ham a yak a yam a hat a canal Panama",
        "A Toyota Race fast safe car A Toyota",
        "As I pee sir, I see Pisa",
        "Aibohphobia",
        "ABBA",
        "A Toyota's a Toyota",
        "This is not a palindrome");
        System.out.println("These are palindromes: " );
        findPalindrome(palindromes).forEach(System.out::println);
        findPalindromeKeepOriginal(palindromes).forEach(System.out::println);

    }
}