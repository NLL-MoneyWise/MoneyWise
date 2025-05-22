import { ConsumptionRequest } from './../types/request/requset-consumptione';
import { ConsumptioneResponse } from '../types/reponse/response-consumptione';
import { AxiosInstance } from 'axios';
import { defaultApi } from '@/app/common/util/api';
import { AllConsumptionResponse } from '../types/reponse/response-consumptione';

interface ConsumptionRepository {
    analyeConsumption(data: ConsumptionRequest): Promise<ConsumptioneResponse>;
}

export class ConsumptionRepositoryImpl implements ConsumptionRepository {
    private static instance: ConsumptionRepositoryImpl | null = null;
    private api: AxiosInstance;

    private constructor() {
        this.api = defaultApi();
    }

    public static getInstance(): ConsumptionRepositoryImpl {
        if (!ConsumptionRepositoryImpl.instance) {
            ConsumptionRepositoryImpl.instance =
                new ConsumptionRepositoryImpl();
        }
        return ConsumptionRepositoryImpl.instance;
    }

    async getAllIncome(): Promise<AllConsumptionResponse> {
        const { data } = await this.api.get(`/income/find/all`);

        return data;
    }

    async getAllFiexedCost(): Promise<AllConsumptionResponse> {
        const { data } = await this.api.get(`/fixed-cost/find/all`);

        return data;
    }

    async analyeConsumption({
        period,
        year,
        month,
        day
    }: ConsumptionRequest): Promise<ConsumptioneResponse> {
        const { data } = await this.api.get('/workflows/consumptions-analyze', {
            params: {
                period,
                year,
                month,
                day
            }
        });

        return data;
    }
}
