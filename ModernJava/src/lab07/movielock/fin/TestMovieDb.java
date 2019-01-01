package lab07.movielock.fin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import lab.util.Util;

public class TestMovieDb {

	private static final int NUMBER_OF_MOVIES = 10000;

	private static void test() {
		
		System.out.println("Performing tests...");
		MovieDb movieDb = new ThreadSafeMovieDb();
		
		List<Callable<Void>> writers = new ArrayList<>();
		
		for (Category nextCategory : Category.values()) {

			writers.add(() -> {
				for (int index = 0; index < NUMBER_OF_MOVIES; ++index) {
					movieDb.add(nextCategory, Integer.toString(index), 2018);

					// Thrown in to create contention
					movieDb.findByName(Integer.toString(index));
				}
				
				return null;
			});
		}
		
		ExecutorService executorService = Executors.newFixedThreadPool(10);

		try {
			for (Future<Void> nextFuture : executorService.invokeAll(writers)) {
				nextFuture.get();
			}
		}
		catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		finally {
			executorService.shutdown();
		}

		for (Category nextCategory : Category.values()) {
			int numberOfMovies = movieDb.findByCategory(nextCategory).size();
			Util.ensure(numberOfMovies == NUMBER_OF_MOVIES, "Expected dramas: " + NUMBER_OF_MOVIES + " - " + "returned: " + numberOfMovies);
		}
		
		System.out.println("All tests passed");
	}
	
	public static void main(String... args) {
		test();
	}
}
