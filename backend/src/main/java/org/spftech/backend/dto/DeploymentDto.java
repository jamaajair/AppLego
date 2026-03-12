package org.spftech.backend.dto;

import java.util.Date;

/**
 * Data Transfer Object for Flowable deployment information.
 * 
 * <p>Represents the essential information about a deployed process definition
 * in a format suitable for API responses.</p>
 * 
 * @see org.flowable.engine.repository.Deployment
 */
public record DeploymentDto(
    String deploymentId,
    String name,
    Date deploymentTime,
    String category,
    String tenantId
) {
    /**
     * Creates a DeploymentDto from a Flowable Deployment entity.
     * 
     * @param deployment the Flowable deployment entity
     * @return DeploymentDto with the deployment information
     */
    public static DeploymentDto fromDeployment(org.flowable.engine.repository.Deployment deployment) {
        return new DeploymentDto(
            deployment.getId(),
            deployment.getName(),
            deployment.getDeploymentTime(),
            deployment.getCategory(),
            deployment.getTenantId()
        );
    }
}