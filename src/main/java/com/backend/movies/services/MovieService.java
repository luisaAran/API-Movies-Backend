package com.backend.movies.services;

import com.backend.movies.dto.MovieCreateDTO;
import com.backend.movies.dto.MovieUpdateDTO;
import com.backend.movies.exceptions.DuplicatedResourceException;
import com.backend.movies.exceptions.InvalidScoreException;
import com.backend.movies.exceptions.ResourceNotFoundException;
import com.backend.movies.models.Movie;
import com.backend.movies.repositories.MovieRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    public static final double MIN_RATING = 0.0;
    public static final double MAX_RATING = 10.0;
    private final MovieRepository movieRepository;
    public MovieService(MovieRepository repository){
        this.movieRepository = repository;
    }
    public List<Movie> findAll() {
        return movieRepository.findAll();
    }
    public Movie createMovie(MovieCreateDTO movie) {
        Optional<Movie> existingMovie = movieRepository.findByTitle(movie.getTitle());
        if(existingMovie.isPresent()){
            throw new DuplicatedResourceException("La pelicula con el titulo: "
                    + movie.getTitle() + " ya existe");
        }
        if(movie.getVotes() == null){
            movie.setVotes(0);
        }
        if(movie.getRating() == null){
            movie.setRating(0.0);
        }
        if(movie.getDescription() == null){
            movie.setDescription("No description available");
        }
        if(movie.getRating() != null){
            if(movie.getRating() < MIN_RATING || movie.getRating() > MAX_RATING){
                throw new InvalidScoreException("La calificacion debe estar entre "
                        + MIN_RATING + " y " + MAX_RATING);
            }
        }
        Movie newMovie = Movie.builder()
                .votes(movie.getVotes())
                .description(movie.getDescription())
                .imageUrl(movie.getImageUrl())
                .rating(movie.getRating())
                .title(movie.getTitle())
                .year(movie.getYear())
                .build();
        return movieRepository.save(newMovie);
    }
    public Movie findById(Long id) {
        Optional<Movie> movie = movieRepository.findById(id);
        if(movie.isEmpty()){
            throw new ResourceNotFoundException("No se encontró la pelicula");
        }
        return movie.get();
    }
    public void deleteById(Long id){
        if(!movieRepository.existsById(id)){
            throw new ResourceNotFoundException("No se encontró la pelicula");
        } else {
            movieRepository.deleteById(id);
        }
    }
    public Movie updateMovieById(Long id, MovieUpdateDTO dto) {
        Movie movie = this.findById(id);
        if(dto.getTitle() != null){
            if(movieRepository.existsByTitle(dto.getTitle()) &&
                    !movie.getTitle().equals(dto.getTitle())){
                throw new DuplicatedResourceException("La pelicula con el titulo: "
                        + dto.getTitle() + " ya existe");
            }
            movie.setTitle(dto.getTitle());
        }
        if(dto.getYear() != null){
            movie.setYear(dto.getYear());
        }
        if(dto.getVotes() != null){
            movie.setVotes(dto.getVotes());
        }
        if(dto.getRating() != null){
            movie.setRating(dto.getRating());
        }
        if(dto.getDescription() != null){
            movie.setDescription(dto.getDescription());
        }
        if(dto.getImageUrl() != null){
            movie.setImageUrl(dto.getImageUrl());
        }
        return movieRepository.save(movie);
    }

    public Movie rateMovie(Long id, double rating) {
        if(rating < MIN_RATING || rating > MAX_RATING){
            throw new InvalidScoreException("La calificacion debe estar entre "
                    + MIN_RATING + " y " + MAX_RATING);
        }
        Movie movie = this.findById(id);
        double newRating = ((movie.getVotes() * movie.getRating()) + rating) / (movie.getVotes() + 1);
        movie.setVotes(movie.getVotes() + 1);
        movie.setRating(newRating);
        return this.movieRepository.save(movie);
    }
}
