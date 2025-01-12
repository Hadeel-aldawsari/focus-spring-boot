package com.example.focus.Service;

import com.example.focus.ApiResponse.ApiException;
import com.example.focus.DTO.MediaDTO;
import com.example.focus.DTO.RequestEditingActiveDTO;
import com.example.focus.DTO.RequestEditingInputDTO;
import com.example.focus.DTO.RequestEditingOutputDTO;
import com.example.focus.Model.Media;
import com.example.focus.Model.RequestEditing;
import com.example.focus.Repository.EditorRepository;
import com.example.focus.Repository.MediaRepository;
import com.example.focus.Repository.PhotographerRepository;
import com.example.focus.Repository.RequestEditingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RequestEditingService {

    private final RequestEditingRepository requestEditingRepository;
    private final EditorRepository editorRepository;
    private final PhotographerRepository photographerRepository;
    private final MediaRepository mediaRepository;

    public List<RequestEditingOutputDTO> getAllRequests() {
        List<RequestEditing> requests = requestEditingRepository.findAll();
        List<RequestEditingOutputDTO> requestDTOs = new ArrayList<>();
        for (RequestEditing request : requests) {
            requestDTOs.add(convertToDTO(request));
        }
        return requestDTOs;
    }

    public List<RequestEditingOutputDTO> getEditorRequests(Integer id) {
        List<RequestEditing> requests = requestEditingRepository.findRequestEditingsByEditor_Id(id);
        List<RequestEditingOutputDTO> requestDTOs = new ArrayList<>();
        for (RequestEditing request : requests) {
            requestDTOs.add(convertToDTO(request));
        }
        return requestDTOs;
    }

    public List<RequestEditingOutputDTO> getPhotographerRequests(Integer id) {
        List<RequestEditing> requests = requestEditingRepository.findRequestEditingsByPhotographerId(id);
        List<RequestEditingOutputDTO> requestDTOs = new ArrayList<>();
        for (RequestEditing request : requests) {
            requestDTOs.add(convertToDTO(request));
        }
        return requestDTOs;
    }

    public List<RequestEditingOutputDTO> getAwaitingOfferRequestsForEditor(Integer id) {
        List<RequestEditing> requests = requestEditingRepository.findRequestEditingsByEditor_IdAndStatus(id,"AwaitingOffer");
        List<RequestEditingOutputDTO> requestDTOs = new ArrayList<>();
        for (RequestEditing request : requests) {
            requestDTOs.add(convertToDTO(request));
        }
        return requestDTOs;
    }


    public RequestEditingOutputDTO getRequestByIdGeneral(Integer id,Integer editorId) {
        RequestEditing request = requestEditingRepository.findRequestEditingById(id);
        if (request==null){new ApiException("Request not found");}
        if (!request.getEditor().getId().equals(editorId)){
            throw new ApiException("this request is not yours");
        }
        return convertToDTO(request);
    }
    public RequestEditingActiveDTO getRequestByIdActive(Integer id,Integer editorId) {
        RequestEditing request = requestEditingRepository.findRequestEditingById(id);
        if (request==null){new ApiException("Request not found");}
        if (!request.getEditor().getId().equals(editorId)){
            throw new ApiException("this request is not yours");
        }
        if (!request.getStatus().equals("Active")){
            throw new ApiException("this request is not Active");
        }
        return convertToActiveDTO(request);
    }
    public RequestEditingOutputDTO markAsCompleted(Integer requestId, Integer photographerId) {
        RequestEditing request = requestEditingRepository.findById(requestId)
                .orElseThrow(() -> new ApiException("Request not found"));

        if (!request.getPhotographer().getId().equals(photographerId)) {
            throw new ApiException("You are not authorized to complete this request.");
        }

        if (!"In Progress".equals(request.getStatus())) {
            throw new ApiException("Request must be in progress to mark it as completed.");
        }

        request.setStatus("Completed");
        requestEditingRepository.save(request);

        return convertToDTO(request);
    }

//    public RequestEditingOutputDTO rateEditor(Integer requestId, Integer photographerId, Integer rating) {
//        RequestEditing request = requestEditingRepository.findById(requestId)
//                .orElseThrow(() -> new ApiException("Request not found"));
//
//        if (!request.getPhotographer().getId().equals(photographerId)) {
//            throw new ApiException("You are not authorized to rate this request.");
//        }
//
//        if (!"Completed".equals(request.getStatus())) {
//            throw new ApiException("Request must be completed to rate the editor.");
//        }
//
////        if (request.getRating() != null) {
////            throw new ApiException("This request has already been rated.");
////        }
////
////        request.setRating(rating);
//        requestEditingRepository.save(request);
//
//        return convertToDTO(request);
//    }


    private  final String UPLOAD_REQUEST_DIR = "C:/Users/doly/Desktop/Upload/";
    public RequestEditingOutputDTO createRequest(RequestEditingInputDTO requestInput, MultipartFile[] files, Integer editorId, Integer photographerId) throws IOException {
        if (editorRepository.findEditorById(editorId) == null) {
            throw new ApiException("Editor not found");
        }
        if (photographerRepository.findPhotographersById(photographerId) == null) {
            throw new ApiException("Photographer not found");
        }

        // إنشاء كائن RequestEditing وتعيين القيم
        RequestEditing request = new RequestEditing();
        request.setEditor(editorRepository.findEditorById(editorId));
        request.setPhotographer(photographerRepository.findPhotographersById(photographerId));
        request.setEstimatedCompletionDate(requestInput.getEstimatedCompletionDate());
        request.setDescription(requestInput.getDescription());
        request.setFullCameraName(requestInput.getFullCameraName());
        request.setSensorSize(requestInput.getSensorSize());
        request.setKitLens(requestInput.getKitLens());
        request.setViewFinder(requestInput.getViewFinder());
        request.setNativeISO(requestInput.getNativeISO());
        request.setStatus("AwaitingOffer");

        // حفظ RequestEditing أولاً
        request = requestEditingRepository.save(request);

        // معالجة الملفات
        List<Media> mediaList = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            String fileType = getFileType(fileName);

            if ("unknown".equals(fileType)) {
                throw new ApiException("Unsupported file type. Only images and videos are allowed");
            }

            // تحديد مسار الملف
            Path filePath = Paths.get(UPLOAD_REQUEST_DIR.concat(fileName));

            // نسخ الملف إلى المجلد المحدد
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // إنشاء كائن Media وتعيين خصائصه
            Media media = new Media();
            media.setMediaURL(filePath.toString());
            media.setMediaType(fileType);
            media.setUploadDate(LocalDateTime.now());
            media.setVisibility(false);
            media.setRequestEditing(request);  // ربط الميديا بـ RequestEditing

            // حفظ Media في قاعدة البيانات
            mediaRepository.save(media);  // حفظ الميديا في قاعدة البيانات
            mediaList.add(media);  // إضافة الميديا إلى القائمة
        }

        // تعيين الميديا إلى RequestEditing
        request.setMedias(new HashSet<>(mediaList));

        // حفظ RequestEditing مع الميديا المربوطة
        return convertToDTO(requestEditingRepository.save(request));
    }

    public RequestEditingOutputDTO updateRequest(Integer id, RequestEditingInputDTO requestInput) {
        RequestEditing request = requestEditingRepository.findById(id).orElseThrow(() -> new ApiException("Request not found"));
        request.setEstimatedCompletionDate(requestInput.getEstimatedCompletionDate());
        request.setDescription(requestInput.getDescription());
        request.setFullCameraName(requestInput.getFullCameraName());
        request.setSensorSize(requestInput.getSensorSize());
        request.setKitLens(requestInput.getKitLens());
        request.setViewFinder(requestInput.getViewFinder());
        request.setNativeISO(requestInput.getNativeISO());
        request.setMedias(null);
        return convertToDTO(requestEditingRepository.save(request));
    }

    public void deleteRequest(Integer id) {
        RequestEditing request = requestEditingRepository.findById(id).orElseThrow(() -> new ApiException("Request not found"));
        requestEditingRepository.delete(request);
    }

//    public RequestEditingOutputDTO acceptRequest(Integer requestId) {
//        RequestEditing request = requestEditingRepository.findById(requestId)
//                .orElseThrow(() -> new ApiException("Request not found"));
//
//        if (!"Pending".equals(request.getStatus())) {
//            throw new ApiException("Only requests with 'Pending' status can be accepted.");
//        }
//
//        request.setStatus("AwaitingOffer");
//        requestEditingRepository.save(request);
//
//        return convertToDTO(request);
//    }

    public RequestEditingOutputDTO rejectRequest(Integer requestId) {
        RequestEditing request = requestEditingRepository.findById(requestId)
                .orElseThrow(() -> new ApiException("Request not found"));

        if (!"Active".equals(request.getStatus())) {
            throw new ApiException("Only requests with 'AwaitingOffer' status can be rejected.");
        }

        request.setStatus("Closed");
        requestEditingRepository.save(request);

        return convertToDTO(request);
    }

    private RequestEditingOutputDTO convertToDTO(RequestEditing request) {
        RequestEditingOutputDTO dto = new RequestEditingOutputDTO();
        dto.setEstimatedCompletionDate(request.getEstimatedCompletionDate());
        dto.setDescription(request.getDescription());
        dto.setFullCameraName(request.getFullCameraName());
        dto.setSensorSize(request.getSensorSize());
        dto.setKitLens(request.getKitLens());
        dto.setViewFinder(request.getViewFinder());
        dto.setNativeISO(request.getNativeISO());
        dto.setEditorName(request.getEditor().getName());
        dto.setPhotographeName(request.getPhotographer().getName());
        dto.setStatus(request.getStatus());
        return dto;
    }
    private RequestEditingActiveDTO convertToActiveDTO(RequestEditing request) {
        RequestEditingActiveDTO dto = new RequestEditingActiveDTO();
        ArrayList<MediaDTO>mediaDTOS=new ArrayList<>();
//        for (Media media : request.getMedias()) {
//            MediaDTO mediaDTO=new MediaDTO(media.getMediaType(),media.getMediaURL(),media.getUploadDate());
//            mediaDTOS.add(mediaDTO);
//        }
        dto.setEstimatedCompletionDate(request.getEstimatedCompletionDate());
        dto.setDescription(request.getDescription());
        dto.setFullCameraName(request.getFullCameraName());
        dto.setSensorSize(request.getSensorSize());
        dto.setKitLens(request.getKitLens());
        dto.setViewFinder(request.getViewFinder());
        dto.setNativeISO(request.getNativeISO());
        dto.setEditorName(request.getEditor().getName());
        dto.setPhotographeName(request.getPhotographer().getName());
        dto.setMedia(mediaDTOS);
        dto.setStatus(request.getStatus());
        return dto;
    }

    // ميثود لتحديد نوع الملف بناءً على امتداده
    private String getFileType(String fileName) {
        String fileType = "unknown";
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png")) {
            fileType = "image";
        } else if (fileName.endsWith(".mp4") || fileName.endsWith(".avi") || fileName.endsWith(".mov")) {
            fileType = "video";
        }
        return fileType;
    }

    private String saveImageFile(MultipartFile file) throws IOException {
        String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        Path path = Paths.get(UPLOAD_REQUEST_DIR+ fileName);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }
}
