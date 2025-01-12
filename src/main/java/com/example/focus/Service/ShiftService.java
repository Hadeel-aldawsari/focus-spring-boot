package com.example.focus.Service;

import com.example.focus.ApiResponse.ApiException;
import com.example.focus.DTO.ShiftDTO;
import com.example.focus.DTO.ShiftDTOIn;
import com.example.focus.Model.Shift;
import com.example.focus.Model.Space;
import com.example.focus.Repository.ShiftRepository;
import com.example.focus.Repository.SpaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShiftService {

    private final ShiftRepository shiftRepository;
    private final SpaceRepository spaceRepository;

    public List<Shift> getAllShifts() {
        return shiftRepository.findAll();
    }

    public Shift getShiftById(Integer id) {
        Shift shift = shiftRepository.findById(id).orElse(null);
        if (shift == null) {
            throw new IllegalArgumentException("Shift not found");
        }
        return shift;
    }

    public Shift createShift(ShiftDTOIn shiftDTOIn,Integer authId) {
        Shift shift = new Shift();
        Space space=spaceRepository.findSpaceById(shiftDTOIn.getSpaceId());
        if (!space.getStudio().getMyUser().getId().equals(authId)){
            throw new ApiException("you are not authorized to add this shift to this space");
        }
        shift.setName(shiftDTOIn.getName());
        shift.setSpace(space);
        shift.setStartTime(shiftDTOIn.getStartTime());
        shift.setEndTime(shiftDTOIn.getEndTime());
        shift.setStatus("Available");
        return shiftRepository.save(shift);
    }

    public Shift updateShiftStatus(Integer id, String status,Integer authId) {
        Shift shift = shiftRepository.findById(id).orElse(null);
        if (shift == null) {
            throw new IllegalArgumentException("Shift not found");
        }
        Space space=spaceRepository.findSpaceById(shift.getSpace().getId());
        if (!space.getStudio().getMyUser().getId().equals(authId)){
            throw new ApiException("you are not authorized to update this shift for this space");
        }

        shift.setStatus(status);
        return shiftRepository.save(shift);
    }

    public void deleteShift(Integer id,Integer authId) {

        Shift shift = shiftRepository.findById(id).orElse(null);
        if (shift == null) {
            throw new ApiException("Shift not found");
        }
        Space space=spaceRepository.findSpaceById(shift.getSpace().getId());
        if (!space.getStudio().getMyUser().getId().equals(authId)){
            throw new ApiException("you are not authorized to delete this shift from this space");
        }
        shiftRepository.delete(shift);
    }

    public List<Shift> getShiftsBySpaceId(Integer spaceId) {
        return shiftRepository.findBySpaceId(spaceId);
    }

}
