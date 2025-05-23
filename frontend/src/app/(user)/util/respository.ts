import { PostIncomeRequest } from './../types/request/request-income';
import {
    ConsumptionRequest,
    PostFixedConsumptionRequest
} from './../types/request/requset-consumptione';
import {
    ConsumptioneResponse,
    PostFixedConsumptionResponse
} from '../types/reponse/response-consumptione';
import { AxiosInstance } from 'axios';
import { defaultApi } from '@/app/common/util/api';
import { AllConsumptionResponse } from '../types/reponse/response-consumptione';
import { GetAllIncomeResponse } from '../types/reponse';

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

    async getAllIncome(): Promise<GetAllIncomeResponse> {
        const { data } = await this.api.get(`/income/find/all`);

        return data;
    }

    async postIncome({
        cost,
        day
    }: PostIncomeRequest): Promise<AllConsumptionResponse> {
        const { data } = await this.api.post(`/income/create-update`, {
            cost,
            day
        });

        return data;
    }

    async postFiexdConsumption({
        cost,
        day
    }: PostFixedConsumptionRequest): Promise<PostFixedConsumptionResponse> {
        const { data } = await this.api.post(`/fixed-cost/create-update`, {
            cost,
            day
        });

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
