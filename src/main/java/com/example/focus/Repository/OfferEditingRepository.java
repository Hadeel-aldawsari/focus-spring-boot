package com.example.focus.Repository;

import com.example.focus.Model.OfferEditing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfferEditingRepository extends JpaRepository<OfferEditing, Integer> {

    List<OfferEditing> findOfferEditingByEditor_IdAndStatus(Integer editorId, String status);
    List<OfferEditing> findOfferEditingByEditor_Id(Integer editorId);
    OfferEditing findOfferEditingById(Integer id);


    List<OfferEditing> findByRequestEditingId(Integer requestId);
}
