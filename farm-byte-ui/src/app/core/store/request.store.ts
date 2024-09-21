import {signalStoreFeature, withState} from "@ngrx/signals";

type RequestState = {
  loading: boolean;
  error?: any;
}

function withRequestStatus() {
  return signalStoreFeature(withState<RequestState>({loading: false}));
}

function setLoading(): Partial<RequestState> {
  return {loading: true};
}

function setLoaded(): Partial<RequestState> {
  return {loading: false};
}

function setError(value: any): Partial<RequestState> {
  return {error: value};
}

export {withRequestStatus, setLoading, setLoaded, setError};
