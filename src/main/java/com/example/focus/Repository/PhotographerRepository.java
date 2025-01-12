package com.example.focus.Repository;

import com.example.focus.Model.Photographer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotographerRepository extends JpaRepository<Photographer,Integer> {

    Photographer findPhotographersById(Integer id);

}