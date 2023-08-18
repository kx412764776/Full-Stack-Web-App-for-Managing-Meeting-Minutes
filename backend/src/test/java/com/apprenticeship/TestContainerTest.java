package com.apprenticeship;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class TestContainerTest extends AbstractTestContainerWithDatabase {

    @Test
    void canStartPostgresDB() {
        assertThat(postgreSQLContainer.isRunning()).isTrue();
        assertThat(postgreSQLContainer.isCreated()).isTrue();
    }
}
