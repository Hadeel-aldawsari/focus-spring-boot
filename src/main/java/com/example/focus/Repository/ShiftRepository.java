package com.example.focus.Repository;

import com.example.focus.Model.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Integer> {
    List<Shift> findBySpaceId(Integer spaceId);
    Shift findShiftById(Integer id);

    Shift findShiftBySpaceIdAndDateAndName(Integer spaceId, LocalDateTime date, String name);

    List<Shift> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);

    List<Shift> findByStatus(String status);
}
