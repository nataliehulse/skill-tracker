package com.management.skills.repository;

import com.management.skills.entity.SkillsEntity;
import com.management.skills.entity.PeopleEntity;
import com.management.skills.entity.PeopleSkillsEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PeopleSkillsRepository extends JpaRepository<PeopleSkillsEntity, Long> {

  Optional<PeopleSkillsEntity> findByPeopleEntityAndSkillsEntity(PeopleEntity peopleEntity, SkillsEntity skillsEntity);

  Optional<List<PeopleSkillsEntity>> findByPeopleEntity(PeopleEntity peopleEntity);

  Optional<List<PeopleSkillsEntity>> findBySkillsEntity(SkillsEntity skillsEntity);

  Long deleteById(Long id);

}
