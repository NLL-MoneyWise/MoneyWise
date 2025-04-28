'use client';

import { create } from 'zustand';
import { persist, createJSONStorage } from 'zustand/middleware';
import { v4 as uuidv4 } from 'uuid';
import { CustomError, ErrorType } from '../types/error/error';

export interface Toast {
    id: string;
    variant: 'success' | 'warning' | 'error';
    message: string;
    createdAt: number;
}

interface ToastStore {
    toasts: Toast[];
    addToast: (
        message: string,
        variant: 'success' | 'warning' | 'error'
    ) => void;
    removeToast: (id: string) => void;
}

const STORAGE_KEY = 'toast-storage';

export const useToastStore = create<ToastStore>()(
    persist(
        (set) => ({
            toasts: [],
            addToast: (message, variant) => {
                const id = uuidv4();
                const createdAt = Date.now();

                set((state) => ({
                    toasts: [
                        ...state.toasts,
                        { id, message, variant, createdAt }
                    ]
                }));

                setTimeout(() => {
                    set((state) => ({
                        toasts: state.toasts.filter((toast) => toast.id !== id)
                    }));
                }, 2000);
            },
            removeToast: (id) => {
                set((state) => ({
                    toasts: state.toasts.filter((toast) => toast.id !== id)
                }));

                try {
                    const storageData = sessionStorage.getItem(STORAGE_KEY);
                    if (storageData) {
                        const data = JSON.parse(storageData);
                        if (data.state && Array.isArray(data.state.toasts)) {
                            data.state.toasts = data.state.toasts.filter(
                                (toast: Toast) => toast.id !== id
                            );
                            sessionStorage.setItem(
                                STORAGE_KEY,
                                JSON.stringify(data)
                            );
                        }
                    }
                } catch (error) {
                    throw new CustomError(
                        '토스트 삭제 중 에러가 발생했습니다.',
                        400,
                        ErrorType.DATABASE
                    );
                }
            }
        }),
        {
            name: STORAGE_KEY,
            storage: createJSONStorage(() => sessionStorage),
            partialize: (state) => ({ toasts: state.toasts }),
            onRehydrateStorage: () => (state) => {
                if (state) {
                    // 페이지 로드 시 오래된 토스트 제거
                    const now = Date.now();
                    state.toasts = state.toasts.filter(
                        (toast) => now - toast.createdAt < 2000
                    );
                }
            }
        }
    )
);
