import React from 'react';

interface MemoPreviewProps {
    content: string;
}

const MemoPreview = ({ content }: MemoPreviewProps) => (
    <div className="memo-content flex items-center gap-1 text-xs text-yellow-500 bg-yellow-100 rounded px-2 py-1 mb-1 max-w-full truncate">
        <span className="whitespace-nowrap overflow-hidden text-ellipsis">
            {content.slice(0, 20)}
            {content.length > 20 ? '...' : ''}
        </span>
    </div>
);

export default MemoPreview;
