import {signalStoreFeature, withState} from "@ngrx/signals";
import {FormMode} from "../enums/form-mode.enum";

type FormModeState = {
 formMode: FormMode | undefined;
}

function withFormModeStatus() {
  return signalStoreFeature(withState<FormModeState>({formMode: undefined}));
}

function setFormMode(value: FormMode): Partial<FormModeState> {
  return {formMode: value};
}


export {withFormModeStatus, setFormMode};
