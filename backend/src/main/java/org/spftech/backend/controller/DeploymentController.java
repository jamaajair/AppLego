package org.spftech.backend.controller;

import org.flowable.engine.repository.Deployment;
import org.spftech.backend.dto.DeploymentDto;
import org.spftech.backend.service.ProcessDeploymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * REST controller for managing Flowable process deployments.
 * 
 * <p>Provides endpoints for:
 * - Uploading and deploying BPMN process definitions
 * - Listing all deployments
 * - Retrieving specific deployment details
 * - Deleting deployments
 * </p>
 * 
 * @see ProcessDeploymentService
 */
@RestController
@RequestMapping("/api/deployments")
@RequiredArgsConstructor
public class DeploymentController {

    private final org.spftech.backend.service.ProcessDeploymentService processDeploymentService;

    /**
     * Uploads and deploys a BPMN or BAR process definition file.
     * 
     * @param file the multipart file containing the process definition
     * @return ResponseEntity with deployment information or error details
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> deployProcess(@RequestParam("file") MultipartFile file) {
        try {
            Deployment deployment = processDeploymentService.deployProcess(file);
            DeploymentDto deploymentDto = DeploymentDto.fromDeployment(deployment);

            return new ResponseEntity<>(Map.of("data", deploymentDto), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            return new ResponseEntity<>(Map.of("message", "Error reading file: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("message", "Unexpected error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Lists all deployments in the Flowable engine.
     * 
     * @return ResponseEntity with list of deployments
     */
    @GetMapping
    public ResponseEntity<Map<String, List<DeploymentDto>>> getAllDeployments() {
        List<Deployment> deployments = processDeploymentService.getDeployments();
        List<DeploymentDto> deploymentDtos = deployments.stream()
            .map(DeploymentDto::fromDeployment)
            .toList();
        return new ResponseEntity<>(Map.of("data", deploymentDtos), HttpStatus.OK);
    }

    /**
     * Retrieves a specific deployment by its ID.
     * 
     * @param deploymentId the ID of the deployment to retrieve
     * @return ResponseEntity with deployment details or 404 if not found
     */
    @GetMapping("/{deploymentId}")
    public ResponseEntity<Map<String, Object>> getDeployment(@PathVariable String deploymentId) {
        Deployment deployment = processDeploymentService.getDeployment(deploymentId);

        if (deployment == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        DeploymentDto deploymentDto = DeploymentDto.fromDeployment(deployment);
        return new ResponseEntity<>(Map.of("data", deploymentDto), HttpStatus.OK);
    }

    /**
     * Deletes a deployment.
     * 
     * @param deploymentId the ID of the deployment to delete
     * @param cascade if true, also deletes deployed process definitions (default: false)
     * @return ResponseEntity with success status or error details
     */
    @DeleteMapping("/{deploymentId}")
    public ResponseEntity<Void> deleteDeployment(
            @PathVariable String deploymentId,
            @RequestParam(defaultValue = "false") boolean cascade) {

        boolean success = processDeploymentService.deleteDeployment(deploymentId, cascade);

        if (!success) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.noContent().build();
    }
}