import React from 'react';
import type { Dossier } from '../types/dossier';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Grid from '@mui/material/Grid';
import Button from '@mui/material/Button';

interface DossierGridProps {
  dossier: Dossier;
  onEditClick: () => void;
  onValidateClick: () => void;
  onAddStakeholderClick?: () => void;
  onSeeStakeholders?: () => void;
}

const DossierGrid: React.FC<DossierGridProps> = ({ dossier, onEditClick, onValidateClick, onAddStakeholderClick, onSeeStakeholders }) => {

  const itemGridStyle = {
    border: '1px solid #ccc',
    p: 1,
  };

return (
    <Box sx={{ flexGrow: 1 }}>
      <Grid container spacing={0.5} sx={{p:1 ,border:'1px solid #ccc'}}>
        <Grid  size={2} sx={itemGridStyle} >
            <Typography variant='h6'> Dossier </Typography>
        </Grid>
        <Grid  size={2} sx={itemGridStyle}>
            <Typography> {dossier.type.labelFr} </Typography>
        </Grid>
        <Grid  size={2} sx={itemGridStyle}>
            <Typography> Ref: {dossier.ref} </Typography>
        </Grid>
        <Grid  size={4} sx={itemGridStyle}>
            <Typography> {dossier.label} </Typography>
        </Grid>
        <Grid  size={2} sx={itemGridStyle}>
            <Typography> {dossier.state.labelFr} </Typography>
        </Grid>
        <Grid  size={4} sx={itemGridStyle}>
            <Typography> Created date: {dossier.createdAt} </Typography>
        </Grid>
        <Grid  size={4} sx={itemGridStyle}>
            <Typography> Start date: {dossier.startNoLaterThan} </Typography>
        </Grid>
        <Grid  size={4} sx={itemGridStyle}>
            <Typography> Completion date: {dossier.expectCompletion} </Typography>
        </Grid>
        <Grid  size={12} sx={{ display: 'flex', justifyContent: 'flex-end' }}>
            <Button
                variant="outlined"
                size="small"
                onClick={onEditClick}
                >
                Modifier
            </Button>
            <Button
                variant="outlined"
                size="small"
                onClick={onValidateClick}
                >
                Valider
            </Button>
            {onAddStakeholderClick && (
            <Button
                variant="outlined"
                size="small"
                onClick={onAddStakeholderClick}
                color="primary"
            >
                Ajouter Stakeholder
            </Button>
            )}
            {onSeeStakeholders && (
            <Button
                variant="outlined"
                size="small"
                onClick={onSeeStakeholders}
                color="primary"
            >
                See Stakeholders
            </Button>
            )}
            
        </Grid>
      </Grid>
    </Box>
    );
};

export default DossierGrid;