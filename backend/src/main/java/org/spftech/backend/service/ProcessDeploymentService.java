package org.spftech.backend.service;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.engine.repository.DeploymentQuery;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Service for deploying BPMN process definitions to Flowable.
 * 
 * <p>This service handles:
 * - File upload and validation
 * - Process deployment using Flowable's RepositoryService
 * - Deployment management operations
 * </p>
 * 
 * @see org.flowable.engine.RepositoryService
 */
@Service
@RequiredArgsConstructor
public class ProcessDeploymentService {

    private final RepositoryService repositoryService;

    /**
     * Deploys an XML file to Flowable.
     * 
     * @param file the multipart file containing the XML process definition
     * @return the deployed process definition information
     * @throws IllegalArgumentException if the file is invalid or has wrong type
     * @throws IOException if there's an error reading the file
     */
    public Deployment deployProcess(MultipartFile file) throws IOException {
        // Validate the file
        validateFile(file);

        // Build and execute the deployment
        DeploymentBuilder deploymentBuilder = buildDeployment(file);
        return deploymentBuilder.deploy();
    }

    /**
     * Lists all deployments in the Flowable engine.
     * 
     * @return list of all deployments
     */
    public List<Deployment> getDeployments() {
        DeploymentQuery query = repositoryService.createDeploymentQuery();
        return query.orderByDeploymentName().asc().list();
    }

    /**
     * Gets a specific deployment by its ID.
     * 
     * @param deploymentId the ID of the deployment to retrieve
     * @return the deployment, or null if not found
     */
    public Deployment getDeployment(String deploymentId) {
        return repositoryService.createDeploymentQuery()
                .deploymentId(deploymentId)
                .singleResult();
    }

    /**
     * Deletes a deployment and optionally its process definitions.
     * 
     * @param deploymentId the ID of the deployment to delete
     * @param cascade true to also delete deployed process definitions, false otherwise
     * @return true if deletion was successful
     */
    public boolean deleteDeployment(String deploymentId, boolean cascade) {
        Deployment deployment = getDeployment(deploymentId);
        if (deployment == null) {
            return false;
        }

        repositoryService.deleteDeployment(deploymentId, cascade);
        return true;
    }

    /**
     * Validates a file for deployment.
     * 
     * @param file the file to validate
     * @throws IllegalArgumentException if the file is invalid
     */
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }

        String filename = file.getOriginalFilename();
        if (filename == null) {
            throw new IllegalArgumentException("File name cannot be null");
        }

        // Support only .bpmn20.xml files
        String lowerCaseFilename = filename.toLowerCase();
        if (!lowerCaseFilename.endsWith(".bpmn20.xml")) {
            throw new IllegalArgumentException("Only .bpmn20.xml files are supported. Received: " + filename);
        }
    }

    /**
     * Builds a Flowable deployment with the given file.
     * 
     * @param file the file to deploy
     * @return DeploymentBuilder configured with the file
     * @throws IOException if there's an error reading the file
     */
    private DeploymentBuilder buildDeployment(MultipartFile file) throws IOException {
        byte[] fileBytes = file.getBytes();
        return repositoryService
            .createDeployment()
            .name(file.getOriginalFilename())
            .addBytes(file.getOriginalFilename(), fileBytes);
    }
}