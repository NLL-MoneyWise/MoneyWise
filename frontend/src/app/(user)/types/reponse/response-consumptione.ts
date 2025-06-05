import { Response } from '../../../common/types/response/reponse.dto';

export interface CategoryItem {
    name: string;
    amount: number;
}

export interface ConsumptionsType extends CategoryItem {
    id: number;
    catergory: string;
    quantity: number;
}

export interface ConsumptionDate {
    totalAmount: number;
    byCategory: CategoryItem[];
    topExpenses: CategoryItem[];
    storeExpenses: CategoryItem[];
    date: string;
}

export interface ConsumptioneResponse extends Response, ConsumptionDate {
    totalAmount: number;
    byCategory: CategoryItem[];
    topExpenses: CategoryItem[];
    storeExpenses: CategoryItem[];
}

export interface GetConsumptioneResponse extends Response {
    dailyList: ConsumptionDate[];
}

export interface AllConsumptionResponse extends Response {
    date: string;
    store_nam: string;
}

export interface FixedCost {
    id: number;
    date: string;
    category: string;
    amount: number;
    name: string;
}

export interface GetAllFiexedCostResponse extends Response {
    fixedCostDTOList: FixedCost[];
}

export interface PostFixedConsumptionResponse extends Response {}

export interface DeleteConsumptionResponse extends Response {}
