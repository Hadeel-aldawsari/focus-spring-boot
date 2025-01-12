package com.example.focus.Repository;

import com.example.focus.Model.ProfilePhotographer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfilePhotographerRepository extends JpaRepository<ProfilePhotographer,Integer> {

    ProfilePhotographer findProfileById(Integer id);
    ProfilePhotographer findProfilePhotographerById(Integer id);
}