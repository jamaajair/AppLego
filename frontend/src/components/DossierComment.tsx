import React from 'react';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';

interface DossierCommentsProps {
  comments: string;
  isEditing: boolean;
  onStartEditing: () => void;
  onSave: (newComments: string) => void;
  onCommentsChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

const DossierComment: React.FC<DossierCommentsProps> = ({
  comments,
  isEditing,
  onStartEditing,
  onSave,
  onCommentsChange,
}) => {
  return (
    <Box sx={{ pt: 2 }}>
      {isEditing ? (
        <>
          <TextField
            id="outlined-multiline-static"
            label="Commentaire"
            multiline
            rows={4}
            value={comments}
            onChange={onCommentsChange}
            sx={{ width: '100%' }}
          />
          <Button
            variant="contained"
            color="primary"
            onClick={() => onSave(comments)}
            sx={{ mt: 1 }}
          >
            Enregistrer
          </Button>
        </>
      ) : (
        <>
          <TextField
            disabled
            id="outlined-disabled"
            label="Commentaire"
            rows={2}
            value={comments}
            sx={{ width: '100%' }}
          />
          <Button
            variant="outlined"
            onClick={onStartEditing}
            sx={{ mt: 1 }}
          >
            Modifier
          </Button>
        </>
      )}
    </Box>
  );
};

export default DossierComment;
