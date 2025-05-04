export type PeriodType = 'all' | 'year' | 'month' | 'day';

export interface ConsumptionRequest {
    period: PeriodType;
    year: string;
    month?: string;
    day?: string;
}
