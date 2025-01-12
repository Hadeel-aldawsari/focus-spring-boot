package com.example.focus.Controller;

import com.example.focus.ApiResponse.ApiResponse;
import com.example.focus.DTO.PhotographerDTOin;
import com.example.focus.DTO.ToolDTO;
import com.example.focus.Model.MyUser;
import com.example.focus.Service.PhotographerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/focus/photographer")
@RequiredArgsConstructor
public class PhotographerController {

    private final PhotographerService photographerService;

    @GetMapping("/get-all")
    public ResponseEntity getAllPhotographer(){
        return ResponseEntity.status(200).body(photographerService.getAllPhotographers());
    }

    @PostMapping("/register")
    public ResponseEntity PhotographerRegistration(@RequestBody @Valid PhotographerDTOin photographer){
        photographerService.PhotographerRegistration(photographer);
        return ResponseEntity.status(200).body(new ApiResponse("photographer added"));
    }

    @PutMapping("/update-photographer")
    public ResponseEntity updatePhotographer(@AuthenticationPrincipal MyUser myUser, @RequestBody @Valid PhotographerDTOin photographer){
        photographerService.updatePhotographer(myUser.getId(),photographer);
        return ResponseEntity.status(200).body(new ApiResponse("photographer updated"));
    }

    @DeleteMapping("/delete-photographer")
    public ResponseEntity deletePhotographer(@AuthenticationPrincipal MyUser myUser){
        photographerService.deletePhotographer(myUser.getId());
        return ResponseEntity.status(200).body(new ApiResponse("photographer deleted"));
    }

    @GetMapping("/get-my-rent-tools")
    public ResponseEntity viewMyRentTools(@AuthenticationPrincipal MyUser myUser){
        List<ToolDTO> rentTools = photographerService.viewMyRentTools(myUser.getId());
        return ResponseEntity.status(200).body(rentTools);
    }

    @GetMapping("/get-my-rental-tools")
    public ResponseEntity viewRentalTools(@AuthenticationPrincipal MyUser myUser){
        List<ToolDTO> rentTools = photographerService.viewRentalTools(myUser.getId());
        return ResponseEntity.status(200).body(rentTools);
    }



}