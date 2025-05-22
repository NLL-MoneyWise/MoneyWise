'use client';

import { Suspense, SuspenseProps } from 'react';
import useClientMount from '@/app/common/hooks/useClinetMount';

const SSRSafeSuspense = ({ fallback, children }: SuspenseProps) => {
    const { isMounted } = useClientMount();

    if (!isMounted) return <>{fallback}</>;
    return <Suspense fallback={fallback}>{children}</Suspense>;
};

export default SSRSafeSuspense;
