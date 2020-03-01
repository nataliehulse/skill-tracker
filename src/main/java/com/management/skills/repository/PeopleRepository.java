package com.management.skills.repository;

import com.management.skills.entity.PeopleEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PeopleRepository extends JpaRepository<PeopleEntity, Long> {

  Optional<PeopleEntity> findByEmailAddressIgnoreCase(String emailAddress);

  Optional<PeopleEntity> findById(Long id);

  Long deleteById(Long id);

}
