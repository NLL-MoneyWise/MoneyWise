import { Response } from '../../../common/types/response/reponse.dto';

// 차후에 수정
export interface LoginResponse extends Response {
    access_token: string;
    nickname: string;
    email: string;
}
