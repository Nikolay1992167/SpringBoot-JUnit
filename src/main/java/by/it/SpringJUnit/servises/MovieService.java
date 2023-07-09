package by.it.SpringJUnit.servises;

import by.it.SpringJUnit.model.Movie;
import by.it.SpringJUnit.repositories.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    public Movie save(Movie movie){
        return movieRepository.save(movie);
    }

    public List<Movie> getAllMovies(){
        return movieRepository.findAll();
    }

    public Movie getMovieById(Long id){
        return movieRepository.findById(id).orElseThrow(()->new RuntimeException("Movie not found with id"+id));
    }

    public Movie updateMovie(Movie movie, Long id){
        Movie existingMovie = movieRepository.findById(id).get();
        existingMovie.setGenera(movie.getGenera());
        existingMovie.setName(movie.getName());
        existingMovie.setReleaseDate(movie.getReleaseDate());
        return movieRepository.save(existingMovie);
    }

    public void deleteMovie(Long id){
        Movie existingMovie = movieRepository.findById(id).get();
        movieRepository.delete(existingMovie);
    }
}
