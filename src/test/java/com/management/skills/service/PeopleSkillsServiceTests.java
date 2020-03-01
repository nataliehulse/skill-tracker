package com.management.skills.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.management.skills.entity.PeopleEntity;
import com.management.skills.entity.PeopleSkillsEntity;
import com.management.skills.entity.SkillsEntity;
import com.management.skills.model.PeopleSkills;
import com.management.skills.model.dto.SkillDto;
import com.management.skills.model.dto.SkillLevelDto;
import com.management.skills.model.Skills;
import com.management.skills.repository.PeopleRepository;
import com.management.skills.repository.PeopleSkillsRepository;
import com.management.skills.repository.SkillsRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
public class PeopleSkillsServiceTests {

  private PeopleSkillsService service;

  @Mock
  private PeopleRepository peopleRepository;

  @Mock
  private PeopleSkillsRepository peopleSkillsRepository;

  @Mock
  private SkillsRepository skillsRepository;

  private static final String EXPERT ="Expert";
  private static final String PRACTITIONER = "Practitioner";
  private static final String WORKING = "Working";
  private static final String AWARENESS = "Awareness";
  private static final PeopleEntity PERSON1 = PeopleEntity.builder()
      .emailAddress("person1@email.com").firstName("Person").lastName("One")
      .peopleSkillsEntityList(Collections.singletonList(
          PeopleSkillsEntity.builder().skillsEntity(
              SkillsEntity.builder().description("Java").build())
              .level(EXPERT)
              .build()))
      .build();
  private static final PeopleEntity PERSON2 =  PeopleEntity.builder()
      .emailAddress("person2@email.com").firstName("Person").lastName("Two")
      .peopleSkillsEntityList(Collections.singletonList(
          PeopleSkillsEntity.builder().skillsEntity(
              SkillsEntity.builder().description("Spring").build())
              .level(PRACTITIONER)
              .build()))
      .build();

  @Before
  public void before() {
    service = new PeopleSkillsService(peopleRepository, peopleSkillsRepository, skillsRepository);
  }

  @Test
  public void testFindPeopleByIdReturnsObject() {

    when(peopleRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.of(PERSON1));

    Optional<PeopleEntity> result = service.findPeopleById(1L);

    assertTrue(result.isPresent());
    assertEquals(PERSON1, result.get());
  }

  @Test
  public void testFindPeopleByIdReturnsEmptyObject() {

    when(peopleRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.empty());

    Optional<PeopleEntity> result = service.findPeopleById(1L);

    assertFalse(result.isPresent());
  }

  @Test
  public void testFindSkillReturnsObject() {

    SkillsEntity skillsEntity = SkillsEntity.builder().description("Java").build();

    when(skillsRepository.findByDescriptionIgnoreCase("Java"))
        .thenReturn(Optional.of(skillsEntity));

    Optional<SkillsEntity> result = service.findSkill("Java");

    assertTrue(result.isPresent());
    assertEquals(skillsEntity, result.get());
  }

  @Test
  public void testFindSkillReturnsEmpty() {

    when(skillsRepository.findByDescriptionIgnoreCase("Java"))
        .thenReturn(Optional.empty());

    Optional<SkillsEntity> result = service.findSkill("Java");

    assertFalse(result.isPresent());
  }

  @Test
  public void testAddNewSkillReturnsObject() {

    List<SkillsEntity> skillsEntityList = Arrays.asList(
        SkillsEntity.builder().description("Java").build(),
        SkillsEntity.builder().description("Spring").build(),
        SkillsEntity.builder().description("Postgres").build()
    );

    when(skillsRepository.findAll()).thenReturn(skillsEntityList);

    List<SkillsEntity> result = service.addNewSkill("Postgres");

    assertEquals(skillsEntityList, result);
    verify(skillsRepository, times(1)).save(Mockito.any(SkillsEntity.class));
  }

  @Test
  public void testAddSkillToPersonReturnsObject() {

    when(peopleRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.of(PERSON1));

    SkillsEntity skillsEntity = SkillsEntity.builder().description("Spring").build();

    when(service.findSkill(Mockito.anyString()))
        .thenReturn(Optional.of(skillsEntity));

    PeopleSkillsEntity peopleSkillsEntity =
        PeopleSkillsEntity.builder().skillsEntity(
            SkillsEntity.builder().description("Spring").build())
            .level(WORKING)
            .build();

    when(peopleSkillsRepository.save(Mockito.any(PeopleSkillsEntity.class))).thenReturn(peopleSkillsEntity);

    SkillLevelDto dto = SkillLevelDto.builder().skill("Spring").level(WORKING).build();

    Optional<PeopleSkillsEntity> result = service.addSkillToPeople
        (1L, dto);

    assertTrue(result.isPresent());
    assertEquals(peopleSkillsEntity, result.get());
  }

  @Test
  public void testAddSkillToPersonWithNoExistingSkillReturnsEmpty() {

    when(peopleRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.of(PERSON1));

    when(service.findSkill(Mockito.anyString()))
        .thenReturn(Optional.empty());

    SkillLevelDto dto = SkillLevelDto.builder().skill("Spring").level(WORKING).build();

    Optional<PeopleSkillsEntity> result = service.addSkillToPeople
        (1L, dto);

    assertFalse(result.isPresent());
  }

  @Test
  public void testAddSkillToPeopleWithNoExistingPersonReturnsEmpty() {

    when(peopleRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.empty());

    SkillLevelDto dto = SkillLevelDto.builder().skill("Spring").level(WORKING).build();

    Optional<PeopleSkillsEntity> result = service.addSkillToPeople
        (1L, dto);

    assertFalse(result.isPresent());
  }

  @Test
  public void testFindPeopleByEmailAddressReturnsObject() {

    when(peopleRepository.findByEmailAddressIgnoreCase("person1@email.com"))
        .thenReturn(Optional.of(PERSON1));

    PeopleSkills expectedPeopleSkills =
        PeopleSkills.builder().emailAddress("person1@email.com").firstName("Person").lastName("One")
            .skillsList(Collections.singletonList(
                Skills.builder().description("Java").level(EXPERT)
                    .build()))
            .build();

    Optional<PeopleSkills> result = service.findPeopleByEmailAddress("person1@email.com");

    assertTrue(result.isPresent());
    assertEquals(expectedPeopleSkills, result.get());
  }

  @Test
  public void testFindPeopleByEmailAddressReturnsEmptyObject() {

    when(peopleRepository.findByEmailAddressIgnoreCase("person1@email.com"))
        .thenReturn(Optional.empty());

    Optional<PeopleSkills> result = service.findPeopleByEmailAddress("person1@email.com");

    assertFalse(result.isPresent());
  }

  @Test
  public void testFindAllPeopleReturnsList() {

    List<PeopleEntity> peopleEntityList = Arrays.asList(PERSON1,PERSON2);

    when(peopleRepository.findAll()).thenReturn(peopleEntityList);

    List<PeopleSkills> expectedPeopleSkills = Arrays.asList(
        PeopleSkills.builder().emailAddress("person1@email.com").firstName("Person").lastName("One")
            .skillsList(Collections.singletonList(
                Skills.builder().description("Java").level(EXPERT)
                    .build()))
            .build(),
        PeopleSkills.builder().emailAddress("person2@email.com").firstName("Person").lastName("Two")
            .skillsList(Collections.singletonList(
                Skills.builder().description("Spring").level(PRACTITIONER)
                    .build()))
            .build()
    );

    Optional<List<PeopleSkills>> result = service.findAllPeople();

    assertTrue(result.isPresent());
    assertEquals(expectedPeopleSkills, result.get());
  }

  @Test
  public void testFindAllPeopleReturnsEmptyList() {

    when(peopleRepository.findAll()).thenReturn(Collections.emptyList());

    Optional<List<PeopleSkills>> result = service.findAllPeople();

    assertEquals(0, result.get().size());
  }

  @Test
  public void testFindAllSkillsReturnsList() {

    List<SkillsEntity> skillsEntityList = Arrays.asList(
        SkillsEntity.builder().description("Java").build(),
        SkillsEntity.builder().description("Spring").build()
    );

    when(skillsRepository.findAll()).thenReturn(skillsEntityList);

    Optional<List<SkillsEntity>> result = service.findAllSkills();

    assertTrue(result.isPresent());
    assertEquals(skillsEntityList, result.get());
  }

  @Test
  public void testFindAllSkillsReturnsEmptyList() {

    when(skillsRepository.findAll()).thenReturn(Collections.emptyList());

    Optional<List<SkillsEntity>> result = service.findAllSkills();

    assertEquals(0, result.get().size());
  }

  @Test
  public void testUpdateSkillLevelReturnsObject() {

    when(peopleRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.of(PERSON1));

    PeopleSkillsEntity peopleSkillsEntity =
        PeopleSkillsEntity.builder().skillsEntity(
            SkillsEntity.builder().description("Spring").build())
            .level(AWARENESS)
            .build();

    when(peopleSkillsRepository.save(Mockito.any(PeopleSkillsEntity.class))).thenReturn(peopleSkillsEntity);

    SkillLevelDto dto = SkillLevelDto.builder().skill("Spring").level(AWARENESS).build();
    Optional<PeopleSkillsEntity> result = service.updateSkillLevel
        (1L, dto);

    assertTrue(result.isPresent());
    assertEquals(peopleSkillsEntity, result.get());
  }

  @Test
  public void testUpdateSkillLevelReturnsEmptyObject() {

    when(peopleRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.empty());

    SkillLevelDto dto = SkillLevelDto.builder().skill("Spring").level(WORKING).build();
    Optional<PeopleSkillsEntity> result = service.updateSkillLevel
        (1L, dto);

    assertFalse(result.isPresent());
  }

  @Test
  public void testRemoveSkillFromPeopleReturns1() {

    PeopleEntity peopleEntity =
        PeopleEntity.builder().emailAddress("person1@email.com").firstName("Person").lastName("One")
            .peopleSkillsEntityList(Arrays.asList(
                PeopleSkillsEntity.builder().id(1L)
                    .skillsEntity(
                    SkillsEntity.builder().description("Java").build())
                    .level(EXPERT)
                    .build(),
                PeopleSkillsEntity.builder().id(2L)
                    .skillsEntity(
                    SkillsEntity.builder().description("Spring").build())
                    .level(PRACTITIONER)
                    .build()
            )).build();

    when(peopleRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.of(peopleEntity));

    when(peopleSkillsRepository.deleteById(Mockito.anyLong())).thenReturn(1L);

    SkillDto dto = SkillDto.builder().skill("Spring").build();
    Long result = service.removeSkillFromPeople(1L, dto);

    assertEquals(1, result.intValue());
    verify(peopleSkillsRepository, times(1)).deleteById(2L);
  }

  @Test
  public void testRemoveSkillFromPeopleReturns0() {

    when(peopleRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.empty());

    SkillDto dto = SkillDto.builder().skill("Spring").build();
    Long result = service.removeSkillFromPeople(1L, dto);

    assertEquals(0, result.intValue());
    verify(peopleSkillsRepository, times(0)).deleteById(Mockito.anyLong());
  }

  @Test
  public void testRemovePeopleByIdReturns1() {

    when(peopleRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.of(PERSON1));

    when(peopleRepository.deleteById(Mockito.anyLong())).thenReturn(1L);

    Long result = service.removePeopleById(1L);

    assertEquals(1, result.intValue());
    verify(peopleRepository, times(1)).deleteById(Mockito.anyLong());
  }

  @Test
  public void testRemovePeopleByIdReturns0() {

    when(peopleRepository.findById(Mockito.anyLong()))
        .thenReturn(Optional.empty());

    Long result = service.removePeopleById(1L);

    assertEquals(0, result.intValue());
    verify(peopleRepository, times(0)).deleteById(Mockito.anyLong());
  }

  @Test
  public void testDeleteSkillReturns1() {

    SkillsEntity skillsEntity = SkillsEntity.builder().id(1L).description("Java").build();

    when(skillsRepository.findByDescriptionIgnoreCase("Java"))
        .thenReturn(Optional.of(skillsEntity));

    List<PeopleSkillsEntity> peopleSkillsEntity = Collections.singletonList(PeopleSkillsEntity.builder()
        .skillsEntity(skillsEntity)
        .build());
    when(peopleSkillsRepository.findBySkillsEntity(Mockito.any(SkillsEntity.class)))
        .thenReturn(Optional.of(peopleSkillsEntity));

    when(skillsRepository.deleteById(Mockito.anyLong())).thenReturn(1L);
    Long result = service.removeSkill("Java");

    assertEquals(1, result.intValue());
    verify(peopleSkillsRepository, times(1)).delete(Mockito.any(PeopleSkillsEntity.class));
    verify(skillsRepository, times(1)).deleteById(Mockito.any(Long.class));
  }

  @Test
  public void testDeleteSkillReturns0() {

    when(skillsRepository.findByDescriptionIgnoreCase("Java"))
        .thenReturn(Optional.empty());

    Long result = service.removeSkill("Java");

    assertEquals(0, result.intValue());
    verify(skillsRepository, times(0)).deleteById(Mockito.any(Long.class));
  }

}
