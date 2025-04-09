'use client';
import React, { useEffect, useState } from 'react';
import { Check } from 'lucide-react';
import FileUpload from '@/app/common/components/FileUpload/UploadButton';
import FloatingActionButton from '@/app/common/components/FloatingButton/FloatingButton';
import { useToastStore } from '@/app/common/hooks/useToastStore';
import PreviewImg from '@/app/upload/components/PreviewImg/PreviewImg';
import Button from '@/app/common/components/Button/Button';
import Text from '@/app/common/components/Text/Text';

const UploadPage = () => {
    const [receipt, setReceipt] = useState<File[]>([]);
    const [previewUrls, setPreviewUrls] = useState<string[]>([]);

    const { addToast } = useToastStore();

    const handleReciptUpload = (newFiles: File[]) => {
        console.log('새로 업로드된 파일들:', newFiles);

        const isDuplicate = newFiles.some((newFile) =>
            receipt.some((existingFile) => existingFile.name === newFile.name)
        );

        if (isDuplicate) {
            addToast('이미 존재하는 파일입니다.', 'error');
            return;
        }

        const updatedReceipt = [...receipt, ...newFiles];
        console.log('업데이트된 전체 파일:', updatedReceipt);

        const newFileUrls = newFiles.map((file) => URL.createObjectURL(file));

        const updatedUrls = [...previewUrls, ...newFileUrls];

        setReceipt(updatedReceipt);
        setPreviewUrls(updatedUrls);
    };

    const handleDeleteFile = (index: number) => {
        URL.revokeObjectURL(previewUrls[index]);

        const newReceipt = receipt.filter((_, i) => i !== index);
        const newUrls = previewUrls.filter((_, i) => i !== index);

        setReceipt(newReceipt);
        setPreviewUrls(newUrls);
    };

    useEffect(() => {
        return () => {
            previewUrls.forEach((url) => URL.revokeObjectURL(url));
        };
    }, []);

    return (
        <>
            <div className="bg-white w-[800px] rounded-lg shadow-lg p-8 mx-auto my-10 border border-gray-100 overflow">
                <Text.Title className="flex justify-center mb-4">
                    파일 업로드
                </Text.Title>

                <Text.SmallText className="mb-4 text-gray-500">
                    필요한 영수증 파일을 업로드해주세요. 이미지 파일만
                    가능합니다.
                </Text.SmallText>

                <FileUpload
                    onFileUpload={handleReciptUpload}
                    maxSize={10}
                    accept="image/*"
                    multiple={true}
                />

                <Button
                    className=" text-white py-2 px-4 rounded-md transition-colors duration-200 flex items-center text mt-4"
                    disabled={previewUrls.length === 0}
                >
                    <Check className="w-6 h-6 mr-2" />
                    업로드 완료
                </Button>

                <PreviewImg
                    previewUrls={previewUrls}
                    handleDeleteFile={handleDeleteFile}
                />
            </div>

            <FloatingActionButton />
        </>
    );
};

export default UploadPage;
