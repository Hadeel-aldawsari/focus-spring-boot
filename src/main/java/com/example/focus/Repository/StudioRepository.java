package com.example.focus.Repository;

import com.example.focus.Model.Space;
import com.example.focus.Model.Studio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudioRepository extends JpaRepository<Studio, Integer> {
    Studio findStudioById(Integer id);
    List<Studio> findStudioByCity(String city);
    //Studio findStudioByUsername(String username);
    Studio findStudioByCommercialRecord(String commercialRecord);


}
