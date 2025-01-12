package com.example.focus.Controller;


import com.example.focus.ApiResponse.ApiResponse;
import com.example.focus.DTO.ProfileDTOin;
import com.example.focus.Model.MyUser;
import com.example.focus.Service.ProfileEditorService;
import com.example.focus.Service.ProfilePhotographerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/focus/editor/profile")
@RequiredArgsConstructor
public class ProfileEditorController {

    private final ProfileEditorService profileEditorService;


    @GetMapping("/get-all")
    public ResponseEntity getAllProfileEditor(){
        return ResponseEntity.status(200).body(profileEditorService.getAllEditorsProfiles()) ;
    }

    @GetMapping("/get-my-profile")
    public ResponseEntity getMyProfileEditor(@AuthenticationPrincipal MyUser myUser){
        return ResponseEntity.status(200).body(profileEditorService.getMyProfile(myUser.getId()));
    }

    @GetMapping("/get-specific-profile/{id}")
    public ResponseEntity getSpecificProfileEditor(@PathVariable Integer id,  @AuthenticationPrincipal MyUser myUser){
        return ResponseEntity.status(200).body(profileEditorService.getSpecificProfile(myUser.getId(),id));
    }


    @PostMapping("/upload-media")
    public ResponseEntity uploadMedia(@AuthenticationPrincipal MyUser myUser, @RequestParam("file") MultipartFile file) {
        try {
            profileEditorService.uploadEditorMedia(myUser.getId(),file);

        }  catch (IOException e) {
            return ResponseEntity.status(500).body("Error occurred while uploading the file.");
        }
        return ResponseEntity.status(200).body(new ApiResponse("Upload successful."));
    }


    @PutMapping("/update")
    public ResponseEntity updateProfile(@AuthenticationPrincipal MyUser myUser, @ModelAttribute @Valid ProfileDTOin profileDTOin, @RequestParam("file") MultipartFile file){
        try {
            profileEditorService.updateEditorProfile(myUser.getId(),profileDTOin,file);

        }  catch (IOException e) {
            return ResponseEntity.status(500).body("Error occurred while uploading the file.");
        }
        return ResponseEntity.status(200).body(new ApiResponse("updated successful."));
    }
}
