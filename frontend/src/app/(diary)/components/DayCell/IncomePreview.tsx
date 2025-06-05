import React from 'react';

interface IncomePreviewProps {
    amount: number;
}

const IncomePreview = ({ amount }: IncomePreviewProps) => (
    <div className="flex items-center text-blue-600 text-xs font-semibold bg-blue-50 rounded px-2 py-0.5 mb-1">
        <span className="mr-1">💙 수입</span>
        {amount.toLocaleString()}원
    </div>
);

export default IncomePreview;
