package com.example.focus.Service;

import com.example.focus.ApiResponse.ApiException;
import com.example.focus.DTO.OfferEditingInputDTO;
import com.example.focus.DTO.OfferEditingOutputDTO;
import com.example.focus.Model.*;
import com.example.focus.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OfferEditingService {

    private final OfferEditingRepository offerEditingRepository;
    private final RequestEditingRepository requestEditingRepository;
    private final EditorRepository editorRepository;
    private final EmailService emailService;
    private final PhotographerRepository photographerRepository;
    private final MyUserRepository myUserRepository;


    //admin
    public List<OfferEditingOutputDTO> getAllOffers() {
        List<OfferEditing> offers = offerEditingRepository.findAll();
        List<OfferEditingOutputDTO> offerDTOs = new ArrayList<>();
        for (OfferEditing offer : offers) {
            offerDTOs.add(convertToDTO(offer));
        }
        return offerDTOs;
    }

    public List<OfferEditingOutputDTO> getMyOffer(Integer editorId) {
        MyUser myUser=myUserRepository.findMyUserById(editorId);
         if(myUser==null){
             throw  new ApiException("User not found");
         }

        List<OfferEditing> offers = offerEditingRepository.findOfferEditingByEditor_Id(editorId);
        List<OfferEditingOutputDTO> offerDTOs = new ArrayList<>();
        for (OfferEditing offer : offers) {
            offerDTOs.add(convertToDTO(offer));
        }
        return offerDTOs;
    }

    public List<OfferEditingOutputDTO> getByRequestEditor(Integer userid,Integer requestid) {
        Editor editor=editorRepository.findEditorById(userid);
        RequestEditing requestEditing=requestEditingRepository.findRequestEditingById(requestid);
        if(! requestEditing.getEditor().getId().equals(editor.getId())){
            throw  new ApiException("unautherazied,you don't have permission to this request");
        }

        List<OfferEditing> offers = offerEditingRepository.findByRequestEditingId(requestid);
        List<OfferEditingOutputDTO> offerDTOs = new ArrayList<>();
        for (OfferEditing offer : offers) {
            offerDTOs.add(convertToDTO(offer));
        }
        return offerDTOs;
    }

    public List<OfferEditingOutputDTO> getPhotographerOffers(Integer photographerId) {
        List<OfferEditing> offers = offerEditingRepository.findAll();
        List<OfferEditingOutputDTO> photographerOffers = new ArrayList<>();
        for (OfferEditing offer : offers) {
            if (offer.getRequestEditing().getPhotographer().getId().equals(photographerId)) {
                photographerOffers.add(convertToDTO(offer));
            }
        }
        return photographerOffers;
    }

    public OfferEditingOutputDTO getOfferById(Integer userid,Integer offerid) {
        MyUser myUser=myUserRepository.findMyUserById(userid);
       OfferEditing offer=offerEditingRepository.findOfferEditingById(offerid);
       if(offer==null){
          throw new ApiException("Offer not found");
       }

        if(! myUser.getId().equals(offer.getRequestEditing().getPhotographer().getId())) {
            throw new ApiException("unauthenticated, to view this offer");
        }
          return convertToDTO(offer);

    }

    public OfferEditingOutputDTO createOffer(OfferEditingInputDTO offerInput, Integer editorId) {
        if (editorRepository.findEditorById(editorId) == null) {
            throw new ApiException("Editor not found");
        }

        OfferEditing offer = new OfferEditing();
        offer.setRequestEditing(requestEditingRepository.findById(offerInput.getRequestId())
                .orElseThrow(() -> new ApiException("Request not found")));
        RequestEditing request = offer.getRequestEditing();

        if (!"AwaitingOffer".equals(request.getStatus())) {
            throw new ApiException("Request is not AwaitingOffer");
        }

        offer.setOfferDate(LocalDateTime.now());
        offer.setOfferedPrice(offerInput.getOfferedPrice());
        offer.setEditor(editorRepository.findEditorById(editorId));
        offer.setEstimatedCompletionTime(offerInput.getEstimatedCompletionTime());
        offer.setStatus("Applied");
        request.setStatus("HasOffer");

        OfferEditing savedOffer = offerEditingRepository.save(offer);

        sendOfferEmails(savedOffer);

        return convertToDTO(savedOffer);
    }

    public OfferEditingOutputDTO updateOffer(Integer id, OfferEditingInputDTO offerInput,Integer auth) {
        OfferEditing offer = offerEditingRepository.findById(id)
                .orElseThrow(() -> new ApiException("Offer not found"));

        if (!offer.getEditor().getId().equals(auth)){
            throw new ApiException("unAuthenticated, you don't have permission to this offer");
        }
        offer.setOfferedPrice(offerInput.getOfferedPrice());
        offer.setEstimatedCompletionTime(offerInput.getEstimatedCompletionTime());

        String userEmail = offer.getRequestEditing().getPhotographer().getMyUser().getEmail();
        String subject = "Offer Updated";
        String body = "Hello " + offer.getRequestEditing().getPhotographer().getName() +
                ",\n\nAn offer for your request has been updated. Please log in to review the details.\n\nThank you!";
        emailService.sendEmail(userEmail, subject, body);

        return convertToDTO(offerEditingRepository.save(offer));
    }

    public void deleteOffer(Integer offerId,Integer id) {

        OfferEditing offer = offerEditingRepository.findById(offerId)
                .orElseThrow(() -> new ApiException("Offer not found"));
        if (!offer.getRequestEditing().getEditor().getId().equals(id)){
            throw new ApiException("Unauthorized action you  arent this offer owner");
        }

        offerEditingRepository.delete(offer);

        String userEmail = offer.getRequestEditing().getPhotographer().getMyUser().getEmail();
        String subject = "Offer Deleted";
        String body = "Hello " + offer.getRequestEditing().getPhotographer().getName() +
                ",\n\nAn offer for your request has been deleted.\n\nThank you!";
        emailService.sendEmail(userEmail, subject, body);
    }

    public OfferEditingOutputDTO acceptOffer(Integer offerId, Integer photographerId) {
        OfferEditing offer = offerEditingRepository.findById(offerId)
                .orElseThrow(() -> new ApiException("Offer not found"));

        Photographer photographer = photographerRepository.findPhotographersById(photographerId);

        if (photographer == null || !offer.getRequestEditing().getPhotographer().getId().equals(photographerId)) {
            throw new ApiException("Unauthorized action");
        }

        if (!"Applied".equals(offer.getStatus())) {
            throw new ApiException("Only offers with 'Applied' status can be accepted");
        }

        offer.setStatus("Accepted");
        offer.getRequestEditing().setStatus("In Progress");

        OfferEditing savedOffer = offerEditingRepository.save(offer);

        sendAcceptEmails(savedOffer);

        return convertToDTO(savedOffer);
    }

    private void sendOfferEmails(OfferEditing offer) {
        String photographerEmail = offer.getRequestEditing().getPhotographer().getMyUser().getEmail();
        String photographerSubject = "Offer Received for Your Request";
        String photographerBody = "Hello " + offer.getRequestEditing().getPhotographer().getName() +
                ",\n\nAn offer has been sent by " + offer.getEditor().getName() +
                " for your request. Please log in to review the details.\n\nThank you!";
        emailService.sendEmail(photographerEmail, photographerSubject, photographerBody);

        String editorEmail = offer.getEditor().getMyUser().getEmail();
        String editorSubject = "Offer Submitted Successfully";
        String editorBody = "Hello " + offer.getEditor().getName() +
                ",\n\nYou have successfully submitted an offer for request ID: " +
                offer.getRequestEditing().getId() + ".\n\nThank you!";
        emailService.sendEmail(editorEmail, editorSubject, editorBody);
    }

    private void sendAcceptEmails(OfferEditing offer) {
        String photographerEmail = offer.getRequestEditing().getPhotographer().getMyUser().getEmail();
        String photographerSubject = "Offer Accepted";
        String photographerBody = "Hello " + offer.getRequestEditing().getPhotographer().getName() +
                ",\n\nYou have accepted an offer from " + offer.getEditor().getName() +
                ". Your request is now active.\n\nThank you!";
        emailService.sendEmail(photographerEmail, photographerSubject, photographerBody);

        String editorEmail = offer.getEditor().getMyUser().getEmail();
        String editorSubject = "Offer Accepted by Photographer";
        String editorBody = "Hello " + offer.getEditor().getName() +
                ",\n\nYour offer for request ID: " + offer.getRequestEditing().getId() +
                " has been accepted by the photographer.\n\nThank you!";
        emailService.sendEmail(editorEmail, editorSubject, editorBody);
    }
    public OfferEditingOutputDTO rejectOffer(Integer offerId, Integer photographerId) {

        OfferEditing offer = offerEditingRepository.findById(offerId)
                .orElseThrow(() -> new ApiException("Offer not found"));

        if (!offer.getRequestEditing().getPhotographer().equals(photographerId)){
            throw new ApiException("Unauthorized action you  arent this request owner");
        }

        Photographer photographer = photographerRepository.findPhotographersById(photographerId);
        if (photographer == null) {
            throw new ApiException("Photographer not found");
        }

        if (!offer.getRequestEditing().getPhotographer().getId().equals(photographerId)) {
            throw new ApiException("You are not authorized to reject this offer");
        }

        if (!"Applied".equals(offer.getStatus())) {
            throw new ApiException("Only offers with 'Applied' status can be rejected");
        }

        offer.setStatus("Rejected");
        return convertToDTO(offerEditingRepository.save(offer));
    }
    private OfferEditingOutputDTO convertToDTO(OfferEditing offer) {
        OfferEditingOutputDTO dto = new OfferEditingOutputDTO();
        dto.setRequestId(offer.getRequestEditing().getId());
        dto.setOfferDate(offer.getOfferDate());
        dto.setOfferedPrice(offer.getOfferedPrice());
        dto.setEstimatedCompletionTime(offer.getEstimatedCompletionTime());
        dto.setEditorName(offer.getEditor().getName());
        dto.setStatus(offer.getStatus());
        return dto;
    }
}
