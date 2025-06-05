import React from 'react';
import { Eraser } from 'lucide-react';

interface ConsumptionItem {
    name: string;
    amount: number;
}

interface ConsumptionPreviewProps {
    totalAmount: number;
    items?: ConsumptionItem[];
    showBreakdown?: boolean;
}

const ConsumptionPreview = ({
    totalAmount,
    items,
    showBreakdown
}: ConsumptionPreviewProps) => {
    // const { deleteConsumption } = useConsumption();

    if (showBreakdown && items && items.length > 0) {
        return (
            <div className="consumption-data bg-red-50 rounded px-2 py-1">
                <div className="flex items-center text-red-600 text-xs font-semibold mb-0.5">
                    <span className="mr-1">💸 지출</span>
                    {totalAmount.toLocaleString()}원
                </div>
                <div className="space-y-0.5">
                    {items.map((item, idx) => (
                        <div
                            key={idx}
                            className="flex items-center text-xs text-red-700 ml-2 gap-1"
                        >
                            <span className="truncate max-w-[80px]">
                                - {item.name}
                            </span>
                            :{' '}
                            <span className="font-medium">
                                {item.amount.toLocaleString()}원
                            </span>
                            <Eraser className="w-4 h-4 hover:text-red-500 cursor-pointer" />
                        </div>
                    ))}
                </div>
            </div>
        );
    }
    return (
        <div className="flex items-center text-red-600 text-xs font-semibold bg-red-50 rounded px-2 py-0.5">
            <span className="mr-1">💸 지출</span>
            {totalAmount.toLocaleString()}원
        </div>
    );
};

export default ConsumptionPreview;
