package com.avans.sofa3devops;

import com.avans.sofa3devops.domain.tools.AssemblyScanner;
import com.avans.sofa3devops.domainservices.exceptions.AssemblyException;
import com.avans.sofa3devops.domainservices.exceptions.PipelineException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class ExceptionTest {

    @Test
    void AssemblyExceptionTest() throws AssemblyException {
        String nonExistingPackage = "com.this.packagedoes.not.exist";

        assertThrows(AssemblyException.class, () -> {
            AssemblyScanner.getAllClasses(nonExistingPackage);
        });
    }

    @Test
    void PipelineExceptionTest() {
        // Arrange
        String errorMessage = "Test error message";

        // Act
        PipelineException exception = new PipelineException(errorMessage);

        // Assert
        assertEquals(errorMessage, exception.getMessage());
    }
}
