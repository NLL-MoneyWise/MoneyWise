'use client';
import { Suspense } from 'react';
import FloatingActionButton from '../common/components/FloatingButton/FloatingButton';

export default function AnalyzeLayout({
    children
}: {
    children: React.ReactNode;
}) {
    return (
        <div className="min-w-[375px] max-w-[768px] m-auto">
            <Suspense fallback={'로딩 중..'}>
                <>{children}</>
                <FloatingActionButton />
            </Suspense>
        </div>
    );
}
