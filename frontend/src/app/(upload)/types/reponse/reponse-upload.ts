import { Response } from '../../../common/types/response/reponse.dto';

export interface UploadResponse extends Response {
    preSignedUrl: string;
    accessUrl: string;
}
