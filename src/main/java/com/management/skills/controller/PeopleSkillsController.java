package com.management.skills.controller;

import com.management.skills.entity.PeopleSkillsEntity;
import com.management.skills.entity.SkillsEntity;
import com.management.skills.model.PeopleSkills;
import com.management.skills.model.dto.SkillDto;
import com.management.skills.model.dto.SkillLevelDto;
import com.management.skills.service.PeopleSkillsService;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class PeopleSkillsController {

  private PeopleSkillsService peopleSkillsService;

  public static final String ADD_NEW_SKILLS_ENDPOINT = "/add-skill/{skill}";
  public static final String ADD_PEOPLE_SKILL_ENDPOINT = "/add-people-skill/{userId}";
  public static final String DISPLAY_PEOPLE_BY_EMAIL_ENDPOINT = "/find-people/{emailAddress:.*}";
  public static final String DISPLAY_ALL_PEOPLE_ENDPOINT = "/display-all-people";
  public static final String DISPLAY_ALL_SKILLS_ENDPOINT = "/display-all-skills";
  public static final String UPDATE_SKILL_LEVEL_ENDPOINT = "/update-skill-level/{userId}";
  public static final String REMOVE_PEOPLE_SKILL_ENDPOINT = "/remove-people-skill/{userId}";
  public static final String REMOVE_PEOPLE_ENDPOINT = "/remove-people/{userId}";
  public static final String REMOVE_SKILL_ENDPOINT = "/remove-skill/{skill}";

  private static final String EMAIL = "emailAddress";
  private static final String USER_ID = "userId";
  private static final String SKILL = "skill";
  private static final String MESSAGE = "No of rows removed: ";

  @Autowired
  public PeopleSkillsController(PeopleSkillsService peopleSkillsService) {
    this.peopleSkillsService = peopleSkillsService;
  }

  @PostMapping(path = ADD_NEW_SKILLS_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
  public List<SkillsEntity> addNewSkill(@PathVariable(SKILL) String skill,
      HttpServletResponse httpResponse) throws IOException {

    List<SkillsEntity> skillsEntityList = peopleSkillsService.addNewSkill(skill);

    if(skillsEntityList.size() == 0) {
      log.error("Skills list not found");
      httpResponse.sendError(404);
      return null; // Error page to be added
    }
    return skillsEntityList;

  }

  @PostMapping(path = ADD_PEOPLE_SKILL_ENDPOINT,  consumes={MediaType.APPLICATION_JSON_VALUE},
      produces = MediaType.APPLICATION_JSON_VALUE)
  public String addPeopleSkill(@PathVariable(USER_ID) Long userId,
      @RequestBody SkillLevelDto skillLevelDto,
      HttpServletResponse httpResponse) throws IOException  {

    Optional<PeopleSkillsEntity> entity =
        peopleSkillsService.addSkillToPeople(userId, skillLevelDto);

        if(entity.isPresent()) {
          return "Row saved";
        } else {
          log.error("PeopleSkillsEntity not found");
          httpResponse.sendError(404);
          return null; // Error page to be added
        }
  }

  @GetMapping(path = DISPLAY_PEOPLE_BY_EMAIL_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
  public PeopleSkills displayPeopleSkillsByEmailAddress(@PathVariable(EMAIL) String emailAddress) {

    return peopleSkillsService.findPeopleByEmailAddress(emailAddress)
        .orElse(PeopleSkills.builder().build());
  }

  @GetMapping(path = DISPLAY_ALL_PEOPLE_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
  public List<PeopleSkills> displayAllPeople() {

    return peopleSkillsService.findAllPeople().orElse(Collections.emptyList());

  }

  @GetMapping(path = DISPLAY_ALL_SKILLS_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
  public List<SkillsEntity> displayAllSkills() {

    return peopleSkillsService.findAllSkills().orElse(Collections.emptyList());

  }

  @PutMapping(path = UPDATE_SKILL_LEVEL_ENDPOINT, consumes={MediaType.APPLICATION_JSON_VALUE},
      produces = MediaType.APPLICATION_JSON_VALUE)
  public String updateSkillLevel(@PathVariable(USER_ID) Long userId,
      @RequestBody SkillLevelDto skillLevelDto,
      HttpServletResponse httpResponse) throws IOException {

    Optional<PeopleSkillsEntity> entity = peopleSkillsService
        .updateSkillLevel(userId, skillLevelDto);

    if(entity.isPresent()) {
      return "Record updated";
    } else {
      log.error("PeopleSkillsEntity not updated");
      httpResponse.sendError(404);
      return null; // Error page to be added
    }
  }

  @DeleteMapping(path = REMOVE_PEOPLE_SKILL_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
  public String removeSkillFromPeople(@PathVariable(USER_ID) Long userId,
      @RequestBody SkillDto dto) {

    Long noRowsRemoved = peopleSkillsService.removeSkillFromPeople(userId, dto);

    return MESSAGE + noRowsRemoved;

  }

  @DeleteMapping(path = REMOVE_PEOPLE_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
  public String removePeopleById(@PathVariable(USER_ID) Long userId) {

    Long noRowsRemoved = peopleSkillsService.removePeopleById(userId);

    return MESSAGE + noRowsRemoved;
  }

  @DeleteMapping(path = REMOVE_SKILL_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
  public String removeSkill(@PathVariable(SKILL) String skill) {

    Long noRowsRemoved = peopleSkillsService.removeSkill(skill);

    return MESSAGE + noRowsRemoved;
  }

}
