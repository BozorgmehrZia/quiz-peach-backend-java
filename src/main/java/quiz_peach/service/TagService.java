package quiz_peach.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import quiz_peach.domain.dto.CreateTagRequestDTO;
import quiz_peach.domain.dto.TagDTO;
import quiz_peach.domain.entities.Tag;
import quiz_peach.exceptions.InputInvalidException;
import quiz_peach.repository.TagRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public List<TagDTO> getTags(String name, String sort) {
        List<Tag> tags = tagRepository.findByNameContainingIgnoreCaseOrderByQuestionNumber(name, sort.equalsIgnoreCase("desc"));
        return tags.stream()
                .map(tag -> new TagDTO(tag.getId(), tag.getName(), tag.getQuestionNumber()))
                .collect(Collectors.toList());
    }

    public TagDTO createTag(CreateTagRequestDTO tagRequestDTO) {
        String name = tagRequestDTO.name();
        if (name == null || name.trim().isEmpty()) {
            throw new InputInvalidException("Name is required and must be a string.");
        }

        if (tagRepository.existsByName(name)) {
            throw new InputInvalidException("Tag already exists.");
        }

        Tag tag = tagRepository.save(Tag.builder().name(name).questionNumber(0).build());

        return new TagDTO(tag.getId(), tag.getName(), tag.getQuestionNumber());
    }
}
