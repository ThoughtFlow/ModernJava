package lab07.movielock.fin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ThreadSafeMovieDb implements MovieDb {

	private final Map<Category, List<Movie>> database = new HashMap<>();
	private final ReadWriteLock locker = new ReentrantReadWriteLock();

	@Override
	public void add(Set<Category> categories, String name, Integer yearReleased) {

		Movie movieToAdd = new Movie(categories, name, yearReleased);

		try {
			locker.writeLock().lock();

			categories.forEach(category -> {
				database.computeIfAbsent(category, k -> new LinkedList<Movie>());
				database.compute(category, (k, v) -> {v.add(movieToAdd); return v;});
			});
		}
		finally {
			// Make sure the unlock is in a finally block.
			locker.writeLock().unlock();
		}
	}
	
	@Override
	public void add(Category category, String name, Integer yearReleased) {

		Set<Category> categories = new HashSet<>();
		categories.add(category);
		add(categories, name, yearReleased);
	}
	
	@Override
	public Movie findByName(String name) {
		AtomicReference<Movie> foundMovie = new AtomicReference<>();

		Consumer<Movie> consumer = nextTitle -> {if (nextTitle.getName().equals(name)) foundMovie.set(nextTitle);};

		try {
			locker.readLock().lock();

			database.values().forEach(nextList -> nextList.forEach(consumer));
		}
		finally {
			// Make sure the unlock is in a finally block.
			locker.readLock().unlock();
		}

		return foundMovie.get();
	}
	
	@Override
	public List<String> findByCategory(Category category) {
		List<String> movieTitles = new ArrayList<>();
		
		List<Movie> movies = database.getOrDefault(category, Collections.emptyList());

		try {
			locker.readLock().lock();
		   movies.forEach(next -> movieTitles.add(next.getName()));
		}
		finally {
			// Make sure the unlock is in a finally block.
			locker.readLock().unlock();
		}
		
		return movieTitles;
	}

	@Override
	public boolean delete(String name) {

		final boolean existedBeforeDelete = findByName(name) != null;
		Predicate<Movie> p = movie -> movie.getName().equals(name);

		try {
			locker.writeLock().lock();
		    database.values().forEach(list -> list.removeIf(p));
		}
		finally {
			// Make sure the unlock is in a finally block.
			locker.writeLock().unlock();
		}

		return existedBeforeDelete;
	}
}