package com.tracker.sgi.integration;

import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertFalse;


@SpringBootTest
@ActiveProfiles("test")
class ContextSmokeTest {

	@Autowired
	EntityManagerFactory emf;

	@Test
	void debeTenerEntidadesRegistradas() {
		var entities = emf.getMetamodel().getEntities();
		entities.forEach(e -> System.out.println(e.getName()));
		assertFalse(entities.isEmpty(), "No hay entidades registradas");
	}
}

