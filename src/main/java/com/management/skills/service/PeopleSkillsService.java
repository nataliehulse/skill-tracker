package com.management.skills.service;

import com.management.skills.entity.SkillsEntity;
import com.management.skills.entity.PeopleEntity;
import com.management.skills.entity.PeopleSkillsEntity;
import com.management.skills.model.dto.SkillDto;
import com.management.skills.model.dto.SkillLevelDto;
import com.management.skills.model.Skills;
import com.management.skills.model.PeopleSkills;
import com.management.skills.repository.PeopleRepository;
import com.management.skills.repository.PeopleSkillsRepository;
import com.management.skills.repository.SkillsRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class PeopleSkillsService {

  private PeopleRepository peopleRepository;
  private PeopleSkillsRepository peopleSkillsRepository;
  private SkillsRepository skillsRepository;

  public PeopleSkillsService(PeopleRepository peopleRepository,
      PeopleSkillsRepository peopleSkillsRepository, SkillsRepository skillsRepository) {
    this.peopleRepository = peopleRepository;
    this.peopleSkillsRepository = peopleSkillsRepository;
    this.skillsRepository = skillsRepository;
  }

  public Optional<PeopleEntity> findPeopleById(Long id) {

    return peopleRepository.findById(id);
  }

  public Optional<SkillsEntity> findSkill(String skill) {

    return skillsRepository.findByDescriptionIgnoreCase(skill);
  }

  @Transactional
  public List<SkillsEntity> addNewSkill(String skill) {

    SkillsEntity skillsEntity = SkillsEntity.builder()
        .description(skill)
        .build();

    skillsRepository.save(skillsEntity);

    return skillsRepository.findAll();
  }

  @Transactional
  public Optional<PeopleSkillsEntity> addSkillToPeople(Long userId, SkillLevelDto dto) {

    Optional<PeopleEntity> peopleEntity = findPeopleById(userId);
    if(peopleEntity.isPresent()) {

      Optional<SkillsEntity> skillsEntity = findSkill(dto.getSkill());

      if(skillsEntity.isPresent()) {
        PeopleSkillsEntity peopleSkillsEntity = PeopleSkillsEntity.builder()
            .skillsEntity(skillsEntity.get())
            .peopleEntity(peopleEntity.get())
            .level(dto.getLevel())
            .build();

       return Optional.of(peopleSkillsRepository.save(peopleSkillsEntity));
      }
    }
    return Optional.empty();

  }

  public Optional<PeopleSkills> findPeopleByEmailAddress(String emailAddress) {

    return peopleRepository.findByEmailAddressIgnoreCase(emailAddress)
        .map(this::convertEntityToModel);

  }

  public Optional<List<PeopleSkills>> findAllPeople() {

    return Optional.of(peopleRepository.findAll()
        .stream()
        .map(this::convertEntityToModel)
        .collect(Collectors.toList()));
  }

  public Optional<List<SkillsEntity>> findAllSkills() {

    return Optional.of(skillsRepository.findAll());
  }

  @Transactional
  public Optional<PeopleSkillsEntity> updateSkillLevel(Long userId, SkillLevelDto dto) {

    Optional<PeopleEntity> peopleEntity = findPeopleById(userId);
    if(peopleEntity.isPresent()) {

      PeopleSkillsEntity peopleSkillsEntity = peopleEntity.get().getPeopleSkillsEntityList()
          .stream()
          .filter(f -> dto.getSkill().equalsIgnoreCase(f.getSkillsEntity().getDescription()))
          .findFirst().orElse(PeopleSkillsEntity.builder().build());

      peopleSkillsEntity.setLevel(dto.getLevel());

      return Optional.of(peopleSkillsRepository.save(peopleSkillsEntity));
    }
    return Optional.empty();
  }

  @Transactional
  public Long removeSkillFromPeople(Long userId, SkillDto dto) {

    Optional<PeopleEntity> staffEntity = findPeopleById(userId);
    if(staffEntity.isPresent()) {

      PeopleSkillsEntity peopleSkillsEntity = staffEntity.get().getPeopleSkillsEntityList()
          .stream()
          .filter(f -> dto.getSkill().equalsIgnoreCase(f.getSkillsEntity().getDescription()))
          .findFirst().orElse(PeopleSkillsEntity.builder().build());

      if(peopleSkillsEntity.getId() != null) {
        return peopleSkillsRepository.deleteById(peopleSkillsEntity.getId());
      }

    }
    return 0L;

  }

  @Transactional
  public Long removePeopleById(Long userId) {

    Optional<PeopleEntity> peopleEntity = findPeopleById(userId);
    if(peopleEntity.isPresent()) {

      List<PeopleSkillsEntity> peopleSkillsEntityList =
          peopleEntity.map(PeopleEntity::getPeopleSkillsEntityList).orElse(Collections.emptyList());
      peopleSkillsEntityList.stream().forEach(f ->
          peopleSkillsRepository.deleteById(f.getId()));

      return peopleRepository.deleteById(peopleEntity.get().getId());

      }
    return 0L;
  }

  @Transactional
  public Long removeSkill(String skill) {

    Optional<SkillsEntity> skillsEntity = skillsRepository.findByDescriptionIgnoreCase(skill);

    if(skillsEntity.isPresent()) {

      Optional<List<PeopleSkillsEntity>> peopleSkillsEntityList
          = peopleSkillsRepository.findBySkillsEntity(skillsEntity.get());

      peopleSkillsEntityList.orElse(Collections.emptyList())
          .forEach(f -> peopleSkillsRepository.delete(f));

      return skillsRepository.deleteById(skillsEntity.get().getId());
    }

    return 0L;
  }

  private PeopleSkills convertEntityToModel(PeopleEntity entity){
    return
        PeopleSkills.builder()
        .firstName(entity.getFirstName())
        .lastName(entity.getLastName())
        .emailAddress(entity.getEmailAddress())
        .skillsList(entity.getPeopleSkillsEntityList().stream()
        .map(skill -> Skills.builder()
            .description(skill.getSkillsEntity().getDescription())
            .level(skill.getLevel())
            .build()).collect(Collectors.toList()))
        .build();
  }

}
