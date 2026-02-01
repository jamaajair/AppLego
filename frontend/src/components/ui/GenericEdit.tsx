import useGenericEdit from "../../hooks/useGenericEdit";
import GenericForm from "./GenericForm";
import { useState } from "react";
import { Button } from "@mui/material";

interface GenericEditProps {
    configPath?: string | null;
    dataUrl?: string | null;
    submitUrl?: string | null;
}

export default function GenericEdit({ configPath, dataUrl, submitUrl }: GenericEditProps) {
    const form = useGenericEdit(configPath, dataUrl, submitUrl);
    const [editing, setEditing] = useState(false);

    return (
        <div>
            <GenericForm
                {...form}
                readOnly={!editing}
            />

            {!editing &&
                <Button onClick={() => setEditing(true)}>
                    Modifier
                </Button>
            }
        </div>
    );
}