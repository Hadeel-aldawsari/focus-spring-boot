package com.example.focus.Controller;


import com.example.focus.ApiResponse.ApiResponse;
import com.example.focus.DTO.EditorDTO;
import com.example.focus.DTO.EditorDTOin;
import com.example.focus.Model.Editor;
import com.example.focus.Model.MyUser;
import com.example.focus.Service.EditorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/focus/editor")
@RequiredArgsConstructor
public class EditorController {
    private final EditorService editorService;

    //primitive all
    @GetMapping("/get-all")
    public ResponseEntity getAllEditors() {
        List<EditorDTO> editorDTOS = editorService.getAllEditors();
        return ResponseEntity.ok(editorDTOS);
    }

    //primitive all
    @PostMapping("/register")
    public ResponseEntity EditorRegistration(@RequestBody @Valid EditorDTOin editor) {
        editorService.EditorRegistration(editor);
        return ResponseEntity.ok(new ApiResponse("Editor added successfully"));
    }

    //Editor
    @PutMapping("/update-editor")
    public ResponseEntity updateEditor(@AuthenticationPrincipal MyUser myUser, @RequestBody @Valid EditorDTOin editor) {
        editorService.updateEditor(myUser.getId(), editor);
        return ResponseEntity.ok(new ApiResponse("Editor updated successfully"));
    }

    //admin
    @DeleteMapping("/delete-editor/{editorid}")
    public ResponseEntity deleteEditor(@AuthenticationPrincipal MyUser myUser,@PathVariable Integer editorid) {
        editorService.deleteEditor(myUser.getId(),editorid);
        return ResponseEntity.ok(new ApiResponse("Editor deleted successfully"));
    }
}