import { Response } from '../../../common/types/response/reponse.dto';

export interface ValidateResponse extends Response {}

export interface RefreshValidateResponse extends Response {
    access_token: string;
}
