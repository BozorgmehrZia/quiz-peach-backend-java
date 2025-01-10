package quiz_peach.controller;

import quiz_peach.domain.dto.*;
import quiz_peach.service.FormService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/forms")
public class FormController {

    private final FormService formService;

    public FormController(FormService formService) {
        this.formService = formService;
    }

    @GetMapping
    public ResponseEntity<List<FormResponseDTO>> getAllForms() {
        return ResponseEntity.ok(formService.getAllForms());
    }

    @PostMapping
    public ResponseEntity<FormResponseDTO> createForm(@RequestBody CreateFormRequestDTO formDTO) {
        return ResponseEntity.ok(formService.createForm(formDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FormResponseDTO> getFormById(@PathVariable Long id) {
        return ResponseEntity.ok(formService.getFormById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FormResponseDTO> updateForm(@PathVariable Long id,
                                                      @RequestBody UpdateFormRequestDTO updatedFormDTO) {
        return ResponseEntity.ok(formService.updateForm(id, updatedFormDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteForm(@PathVariable Long id) {
        formService.deleteForm(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/fields")
    public ResponseEntity<List<FieldResponseDTO>> getFieldsByFormId(@PathVariable Long id) {
        return ResponseEntity.ok(formService.getFieldsByFormId(id));
    }

    @PutMapping("/{id}/fields")
    public ResponseEntity<List<FieldResponseDTO>> updateFieldsByFormId(@PathVariable Long id,
                                                                       @RequestBody List<FieldRequestDTO> fieldDTOs) {
        return ResponseEntity.ok(formService.updateFieldsByFormId(id, fieldDTOs));
    }

    @PostMapping("/{id}/publish")
    public ResponseEntity<FormResponseDTO> togglePublishForm(@PathVariable Long id) {
        return ResponseEntity.ok(formService.togglePublishForm(id));
    }

    @GetMapping("/published")
    public ResponseEntity<List<FormResponseDTO>> getPublishedForms() {
        return ResponseEntity.ok(formService.getPublishedForms());
    }
}