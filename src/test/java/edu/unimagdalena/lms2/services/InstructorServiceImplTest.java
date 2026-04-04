package edu.unimagdalena.lms2.services;

import edu.unimagdalena.lms2.dto.InstructorDto.InstructorRequest;
import edu.unimagdalena.lms2.entities.Instructor;
import edu.unimagdalena.lms2.repositories.InstructorRepository;
import edu.unimagdalena.lms2.services.implement.InstructorServiceImpl;
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
class InstructorServiceImplTest {

    @Mock
    InstructorRepository instructorRepository;

    @InjectMocks
    InstructorServiceImpl instructorService;

    @Test
    void create_deberiaGuardarYRetornarInstructorResponse() {
        var id = UUID.randomUUID();
        var req = new InstructorRequest("Carlos López", "carlos@unimagdalena.edu.co");
        when(instructorRepository.save(any(Instructor.class))).thenAnswer(inv -> {
            Instructor i = inv.getArgument(0);
            i.setId(id);
            return i;
        });

        var res = instructorService.create(req);

        assertThat(res.id()).isEqualTo(id);
        assertThat(res.fullName()).isEqualTo("Carlos López");
        assertThat(res.email()).isEqualTo("carlos@unimagdalena.edu.co");
        verify(instructorRepository).save(any(Instructor.class));
    }

    @Test
    void findById_cuandoExiste_deberiaRetornarResponse() {
        var id = UUID.randomUUID();
        var instructor = Instructor.builder().id(id).fullName("Carlos López")
                .email("carlos@unimagdalena.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        when(instructorRepository.findById(id)).thenReturn(Optional.of(instructor));

        var res = instructorService.findById(id);

        assertThat(res.id()).isEqualTo(id);
    }

    @Test
    void findById_cuandoNoExiste_deberiaLanzarExcepcion() {
        var id = UUID.randomUUID();
        when(instructorRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> instructorService.findById(id))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void findAll_deberiaRetornarLista() {
        var instructor = Instructor.builder().id(UUID.randomUUID()).fullName("Carlos López")
                .email("carlos@unimagdalena.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        when(instructorRepository.findAll()).thenReturn(List.of(instructor));

        var res = instructorService.findAll();

        assertThat(res).hasSize(1);
    }

    @Test
    void update_deberiaActualizarCampos() {
        var id = UUID.randomUUID();
        var existing = Instructor.builder().id(id).fullName("Carlos López")
                .email("carlos@unimagdalena.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var req = new InstructorRequest("Carlos Actualizado", "nuevo@email.com");
        when(instructorRepository.findById(id)).thenReturn(Optional.of(existing));
        when(instructorRepository.save(any(Instructor.class))).thenAnswer(inv -> inv.getArgument(0));

        var res = instructorService.update(id, req);

        assertThat(res.fullName()).isEqualTo("Carlos Actualizado");
        assertThat(res.email()).isEqualTo("nuevo@email.com");
    }

    @Test
    void delete_cuandoExiste_deberiaEliminar() {
        var id = UUID.randomUUID();
        when(instructorRepository.existsById(id)).thenReturn(true);

        instructorService.delete(id);

        verify(instructorRepository).deleteById(id);
    }

    @Test
    void findByEmail_deberiaRetornarResponse() {
        var instructor = Instructor.builder().id(UUID.randomUUID()).fullName("Carlos López")
                .email("carlos@unimagdalena.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        when(instructorRepository.findByEmail("carlos@unimagdalena.edu.co")).thenReturn(Optional.of(instructor));

        var res = instructorService.findByEmail("carlos@unimagdalena.edu.co");

        assertThat(res.email()).isEqualTo("carlos@unimagdalena.edu.co");
    }

    @Test
    void findByFullNameIgnoreCase_deberiaRetornarResponse() {
        var instructor = Instructor.builder().id(UUID.randomUUID()).fullName("Carlos López")
                .email("carlos@unimagdalena.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        when(instructorRepository.findByFullNameIgnoreCase("carlos lópez")).thenReturn(Optional.of(instructor));

        var res = instructorService.findByFullNameIgnoreCase("carlos lópez");

        assertThat(res.fullName()).isEqualTo("Carlos López");
    }

    @Test
    void findInstructorsWithActiveCourses_deberiaRetornarLista() {
        var instructor = Instructor.builder().id(UUID.randomUUID()).fullName("Carlos López")
                .email("carlos@unimagdalena.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        when(instructorRepository.findInstructorsWithActiveCourses()).thenReturn(List.of(instructor));

        var res = instructorService.findInstructorsWithActiveCourses();

        assertThat(res).hasSize(1);
    }

    @Test
    void findInstructorsWithProfile_deberiaRetornarLista() {
        var instructor = Instructor.builder().id(UUID.randomUUID()).fullName("Carlos López")
                .email("carlos@unimagdalena.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        when(instructorRepository.findInstructorsWithProfile()).thenReturn(List.of(instructor));

        var res = instructorService.findInstructorsWithProfile();

        assertThat(res).isNotEmpty();
    }
}
