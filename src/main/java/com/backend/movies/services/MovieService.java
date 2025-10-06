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
/**
 * Servicio para manejar las operaciones relacionadas con las películas.
 * Proporciona métodos para crear, actualizar, eliminar y buscar películas.
 */
public class MovieService {
    /**
     * Constantes para la calificación mínima y máxima permitida.
     */
    public static final double MIN_RATING = 0.0;
    public static final double MAX_RATING = 10.0;
    /**
     * Repositorio de películas inyectado en el servicio.
     */
    private final MovieRepository movieRepository;

    /**
     * Constructor que inyecta el repositorio de películas.
     * @param repository
     */
    public MovieService(MovieRepository repository){
        this.movieRepository = repository;
    }

    /**
     * Método para obtener todas las películas.
     * @return
     */
    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    /**
     * Método para crear una nueva película.
     * Valida que no exista una película con el mismo título
     * @param movie
     * @return
     */
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

    /**
     * Método para buscar una película por su ID.
     * Lanza una excepción si no se encuentra la película.
     * @param id
     * @return
     */
    public Movie findById(Long id) {
        Optional<Movie> movie = movieRepository.findById(id);
        if(movie.isEmpty()){
            throw new ResourceNotFoundException("No se encontró la pelicula");
        }
        return movie.get();
    }

    /**
     * Método para eliminar una película por su ID.
     * Lanza una excepción si no se encuentra la película.
     * @param id
     */
    public void deleteById(Long id){
        if(!movieRepository.existsById(id)){
            throw new ResourceNotFoundException("No se encontró la pelicula");
        } else {
            movieRepository.deleteById(id);
        }
    }

    /**
     * Método para actualizar una película por su ID.
     * Valida que no exista otra película con el mismo título
     * y que la calificación esté dentro del rango permitido.
     * @param id
     * @param dto
     * @return
     */
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
            if(dto.getRating() < MIN_RATING || dto.getRating() > MAX_RATING){
                throw new InvalidScoreException("La calificacion debe estar entre "
                        + MIN_RATING + " y " + MAX_RATING);
            }
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

    /**
     * Método para calificar una película.
     * Valida que la calificación esté dentro del rango permitido
     * @param id
     * @param rating
     * @return
     */
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
