package com.afba.imageplus.repository.sqlserver;

import com.afba.imageplus.model.sqlserver.EmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Integer> {

    Optional<EmailTemplate> findByTemplateNameAndIsActive(String templateName, Boolean isActive);

}
