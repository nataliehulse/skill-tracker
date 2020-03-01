package com.management.skills.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PeopleSkills {

  private String firstName;
  private String lastName;
  private String emailAddress;
  private List<Skills> skillsList;

}
