import { Response } from '@/app/common/types/response/reponse.dto';

export interface Income {
    day: number;
    cost: number;
}

export interface GetAllIncomeResponse extends Response {
    incomeDTOList: Income[];
}

export interface PostIncomeResponse extends Response {}

export interface DeleteIncomeResponse extends Response {}
