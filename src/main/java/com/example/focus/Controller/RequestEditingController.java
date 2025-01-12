package com.example.focus.Controller;

import com.example.focus.ApiResponse.ApiResponse;
import com.example.focus.DTO.RequestEditingInputDTO;
import com.example.focus.DTO.RequestEditingOutputDTO;
import com.example.focus.Model.MyUser;
import com.example.focus.Service.RequestEditingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/focus/request-editing")
@RequiredArgsConstructor
public class RequestEditingController {

    private final RequestEditingService requestEditingService;

    @GetMapping("/get-all")
    public ResponseEntity getAllRequests() {
        List<RequestEditingOutputDTO> requests = requestEditingService.getAllRequests();
        return ResponseEntity.status(200).body(requests);
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity getRequestById(@PathVariable Integer id, @AuthenticationPrincipal Integer editorId) {
        return ResponseEntity.status(200).body(requestEditingService.getRequestByIdGeneral(id, editorId));
    }

    @GetMapping("/get-by-id-active/{id}")
    public ResponseEntity getActiveRequestById(@PathVariable Integer id, @AuthenticationPrincipal MyUser myUser) {
        return ResponseEntity.status(200).body(requestEditingService.getRequestByIdActive(id, myUser.getId()));
    }

    @GetMapping("/get-editor-requests")
    public ResponseEntity getEditorRequests(@AuthenticationPrincipal MyUser myUser) {
        return ResponseEntity.status(200).body(requestEditingService.getEditorRequests(myUser.getId()));
    }

    @GetMapping("/get-awaiting-offer-editor-requests")
    public ResponseEntity getAwaitingOfferRequests(@AuthenticationPrincipal MyUser myUser) {
        return ResponseEntity.status(200).body(requestEditingService.getAwaitingOfferRequestsForEditor(myUser.getId()));
    }

    @GetMapping("/get-photographer-requests")
    public ResponseEntity getPhotographerRequests(@AuthenticationPrincipal MyUser myUser) {
        return ResponseEntity.status(200).body(requestEditingService.getPhotographerRequests(myUser.getId()));
    }

    @PostMapping("/create/{editorId}")
    public ResponseEntity createRequest(@ModelAttribute @Valid RequestEditingInputDTO requestInput, @RequestParam("files") MultipartFile[] files,
                                        @PathVariable Integer editorId, @AuthenticationPrincipal MyUser myUser) {

        RequestEditingOutputDTO createdRequest=new RequestEditingOutputDTO();
        try {
           createdRequest = requestEditingService.createRequest(requestInput,files, editorId, myUser.getId());

        }  catch (IOException e) {
            return ResponseEntity.status(500).body(new ApiResponse("Error occurred while uploading the file."));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Request Created successfully."));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateRequest(@PathVariable Integer id, @RequestBody @Valid RequestEditingInputDTO requestInput,
                                        @AuthenticationPrincipal MyUser myUser) {
        RequestEditingOutputDTO updatedRequest = requestEditingService.updateRequest(id, requestInput);
        return ResponseEntity.status(200).body(updatedRequest);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteRequest(@PathVariable Integer id,@AuthenticationPrincipal MyUser myUser) {
        requestEditingService.deleteRequest(id);
        return ResponseEntity.status(200).body(new ApiResponse("Request deleted successfully"));
    }

    @PutMapping("/reject/{requestId}")
    public ResponseEntity rejectRequest(@PathVariable Integer requestId,@AuthenticationPrincipal MyUser myUser) {
        RequestEditingOutputDTO rejectedRequest = requestEditingService.rejectRequest(requestId);
        return ResponseEntity.status(200).body(rejectedRequest);
    }

    @PutMapping("/mark-complete/{id}")
    public ResponseEntity markAsComplete(@PathVariable Integer id, @AuthenticationPrincipal MyUser myUser) {
        RequestEditingOutputDTO completedRequest = requestEditingService.markAsCompleted(id, myUser.getId());
        return ResponseEntity.status(200).body(completedRequest);
    }
}
