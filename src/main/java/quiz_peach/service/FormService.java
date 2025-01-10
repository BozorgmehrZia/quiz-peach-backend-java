package quiz_peach.service;

import quiz_peach.domain.dto.*;
import quiz_peach.domain.entities.Form;
import quiz_peach.exceptions.FormNotFoundException;
import quiz_peach.mapper.FormMapper;
import quiz_peach.repository.FormRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FormService {

    private final FormRepository formRepository;
    private final FormMapper formMapper;

    public FormService(FormRepository formRepository) {
        this.formRepository = formRepository;
        this.formMapper = FormMapper.INSTANCE;
    }

    public List<FormResponseDTO> getAllForms() {
        return formRepository.findAll().stream()
                .map(formMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public FormResponseDTO createForm(CreateFormRequestDTO formDTO) {
        Form form = formMapper.toEntity(formDTO);
        return formMapper.toResponseDTO(formRepository.save(form));
    }

    public FormResponseDTO getFormById(Long id) {
        Form form = getForm(id);
        return formMapper.toResponseDTO(form);
    }

    public FormResponseDTO updateForm(Long id, UpdateFormRequestDTO updatedFormDTO) {
        Form existingForm = getForm(id);
        Form updatedForm = formMapper.updateEntity(existingForm, updatedFormDTO);
        return formMapper.toResponseDTO(formRepository.save(updatedForm));
    }

    public void deleteForm(Long id) {
        if (!formRepository.existsById(id)) {
            throw new FormNotFoundException("Form not found with id %d".formatted(id));
        }
        formRepository.deleteById(id);
    }

    @Transactional
    public List<FieldResponseDTO> getFieldsByFormId(Long id) {
        Form form = getForm(id);
        return form.getFields().stream()
                .map(formMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<FieldResponseDTO> updateFieldsByFormId(Long id, List<FieldRequestDTO> fieldDTOs) {
        Form form = getForm(id);
        form.setFields(fieldDTOs.stream().map(formMapper::toEntity).collect(Collectors.toList()));
        Form updatedForm = formRepository.save(form);
        return updatedForm.getFields().stream()
                .map(formMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public FormResponseDTO togglePublishForm(Long id) {
        Form form = getForm(id);
        form.setPublished(!form.isPublished());
        return formMapper.toResponseDTO(formRepository.save(form));
    }

    public List<FormResponseDTO> getPublishedForms() {
        return formRepository.findByPublishedTrue().stream()
                .map(formMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    private Form getForm(Long id) {
        return formRepository.findById(id)
                .orElseThrow(() -> new FormNotFoundException("Form not found with id %d".formatted(id)));
    }
}