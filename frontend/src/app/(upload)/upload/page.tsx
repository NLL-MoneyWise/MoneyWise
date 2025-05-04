'use client';
import React, { useEffect, useState } from 'react';
import { Check } from 'lucide-react';
import FloatingActionButton from '@/app/common/components/FloatingButton/FloatingButton';
import { useToastStore } from '@/app/common/hooks/useToastStore';
import Button from '@/app/common/components/Button/Button';
import Text from '@/app/common/components/Text/Text';
import PreviewImg from '../components/PreviewImg/PreviewImg';
import FileUpload from '../components/FileUpload/UploadButton';
import useUpload from '../hooks/useUpload';

const UploadPage = () => {
    const [receipt, setReceipt] = useState<File[]>([]);
    const [previewUrls, setPreviewUrls] = useState<string[]>([]);

    const { addToast } = useToastStore();
    const {
        uploadRecipt: { data, isLoading, refetch },
        analyzeRecipt
    } = useUpload();

    if (isLoading) {
        <div>로딩</div>;
    }

    // const { isPending } = analyzeRecipt;

    const handleReciptUpload = (newFiles: File[]) => {
        const isDuplicate = newFiles.some((newFile) =>
            receipt.some((existingFile) => existingFile.name === newFile.name)
        );

        if (isDuplicate) {
            addToast('이미 존재하는 파일입니다.', 'error');
            return;
        }

        const updatedReceipt = [...receipt, ...newFiles];

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

    const handleClick = async () => {
        try {
            // 파일이 없으면 리턴
            if (receipt.length === 0) {
                addToast('업로드할 파일이 없음', 'error');
                return;
            }

            // data가 없으면 리턴
            if (!data) {
                addToast('에러가 발생했습니다.', 'error');
                return;
            }

            // .preSignedUrl이 문자열인 경우 (단일 URL)
            if (typeof data.preSignedUrl === 'string') {
                const file = receipt[0];
                const response = await fetch(data.preSignedUrl, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': file.type,
                        mode: 'no-cors'
                    },
                    body: file
                });

                if (!response.ok) {
                    throw new Error('업로드 실패');
                }
                await analyzeRecipt.mutateAsync({ accessUrl: data.accessUrl });
            }
            // .preSignedUrl이 배열인 경우 (여러 URL)
            else if (Array.isArray(data.preSignedUrl)) {
                const uploadPromises = receipt
                    .map((file, index) => {
                        if (index >= data.preSignedUrl.length) return null;

                        return fetch(data.preSignedUrl[index], {
                            method: 'PUT',
                            headers: {
                                'Content-Type': file.type
                            },
                            body: file
                        });
                    })
                    .filter(Boolean);

                const results = await Promise.all(uploadPromises);

                if (results.every((res) => res!.ok)) {
                    await analyzeRecipt.mutateAsync({
                        accessUrl: data.accessUrl
                    });

                    addToast('모든 파일 업로드 성공!', 'success');
                } else {
                    throw new Error('일부 파일 업로드 실패');
                }
            }
        } catch (error) {
            refetch();
            return;
        }
    };

    useEffect(() => {
        return () => {
            previewUrls.forEach((url) => URL.revokeObjectURL(url));
        };
    }, []);

    return (
        <>
            <div className="bg-white w-[800px] rounded-lg shadow-lg p-8 mx-auto my-10 border border-gray-100 overflow">
                <Text.Title className="flex justify-center mb-4  text-gray-500">
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
                    onClick={handleClick}
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
