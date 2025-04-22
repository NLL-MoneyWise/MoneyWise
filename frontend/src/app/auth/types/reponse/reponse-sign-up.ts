import { responseType } from '../../../common/types/response/reponse.dto';

export interface SignUpResponse extends responseType {
    accessToken: string;
}
