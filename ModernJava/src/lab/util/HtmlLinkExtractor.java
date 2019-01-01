package lab.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to extract href links from an html page using regex. 
 */
public class HtmlLinkExtractor {

	private static final String HTML_A_TAG_PATTERN = "(?i)<a([^>]+)>(.+?)</a>";
	private static final String HTML_A_HREF_TAG_PATTERN = "\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))";
	private static final String HTTP_PROTOCOL = "http";
	
	private Pattern patternTag;
	private Pattern patternLink;
	private Matcher matcherTag;
	private Matcher matcherLink;

	public HtmlLinkExtractor() {
		patternTag = Pattern.compile(HTML_A_TAG_PATTERN);
		patternLink = Pattern.compile(HTML_A_HREF_TAG_PATTERN);
	}

	/**
	 * Given the html page, returns a list of strings representing URLs originating from  HREFs. Extracts only external links for .com and .org urls.
	 * 
	 * @param html The html page to scrape.
	 * @return A list of strings of scraped links.
	 */
	public List<String> extractLinks(String html) {

		List<String> result = new ArrayList<>();

		matcherTag = patternTag.matcher(html);

		while (matcherTag.find()) {

			String href = matcherTag.group(1); 

			matcherLink = patternLink.matcher(href);

			while (matcherLink.find()) {

				String link = matcherLink.group(1); 
				if (link.contains(HTTP_PROTOCOL)) {
					int endPosition = link.indexOf(".com") > -1 ? link.indexOf(".com") : link.indexOf(".org"); 
					if (endPosition > -1) {
						int startPosition = link.indexOf("http");
						link = link.substring(startPosition, endPosition + 4);
						result.add(link);
					}
				}
			}
		}

		return result;
	}
}