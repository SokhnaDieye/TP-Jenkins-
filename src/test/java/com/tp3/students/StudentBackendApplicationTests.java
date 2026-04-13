package com.tp3.students;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class StudentBackendApplicationTests {

    @Test
    void contextLoads() {
        // Vérifie que le contexte Spring démarre correctement
    }
}
