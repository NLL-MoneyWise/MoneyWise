import { DeleteConsumptionRequest } from './../types/request/requset-consumptione';
import { GetConsumptionRequest } from './../types/request/';
import { useQueryClient, useQuery, useMutation } from '@tanstack/react-query';
import { useToastStore } from '@/app/common/hooks/useToastStore';
import { ConsumptionRepositoryImpl } from '../util/respository';
import { DeleteConsumptionResponse } from '../types/reponse';

const useConsumption = (params?: GetConsumptionRequest) => {
    const counsumptionRepository = ConsumptionRepositoryImpl.getInstance();
    const addToast = useToastStore((state) => state.addToast);
    const queryClient = useQueryClient();

    const getConsumption = useQuery({
        queryKey: ['consumptione', params],
        queryFn: () => {
            if (params) {
                return counsumptionRepository.getConsumption(params);
            }
        },
        staleTime: 60000,
        gcTime: 900000,
        enabled: !!params
    });

    const prefetchNeighborMonths = (date: Date) => {
        // 이전 달
        const prevMonth = new Date(date);
        prevMonth.setMonth(date.getMonth() - 1);
        queryClient.prefetchQuery({
            queryKey: [
                'consumptione',
                {
                    year: prevMonth.getFullYear().toString(),
                    month: (prevMonth.getMonth() + 1).toString()
                }
            ],
            queryFn: () =>
                counsumptionRepository.getConsumption({
                    year: prevMonth.getFullYear().toString(),
                    month: (prevMonth.getMonth() + 1).toString()
                }),
            staleTime: 60000,
            gcTime: 900000
        });

        // 다음 달
        const nextMonth = new Date(date);
        nextMonth.setMonth(date.getMonth() + 1);
        queryClient.prefetchQuery({
            queryKey: [
                'consumptione',
                {
                    year: nextMonth.getFullYear().toString(),
                    month: (nextMonth.getMonth() + 1).toString()
                }
            ],
            queryFn: () =>
                counsumptionRepository.getConsumption({
                    year: nextMonth.getFullYear().toString(),
                    month: (nextMonth.getMonth() + 1).toString()
                }),
            staleTime: 60000,
            gcTime: 900000
        });
    };

    const deleteFixedConsumption = useMutation<
        DeleteConsumptionResponse,
        Error,
        DeleteConsumptionRequest
    >({
        mutationFn: counsumptionRepository.deleteFixedConsumption.bind(
            counsumptionRepository
        ),
        onSuccess: (data) => {
            addToast(data.message, 'success');
            queryClient.invalidateQueries({ queryKey: ['consumptione'] });
        }
    });
    return {
        getConsumption,
        prefetchNeighborMonths,
        deleteFixedConsumption
    };
};

export default useConsumption;
