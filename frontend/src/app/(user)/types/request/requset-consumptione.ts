export type PeriodType = 'all' | 'year' | 'month' | 'week';

export interface ConsumptionRequest {
    period: PeriodType;
    year: string;
    month?: string;
    week?: string;
}

export interface GetConsumptionRequest {
    year: string;
    month: string;
    start_day?: string;
    last_day?: string;
}

export interface PostFixedConsumptionRequest {
    day: string;
    amount: string;
    category: string;
    name: string;
}

export interface DeleteConsumptionRequest {
    id: number;
}
