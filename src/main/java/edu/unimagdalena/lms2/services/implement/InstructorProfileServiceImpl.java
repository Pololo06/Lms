package edu.unimagdalena.lms2.services.implement;

import edu.unimagdalena.lms2.dto.InstructorProfileDto.InstructorProfileRequest;
import edu.unimagdalena.lms2.dto.InstructorProfileDto.InstructorProfileResponse;
import edu.unimagdalena.lms2.entities.Instructor;
import edu.unimagdalena.lms2.entities.InstructorProfile;
import edu.unimagdalena.lms2.repositories.InstructorProfileRepository;
import edu.unimagdalena.lms2.repositories.InstructorRepository;
import edu.unimagdalena.lms2.services.InstructorProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InstructorProfileServiceImpl implements InstructorProfileService {

    private final InstructorProfileRepository profileRepository;
    private final InstructorRepository instructorRepository;


    private InstructorProfileResponse toResponse(InstructorProfile p) {
        return new InstructorProfileResponse(
                p.getId(),
                p.getPhone(),
                p.getBio(),
                p.getInstructor().getId(),
                p.getInstructor().getFullName()
        );
    }

    // CRUD

    @Override
    @Transactional
    public InstructorProfileResponse create(InstructorProfileRequest request) {
        Instructor instructor = instructorRepository.findById(request.instructorId())
                .orElseThrow(() -> new RuntimeException("Instructor no encontrado con id: " + request.instructorId()));

        InstructorProfile profile = InstructorProfile.builder()
                .phone(request.phone())
                .bio(request.bio())
                .instructor(instructor)
                .build();

        return toResponse(profileRepository.save(profile));
    }

    @Override
    @Transactional(readOnly = true)
    public InstructorProfileResponse findById(UUID id) {
        InstructorProfile profile = profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado con id: " + id));
        return toResponse(profile);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InstructorProfileResponse> findAll() {
        return profileRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public InstructorProfileResponse update(UUID id, InstructorProfileRequest request) {
        InstructorProfile profile = profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado con id: " + id));

        profile.setPhone(request.phone());
        profile.setBio(request.bio());

        if (request.instructorId() != null) {
            Instructor instructor = instructorRepository.findById(request.instructorId())
                    .orElseThrow(() -> new RuntimeException("Instructor no encontrado con id: " + request.instructorId()));
            profile.setInstructor(instructor);
        }

        return toResponse(profileRepository.save(profile));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (!profileRepository.existsById(id)) {
            throw new RuntimeException("Perfil no encontrado con id: " + id);
        }
        profileRepository.deleteById(id);
    }

    // Casos de uso

    @Override
    @Transactional(readOnly = true)
    public InstructorProfileResponse findByPhone(String phone) {
        InstructorProfile profile = profileRepository.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado con teléfono: " + phone));
        return toResponse(profile);
    }
}
