package org.delcom.todos;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApplicationTest {
	@Test
<<<<<<< HEAD:src/test/java/org/delcom/todos/ApplicationTests.java
	void mainMethod_ShouldRunSpringApplication() throws Exception {
=======
	@Timeout(10)
	void mainMethod_ShouldRunSpringApplication() {
>>>>>>> 678978baab68e13ebfffdbf3cc654a39d4b0b094:src/test/java/org/delcom/starter/ApplicationTests.java
		// Mock SpringApplication.run untuk test main method
		try (var mockedSpring = mockStatic(SpringApplication.class)) {
			ConfigurableApplicationContext mockContext = mock(ConfigurableApplicationContext.class);
			mockedSpring.when(() -> SpringApplication.run(Application.class, new String[] {}))
					.thenReturn(mockContext);

			// Jalankan main method
			assertDoesNotThrow(() -> Application.main(new String[] {}));

			// Verify SpringApplication.run dipanggil
			mockedSpring.verify(() -> SpringApplication.run(Application.class, new String[] {}));
		}
	}

	@Test
	void contextLoads_ShouldNotThrowException() throws Exception {
		// Test bahwa Spring context bisa dimuat
		assertDoesNotThrow(() -> {
			// Test basic class loading
			Class<?> clazz = Class.forName("org.delcom.todos.Application");
			assertNotNull(clazz);
		});
	}

	@Test
	void todoApplication_ShouldHaveSpringBootAnnotation() throws Exception {
		// Test bahwa class memiliki annotation @SpringBootApplication
		assertNotNull(Application.class
				.getAnnotation(org.springframework.boot.autoconfigure.SpringBootApplication.class));
	}

	@Test
	void todoApplication_CanBeInstantiated() throws Exception {
		// Test bahwa kita bisa membuat instance Application
		assertDoesNotThrow(() -> {
			Application app = new Application();
			assertNotNull(app);
		});
	}
}
