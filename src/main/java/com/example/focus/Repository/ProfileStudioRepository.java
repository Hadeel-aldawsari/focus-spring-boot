package com.example.focus.Repository;

import com.example.focus.Model.ProfileStudio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileStudioRepository extends JpaRepository<ProfileStudio, Integer> {
    ProfileStudio findProfileStudioById(int id);
}
