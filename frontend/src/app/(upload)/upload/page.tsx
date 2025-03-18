'use client';
import FileUpload from '@/app/common/components/FileUpload/UploadButton';
import React, { useEffect, useState } from 'react';

const UploadPage = () => {
    const [receipt, setReceipt] = useState<File[]>([]);

    const handleReciptUpload = (receipt: File[]) => {
        setReceipt(receipt);
    };

    useEffect(() => {
        console.log(receipt);
    }, [receipt]);

    return (
        <div className="bg-white w-6/12 rounded-lg shadow-lg p-8 mx-auto my-10 border border-gray-100">
            <h2 className="text-2xl text-gray-700 font-bold mb-6 text-center">
                파일 업로드
            </h2>

            <div className="mb-4">
                <p className="text-sm text-gray-500 mb-4">
                    필요한 영수증 파일을 업로드해주세요. 이미지 파일만
                    가능합니다.
                </p>
            </div>

            <FileUpload
                onFileUpload={handleReciptUpload}
                maxSize={10}
                accept="image/*,.pdf"
            />
        </div>
    );
};

export default UploadPage;
