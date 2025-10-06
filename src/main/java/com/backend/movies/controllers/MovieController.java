/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.backend.movies.controllers;
import com.backend.movies.dto.MovieCreateDTO;
import com.backend.movies.dto.MovieUpdateDTO;
import com.backend.movies.models.Movie;
import java.util.List;
import com.backend.movies.services.MovieService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/movies")
public class MovieController {
    private final MovieService movieService;
    public MovieController(MovieService movieService){
        this.movieService = movieService;
    }
    @CrossOrigin
    @GetMapping
    public List<Movie> getAllMovies() {
        return movieService.findAll();
    }
    @CrossOrigin
    @PostMapping
    public ResponseEntity<Movie> createMovie(@RequestBody @Valid MovieCreateDTO dto) {
        Movie savedMovie = movieService.createMovie(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMovie);
    }
    @CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.findById(id));
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id){
        movieService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @CrossOrigin
    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @RequestBody @Valid MovieUpdateDTO dto){
        Movie updatedMovie = movieService.updateMovieById(id, dto);
        return ResponseEntity.ok(updatedMovie);
    }

    @CrossOrigin
    @PutMapping("/vote/{id}")
    public ResponseEntity<Movie> voteMovie(@PathVariable Long id, @RequestBody double rating){
        return ResponseEntity.ok(movieService.rateMovie(id, rating));
    }

    
}
