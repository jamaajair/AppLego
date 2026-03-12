package org.spftech.backend;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.spftech.backend.service.ProcessDeploymentService;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BackendApplicationTests {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ProcessDeploymentService processDeploymentService;

    @Test
    void contextLoads() {
    }

    @Test
    void testAutomaticProcessDeployment() {
        // Test that the sample process is automatically deployed from resources/processes/
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
                .list();
        
        assertFalse(processDefinitions.isEmpty(), "No process definitions found - automatic deployment failed");
        
        // Find our specific process
        ProcessDefinition sampleProcess = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("sampleProcess")
                .singleResult();
        
        assertNotNull(sampleProcess, "Sample process was not automatically deployed");
        assertEquals("Sample Process", sampleProcess.getName());
        assertEquals("sampleProcess", sampleProcess.getKey());
    }

    @Test
    void testUserInitiatedProcessDeployment() throws Exception {
        // Test user-initiated deployment of a BPMN process
        // Using a valid BPMN 2.0 XML based on the sample-process.bpmn20.xml
        String bpmnContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<definitions xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" " +
                "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                "xmlns:flowable=\"http://flowable.org/bpmn\" " +
                "targetNamespace=\"http://www.example.com/bpmn\" " +
                "xsi:schemaLocation=\"http://www.omg.org/spec/BPMN/20100524/MODEL http://www.omg.org/spec/BPMN/20100524/DTD/BPMN20.xsd\">" +
                "<process id=\"userTaskProcess\" name=\"User Task Process\" isExecutable=\"true\">" +
                "<startEvent id=\"startEvent\" name=\"Start\"/>" +
                "<sequenceFlow id=\"flow1\" sourceRef=\"startEvent\" targetRef=\"task1\"/>" +
                "<userTask id=\"task1\" name=\"Review Document\" flowable:assignee=\"admin\"/>" +
                "<sequenceFlow id=\"flow2\" sourceRef=\"task1\" targetRef=\"endEvent\"/>" +
                "<endEvent id=\"endEvent\" name=\"End\"/>" +
                "</process>" +
                "</definitions>";
        
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "user-task-process.bpmn20.xml",
                "application/xml",
                bpmnContent.getBytes(StandardCharsets.UTF_8)
        );
        
        // Deploy the process
        var deployment = processDeploymentService.deployProcess(file);
        
        // Verify deployment was successful
        assertNotNull(deployment);
        assertNotNull(deployment.getId());
        assertEquals("user-task-process.bpmn20.xml", deployment.getName());
        
        // Verify process definition was created
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("userTaskProcess")
                .singleResult();
        
        assertNotNull(processDefinition, "Process definition was not created");
        assertEquals("User Task Process", processDefinition.getName());
        assertEquals("userTaskProcess", processDefinition.getKey());
        assertEquals(1, processDefinition.getVersion());
    }

    @Test
    void testFileValidation_RejectsNonXmlFiles() {
        // Test that non-XML files are rejected
        String txtContent = "This is not an XML file";
        MockMultipartFile txtFile = new MockMultipartFile(
                "file",
                "process.txt",
                "text/plain",
                txtContent.getBytes(StandardCharsets.UTF_8)
        );
        
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> processDeploymentService.deployProcess(txtFile)
        );
        
        assertTrue(exception.getMessage().contains("Only .xml files are supported"));
    }

    @Test
    void testFileValidation_RejectsEmptyFile() {
        // Test that empty files are rejected
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file",
                "empty.xml",
                "application/xml",
                new byte[0]
        );
        
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> processDeploymentService.deployProcess(emptyFile)
        );
        
        assertTrue(exception.getMessage().contains("File cannot be null or empty"));
    }

    @Test
    void testFileValidation_RejectsNullFile() {
        // Test that null files are rejected
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> processDeploymentService.deployProcess(null)
        );
        
        assertTrue(exception.getMessage().contains("File cannot be null or empty"));
    }

}
