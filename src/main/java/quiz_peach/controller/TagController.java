package quiz_peach.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import quiz_peach.domain.dto.CreateTagRequestDTO;
import quiz_peach.domain.dto.TagDTO;
import quiz_peach.domain.entities.User;
import quiz_peach.service.TagService;

import java.util.List;

@RestController
@RequestMapping("/api/tag")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;
    @GetMapping
    public ResponseEntity<List<TagDTO>> getTags(@RequestParam(required = false) String name,
                                                @RequestParam(defaultValue = "desc") String sort,
                                                @AuthenticationPrincipal User user) {
        List<TagDTO> tags = tagService.getTags(name, sort);
        return ResponseEntity.ok(tags);
    }

    @PostMapping
    public ResponseEntity<TagDTO> createTag(@RequestBody CreateTagRequestDTO tagRequestDTO,
                                            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(tagService.createTag(tagRequestDTO));
    }

}