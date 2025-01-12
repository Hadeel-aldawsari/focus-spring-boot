package com.example.focus.Repository;

import com.example.focus.Model.ProfileEditor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileEditorRepository extends JpaRepository<ProfileEditor, Integer> {
    ProfileEditor findProfileEditorById(int id);
}
