'use client';
import {
    MutationCache,
    QueryClient,
    QueryClientProvider
} from '@tanstack/react-query';
import { useToastStore } from './common/hooks/useToastStore';
import { CustomError } from './common/types/error/error';

export function QueryProviders({ children }: { children: React.ReactNode }) {
    const { addToast } = useToastStore();

    const mutationCache = new MutationCache({
        onError: (error) => {
            if (error instanceof CustomError) {
                addToast(error.message, 'warning');
                return;
            }
            addToast('알 수 없는 에러입니다.', 'error');
        }
    });

    const queryClient = new QueryClient({
        mutationCache,
        defaultOptions: {
            queries: {
                retry: false,
                staleTime: 5 * 60 * 1000
            }
        }
    });

    return (
        <QueryClientProvider client={queryClient}>
            {children}
        </QueryClientProvider>
    );
}
