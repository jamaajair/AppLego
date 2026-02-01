import React from 'react';
import { 
  Dialog, 
  DialogTitle, 
  DialogContent, 
  List, 
  ListItem, 
  ListItemText, 
  Typography 
} from '@mui/material';

// la structure d'une partie
interface Party {
  ref: number;
  label: string;
  kind: string;
}

// Props pour le composant
interface PartiesPopupProps {
  open: boolean;
  onClose: () => void;
  parties: Party[];
}

export default function PartiesPopup({ 
  open, 
  onClose, 
  parties 
}: PartiesPopupProps) {
  return (
    <Dialog 
      open={open} 
      onClose={onClose} 
      maxWidth="sm" 
      fullWidth
    >
      <DialogTitle>Parties Prenantes du Dossier</DialogTitle>
      <DialogContent>
        {parties.length === 0 ? (
          <Typography>Aucune partie prenante trouvée</Typography>
        ) : (
          <List>
            {parties.map((partie) => (
              <ListItem key={partie.ref}>
                <ListItemText 
                  primary={partie.label} 
                  secondary={`Type: ${partie.kind}`} 
                />
              </ListItem>
            ))}
          </List>
        )}
      </DialogContent>
    </Dialog>
  );
}