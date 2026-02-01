// This file is the page allowing to link stakeholders (parties) to dossiers outside of the dossier view

import React, { useState, useEffect } from 'react';
import { api } from '../services/api';  // Votre service API existant
import type { Party } from '../types/party';

// Types pour nos données
interface Dossier {
  ref: string;
  label: string;
}

export default function Stakeholders() {
  // États pour nos données
  const [dossiers, setDossiers] = useState<Dossier[]>([]);
  const [parties, setParties] = useState<Party[]>([]);
  const [roles, setRoles] = useState<string[]>([]);

  
  // États de chargement et erreurs
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // États de sélection
  const [selectedDossier, setSelectedDossier] = useState('');
  const [selectedPartie, setSelectedPartie] = useState('');
  const [selectedRole, setSelectedRole] = useState('');
  const [message, setMessage] = useState('');

  // Charger les données depuis l'API
  useEffect(() => {
    const chargerDonnees = async () => {
      try {
        // Charger dossiers et parties
        const dossierResponse = await api.get('/my_dossiers');
        const partieResponse = await api.get('/parties');
        const rolesResponse = await api.get('/participant-roles');

        setDossiers(dossierResponse.data || []);
        setParties(partieResponse || [] );
        setLoading(false);
        setRoles(rolesResponse);
      } catch (err) {
        console.error('Erreur lors du chargement des données :', err);
        setError('Impossible de charger les données');
      } finally {
        setLoading(false);
      }
    };

    chargerDonnees();
  }, []);

  // Fonction pour lier une partie à un dossier
 const lierPartieADossier = async () => {
  if (!selectedDossier || !selectedPartie || !selectedRole) {
    setMessage(' Sélectionnez un dossier, une partie et un rôle');
    return;
  }

  try {
    await api.post(`/dossiers/${selectedDossier}/parties/${selectedPartie}?role=${selectedRole}`, {});
    setMessage(` Partie liée au dossier ${selectedDossier} avec le rôle ${selectedRole}`);
    console.log('Message =', message);
  } catch (err) {
    setMessage(' Erreur lors de la liaison');
  }
};


  // État de chargement
  if (loading) return <div>Chargement...</div>;
  if (error) return <div>Erreur : {error}</div>;

  return (
    <div>
      <h2>Association Parties Prenantes aux Dossiers</h2>

      {/* Sélecteur de dossiers */}
      <select 
        value={selectedDossier} 
        onChange={(e) => setSelectedDossier(e.target.value)}
      >
        <option value="">-- Choisir un dossier --</option>
        {dossiers.map((dossier) => (
          <option key={dossier.ref} value={dossier.ref}>
            {dossier.ref} - {dossier.label}
          </option>
        ))}
      </select>

      {/* Sélecteur de parties */}
      <select 
        value={selectedPartie} 
        onChange={(e) => setSelectedPartie(e.target.value)}
      >
        <option value="">-- Choisir une partie --</option>
        {parties.map((partie) => (
          <option key={partie.ref} value={partie.ref}>
            {partie.label} ({partie.kind})
          </option>
        ))}
      </select>

      {/* Sélecteur de rôle */}
      <select
        value={selectedRole}
        onChange={(e) => setSelectedRole(e.target.value)}
      >
        <option value="">-- Choisir un rôle --</option>
        {roles.map((role) => (
          <option key={role} value={role}>{role}</option>
        ))}
      </select>

            
      {/* Bouton de liaison */}
      <button onClick={lierPartieADossier}>
        Lier la partie au dossier
      </button>

      {/* Message de résultat */}
      {message && <h1>{message}</h1>}
    </div>
  );
}