'use client';

import { useToastStore } from './hooks/useToastStore';

export default function Home() {
    const { addToast } = useToastStore();
    return (
        <div>
            <button
                onClick={() => {
                    addToast('안녕', 'error');
                }}
            >
                {'안녕'}
            </button>
        </div>
    );
}
