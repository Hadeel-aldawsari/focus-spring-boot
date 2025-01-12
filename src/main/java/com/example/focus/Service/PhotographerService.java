package com.example.focus.Service;

import com.example.focus.ApiResponse.ApiException;
import com.example.focus.DTO.*;

import com.example.focus.Model.*;
import com.example.focus.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.focus.Service.EmailService;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PhotographerService {
    private final MyUserRepository myUserRepository;
    private final PhotographerRepository photographerRepository;
    private final ProfilePhotographerRepository profilePhotographerRepository;
    private final RentToolsRepository rentToolsRepository;
    private final ToolRepository toolRepository;
    private final EmailService emailService;
    private final StudioRepository studioRepository;


    public  List<PhotographerDTO> getAllPhotographers() {
        List<Photographer> photographers = photographerRepository.findAll();
        List<PhotographerDTO> photographerDTOS = new ArrayList<>();

        for (Photographer photographer : photographers) {
            PhotographerDTO photographerDTO = new PhotographerDTO(
                    photographer.getName(),
                    photographer.getMyUser().getUsername(),
                    photographer.getMyUser().getEmail(),
                    photographer.getCity(),
                    photographer.getPhoneNumber()
            );
            photographerDTOS.add(photographerDTO);
        }
        return photographerDTOS;
    }


    public void PhotographerRegistration(PhotographerDTOin photographerDTOin) {
         String hashPass=new BCryptPasswordEncoder().encode(photographerDTOin.getPassword());

        MyUser checkUsername =myUserRepository.findMyUserByUsername(photographerDTOin.getUsername());
        MyUser checkEmail =myUserRepository.findMyUserByEmail(photographerDTOin.getEmail());
        if(checkUsername != null){
            throw new ApiException("Username already exists");
        }
        if(checkEmail != null){
            throw new ApiException("email already exists");
        }

        MyUser user = new MyUser();
        user.setUsername(photographerDTOin.getUsername());
        user.setEmail(photographerDTOin.getEmail());
        user.setPassword(hashPass);
        user.setRole("PHOTOGRAPHER");

        Photographer photographer=new Photographer();
        photographer.setMyUser(user);
        photographer.setName(photographerDTOin.getName());
        photographer.setCity(photographerDTOin.getCity());
        photographer.setPhoneNumber(photographerDTOin.getPhoneNumber());


        ProfilePhotographer profilePhotographer = new ProfilePhotographer();
        profilePhotographer.setMyUser(user);
        profilePhotographer.setNumberOfPosts(0);

        user.setPhotographer(photographer);
        myUserRepository.save(user);

    }



    public void updatePhotographer(Integer id, PhotographerDTOin photographerDTOin) {
        Photographer existingPhotographer = photographerRepository.findPhotographersById(id);
        if (existingPhotographer != null) {
            existingPhotographer.setName(photographerDTOin.getName());
            existingPhotographer.setCity(photographerDTOin.getCity());
            existingPhotographer.getMyUser().setUsername(photographerDTOin.getUsername());
            existingPhotographer.getMyUser().setEmail(photographerDTOin.getEmail());
            existingPhotographer.setPhoneNumber(photographerDTOin.getPhoneNumber());
        }else {
            throw new ApiException("Photographer Not Found");
        }
        photographerRepository.save(existingPhotographer);

    }


    public void deletePhotographer(Integer id) {
        MyUser myUser=myUserRepository.findMyUserById(id);
        Photographer photographer=photographerRepository.findPhotographersById(id);
        ProfilePhotographer profilePhotographer=profilePhotographerRepository.findProfilePhotographerById(id);
        myUser.setPhotographer(null);

        if(myUser!=null) {
            myUserRepository.delete(myUser);
        }else{
            throw new ApiException("Photographer Not Found");
        }
    }

    public void rentToolRequest(Integer photographer_id, Integer tool_id, RentToolsDTOIn rentTool) {
        Photographer photographer = photographerRepository.findPhotographersById(photographer_id);
        if(photographer==null) {
            throw new ApiException("photographer not found");
        }

        Tool tool = toolRepository.findToolById(tool_id);
        if(tool==null) {
            throw new ApiException("tool not found");
        }
        if (photographer_id.equals(tool.getPhotographer().getId())) {
            throw new ApiException("The photographer is the owner of the required tool.");
        }


        for (RentTools existingRent : rentToolsRepository.findAll()) {
            for(int i=0;i<rentToolsRepository.findAll().size();i++) {
                if (tool_id.equals(rentToolsRepository.findAll().get(i).getTool().getId()))  {
                    if (!(rentTool.getEndDate().isBefore(existingRent.getStartDate()) || rentTool.getStartDate().isAfter(existingRent.getEndDate()))) {
                        throw new ApiException("This tool is rented now for another photographer.");
                    }
                }
            }

        }

        if (rentTool.getStartDate().isAfter(rentTool.getEndDate())) {
            throw new ApiException("The start date after end date.");
        }

        long daysBetween = ChronoUnit.DAYS.between(rentTool.getStartDate(), rentTool.getEndDate());
        Double totalPrice = tool.getRentalPrice()*(daysBetween+1);
        rentTool.setRentPrice(totalPrice);


        RentTools rentTools = new RentTools();
        tool.setNumberOfRentals(tool.getNumberOfRentals()+1);
        rentTools.setStartDate(rentTool.getStartDate());
        rentTools.setEndDate(rentTool.getEndDate());
        rentTools.setOwner(tool.getPhotographer());
        rentTools.setRenter(photographer);
        rentTools.setRentPrice(totalPrice);
        rentTools.setTool(tool);
        rentToolsRepository.save(rentTools);

        emailService.sendEmail(photographer.getMyUser().getEmail(),
                "You have successfully rented the tool.",
                "Dear " + photographer.getName()+",\n\n" +
                        "We are pleased to inform you that the required tool has been successfully rented.\n" +
                        "Tool Name : " + tool.getName()+"\n"+
                        "Tool Description : " + tool.getDescription()+"\n"+
                        "Tool Model Number : " + tool.getModelNumber()+"\n"+
                        "Tool Brand : " + tool.getBrand()+"\n"+
                        "Tool Brand : " +totalPrice +"SR \n"+
                        "Best regards,\n" +
                        "focus Team");


    }

    // view rent tools for photographer
    public List<ToolDTO> viewMyRentTools(Integer photographer_id) {
        Photographer photographer = photographerRepository.findPhotographersById(photographer_id);
        if(photographer==null) {
            throw new ApiException("photographer not found");
        }
        List<ToolDTO> toolDTOS = new ArrayList<>();
        List<RentTools> rentTools = rentToolsRepository.findAll();
        for (RentTools rentTool : rentTools) {
            if (rentTool.getRenter().getId().equals(photographer.getId())) {
                Tool tool = rentTool.getTool();
                ToolDTO toolDTO = new ToolDTO(tool.getName(),tool.getDescription(),tool.getCategory(),tool.getBrand(),tool.getNumberOfRentals(),tool.getModelNumber(),tool.getRentalPrice(),tool.getImageURL());
                toolDTOS.add(toolDTO);
                return toolDTOS;
            }
        }
        throw new ApiException("photographer not have rent tools");
    }

    // view rental tools photographer rent from owner
    public List<ToolDTO> viewRentalTools(Integer photographer_id) {
        Photographer photographer = photographerRepository.findPhotographersById(photographer_id);
        if(photographer==null) {
            throw new ApiException("photographer not found");
        }

        List<ToolDTO> toolDTOS = new ArrayList<>();
        List<RentTools> rentTools = rentToolsRepository.findAll();
        for (RentTools rentTool : rentTools) {
            if (rentTool.getOwner().getId().equals(photographer.getId())) {
                Tool tool = rentTool.getTool();
                ToolDTO toolDTO = new ToolDTO(tool.getName(),tool.getDescription(),tool.getCategory(),tool.getBrand(),tool.getNumberOfRentals(),tool.getModelNumber(),tool.getRentalPrice(),tool.getImageURL());
                toolDTOS.add(toolDTO);
                return toolDTOS;
            }
        }
        throw new ApiException("photographer not have rental tools");

    }

}