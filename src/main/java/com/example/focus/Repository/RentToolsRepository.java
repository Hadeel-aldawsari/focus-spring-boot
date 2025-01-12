package com.example.focus.Repository;

import com.example.focus.Model.RentTools;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentToolsRepository extends JpaRepository<RentTools,Integer> {
}
