import { responType } from '../../../common/types/response/reponse.dto';

// 차후에 수정
export interface LoginResponse extends responType {
    access_token: string;
    nickname: string;
    email: string;
}
