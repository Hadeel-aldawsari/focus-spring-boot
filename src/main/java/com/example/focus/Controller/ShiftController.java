package com.example.focus.Controller;

import com.example.focus.ApiResponse.ApiResponse;
import com.example.focus.DTO.ShiftDTOIn;
import com.example.focus.Model.MyUser;
import com.example.focus.Service.ShiftService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/focus/shift")
@RequiredArgsConstructor
public class ShiftController {

    private final ShiftService shiftService;

    @GetMapping("/get-all")
    public ResponseEntity getAllShifts() {
        return ResponseEntity.status(200).body(shiftService.getAllShifts());
    }

    @GetMapping("/get-shift/{id}")
    public ResponseEntity getShiftById(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(shiftService.getShiftById(id));
    }

    @PostMapping("/create-shift")
    public ResponseEntity createShift(@RequestBody @Valid ShiftDTOIn shiftDTOIn,@AuthenticationPrincipal MyUser myUser) {
        shiftService.createShift(shiftDTOIn,myUser.getId());
        return ResponseEntity.status(200).body(new ApiResponse("Shift created successfully"));
    }

    @PutMapping("/update-status/{id}/{status}")
    public ResponseEntity updateShiftStatus(@PathVariable Integer id, @PathVariable String status,@AuthenticationPrincipal MyUser myUser) {
        shiftService.updateShiftStatus(id, status,myUser.getId());
        return ResponseEntity.status(200).body(new ApiResponse("Shift status updated successfully"));
    }

    @DeleteMapping("/delete-shift/{id}")
    public ResponseEntity deleteShift(@PathVariable Integer id,@AuthenticationPrincipal MyUser myUser) {
        shiftService.deleteShift(id,myUser.getId());
        return ResponseEntity.status(200).body(new ApiResponse("Shift deleted successfully"));
    }

    @GetMapping("/get-by-space/{spaceId}")
    public ResponseEntity getShiftsBySpaceId(@PathVariable Integer spaceId) {
        return ResponseEntity.status(200).body(shiftService.getShiftsBySpaceId(spaceId));
    }
}
