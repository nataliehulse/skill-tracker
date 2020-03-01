package com.management.skills.repository;

import com.management.skills.entity.SkillsEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillsRepository extends JpaRepository<SkillsEntity, Long> {

  Optional<SkillsEntity> findByDescriptionIgnoreCase(String skill);

  Long deleteById(Long id);

}
