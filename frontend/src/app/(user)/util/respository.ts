import { ConsumptionRequest } from './../types/request/requset-consumptione';
import { ConsumptioneResponse } from '../types/reponse/response-consumptione';
import { AxiosInstance } from 'axios';
import { defaultApi } from '@/app/common/util/api';

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
