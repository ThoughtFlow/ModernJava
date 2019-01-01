package lab07.movielock.init;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ThreadSafeMovieDb implements MovieDb {

	private final Map<Category, List<Movie>> database = new HashMap<>();

	@Override
	public void add(Set<Category> categories, String name, Integer yearReleased) {

		Movie movieToAdd = new Movie(categories, name, yearReleased);

		// Protect multi-threaded access to database
		categories.forEach(category -> {
			database.computeIfAbsent(category, k -> new LinkedList<Movie>());
			database.compute(category, (k, v) -> {v.add(movieToAdd); return v;});
		});
	}
	
	@Override
	public void add(Category category, String name, Integer yearReleased) {

		// Protect multi-threaded access to database
		Set<Category> categories = new HashSet<>();
		categories.add(category);
		add(categories, name, yearReleased);
	}
	
	@Override
	public Movie findByName(String name) {
		AtomicReference<Movie> foundMovie = new AtomicReference<>();

		Consumer<Movie> consumer = nextTitle -> {if (nextTitle.getName().equals(name)) foundMovie.set(nextTitle);};
		
		// Protect multi-threaded access to database
		database.values().forEach(nextList -> nextList.forEach(consumer));

		return foundMovie.get();
	}
	
	@Override
	public List<String> findByCategory(Category category) {
		List<String> movieTitles = new ArrayList<>();

		// Protect multi-threaded access to database
		List<Movie> movies = database.getOrDefault(category, Collections.emptyList());

		movies.forEach(next -> movieTitles.add(next.getName()));

		return movieTitles;
	}

	@Override
	public boolean delete(String name) {

		final boolean existedBeforeDelete = findByName(name) != null;
		Predicate<Movie> p = movie -> movie.getName().equals(name);

		// Protect multi-threaded access to database
		database.values().forEach(list -> list.removeIf(p));

		return existedBeforeDelete;
	}
}