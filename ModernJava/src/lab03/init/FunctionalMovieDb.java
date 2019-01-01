package lab03.init;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FunctionalMovieDb implements MovieDb {

	@SuppressWarnings("unused")
	private final Map<Category, List<Movie>> database = new HashMap<>();

	@Override
	public void add(Set<Category> categories, String name, Integer yearReleased) {

		// Implement this
	}
	
	@Override
	public void add(Category category, String name, Integer yearReleased) {

		// Nothing more to implement
		Set<Category> categories = new HashSet<>();
		categories.add(category);
		add(categories, name, yearReleased);
	}
	
	@Override
	public Movie findByName(String name) {
		
		// Implement this
		return null;
	}
	
	@Override
	public List<String> findByCategory(Category category) {
		
		// Implement this
		return new ArrayList<>();
	}

	@Override
	public boolean delete(String name) {
		
		// Implement this
		return false;
	}
}