package com.example.focus.Repository;

import com.example.focus.Model.BookSpace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookSpaceRepository extends JpaRepository<BookSpace, Integer> {

    List<BookSpace> findByPhotographerId(Integer photographerId);

    BookSpace findBookByShiftId(Integer shiftId);

    List<BookSpace> findByStatus(String status);
}
