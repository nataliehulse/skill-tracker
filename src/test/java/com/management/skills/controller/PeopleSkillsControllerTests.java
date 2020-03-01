package com.management.skills.controller;

import static com.management.skills.controller.PeopleSkillsController.ADD_NEW_SKILLS_ENDPOINT;
import static com.management.skills.controller.PeopleSkillsController.ADD_PEOPLE_SKILL_ENDPOINT;
import static com.management.skills.controller.PeopleSkillsController.DISPLAY_ALL_PEOPLE_ENDPOINT;
import static com.management.skills.controller.PeopleSkillsController.DISPLAY_ALL_SKILLS_ENDPOINT;
import static com.management.skills.controller.PeopleSkillsController.DISPLAY_PEOPLE_BY_EMAIL_ENDPOINT;
import static com.management.skills.controller.PeopleSkillsController.REMOVE_PEOPLE_ENDPOINT;
import static com.management.skills.controller.PeopleSkillsController.REMOVE_PEOPLE_SKILL_ENDPOINT;
import static com.management.skills.controller.PeopleSkillsController.REMOVE_SKILL_ENDPOINT;
import static com.management.skills.controller.PeopleSkillsController.UPDATE_SKILL_LEVEL_ENDPOINT;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.management.skills.entity.PeopleEntity;
import com.management.skills.entity.PeopleSkillsEntity;
import com.management.skills.entity.SkillsEntity;
import com.management.skills.model.PeopleSkills;
import com.management.skills.model.dto.SkillDto;
import com.management.skills.model.dto.SkillLevelDto;
import com.management.skills.model.Skills;
import com.management.skills.service.PeopleSkillsService;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringJUnit4ClassRunner.class)
public class PeopleSkillsControllerTests {

  private MockMvc mockMvc;

  ObjectMapper mapper = new ObjectMapper();

  private PeopleSkillsController controller;

  @Mock
  private PeopleSkillsService peopleSkillsService;

  private static final String EXPERT ="Expert";
  private static final String PRACTITIONER = "Practitioner";
  private static final String WORKING = "Working";
  private static final String AWARENESS = "Awareness";

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);

    mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
    controller = Mockito.spy(new PeopleSkillsController(
        peopleSkillsService));

    mockMvc = standaloneSetup(controller).build();
  }

  @Test
  public void test_addNewSkill()
      throws Exception {

    List<SkillsEntity> skillsEntityList = Arrays.asList(
        SkillsEntity.builder().description("Java").build(),
        SkillsEntity.builder().description("Spring").build(),
        SkillsEntity.builder().description("Postgres").build()
    );

    when(peopleSkillsService.addNewSkill("Postgres"))
        .thenReturn(skillsEntityList);

    mockMvc
        .perform(MockMvcRequestBuilders.post(ADD_NEW_SKILLS_ENDPOINT, "Postgres"))
        .andExpect(handler().methodName("addNewSkill"))
        .andExpect(status().isOk())
        .andExpect(content().string(is(equalTo(mapper.writeValueAsString(skillsEntityList)))));
  }

  @Test
  public void test_addPeopleSkill()
      throws Exception {

    PeopleSkillsEntity peopleSkills = PeopleSkillsEntity.builder()
        .peopleEntity(PeopleEntity.builder()
            .emailAddress("person1@email.com").firstName("Person").lastName("One").build())
        .skillsEntity(SkillsEntity.builder().description("Spring").build())
        .level(WORKING)
        .build();

    when(peopleSkillsService.addSkillToPeople(Mockito.anyLong(), Mockito.any(SkillLevelDto.class)))
        .thenReturn(Optional.of(peopleSkills));

    mockMvc
        .perform(MockMvcRequestBuilders.post(ADD_PEOPLE_SKILL_ENDPOINT, 1)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(validSkillsDtoRequestAsJson()))
        .andExpect(handler().methodName("addPeopleSkill"))
        .andExpect(status().isOk())
        .andExpect(content().string(is(equalTo("Row saved"))));
  }

  @Test
  public void test_addPeopleSkill_returns_empty()
      throws Exception {

    when(peopleSkillsService.addSkillToPeople(Mockito.anyLong(), Mockito.any(SkillLevelDto.class)))
        .thenReturn(Optional.empty());

    mockMvc
        .perform(MockMvcRequestBuilders.post(ADD_PEOPLE_SKILL_ENDPOINT, 1)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(validSkillsDtoRequestAsJson()))
        .andExpect(handler().methodName("addPeopleSkill"))
        .andExpect(status().isOk())
        .andExpect(content().string(is(equalTo("Row not saved"))));
  }

  @Test
  public void test_displayPeopleSkillsByEmailAddress()
      throws Exception {

    PeopleSkills peopleSkills = PeopleSkills.builder()
        .emailAddress("person1@email.com").firstName("Person").lastName("One")
        .skillsList(Arrays.asList(
            Skills.builder().description("Java").level(EXPERT)
                .build(),
            Skills.builder().description("Spring").level(WORKING)
                .build()
        ))
        .build();

    when(peopleSkillsService.findPeopleByEmailAddress(Mockito.anyString()))
        .thenReturn(Optional.of(peopleSkills));

    mockMvc
        .perform(MockMvcRequestBuilders.get(DISPLAY_PEOPLE_BY_EMAIL_ENDPOINT, "test@test"))
        .andExpect(handler().methodName("displayPeopleSkillsByEmailAddress"))
        .andExpect(status().isOk())
        .andExpect(content().string(is(equalTo(mapper.writeValueAsString(peopleSkills)))));
  }

  @Test
  public void test_displayPeopleSkillsByEmailAddress_returns_empty()
      throws Exception {

    when(peopleSkillsService.findPeopleByEmailAddress(Mockito.anyString()))
        .thenReturn(Optional.empty());

    mockMvc
        .perform(MockMvcRequestBuilders.get(DISPLAY_PEOPLE_BY_EMAIL_ENDPOINT, "test@test"))
        .andExpect(handler().methodName("displayPeopleSkillsByEmailAddress"))
        .andExpect(status().isOk())
        .andExpect(content().string(is(equalTo(mapper.writeValueAsString(new PeopleSkills())))));
  }


  @Test
  public void test_displayAllPeople()
      throws Exception {

    List<PeopleSkills> peopleSkills = Arrays.asList(
        PeopleSkills.builder()
            .emailAddress("person1@email.com").firstName("Person").lastName("One")
            .skillsList(Arrays.asList(
                Skills.builder().description("Java").level(EXPERT)
                    .build(),
                Skills.builder().description("Spring").level(WORKING)
                    .build()
            ))
            .build(),
        PeopleSkills.builder()
            .emailAddress("person2@email.com").firstName("Person").lastName("Two")
            .skillsList(Arrays.asList(
                Skills.builder().description("Java").level(PRACTITIONER)
                    .build(),
                Skills.builder().description("Spring").level(AWARENESS)
                    .build()
            ))
            .build());

    when(peopleSkillsService.findAllPeople())
        .thenReturn(Optional.of(peopleSkills));

    mockMvc
        .perform(MockMvcRequestBuilders.get(DISPLAY_ALL_PEOPLE_ENDPOINT))
        .andExpect(handler().methodName("displayAllPeople"))
        .andExpect(status().isOk())
        .andExpect(content().string(is(equalTo(mapper.writeValueAsString(peopleSkills)))));
  }

  @Test
  public void test_displayAllSkills()
      throws Exception {

    List<SkillsEntity> skillsEntityList = Arrays.asList(
        SkillsEntity.builder().description("Java").build(),
        SkillsEntity.builder().description("Spring").build(),
        SkillsEntity.builder().description("Postgres").build()
    );

    when(peopleSkillsService.findAllSkills())
        .thenReturn(Optional.of(skillsEntityList));

    mockMvc
        .perform(MockMvcRequestBuilders.get(DISPLAY_ALL_SKILLS_ENDPOINT))
        .andExpect(handler().methodName("displayAllSkills"))
        .andExpect(status().isOk())
        .andExpect(content().string(is(equalTo(mapper.writeValueAsString(skillsEntityList)))));
  }

  @Test
  public void test_updateSkillLevel()
      throws Exception {

    PeopleSkillsEntity peopleSkills = PeopleSkillsEntity.builder()
        .peopleEntity(PeopleEntity.builder()
            .emailAddress("person1@email.com").firstName("Person").lastName("One").build())
        .skillsEntity(SkillsEntity.builder().description("Spring").build())
        .level(AWARENESS)
        .build();

    when(peopleSkillsService.updateSkillLevel(Mockito.anyLong(), Mockito.any(SkillLevelDto.class)))
        .thenReturn(Optional.of(peopleSkills));

    mockMvc
        .perform(MockMvcRequestBuilders.put(UPDATE_SKILL_LEVEL_ENDPOINT, 1)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(validSkillLevelDtoRequestAsJson()))
        .andExpect(handler().methodName("updateSkillLevel"))
        .andExpect(status().isOk())
        .andExpect(content().string(is(equalTo("Record updated"))));
  }

  private String validSkillLevelDtoRequestAsJson() throws JsonProcessingException {
    return mapper.writeValueAsString(SkillLevelDto.builder()
        .skill("Java")
        .level("Expert")
        .build());
  }

  @Test
  public void test_updateSkillLevel_returns_empty()
      throws Exception {

    when(peopleSkillsService.updateSkillLevel(Mockito.anyLong(), Mockito.any(SkillLevelDto.class)))
        .thenReturn(Optional.empty());

    mockMvc
        .perform(MockMvcRequestBuilders.put(UPDATE_SKILL_LEVEL_ENDPOINT, 1L)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(validSkillLevelDtoRequestAsJson()))
        .andExpect(handler().methodName("updateSkillLevel"))
        .andExpect(status().isOk())
        .andExpect(content().string(is(equalTo("Record not updated"))));
  }

  @Test
  public void test_removeSkillFromPeople()
      throws Exception {

    when(peopleSkillsService.removeSkillFromPeople(Mockito.anyLong(), Mockito.any(SkillDto.class)))
        .thenReturn(1L);

    mockMvc
        .perform(MockMvcRequestBuilders.delete(REMOVE_PEOPLE_SKILL_ENDPOINT, 1, "Java")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(validSkillsDtoRequestAsJson()))
        .andExpect(handler().methodName("removeSkillFromPeople"))
        .andExpect(status().isOk())
        .andExpect(content().string(is(equalTo("No of rows removed: 1"))));
  }

  private String validSkillsDtoRequestAsJson() throws JsonProcessingException {
    return mapper.writeValueAsString(SkillDto.builder()
        .skill("Java")
        .build());
  }

  @Test
  public void test_removePeopleByUserId()
      throws Exception {

    when(peopleSkillsService.removePeopleById(Mockito.anyLong()))
        .thenReturn(1L);

    mockMvc
        .perform(MockMvcRequestBuilders.delete(REMOVE_PEOPLE_ENDPOINT, 1))
        .andExpect(handler().methodName("removePeopleById"))
        .andExpect(status().isOk())
        .andExpect(content().string(is(equalTo("No of rows removed: 1"))));
  }

  @Test
  public void test_removeSkill()
      throws Exception {

    when(peopleSkillsService.removeSkill(Mockito.anyString()))
        .thenReturn(1L);

    mockMvc
        .perform(MockMvcRequestBuilders.delete(REMOVE_SKILL_ENDPOINT, "skill"))
        .andExpect(handler().methodName("removeSkill"))
        .andExpect(status().isOk())
        .andExpect(content().string(is(equalTo("No of rows removed: 1"))));
  }

}
