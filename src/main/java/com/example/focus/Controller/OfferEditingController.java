package com.example.focus.Controller;

import com.example.focus.ApiResponse.ApiResponse;
import com.example.focus.DTO.OfferEditingInputDTO;
import com.example.focus.DTO.OfferEditingOutputDTO;
import com.example.focus.Model.MyUser;
import com.example.focus.Service.OfferEditingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/focus/offer-editing")
@RequiredArgsConstructor
public class OfferEditingController {

    private final OfferEditingService offerEditingService;

    //admin
    @GetMapping("/get-all")
    public ResponseEntity getAllOffers(@AuthenticationPrincipal MyUser user) {
        List<OfferEditingOutputDTO> offers = offerEditingService.getAllOffers();
        return ResponseEntity.status(200).body(offers);
    }

    //photographer
    @GetMapping("/get-by-id/{offerid}")
    public ResponseEntity getOfferById(@AuthenticationPrincipal MyUser myUser,@PathVariable Integer offerid) {
        OfferEditingOutputDTO offer = offerEditingService.getOfferById(myUser.getId(),offerid);
        return ResponseEntity.status(200).body(offer);
    }

    //editor
    @GetMapping("/editor/get-my-offer")
    public ResponseEntity getMyOffer(@AuthenticationPrincipal MyUser myUser) {
        return ResponseEntity.status(200).body(offerEditingService.getMyOffer(myUser.getId()));
    }

    //editor
    @GetMapping("/get-by-request/{requestid}")
    public ResponseEntity getByRequestEditorOffers( @AuthenticationPrincipal MyUser myUser,@PathVariable Integer requestid) {
        return ResponseEntity.status(200).body(offerEditingService.getByRequestEditor(myUser.getId(),requestid));
    }

    //photographer
    @GetMapping("/get-by-photographer")
    public ResponseEntity getPhotographerOffers(@AuthenticationPrincipal MyUser user) {
        return ResponseEntity.status(200).body(offerEditingService.getPhotographerOffers(user.getId()));
    }



    @PostMapping("/create")
    public ResponseEntity createOffer(@RequestBody @Valid OfferEditingInputDTO offerInput, @AuthenticationPrincipal MyUser user) {
        OfferEditingOutputDTO createdOffer = offerEditingService.createOffer(offerInput, user.getId());
        return ResponseEntity.status(200).body(createdOffer);
    }

    //editor
    @PutMapping("/update/{id}")
    public ResponseEntity updateOffer(@PathVariable Integer id, @RequestBody @Valid OfferEditingInputDTO offerInput,@AuthenticationPrincipal MyUser user) {
        OfferEditingOutputDTO updatedOffer = offerEditingService.updateOffer(id, offerInput,user.getId());
        return ResponseEntity.status(200).body(updatedOffer);
    }

    //editor
    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteOffer(@PathVariable Integer id,@AuthenticationPrincipal MyUser user) {
        offerEditingService.deleteOffer(id,user.getId());
        return ResponseEntity.status(200).body(new ApiResponse("Offer deleted successfully"));
    }

    //photog
    @PutMapping("/accept/{offerId}")
    public ResponseEntity acceptOffer(@PathVariable Integer offerId, @AuthenticationPrincipal MyUser user) {
        OfferEditingOutputDTO acceptedOffer = offerEditingService.acceptOffer(offerId, user.getId());
        return ResponseEntity.status(200).body(acceptedOffer);
    }

    //photog
    @PutMapping("/reject/{offerId}")
    public ResponseEntity rejectOffer(@PathVariable Integer offerId,@AuthenticationPrincipal MyUser user) {
        OfferEditingOutputDTO rejectedOffer = offerEditingService.rejectOffer(offerId, user.getId());
        return ResponseEntity.status(200).body(rejectedOffer);
    }


}
