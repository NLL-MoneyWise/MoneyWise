'use client';
import React, { useState } from 'react';
import DateNavigator from '../components/DateNavigator/DateNavigator';
import { ErrorBoundary } from 'react-error-boundary';
import NotFoundData from '../components/NotFoundData/NotFoundData';
import { ViewType } from '../types/viewtype';
import { QueryErrorResetBoundary } from '@tanstack/react-query';
import AnalyzeContent from '../components/AnalyzeContent/AnalyzeContent';

const AnalyzePage = () => {
    const [currentDate, setCurrentDate] = useState<Date>(new Date());
    const [viewType, setViewType] = useState<ViewType>('전체');

    return (
        <div className="rounded-t-lg text-center h-screen bg-slate-50 flex flex-col overflow-hidden">
            <DateNavigator
                currentDate={currentDate}
                setCurrentDate={setCurrentDate}
                viewType={viewType}
                setViewType={setViewType}
            />
            <QueryErrorResetBoundary>
                {({ reset }) => (
                    <ErrorBoundary
                        onReset={reset}
                        fallbackRender={({ resetErrorBoundary }) => (
                            <NotFoundData
                                handleClick={() => resetErrorBoundary()}
                            />
                        )}
                    >
                        <AnalyzeContent
                            date={currentDate}
                            viewType={viewType}
                        />
                    </ErrorBoundary>
                )}
            </QueryErrorResetBoundary>
        </div>
    );
};

export default AnalyzePage;
