'use client';

import { useEffect, useRef } from 'react';

function MSWProvider() {
    const ref = useRef<HTMLDivElement>(null);

    useEffect(() => {
        if (!ref.current) return;

        async function setupWokrer() {
            if (typeof window === 'undefined') {
                return;
            }
            const { worker } = await import('../mocks/browser');
            await worker.start();
            console.log('worker started');
        }
        setupWokrer();
    }, []);

    return <div ref={ref} />;
}

export default MSWProvider;
