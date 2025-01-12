package com.example.focus.Service;

import com.example.focus.ApiResponse.ApiException;
import com.example.focus.DTO.ToolDTO;
import com.example.focus.DTO.ToolDTOIn;
import com.example.focus.Model.MyUser;
import com.example.focus.Model.Photographer;
import com.example.focus.Model.Tool;
import com.example.focus.Repository.MyUserRepository;
import com.example.focus.Repository.PhotographerRepository;
import com.example.focus.Repository.ToolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ToolService {
    private final ToolRepository toolRepository;
    private final PhotographerRepository photographerRepository;
    private final MyUserRepository myUserRepository;
    private final EmailService emailService;


    public List<ToolDTO> getAllTools() {
        List<Tool> tools = toolRepository.findAll();
        List<ToolDTO> toolDTOS = new ArrayList<>();
        for (Tool tool : tools) {
            ToolDTO toolDTO = new ToolDTO(
                    tool.getName(),
                    tool.getDescription(),
                    tool.getCategory(),
                    tool.getModelNumber(),
                    tool.getNumberOfRentals(),
                    tool.getBrand(),
                    tool.getRentalPrice(),
                    tool.getImageURL()
            );
            toolDTOS.add(toolDTO);
        }
        return toolDTOS;
    }

    // photographer add tool

    private static final String UPLOAD_DIR = "C:/Users/doly/Desktop/Upload/Tool/";

    public void addTool(Integer photographer_id, ToolDTOIn toolDTOIn, MultipartFile file) throws IOException {
        MyUser myUser=myUserRepository.findMyUserById(photographer_id);
        if(myUser==null) {
            throw new ApiException("user Not Found");
        }
        List<Tool> tool = toolRepository.findAll();
        for (Tool tool1 : tool) {
            if(toolDTOIn.getModelNumber().equals(tool1.getModelNumber()) && tool1.getBrand().equals(toolDTOIn.getBrand())) {
                throw new ApiException("Tool Already Exist");
            }
        }

        if (!isValidImageFile(file)) {
            throw new ApiException("Invalid image file. Only JPG, PNG, and JPEG files are allowed");
        }


        Path filePath = Paths.get(UPLOAD_DIR.concat(saveImageFile(file)));


        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        String filePathString = filePath.toString();


        Tool newTool = new Tool();
        newTool.setName(toolDTOIn.getName());
        newTool.setNumberOfRentals(0);
        newTool.setPhotographer(myUser.getPhotographer());
        newTool.setModelNumber(toolDTOIn.getModelNumber());
        newTool.setImageURL(filePathString); // حفظ اسم الملف بدلاً من URL
        newTool.setBrand(toolDTOIn.getBrand());
        newTool.setDescription(toolDTOIn.getDescription());
        newTool.setCategory(toolDTOIn.getCategory());
        newTool.setRentalPrice(toolDTOIn.getRentalPrice());
        toolRepository.save(newTool);

    }



    public void updateTool(Integer photographer_id,Integer tool_id, ToolDTOIn tool,MultipartFile file)throws IOException{
        Photographer photographer = photographerRepository.findPhotographersById(photographer_id);
        if(photographer==null) {
            throw new ApiException("Photographer Not Found");
        }

        if (!isValidImageFile(file)) {
            throw new ApiException("Invalid image file. Only JPG, PNG, and JPEG files are allowed");
        }

        Path filePath = Paths.get(UPLOAD_DIR.concat(saveImageFile(file)));

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        String filePathString = filePath.toString();
        Tool existingTool = toolRepository.findToolById(tool_id);
        if (existingTool != null) {
            existingTool.setName(tool.getName());
            existingTool.setDescription(tool.getDescription());
            existingTool.setCategory(tool.getCategory());
            existingTool.setBrand(tool.getBrand());
            existingTool.setModelNumber(tool.getModelNumber());
            existingTool.setRentalPrice(tool.getRentalPrice());
            existingTool.setImageURL(filePathString);
            toolRepository.save(existingTool);
        } else {
            throw new ApiException("Tool not found");
        }
    }

    // التحقق من نوع الملف
    private boolean isValidImageFile(MultipartFile file) {
        String fileName = file.getOriginalFilename().toLowerCase();
        return fileName.endsWith(".jpg") || fileName.endsWith(".png") || fileName.endsWith(".jpeg");
    }

    // حفظ الصورة في المسار المحدد
    private String saveImageFile(MultipartFile file) throws IOException {
        String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        Path path = Paths.get(UPLOAD_DIR + fileName);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }


    public List<ToolDTO> getToolsByNumberOfRental(Integer rental_number) {
        List<Tool> tool = toolRepository.findToolByNumberOfRentals(rental_number);
        if(tool.isEmpty()) {
            throw new ApiException("Not have tool by this rental number");
        }
        List<ToolDTO> toolDTOS = new ArrayList<>();
        for (Tool tool1 : tool) {
            ToolDTO toolDTO = new ToolDTO(
                    tool1.getName(),
                    tool1.getDescription(),
                    tool1.getCategory(),
                    tool1.getModelNumber(),
                    tool1.getNumberOfRentals(),
                    tool1.getBrand(),
                    tool1.getRentalPrice(),
                    tool1.getImageURL()
            );
            toolDTOS.add(toolDTO);
        }
        return toolDTOS;
    }

    public List<ToolDTO> getToolsByNumberOfRentalOrAbove(Integer rental_number) {
        List<Tool> tool = toolRepository.findToolByNumberOfRentals(rental_number);

        if(tool.isEmpty()) {
            throw new ApiException("Not have tool by this rental number or above");
        }

        List<ToolDTO> toolDTOS = new ArrayList<>();
        for (Tool tool1 : tool) {
            if (tool1.getNumberOfRentals() >= rental_number) {
                ToolDTO toolDTO = new ToolDTO(
                        tool1.getName(),
                        tool1.getDescription(),
                        tool1.getCategory(),
                        tool1.getModelNumber(),
                        tool1.getNumberOfRentals(),
                        tool1.getBrand(),
                        tool1.getRentalPrice(),
                        tool1.getImageURL()
                );
                toolDTOS.add(toolDTO);
            }
        }
        if(toolDTOS.isEmpty()) {
            throw new ApiException("Not have tool by this rental number or above");
        }

        return toolDTOS;
    }

    public List<ToolDTO> getToolsByNumberOfRentalOrBelow(Integer rental_number) {
        List<Tool> tool = toolRepository.findToolByNumberOfRentals(rental_number);

        if(tool.isEmpty()) {
            throw new ApiException("Not have tool by this rental number or below");
        }

        List<ToolDTO> toolDTOS = new ArrayList<>();
        for (Tool tool1 : tool) {
            if (tool1.getNumberOfRentals() <= rental_number) {
                ToolDTO toolDTO = new ToolDTO(
                        tool1.getName(),
                        tool1.getDescription(),
                        tool1.getCategory(),
                        tool1.getModelNumber(),
                        tool1.getNumberOfRentals(),
                        tool1.getBrand(),
                        tool1.getRentalPrice(),
                        tool1.getImageURL()
                );
                toolDTOS.add(toolDTO);
            }
        }
        if(toolDTOS.isEmpty()) {
            throw new ApiException("Not have tool by this rental number or below");
        }

        return toolDTOS;
    }

    public List<ToolDTO> getMyTools(Integer photographer_id) {
        Photographer photographer = photographerRepository.findPhotographersById(photographer_id);
        if(photographer==null) {
            throw new ApiException("Photographer Not Found");
        }
        Set<Tool> phTools = photographer.getTools();
        List<ToolDTO> toolDTOS = new ArrayList<>();
        for (Tool tool : phTools) {
            ToolDTO toolDTO = new ToolDTO(
                    tool.getName(),
                    tool.getDescription(),
                    tool.getCategory(),
                    tool.getModelNumber(),
                    tool.getNumberOfRentals(),
                    tool.getBrand(),
                    tool.getRentalPrice(),
                    tool.getImageURL()
            );
            toolDTOS.add(toolDTO);
        }
        if(toolDTOS.isEmpty()) {
            throw new ApiException("You do not have any tool");
        }
        return toolDTOS;
    }

    public List<ToolDTO> getPhotographerTools(Integer photographer_id,Integer photographer_id2) {
        Photographer photographer = photographerRepository.findPhotographersById(photographer_id);
        if(photographer==null) {
            throw new ApiException("Photographer Not Found");
        }

        if(photographer_id.equals(photographer_id2)) {
            throw new ApiException("Photographer Ids Are Equal");
        }

        Photographer photographer2 = photographerRepository.findPhotographersById(photographer_id2);
        if(photographer2==null) {
            throw new ApiException("Photographer Not Found");
        }

        Set<Tool> phTools = photographer2.getTools();
        List<ToolDTO> toolDTOS = new ArrayList<>();
        for (Tool tool : phTools) {
            ToolDTO toolDTO = new ToolDTO(
                    tool.getName(),
                    tool.getDescription(),
                    tool.getCategory(),
                    tool.getModelNumber(),
                    tool.getNumberOfRentals(),
                    tool.getBrand(),
                    tool.getRentalPrice(),
                    tool.getImageURL()
            );
            toolDTOS.add(toolDTO);
        }
        if(toolDTOS.isEmpty()) {
            throw new ApiException("Photographer do not have any tool");
        }
        return toolDTOS;
    }



    public void deleteTool(Integer photographer_id,Integer tool_id) {
        Photographer photographer = photographerRepository.findPhotographersById(photographer_id);
        if(photographer==null) {
            throw new ApiException("Photographer Not Found");
        }
        Tool existingTool = toolRepository.findToolById(tool_id);
        if (existingTool != null) {
            toolRepository.delete(existingTool);
        } else {
            throw new ApiException("Tool not found");
        }
    }


}