import { responseType } from '../../../common/types/response/reponse.dto';

// 차후에 수정
export interface LoginResponse extends responseType {
    access_token: string;
    nickname: string;
    email: string;
}
