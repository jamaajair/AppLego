import { useState, useEffect } from 'react';
import { api } from '../services/api';
import type { Deployment } from '../models/deployment';
import { isValidBpmnFile } from '../services/utils';

export default function useDeployment() {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [deployments, setDeployments] = useState<Deployment[]>([]);

    const loadDeployments = async () => {
        try {
            setLoading(true);
            setError(null);
            
            const response = await api.get('/deployments');
            const deploymentData = response?.data || [];
            
            setDeployments(deploymentData);
        } catch (err) {
            console.error('Erreur lors du chargement des déploiements:', err);
            setError('Impossible de charger les déploiements');
        } finally {
            setLoading(false);
        }
    };

    const deployProcess = async (file: File) => {
        try {
            setLoading(true);
            setError(null);
            
            if (!isValidBpmnFile(file)) {
                throw new Error('Type de fichier invalide. Seuls les fichiers .bpmn20.xml sont autorisés.');
            }
            
            const formData = new FormData();
            formData.append('file', file);
            
            const response = await api.post('/deployments', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            });
            
            const deploymentData = response.data?.data;
            
            return deploymentData;
        } catch (err: unknown) {
            console.error('Erreur lors du déploiement du processus:', err);
            if (err instanceof Error && err.message.includes('Type de fichier invalide')) {
                setError(err.message);
            } else if ((err as any)?.response?.status === 400) {
                setError('Type de fichier invalide. Seuls les fichiers .bpmn20.xml sont autorisés.');
            } else if ((err as any)?.response?.status === 404) {
                setError('Endpoint de déploiement introuvable.');
            } else {
                setError('Échec du déploiement du processus');
            }
            throw err;
        } finally {
            setLoading(false);
        }
    };

    const deleteDeployment = async (deploymentId: string, cascade: boolean = false) => {
        try {
            setLoading(true);
            setError(null);
            
            await api.delete(`/deployments/${deploymentId}?cascade=${cascade}`, undefined);
            
        } catch (err: unknown) {
            console.error('Erreur lors de la suppression du déploiement:', err);
            setError('Échec de la suppression du déploiement');
            throw err;
        } finally {
            setLoading(false);
        }
    };

    return {
        loading,
        error,
        deployments,
        
        loadDeployments,
        deployProcess,
        deleteDeployment,
    };
}
