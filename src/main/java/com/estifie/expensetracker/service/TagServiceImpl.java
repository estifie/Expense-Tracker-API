package com.estifie.expensetracker.service;

import com.estifie.expensetracker.enums.Permission;
import com.estifie.expensetracker.exception.auth.AuthorizationException;
import com.estifie.expensetracker.exception.tag.TagNotFoundException;
import com.estifie.expensetracker.model.Tag;
import com.estifie.expensetracker.repository.TagRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final UserService userService;

    public TagServiceImpl(TagRepository tagRepository, UserService userService) {
        this.tagRepository = tagRepository;
        this.userService = userService;
    }

    public void create(String tagName) {
        Optional<Tag> tag = tagRepository.findByName(tagName);

        if (tag.isEmpty()) {
            tagRepository.save(new Tag(tagName));
        }
    }

    public Optional<Tag> findById(String id, boolean fetchDeleted) {
        return fetchDeleted ? tagRepository.findById(id) : tagRepository.findByIdAndDeletedAtIsNull(id);
    }

    public List<Tag> findAll(boolean fetchDeleted) {
        return fetchDeleted ? tagRepository.findAll() : tagRepository.findAllByDeletedAtIsNull();
    }

    public void delete(String id, boolean hardDelete) {
        if (!hardDelete) {
            Tag tag = tagRepository.findById(id)
                    .orElseThrow(TagNotFoundException::new);
            tag.setDeletedAt(LocalDateTime.now());
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        boolean canHardDelete =
                userService.hasPermission(authentication.getName(),
                        Permission.HARD_DELETE_TAG.name());

        if (!canHardDelete) {
            throw new AuthorizationException("You do not have permission to perform this action");
        }

        tagRepository.deleteById(id);
    }

    public Optional<Tag> findByName(String name) {
        return tagRepository.findByName(name);
    }
}
