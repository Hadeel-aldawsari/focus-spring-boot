package com.example.focus.Service;
import com.example.focus.ApiResponse.ApiException;
import com.example.focus.DTO.*;
//import com.example.focus.Model.BookSpace;
//import com.example.focus.Model.Space;
import com.example.focus.Model.*;
//import com.example.focus.Repository.BookSpaceRepository;
//import com.example.focus.Repository.SpaceRepository;
import com.example.focus.Repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudioService {
    private final MyUserRepository myUserRepository;
    private final StudioRepository studioRepository;
    private final EmailService emailService;
    private final com.example.focus.service.S3Service s3Service;

    public List<StudioDTO> getAllStudios(){
        List<Studio> studios = studioRepository.findAll();
        List<StudioDTO> studioDTOS = new ArrayList<>();
        for (Studio studio : studios){
            StudioDTO studioDTO = new StudioDTO();
            studioDTO.setName(studio.getName());
            studioDTO.setUsername(studio.getMyUser().getUsername());
            studioDTO.setEmail(studio.getMyUser().getEmail());
            studioDTO.setPhoneNumber(studio.getPhoneNumber());
            studioDTO.setCity(studio.getCity());
            studioDTO.setAddress(studio.getAddress());
            studioDTO.setCommercialRecord(studio.getCommercialRecord());
            studioDTO.setStatus(studio.getStatus());
            studio.setImageURL(studio.getImageURL());
            studioDTOS.add(studioDTO);
        }
        return studioDTOS;
    }

    public void registerStudio(StudioDTOIn studioDTOIn){
        MyUser checkUsername =myUserRepository.findMyUserByUsername(studioDTOIn.getUsername());
        MyUser checkEmail =myUserRepository.findMyUserByEmail(studioDTOIn.getEmail());
        if(checkUsername != null){
            throw new ApiException("Username already exists");
        }
        if(checkEmail != null){
            throw new ApiException("email already exists");
        }
        String hashPass=new BCryptPasswordEncoder().encode(studioDTOIn.getPassword());


        MyUser user = new MyUser();
        user.setUsername(studioDTOIn.getUsername());
        user.setEmail(studioDTOIn.getEmail());
        user.setPassword(hashPass);
        user.setRole("STUDIO");


        Studio studio=new Studio();
        studio.setMyUser(user);
        studio.setCommercialRecord(studioDTOIn.getCommercialRecord());
        studio.setName(studioDTOIn.getName());
        studio.setCity(studioDTOIn.getCity());
        studio.setPhoneNumber(studioDTOIn.getPhoneNumber());
        studio.setAddress(studioDTOIn.getAddress());
        studio.setStatus("Inactive");

        if(studio!=null && user != null) {
            studioRepository.save(studio);
            myUserRepository.save(user);
        }




    }

    public void activateStudio(Integer admin_id,Integer studio_id){
        MyUser user = myUserRepository.findMyUserById(admin_id);
        if(user == null){
            throw new ApiException("admin not found");
        }
        Studio studio = studioRepository.findStudioById(studio_id);
        if(studio == null){
            throw new ApiException("studio not found");
        }
        studio.setStatus("active");
        studioRepository.save(studio);
        //String to, String subject, String body
        emailService.sendEmail(studio.getMyUser().getEmail(),
                "Welcome to Focus !",
                "Dear " + studio.getName() + ",\n\n" +
                        "Congratulations! Your studio has been successfully activated.\n" +
                        "We are thrilled to have you on board and excited to see your work. " +
                        "You can now log in and start your journey with us.\n\n" +
                        "Best regards,\n" +
                        "focus Team");


    }

    public void rejectStudio(Integer admin_id,Integer studio_id){
        MyUser user = myUserRepository.findMyUserById(admin_id);
        if(user == null){
            throw new ApiException("admin not found");
        }
        Studio studio = studioRepository.findStudioById(studio_id);
        if(studio == null){
            throw new ApiException("studio not found");
        }
        studio.setStatus("rejected");
        studioRepository.save(studio);
//to,subject,body
        emailService.sendEmail(studio.getMyUser().getEmail(),
                "Sorry your studio was rejected",
                "Dear " + studio.getName() + ",\n\n" +
                        "We regret to inform you that your studio has been rejected.\n" +
                        "If you have any questions, please feel free to contact us.\n\n" +
                        "Best regards,\n" +
                        "focus Team");

    }
    public void uploadImage(Integer id, MultipartFile file) throws IOException {
        MyUser user = myUserRepository.findMyUserById(id);
        if (user == null) {
            throw new ApiException("Profile Not Found");
        }

        if (!isValidImageFile(file)) {
            throw new ApiException("Invalid image file. Only JPG, PNG, and JPEG files are allowed");
        }

        String s3FileName = s3Service.uploadFile(file);
        user.getStudio().setImageURL(s3FileName);
        myUserRepository.save(user);
    }


    // التحقق من نوع الملف
    private boolean isValidImageFile(MultipartFile file) {
        String fileName = file.getOriginalFilename().toLowerCase();
        return fileName.endsWith(".jpg") || fileName.endsWith(".png") || fileName.endsWith(".jpeg");
    }




    public List<StudioDTOPhotographer> getStudiosByCity(String city) {


        List<Studio> studios = studioRepository.findStudioByCity(city);
        if(studios.isEmpty()) {
            throw new ApiException("Not have any studio");
        }

        List<StudioDTOPhotographer> studioDTOS = new ArrayList<>();
        for (Studio studio1 : studios) {
            StudioDTOPhotographer studioDTO1 = new StudioDTOPhotographer();
            studioDTO1.setName(studio1.getName());
            studioDTO1.setUsername(studio1.getMyUser().getUsername());
            studioDTO1.setPhoneNumber(studio1.getPhoneNumber());
            studioDTO1.setEmail(studio1.getMyUser().getEmail());
            studioDTO1.setAddress(studio1.getAddress());
            studioDTO1.setCommercialRecord(studio1.getCommercialRecord());
            studioDTO1.setCity(studio1.getCity());
            studioDTOS.add(studioDTO1);
        }
        return studioDTOS;

    }

    public StudioDTOPhotographer getSpecificStudio(Integer studio_id) {


        Studio studio = studioRepository.findStudioById(studio_id);
        if(studio==null) {
            throw new ApiException("studio not found");
        }
        StudioDTOPhotographer studioDTO = new StudioDTOPhotographer();
        studioDTO.setName(studio.getName());
        studioDTO.setUsername(studio.getMyUser().getUsername());
        studioDTO.setPhoneNumber(studio.getPhoneNumber());
        studioDTO.setEmail(studio.getMyUser().getEmail());
        studioDTO.setAddress(studio.getAddress());
        studioDTO.setCommercialRecord(studio.getCommercialRecord());
        studioDTO.setCity(studio.getCity());
        return studioDTO;
    }


}
