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

            if (process.env.NEXT_PUBLIC_API_MOCKING === 'enabled') {
                await worker.start();
            }
        }
        setupWokrer();
    }, []);

    return <div ref={ref} />;
}

export default MSWProvider;
