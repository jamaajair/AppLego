import GenericNew from "../../components/ui/GenericNew.tsx";

const NewDossierPage = () => {
    return <GenericNew configPath="/config/new_dossier.json" submitUrl={`/user/dossier/new`} />;
}

export default NewDossierPage;