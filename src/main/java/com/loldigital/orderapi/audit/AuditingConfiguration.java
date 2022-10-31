package com.loldigital.orderapi.audit;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Configuration
@EnableJpaAuditing
public class AuditingConfiguration {
	
	
	@Bean
	public AuditorAware<String> auditorProvider() {
		return new AuditorAwareImpl();
	}

	class AuditorAwareImpl implements AuditorAware<String> {
		Logger logger = LoggerFactory.getLogger(AuditorAwareImpl.class);
		
		@Override
		public Optional<String> getCurrentAuditor() {
			logger.debug("Inside getCurrentAuditor of AuditConfiguration class");
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String userName = "Annonymous User";
			
			if(authentication != null) {
				Object principal = authentication.getPrincipal();
				
				if(principal instanceof UserDetails) {
					UserDetails userDetail = (UserDetails)principal;
					userName = userDetail.getUsername();
				}

			}
			
			logger.debug("Successfully set the auditor. Completed getCurrentAuditor of AuditConfiguration class");
			return Optional.of(userName);

		}

	}
}