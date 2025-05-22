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

export interface ConsumptioneResponse extends Response {
    totalAmount: number;
    byCategory: CategoryItem[];
    topExpenses: CategoryItem[];
    storeExpenses: CategoryItem[];
}

export interface AllConsumptionResponse extends Response {
    date: string;
    store_nam: string;
}

export interface AllFiexedCost extends Response {
    date: string;
    cost: string;
}
