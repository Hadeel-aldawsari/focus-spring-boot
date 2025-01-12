package com.example.focus.Controller;

import com.example.focus.ApiResponse.ApiResponse;
import com.example.focus.DTO.*;
import com.example.focus.Model.MyUser;
import com.example.focus.Service.StudioService;
import com.example.focus.Service.PhotographerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/focus/studio")
public class StudioController {
    private final StudioService studioService;
    private final PhotographerService photographerService;

    @GetMapping("/get-all")
    public ResponseEntity getAllStudios(){
        List<StudioDTO> studios = studioService.getAllStudios();
        return ResponseEntity.status(200).body(studios);
    }

    @PostMapping("/register")
    public ResponseEntity registerStudio(@RequestBody @Valid StudioDTOIn studio){
        studioService.registerStudio(studio);
        return ResponseEntity.status(200).body(new ApiResponse("studio registered successfully"));
    }


    @PutMapping("/upload-image")
    public ResponseEntity updateProfile(@AuthenticationPrincipal MyUser myUser, @RequestParam("file") MultipartFile file){
        try {
            studioService.uploadImage(myUser.getId(),file);

        }  catch (IOException e) {
            return ResponseEntity.status(500).body("Error occurred while uploading the file.");
        }
        return ResponseEntity.status(200).body(new ApiResponse("updated successful."));
    }

    @GetMapping("/get-studio-by-city/{city}")
    public ResponseEntity getStudioByCity(@AuthenticationPrincipal MyUser myUser,@PathVariable String city){
        List<StudioDTOPhotographer> studioDTOS = studioService.getStudiosByCity(city);
        return ResponseEntity.status(200).body(studioDTOS);
    }

    @GetMapping("/get-specific-studio/{studio_id}")
    public ResponseEntity getSpecificStudio(@PathVariable Integer studio_id){
        StudioDTOPhotographer studioDTO = studioService.getSpecificStudio(studio_id);
        return ResponseEntity.status(200).body(studioDTO);
    }



}
