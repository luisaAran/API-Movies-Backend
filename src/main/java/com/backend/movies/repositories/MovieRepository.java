/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.backend.movies.repositories;

import com.backend.movies.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MovieRepository extends JpaRepository<Movie, Long> {
    
}
