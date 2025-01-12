package com.example.focus.Controller;

import com.example.focus.ApiResponse.ApiResponse;
import com.example.focus.DTO.RentToolsDTOIn;
import com.example.focus.DTO.ToolDTO;
import com.example.focus.DTO.ToolDTOIn;
import com.example.focus.Model.MyUser;
import com.example.focus.Model.Tool;
import com.example.focus.Service.PhotographerService;
import com.example.focus.Service.ToolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/focus/tool")
@RequiredArgsConstructor
public class ToolController {
    private final ToolService toolService;
    private final PhotographerService photographerService;

    @GetMapping("/get-all")
    public ResponseEntity getAllTools() {
        List<ToolDTO> tools = toolService.getAllTools();
        return ResponseEntity.status(200).body(tools);
    }

    @PostMapping("/add-tool")
    public ResponseEntity addTool(@AuthenticationPrincipal MyUser myUser, @ModelAttribute  ToolDTOIn toolDTOIn,
                                  @RequestParam("file") MultipartFile file ) {
        try {
            toolService.addTool(myUser.getId(),toolDTOIn,file);

        }  catch (IOException e) {
            return ResponseEntity.status(500).body("Error occurred while uploading the file.");
        }
        return ResponseEntity.status(200).body(new ApiResponse("Tool added successfully"));
    }

    // need to media file
    @PutMapping("/update-tool/{photographer_id}/{tool_id}")
    public ResponseEntity updateTool(@AuthenticationPrincipal MyUser myUser,@PathVariable Integer tool_id,
                                     @ModelAttribute  ToolDTOIn toolDTOIn,@RequestParam("file") MultipartFile file) {
        try {
            toolService.updateTool(myUser.getId(),tool_id,toolDTOIn,file);

        }  catch (IOException e) {
            return ResponseEntity.status(500).body("Error occurred while uploading the file.");
        }
        return ResponseEntity.status(200).body(new ApiResponse("Tool updated successfully"));
    }

    @DeleteMapping("/delete-tool/photographerid/toolid/{tool_id}")
    public ResponseEntity<ApiResponse> deleteTool(@AuthenticationPrincipal MyUser myUser,@PathVariable Integer tool_id) {
        toolService.deleteTool(myUser.getId(),tool_id);
        return ResponseEntity.status(200).body(new ApiResponse("Tool deleted successfully"));
    }

    @PostMapping("/rent-tool/{tool_id}")
    public ResponseEntity rentToolRequest(@AuthenticationPrincipal MyUser myUser,@PathVariable Integer tool_id,
                                          @RequestBody @Valid RentToolsDTOIn rentToolsDTOIn){
        photographerService.rentToolRequest(myUser.getId(),tool_id,rentToolsDTOIn);
        return ResponseEntity.status(200).body(new ApiResponse("Tool rent request success"));
    }

    @GetMapping("/get-tools-by-rental-number/{numberOfRental}")
    public ResponseEntity getToolsByNumberOfRental(@PathVariable Integer numberOfRental) {
        List<ToolDTO> tools = toolService.getToolsByNumberOfRental(numberOfRental);
        return ResponseEntity.status(200).body(tools);
    }

    @GetMapping("/get-tools-by-rental-number-or-above/{numberOfRental}")
    public ResponseEntity getToolsByNumberOfRentalOrAbove(@PathVariable Integer numberOfRental) {
        List<ToolDTO> tools = toolService.getToolsByNumberOfRentalOrAbove(numberOfRental);
        return ResponseEntity.status(200).body(tools);
    }

    @GetMapping("/get-tools-by-rental-number-or-below/{numberOfRental}")
    public ResponseEntity getToolsByNumberOfRentalOrBelow(@PathVariable Integer numberOfRental) {
        List<ToolDTO> tools = toolService.getToolsByNumberOfRentalOrBelow(numberOfRental);
        return ResponseEntity.status(200).body(tools);
    }

    @GetMapping("/get-my-tools")
    public ResponseEntity getMyTools(@AuthenticationPrincipal MyUser myUser) {
        List<ToolDTO> tools = toolService.getMyTools(myUser.getId());
        return ResponseEntity.status(200).body(tools);
    }

    @GetMapping("/get-photographer-tools/{photographer_id2}")
    public ResponseEntity getPhotographerTools(@AuthenticationPrincipal MyUser myUser,@PathVariable Integer photographer_id2) {
        List<ToolDTO> tools = toolService.getPhotographerTools(myUser.getId(),photographer_id2);
        return ResponseEntity.status(200).body(tools);
    }
}