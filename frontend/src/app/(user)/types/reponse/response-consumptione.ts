import { Response } from '../../../common/types/response/reponse.dto';

export interface CategoryItem {
    name: string;
    amount: string;
}

export interface ConsumptioneResponse extends Response {
    totalAmount: number;
    byCategory: CategoryItem[];
    topExpenses: CategoryItem[];
    storeExpenses: CategoryItem[];
}
