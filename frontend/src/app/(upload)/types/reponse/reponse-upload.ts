import { Response } from '../../../common/types/response/reponse.dto';

export interface UploadReciptResponse extends Response {
    preSignedUrl: string;
    accessUrl: string;
}

export interface AnalyzeReceiptResponse extends Response {}
