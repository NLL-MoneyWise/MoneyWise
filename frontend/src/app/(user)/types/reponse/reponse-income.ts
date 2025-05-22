import { Response } from '@/app/common/types/response/reponse.dto';

export interface Income {
    day: number;
    cost: number;
}

export interface GetAllIncome extends Response {
    incomeDTOList: Income[];
}
