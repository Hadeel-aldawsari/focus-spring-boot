package com.example.focus.Service;

import com.example.focus.ApiResponse.ApiException;
import com.example.focus.DTO.SpaceDTO;
import com.example.focus.DTO.SpaceDTOIn;
import com.example.focus.Model.Space;
import com.example.focus.Model.Shift;
import com.example.focus.Model.Studio;
import com.example.focus.Repository.SpaceRepository;
import com.example.focus.Repository.ShiftRepository;
import com.example.focus.Repository.StudioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SpaceService {

    private final SpaceRepository spaceRepository;
    private final ShiftRepository shiftRepository;
    private final StudioRepository studioRepository;

    public List<Space> getAllSpaces() {
        return spaceRepository.findAll();
    }

    public SpaceDTO getSpaceById(Integer id) {
        Space space = spaceRepository.findSpaceById(id);
        SpaceDTO spaceDTO = new SpaceDTO();

        spaceDTO.setName(space.getName());
        spaceDTO.setDescription(space.getDescription());
        spaceDTO.setType(space.getType());
        spaceDTO.setPriceFullDay(space.getFullDayPrice());
        spaceDTO.setPriceDay(space.getDayPrice());
        spaceDTO.setPriceNight(space.getNightPrice());
        spaceDTO.setLength(space.getLength());
        spaceDTO.setWidth(space.getWidth());
        spaceDTO.setStatus(space.getStatus());
        spaceDTO.setImage(space.getImage());

        if (space == null) {
            throw new ApiException("Space not found");
        }
        return spaceDTO;
    }

    private static final String UPLOAD_DIR = "C:/Users/doly/Desktop/Upload/Space/";
    public Space createSpace(Integer studioId,SpaceDTOIn spaceDTOIn, MultipartFile file) throws IOException {
        Studio studio = studioRepository.findStudioById(studioId);
        if (studio == null) {
            throw new ApiException("Studio not found");
        }
        if(!studio.getStatus().equalsIgnoreCase("active")){
            throw new ApiException("Studio inActive");
        }

        if (!isValidImageFile(file)) {
            throw new ApiException("Invalid image file. Only JPG, PNG, and JPEG files are allowed");
        }

        Path filePath = Paths.get(UPLOAD_DIR.concat(saveImageFile(file)));

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        String filePathString = filePath.toString();
        // Create and save the space
        Space space = new Space();
        space.setName(spaceDTOIn.getName());
        space.setType(spaceDTOIn.getType());
        space.setWidth(spaceDTOIn.getWidth());
        space.setLength(spaceDTOIn.getLength());
        space.setDescription(spaceDTOIn.getDescription());
        space.setDayPrice(spaceDTOIn.getDayPrice());
        space.setFullDayPrice(spaceDTOIn.getFullDayPrice());
        space.setNightPrice(spaceDTOIn.getNightPrice());
        space.setImage(filePathString);
        space.setStatus("Available");
        space.setStudio(studioRepository.findStudioById(studioId));
        space = spaceRepository.save(space);
        // Automatically generate shifts for 1 year
        generateShiftsForSpace(space);
        return space;
    }

    public Space updateSpace(Integer id, SpaceDTOIn spaceDTOIn,MultipartFile file) throws IOException {
        Space space = spaceRepository.findSpaceById(id);
        if (space == null) {
            throw new IllegalArgumentException("Space not found");
        }

        if (!isValidImageFile(file)) {
            throw new ApiException("Invalid image file. Only JPG, PNG, and JPEG files are allowed");
        }
        Path filePath = Paths.get(UPLOAD_DIR.concat(saveImageFile(file)));
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        String filePathString = filePath.toString();

        space.setName(spaceDTOIn.getName());
        space.setType(spaceDTOIn.getType());
        space.setWidth(spaceDTOIn.getWidth());
        space.setLength(spaceDTOIn.getLength());
        space.setDescription(spaceDTOIn.getDescription());
        space.setNightPrice(spaceDTOIn.getNightPrice());
        space.setFullDayPrice(spaceDTOIn.getFullDayPrice());
        space.setDayPrice(spaceDTOIn.getDayPrice());
        space.setImage(filePathString);
        return spaceRepository.save(space);
    }

    public void deleteSpace(Integer id) {
        Space space = spaceRepository.findSpaceById(id);
        if (space == null) {
            throw new IllegalArgumentException("Space not found");
        }
        spaceRepository.delete(space);
    }

    public void updateSpaceStatus(Integer id, String status) {
        Space space = spaceRepository.findSpaceById(id);
        if (space == null) {
            throw new IllegalArgumentException("Space not found");
        }
        space.setStatus(status);
        spaceRepository.save(space);
    }

    private void generateShiftsForSpace(Space space) {
        List<Shift> shifts = new ArrayList<>();
        LocalDate startDate = LocalDate.now(); // Start from today
        LocalDate endDate = startDate.plusYears(1); // End 1 year later

        while (startDate.isBefore(endDate)) {
            // Create day shift (8 AM - 8 PM)
            Shift dayShift = new Shift();
            dayShift.setName("Morning Shift");
            dayShift.setDate(startDate.atStartOfDay()); // Use LocalDate for the date
            dayShift.setSpace(space); // Associate the shift with the space
            dayShift.setStartTime(startDate.atTime(8, 0)); // Set start time to 8 AM
            dayShift.setEndTime(startDate.atTime(15, 0)); // Set end time to 8 PM
            dayShift.setStatus("Available"); // Default status
            dayShift.setPrice(space.getDayPrice()); // Set price from space
            shifts.add(dayShift);

            // Create night shift ( PM - 8 AM next day)
            Shift nightShift = new Shift();
            nightShift.setName("Night Shift");
            nightShift.setDate(startDate.atStartOfDay()); // Use LocalDate for the date
            nightShift.setSpace(space);
            nightShift.setStartTime(startDate.atTime(15, 0)); // Set start time to 8 PM
            nightShift.setEndTime(startDate.atTime(20, 0)); // Set end time to 8 AM next day
            nightShift.setStatus("Available");
            nightShift.setPrice(space.getNightPrice()); // Set price from space
            shifts.add(nightShift);

            // Create full-day shift (8 AM - 8 AM next day)
            Shift fullDayShift = new Shift();
            fullDayShift.setName("Full Day");
            fullDayShift.setDate(startDate.atStartOfDay()); // Use LocalDate for the date
            fullDayShift.setSpace(space);
            fullDayShift.setStartTime(startDate.atTime(8, 0)); // Set start time to 8 AM
            fullDayShift.setEndTime(startDate.atTime(20, 0)); // Set end time to 8 AM next day
            fullDayShift.setStatus("Available");
            fullDayShift.setPrice(space.getFullDayPrice()); // Set full-day price
            shifts.add(fullDayShift);

            // Move to the next day
            startDate = startDate.plusDays(1);
        }

        // Save all generated shifts to the database
        shiftRepository.saveAll(shifts);
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

    public List<SpaceDTO> getAllMySpaces(Integer studio_id){
        Studio studio = studioRepository.findStudioById(studio_id);
        if(studio == null) {
            throw new ApiException("studio not found");
        }

        List<Space> spaces =spaceRepository.findSpaceByStudio(studio);
        List<SpaceDTO> spaceDTOS = new ArrayList<>();
        for (Space space : spaces){
            SpaceDTO spaceDTO = new SpaceDTO();
            spaceDTO.setName(space.getName());
            spaceDTO.setWidth(space.getWidth());
            spaceDTO.setLength(space.getLength());
            spaceDTO.setStatus(space.getStatus());
            spaceDTO.setDescription(space.getDescription());
            spaceDTO.setImage(space.getImage());
            spaceDTO.setPriceDay(space.getDayPrice());
            spaceDTO.setPriceNight(space.getNightPrice());
            spaceDTO.setPriceFullDay(space.getFullDayPrice());
            spaceDTO.setType(space.getType());
            spaceDTOS.add(spaceDTO);

        }


        if (spaceDTOS.isEmpty()){
            throw new ApiException("Not have any space");
        }
        return spaceDTOS;

    }

    public List<SpaceDTO> getMyAvailableSpaces(Integer studio_id) {
        Studio studio = studioRepository.findStudioById(studio_id);
        if (studio == null) {
            throw new ApiException("studio not found");
        }
        List<Space> spaces = spaceRepository.findSpaceByStudio(studio);
        List<SpaceDTO> spaceDTOS = new ArrayList<>();
        for (Space space : spaces) {
            if (space.getStatus().equals("Available")) {
                SpaceDTO spaceDTO = new SpaceDTO();
                spaceDTO.setName(space.getName());
                spaceDTO.setLength(space.getLength());
                spaceDTO.setWidth(space.getWidth());
                spaceDTO.setStatus(space.getStatus());
                spaceDTO.setDescription(space.getDescription());
                spaceDTO.setImage(space.getImage());
                spaceDTO.setPriceDay(space.getDayPrice());
                spaceDTO.setPriceNight(space.getNightPrice());
                spaceDTO.setPriceFullDay(space.getFullDayPrice());
                spaceDTO.setType(space.getType());
                spaceDTOS.add(spaceDTO);
                return spaceDTOS;
            }

        }
        if (spaceDTOS.isEmpty()) {
            throw new ApiException("Not have available space");
        }
        return spaceDTOS;
    }


    public SpaceDTO getSpecificSpace(Integer space_id) {
        Space space = spaceRepository.findSpaceById(space_id);
        if (space == null) {
            throw new ApiException("Space not found");
        }
        SpaceDTO spaceDTO = new SpaceDTO();
        spaceDTO.setName(space.getName());
        spaceDTO.setLength(space.getLength());
        spaceDTO.setWidth(space.getWidth());
        spaceDTO.setStatus(space.getStatus());
        spaceDTO.setDescription(space.getDescription());
        spaceDTO.setImage(space.getImage());
        spaceDTO.setPriceDay(space.getDayPrice());
        spaceDTO.setPriceNight(space.getNightPrice());
        spaceDTO.setPriceFullDay(space.getFullDayPrice());
        spaceDTO.setType(space.getType());
        return spaceDTO;
    }
}
