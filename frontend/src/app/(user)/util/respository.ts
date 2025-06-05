import { AxiosInstance } from 'axios';
import { defaultApi } from '@/app/common/util/api';
import {
    ConsumptionRequest,
    PostFixedConsumptionRequest,
    PostIncomeRequest,
    DeleteIncomeRequest,
    GetConsumptionRequest,
    DeleteConsumptionRequest
} from './../types/request';
import {
    GetAllIncomeResponse,
    ConsumptioneResponse,
    GetAllFiexedCostResponse,
    PostFixedConsumptionResponse,
    AllConsumptionResponse,
    DeleteConsumptionResponse,
    DeleteIncomeResponse,
    GetConsumptioneResponse
} from '../types/reponse';

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

    async deleteIncome({
        day
    }: DeleteIncomeRequest): Promise<DeleteIncomeResponse> {
        const { data } = await this.api.delete(`/income/delete/one/${day}`);

        return data;
    }

    async getAllFiexedCost(): Promise<GetAllFiexedCostResponse> {
        const { data } = await this.api.get(`/consumptions/fixed/all`);

        return data;
    }

    async postFiexdConsumption({
        amount,
        day,
        category,
        name
    }: PostFixedConsumptionRequest): Promise<PostFixedConsumptionResponse> {
        const { data } = await this.api.post(`/consumptions/fixed/save`, {
            amount,
            day,
            category,
            name
        });

        return data;
    }

    async deleteFixedConsumption({
        id
    }: DeleteConsumptionRequest): Promise<DeleteConsumptionResponse> {
        const { data } = await this.api.delete(
            `/consumptions/delete/one/${id}`
        );

        return data;
    }

    async analyeConsumption({
        period,
        year,
        month,
        week
    }: ConsumptionRequest): Promise<ConsumptioneResponse> {
        const { data } = await this.api.get('/workflows/consumptions-analyze', {
            params: {
                period,
                year,
                month,
                week
            }
        });

        return data;
    }

    async getConsumption({
        year,
        month,
        start_day,
        last_day
    }: GetConsumptionRequest): Promise<GetConsumptioneResponse> {
        const { data } = await this.api.get('/consumptions/analyze/daily', {
            params: {
                year,
                month,
                start_day,
                last_day
            }
        });

        return data;
    }
}
