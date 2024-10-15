package com.estifie.expensetracker.repository;

import com.estifie.expensetracker.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, String> {
    Optional<Tag> findByName(String name);

    Optional<Tag> findByIdAndDeletedAtIsNull(String id);

    List<Tag> findAllByDeletedAtIsNull();
}
