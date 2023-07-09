package by.it.SpringJUnit.repositories;

import by.it.SpringJUnit.model.Movie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    private Movie avatarMovie;
    private Movie titanicMovie;

    @BeforeEach
    void init() {
        avatarMovie = new Movie();
        avatarMovie.setName("Avatar");
        avatarMovie.setGenera("Action");
        avatarMovie.setReleaseDate(LocalDate.of(2000, Month.APRIL, 22));

        titanicMovie = new Movie();
        avatarMovie.setName("Titanic");
        avatarMovie.setGenera("Romans");
        avatarMovie.setReleaseDate(LocalDate.of(1999, Month.MAY, 20));
    }

    @Test
    @DisplayName("It should save the movie to the database")
    void save() {
        //Act
        Movie newMovie = movieRepository.save(avatarMovie);
        //Assert
        Assertions.assertNotNull(newMovie);
        Assertions.assertNotEquals(newMovie.getId(), null);
    }

    @Test
    @DisplayName("It should return the movies list with size of 2")
    void getAllMovies() {
        movieRepository.save(avatarMovie);
        movieRepository.save(titanicMovie);

        List<Movie> list = movieRepository.findAll();

        Assertions.assertNotNull(list);
        Assertions.assertEquals(2, list.size());
    }

    @Test
    @DisplayName("It should return the movie by its id")
    void getMovieById() {
       movieRepository.save(avatarMovie);

        Movie excitingMovie = movieRepository.findById(avatarMovie.getId()).get();

        Assertions.assertNotNull(excitingMovie);
        Assertions.assertEquals("Action", excitingMovie.getGenera());
        assertThat(avatarMovie.getReleaseDate().isBefore(LocalDate.of(2000, Month.APRIL, 23)));
    }

    @Test
    @DisplayName("It should update the movie with genera FANTASY")
    void updateMovie() {
       movieRepository.save(avatarMovie);

        Movie excitingMovie = movieRepository.findById(avatarMovie.getId()).get();
        excitingMovie.setGenera("FANTASY");
        Movie newMovie = movieRepository.save(excitingMovie);

        Assertions.assertEquals("FANTASY", newMovie.getGenera());
        Assertions.assertEquals("Avatar", newMovie.getName());
    }

    @Test
    @DisplayName("It should delete the existing movie")
    void deleteMovie() {
        movieRepository.save(avatarMovie);
        Long id = avatarMovie.getId();
       movieRepository.save(titanicMovie);

        movieRepository.delete(avatarMovie);
        Optional<Movie> exitingMovie = movieRepository.findById(id);
        List<Movie> list = movieRepository.findAll();
        Assertions.assertEquals(1, list.size());
        assertThat(exitingMovie).isEmpty();
    }

    @Test
    @DisplayName("It should return the movies list with genera ROMANCE")
    void getMovieByGenera() {
       movieRepository.save(avatarMovie);
        Long id = avatarMovie.getId();

       movieRepository.save(titanicMovie);

        List<Movie> list = movieRepository.findByGenera("Romance");

        Assertions.assertNotNull(list);
        assertThat(list.size()).isEqualTo(1);
    }
}
