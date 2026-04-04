package edu.unimagdalena.lms2.services;

import edu.unimagdalena.lms2.dto.InstructorProfileDto.InstructorProfileRequest;
import edu.unimagdalena.lms2.entities.Instructor;
import edu.unimagdalena.lms2.entities.InstructorProfile;
import edu.unimagdalena.lms2.repositories.InstructorProfileRepository;
import edu.unimagdalena.lms2.repositories.InstructorRepository;
import edu.unimagdalena.lms2.services.implement.InstructorProfileServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InstructorProfileServiceImplTest {

    @Mock
    InstructorProfileRepository profileRepository;

    @Mock
    InstructorRepository instructorRepository;

    @InjectMocks
    InstructorProfileServiceImpl profileService;

    @Test
    void create_deberiaGuardarYRetornarResponse() {
        var instructorId = UUID.randomUUID();
        var profileId = UUID.randomUUID();
        var instructor = Instructor.builder().id(instructorId).fullName("Carlos López")
                .email("carlos@unimagdalena.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var req = new InstructorProfileRequest("3001234567", "Docente de sistemas", instructorId);
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));
        when(profileRepository.save(any(InstructorProfile.class))).thenAnswer(inv -> {
            InstructorProfile p = inv.getArgument(0);
            p.setId(profileId);
            return p;
        });

        var res = profileService.create(req);

        assertThat(res.id()).isEqualTo(profileId);
        assertThat(res.phone()).isEqualTo("3001234567");
        assertThat(res.instructorFullName()).isEqualTo("Carlos López");
        verify(profileRepository).save(any(InstructorProfile.class));
    }

    @Test
    void create_cuandoInstructorNoExiste_deberiaLanzarExcepcion() {
        var instructorId = UUID.randomUUID();
        var req = new InstructorProfileRequest("300", "bio", instructorId);
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> profileService.create(req))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining(instructorId.toString());
    }

    @Test
    void findById_cuandoExiste_deberiaRetornarResponse() {
        var profileId = UUID.randomUUID();
        var instructor = Instructor.builder().id(UUID.randomUUID()).fullName("Carlos López")
                .email("carlos@unimagdalena.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var profile = InstructorProfile.builder().id(profileId)
                .phone("3001234567").bio("Docente de sistemas").instructor(instructor).build();
        when(profileRepository.findById(profileId)).thenReturn(Optional.of(profile));

        var res = profileService.findById(profileId);

        assertThat(res.id()).isEqualTo(profileId);
        assertThat(res.bio()).isEqualTo("Docente de sistemas");
    }

    @Test
    void findById_cuandoNoExiste_deberiaLanzarExcepcion() {
        var profileId = UUID.randomUUID();
        when(profileRepository.findById(profileId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> profileService.findById(profileId))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void findAll_deberiaRetornarLista() {
        var instructor = Instructor.builder().id(UUID.randomUUID()).fullName("Carlos López")
                .email("carlos@unimagdalena.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var profile = InstructorProfile.builder().id(UUID.randomUUID())
                .phone("3001234567").bio("Docente de sistemas").instructor(instructor).build();
        when(profileRepository.findAll()).thenReturn(List.of(profile));

        var res = profileService.findAll();

        assertThat(res).hasSize(1);
    }

    @Test
    void update_deberiaActualizarCampos() {
        var profileId = UUID.randomUUID();
        var instructor = Instructor.builder().id(UUID.randomUUID()).fullName("Carlos López")
                .email("carlos@unimagdalena.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var existing = InstructorProfile.builder().id(profileId)
                .phone("3001234567").bio("Docente de sistemas").instructor(instructor).build();
        var req = new InstructorProfileRequest("3109999999", "Nueva bio", instructor.getId());
        when(profileRepository.findById(profileId)).thenReturn(Optional.of(existing));
        when(instructorRepository.findById(instructor.getId())).thenReturn(Optional.of(instructor));
        when(profileRepository.save(any(InstructorProfile.class))).thenAnswer(inv -> inv.getArgument(0));

        var res = profileService.update(profileId, req);

        assertThat(res.phone()).isEqualTo("3109999999");
        assertThat(res.bio()).isEqualTo("Nueva bio");
    }

    @Test
    void delete_cuandoExiste_deberiaEliminar() {
        var profileId = UUID.randomUUID();
        when(profileRepository.existsById(profileId)).thenReturn(true);

        profileService.delete(profileId);

        verify(profileRepository).deleteById(profileId);
    }

    @Test
    void findByPhone_deberiaRetornarResponse() {
        var instructor = Instructor.builder().id(UUID.randomUUID()).fullName("Carlos López")
                .email("carlos@unimagdalena.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var profile = InstructorProfile.builder().id(UUID.randomUUID())
                .phone("3001234567").bio("Docente de sistemas").instructor(instructor).build();
        when(profileRepository.findByPhone("3001234567")).thenReturn(Optional.of(profile));

        var res = profileService.findByPhone("3001234567");

        assertThat(res.phone()).isEqualTo("3001234567");
    }
}
