'use client';
import React from 'react';
import { createPortal } from 'react-dom';
import Toast from '../Toast/Toast';
import { useToastStore } from '@/app/common/hooks/useToastStore';
import useClientMount from '@/app/common/hooks/useClinetMount';

type Toast = {
    id: string;
    message: string;
    variant: 'success' | 'error' | 'warning';
};

const ToastContainer = () => {
    const { toasts, removeToast } = useToastStore();

    const { isMounted } = useClientMount();

    if (!isMounted) return null;

    return createPortal(
        <div className="flex flex-col items-center w-full fixed top-5 pointer-events-none z-10">
            {toasts
                .slice()
                .reverse()
                .map((toast) => (
                    <Toast
                        key={toast.id}
                        variant={toast.variant}
                        onClose={() => removeToast(toast.id)}
                    >
                        {toast.message}
                    </Toast>
                ))}
        </div>,
        document.getElementById('toast-portal') || document.body
    );
};

export default ToastContainer;
