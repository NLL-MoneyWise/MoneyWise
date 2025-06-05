'use client';
import React from 'react';
import FloatingActionButton from '../common/components/FloatingButton/FloatingButton';

export default function AnalyzeLayout({
    children
}: {
    children: React.ReactNode;
}) {
    return (
        <div className="min-w-[375px] max-w-[768px] m-auto">
            <React.Fragment>{children}</React.Fragment>
            <FloatingActionButton />
        </div>
    );
}
