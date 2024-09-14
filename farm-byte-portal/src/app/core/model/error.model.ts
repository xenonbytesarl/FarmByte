export interface ErrorModel {
  timestamp: Date;
  code: number;
  status: string;
  reason: string;
  path: string;
  trackId?: string;
  error?: Array<{
    field: string;
    message: string;
  }>
}
