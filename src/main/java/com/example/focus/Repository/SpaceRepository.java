package com.example.focus.Repository;

import com.example.focus.Model.Space;
import com.example.focus.Model.Studio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpaceRepository extends JpaRepository<Space, Integer> {
    Space findSpaceById(Integer id);
    Space findSpaceByName(String name);
    List<Space> findSpaceByStudio(Studio s);
}
