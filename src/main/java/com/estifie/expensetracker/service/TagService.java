package com.estifie.expensetracker.service;

import com.estifie.expensetracker.model.Tag;

import java.util.List;
import java.util.Optional;

public interface TagService {
    void create(String tagName);

    Optional<Tag> findById(String id, boolean fetchDeleted);

    List<Tag> findAll(boolean fetchDeleted);

    void delete(String id, boolean hardDelete);

    Optional<Tag> findByName(String name);
}
