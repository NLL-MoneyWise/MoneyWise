'use client';

import Text from '@/app/common/components/Text/Text';
import React, { Dispatch } from 'react';
import { ChevronDown } from 'lucide-react';
import clsx from 'clsx';
import { ViewType } from '../../types/viewtype';
interface DateSelectorProps {
    isOpen: boolean;
    handleIsOpenChange: Dispatch<boolean>;
    viewType: ViewType;
    handleViewTypeChange: Dispatch<ViewType>;
}

const DateSelector = ({
    isOpen,
    handleIsOpenChange,
    viewType,
    handleViewTypeChange
}: DateSelectorProps) => {
    return (
        <div className="relative">
            {/* 주간 월간 변경 요소  */}
            <button
                className="flex items-center border border-white  gap-2 relative p-2 rounded-md  w-24 justify-center"
                onClick={() => handleIsOpenChange(!isOpen)}
            >
                <Text>{viewType}</Text>

                <ChevronDown
                    className={clsx(
                        isOpen && 'rotate-180',
                        'animate-ease-out duration-300'
                    )}
                />
            </button>
            {isOpen && (
                <div className="flex flex-col gap-2 absolute shadow-md top-11 left-0 text-black bg-white  p-2 rounded-md scaleIn w-full z-10">
                    <button
                        className="relative w-full "
                        onClick={() => handleViewTypeChange('주간')}
                    >
                        주간
                    </button>
                    <button
                        className="relative w-full "
                        onClick={() => handleViewTypeChange('월간')}
                    >
                        월간
                    </button>
                    <button
                        className="relative w-full"
                        onClick={() => handleViewTypeChange('연간')}
                    >
                        연간
                    </button>
                    <button
                        className="relative w-full"
                        onClick={() => handleViewTypeChange('전체')}
                    >
                        전체
                    </button>
                </div>
            )}
        </div>
    );
};

export default DateSelector;
