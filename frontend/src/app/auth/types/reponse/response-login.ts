import { responType } from '../../../common/types/response/reponse.dto';

export interface LoginResponse extends responType {
    accessToken: string;
}
