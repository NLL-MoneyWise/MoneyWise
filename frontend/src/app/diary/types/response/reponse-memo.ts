import { Response } from '../../../common/types/response/reponse.dto';

export interface SaveMemoResponse extends Response {
    id: number;
}

export interface Memo {
    date: string;
    content: string;
    email: string;
}

export interface GetMemoResponse extends Response {
    memoDTOList: Memo[];
}
