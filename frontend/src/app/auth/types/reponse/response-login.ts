import { responType } from '../../../common/types/response/reponse.dto';

// 차후에 수정
export interface LoginResponse extends responType {
    accessToken: string;
    nickName: string;
    email: string;
}
