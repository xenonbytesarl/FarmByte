import {FormModeType} from "@/shared/model/FormModeType.ts";
import {Button} from "@/components/ui/button.tsx";
import {useTranslation} from "react-i18next";


type Props = {
    mode: FormModeType;
    isLoading: boolean;
    isValid: boolean;
    onEdit: () => void;
    onCancel: () => void;
    onCreate: () => void;
}

const FormCrudButton = ({mode, isLoading, isValid, onEdit, onCancel, onCreate}: Props) => {

    const {t} = useTranslation(['home']);

    const handleEdit = () => {
        onEdit()
    }

    const handleCancel = () => {
        onCancel()
    }

    const handleCreate = () => {
        onCreate();
    }

    const renderCreateButton = () => {
        return mode === FormModeType.READ && (
            <Button onClick={handleCreate} variant="secondary" type="button" className="flex flex-row justify-center items-center">
                <span className="material-symbols-outlined text-xl">add</span>
                <span>{t('form_button_label_create')}</span>
            </Button>
        );
    }

    const renderEditButton = () => {
        return mode === FormModeType.READ && (
            <Button onClick={handleEdit} variant="default" type="button" className="flex flex-row justify-center items-center">
                <span className="material-symbols-outlined text-xl">edit</span>
                <span>{t('form_button_label_edit')}</span>
            </Button>
        );
    }

    const renderSaveButton = () => {
        return (mode === FormModeType.CREATE || mode === FormModeType.EDIT) && (
            <Button variant="default" disabled={!isValid} type="submit" className="flex flex-row justify-center items-center">
                <span className={`material-symbols-outlined text-xl ${isLoading? 'animate-spin': ''}`}>{isLoading? 'progress_activity': 'check'}</span>
                <span>{t('form_button_label_save')} {isLoading? ' ...': ''}</span>
            </Button>
        )
    }

    const renderCancelButton = () => {
        return (mode === FormModeType.CREATE || mode === FormModeType.EDIT) && (
            <Button onClick={handleCancel} variant="secondary" type="button" className="flex flex-row justify-center items-center">
                <span className="material-symbols-outlined text-xl">close</span>
                <span>{t('form_button_label_cancel')}</span>
            </Button>
        )
    }
    return (
        <span className="flex flex-row justify-start items-center gap-3 w-6/12">
            {renderCreateButton()}
            {renderEditButton()}
            {renderSaveButton()}
            {renderCancelButton()}
        </span>
    );
};

export default FormCrudButton;