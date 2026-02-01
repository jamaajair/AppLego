import useGenericNew from "../../hooks/useGenericNew";
import GenericForm from "./GenericForm";

interface GenericNewProps {
    configPath: string;
    submitUrl?: string;
}

export default function GenericNew({ configPath, submitUrl }: GenericNewProps) {
    const form = useGenericNew(configPath, submitUrl);

    return (
        <GenericForm
            {...form}
            readOnly={false}
        />
    );
}