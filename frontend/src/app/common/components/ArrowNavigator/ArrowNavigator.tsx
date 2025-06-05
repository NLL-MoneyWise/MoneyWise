import React from 'react';
import { ChevronLeft, ChevronRight } from 'lucide-react';
import clsx from 'clsx';

interface ArrowNavigatorProps {
    children: React.ReactNode;
    handlePrevious: () => void;
    handleNext: () => void;
    className?: string;
}

const ArrowNavigator = ({
    children,
    handlePrevious,
    handleNext,
    className
}: ArrowNavigatorProps) => {
    return (
        <div
            className={clsx(
                'flex items-center justify-between w-full',
                className
            )}
        >
            <ChevronLeft
                className="h-8 w-8 cursor-pointer"
                onClick={handlePrevious}
            />
            {children}
            <ChevronRight
                className="h-8 w-8 cursor-pointer"
                onClick={handleNext}
            />
        </div>
    );
};

export default ArrowNavigator;
