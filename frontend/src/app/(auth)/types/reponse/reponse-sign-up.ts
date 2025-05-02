import { Response } from '../../../common/types/response/reponse.dto';

export interface SignUpResponse extends Response {
    accessToken: string;
}
