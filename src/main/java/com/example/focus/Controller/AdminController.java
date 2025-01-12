package com.example.focus.Controller;

import com.example.focus.ApiResponse.ApiResponse;
import com.example.focus.Model.MyUser;
import com.example.focus.Service.EmailService;
import lombok.RequiredArgsConstructor;
import com.example.focus.Service.StudioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/focus/admin")
public class AdminController {
    private final StudioService studioService;
    private final EmailService emailService;

    //admin
    @PutMapping("/activate-studio/{studio_id}")
    public ResponseEntity activateStudio(@AuthenticationPrincipal MyUser myuser, @PathVariable Integer studio_id){
        studioService.activateStudio(myuser.getId(), studio_id);
        return ResponseEntity.status(200).body(new ApiResponse("admin activate studio successfully"));
    }

    //admin
    @PutMapping("/reject-studio/{studio_id}")
    public ResponseEntity rejectStudio(@AuthenticationPrincipal MyUser myuser,@PathVariable Integer studio_id){
        studioService.rejectStudio(myuser.getId(), studio_id);
        return ResponseEntity.status(200).body(new ApiResponse("admin reject studio successfully"));
    }


}
