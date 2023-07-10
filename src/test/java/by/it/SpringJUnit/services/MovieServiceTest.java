package by.it.SpringJUnit.services;

import by.it.SpringJUnit.model.Movie;
import by.it.SpringJUnit.repositories.MovieRepository;
import by.it.SpringJUnit.servises.MovieService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

    @InjectMocks
    private MovieService movieService;

    @Mock
    private MovieRepository movieRepository;

    private Movie avatarMovie;
    private Movie titanicMovie;

    @BeforeEach
    void init() {
        avatarMovie = new Movie();
        avatarMovie.setId(1L);
        avatarMovie.setName("Avatar");
        avatarMovie.setGenera("Action");
        avatarMovie.setReleaseDate(LocalDate.of(2000, Month.APRIL, 22));

        titanicMovie = new Movie();
        avatarMovie.setId(2L);
        avatarMovie.setName("Titanic");
        avatarMovie.setGenera("Romans");
        avatarMovie.setReleaseDate(LocalDate.of(1999, Month.MAY, 20));
    }

    @Test
    @DisplayName("Should save the movie object to database")
    void save() {
        when(movieRepository.save(any(Movie.class))).thenReturn(avatarMovie);
        Movie newMovie = movieService.save(avatarMovie);
        Assertions.assertNotNull(newMovie);
        assertThat(newMovie.getName()).isEqualTo("Avatar");
    }

    @Test
    @DisplayName("Should return list of movies with size 2")
    void getMovies() {
        List<Movie> list = new ArrayList<>();
        list.add(avatarMovie);
        list.add(titanicMovie);

        when(movieRepository.findAll()).thenReturn(list);

        List<Movie> movies = movieService.getAllMovies();

        Assertions.assertEquals(2, movies.size());
        Assertions.assertNotNull(movies);
    }

    @Test
    @DisplayName("Should return the Movie object")
    void getMovieById() {
        when(movieRepository.findById(anyLong())).thenReturn(Optional.of(avatarMovie));
        Movie existingMovie = movieService.getMovieById(avatarMovie.getId());
        Assertions.assertNotNull(existingMovie);
        assertThat(existingMovie.getId()).isNotEqualTo(null);
    }

    @Test
    @DisplayName("Should throw the Exception")
    void getMovieByIdForException() {
        when(movieRepository.findById(2L)).thenReturn(Optional.of(avatarMovie));
        Assertions.assertThrows(RuntimeException.class, () -> {
            movieService.getMovieById(avatarMovie.getId());
        });
    }

    @Test
    @DisplayName("Should update the movie into the database")
    void updateMovie(){
        when(movieRepository.findById(anyLong())).thenReturn(Optional.of(avatarMovie));

        when(movieRepository.save(any(Movie.class))).thenReturn(avatarMovie);
        avatarMovie.setGenera("Fantacy");
        Movie exisitingMovie = movieService.updateMovie(avatarMovie, avatarMovie.getId());

        Assertions.assertNotNull(exisitingMovie);
        Assertions.assertEquals("Fantacy", avatarMovie.getGenera());
    }

    @Test
    @DisplayName("Should delete the movie from database")
    void deleteMovie(){
        Long movieId = 1L;
        when(movieRepository.findById(anyLong())).thenReturn(Optional.of(avatarMovie));
        doNothing().when(movieRepository).delete(any(Movie.class));

        movieService.deleteMovie(movieId);

        verify(movieRepository, times(1)).delete(avatarMovie);
    }
}
