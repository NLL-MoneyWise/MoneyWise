'use client';
import FileUpload from '@/app/common/components/FileUpload/UploadButton';
import FloatingActionButton from '@/app/common/components/FloatingButton/FloatingButton';
import { useToastStore } from '@/app/common/hooks/useToastStore';

import React, { useEffect, useState } from 'react';

const UploadPage = () => {
    const [receipt, setReceipt] = useState<File[]>([]);
    const [previewUrls, setPreviewUrls] = useState<string[]>([]);

    const { addToast } = useToastStore();

    const handleReciptUpload = (newFiles: File[]) => {
        console.log('새로 업로드된 파일들:', newFiles);

        // 중복 파일 체크
        const isDuplicate = newFiles.some((newFile) =>
            receipt.some((existingFile) => existingFile.name === newFile.name)
        );

        if (isDuplicate) {
            addToast('이미 존재하는 파일입니다.', 'error');
            return;
        }

        // 기존 파일과 새 파일 합치기
        const updatedReceipt = [...receipt, ...newFiles];
        console.log('업데이트된 전체 파일:', updatedReceipt);

        // 새 파일들의 URL 생성
        const newFileUrls = newFiles.map((file) => URL.createObjectURL(file));

        // 기존 URL과 새 URL 합치기
        const updatedUrls = [...previewUrls, ...newFileUrls];

        // state 업데이트
        setReceipt(updatedReceipt);
        setPreviewUrls(updatedUrls);

        console.log('업데이트된 URL들:', updatedUrls);
    };

    // 파일 삭제 처리 함수
    const handleDeleteFile = (index: number) => {
        // 삭제할 URL을 찾아 메모리 해제
        URL.revokeObjectURL(previewUrls[index]);

        // 파일과 URL 배열에서 해당 항목 제거
        const newReceipt = receipt.filter((_, i) => i !== index);
        const newUrls = previewUrls.filter((_, i) => i !== index);

        setReceipt(newReceipt);
        setPreviewUrls(newUrls);
    };

    // 컴포넌트 언마운트 시 모든 URL 객체 해제
    useEffect(() => {
        return () => {
            previewUrls.forEach((url) => URL.revokeObjectURL(url));
        };
    }, []);

    useEffect(() => {
        console.log('현재 파일 개수:', receipt.length);
        console.log('현재 URL 개수:', previewUrls.length);
    }, [receipt, previewUrls]);

    return (
        <>
            <div className="bg-white w-[800px] rounded-lg shadow-lg p-8 mx-auto my-10 border border-gray-100">
                <h1 className="text-2xl text-gray-700 font-bold mb-6 text-center">
                    파일 업로드
                </h1>

                <div className="mb-4">
                    <p className="text-sm text-gray-500 mb-4">
                        필요한 영수증 파일을 업로드해주세요. 이미지 파일만
                        가능합니다.
                    </p>
                </div>

                <FileUpload
                    onFileUpload={handleReciptUpload}
                    maxSize={10}
                    accept="image/*"
                    multiple={true}
                />

                {previewUrls.length > 0 && (
                    <div className="mt-6">
                        <h2 className="text-lg text-gray-700 font-semibold mb-3">
                            미리보기 ({previewUrls.length}개 파일)
                        </h2>
                        <div className="grid grid-cols-3 gap-4 max-h-96 overflow-y-auto">
                            {previewUrls.map((url, index) => (
                                <div
                                    key={`${url}-${index}`}
                                    className="relative"
                                >
                                    <img
                                        src={url}
                                        alt={`Receipt preview ${index + 1}`}
                                        className="w-full h-40 object-cover rounded-lg shadow-sm border border-gray-200"
                                    />
                                    <button
                                        type="button"
                                        onClick={() => handleDeleteFile(index)}
                                        className="absolute top-2 right-2 bg-red-500 text-white rounded-full w-6 h-6 flex items-center justify-center hover:bg-red-600"
                                    >
                                        ×
                                    </button>
                                    <span className="absolute bottom-2 left-2 bg-gray-800 bg-opacity-75 text-white text-xs px-2 py-1 rounded">
                                        {index + 1}
                                    </span>
                                </div>
                            ))}
                        </div>
                    </div>
                )}
            </div>
            <FloatingActionButton />
        </>
    );
};

export default UploadPage;
