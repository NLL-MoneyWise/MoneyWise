import React, { useCallback, useState } from 'react';
import { MdFileUpload } from '@react-icons/all-files/md/MdFileUpload';
import Text from '@/app/common/components/Text/Text';

interface FileUploadProps {
    onFileUpload: (files: File[]) => void;
    maxSize?: number;
    accept?: string;
    multiple?: boolean;
}

const FileUpload: React.FC<FileUploadProps> = ({
    onFileUpload,
    maxSize = 5,
    accept = '*',
    multiple = false
}) => {
    const [isDragging, setIsDragging] = useState(false);
    const [error, setError] = useState<string>('');

    const handleDrag = useCallback((e: React.DragEvent) => {
        e.preventDefault();
        e.stopPropagation();

        if (e.type === 'dragenter' || e.type === 'dragover') {
            setIsDragging(true);
        } else if (e.type === 'dragleave') {
            setIsDragging(false);
        }
    }, []);

    const validateFiles = useCallback(
        (files: File[]) => {
            for (const file of files) {
                if (file.size > maxSize * 1024 * 1024) {
                    setError(`파일 크기는 ${maxSize}MB 이하여야 합니다.`);
                    return false;
                }

                if (accept !== '*' && !file.type.match(accept)) {
                    setError(`${accept} 형식의 파일만 업로드 가능합니다.`);
                    return false;
                }
            }
            return true;
        },
        [maxSize, accept]
    );

    const handleDrop = useCallback(
        (e: React.DragEvent) => {
            e.preventDefault();
            e.stopPropagation();
            setIsDragging(false);
            setError('');

            const { files } = e.dataTransfer;
            const fileList = multiple ? Array.from(files) : [files[0]];

            if (validateFiles(fileList)) {
                onFileUpload(fileList);
            }
        },
        [multiple, validateFiles, onFileUpload]
    );

    const handleFileInput = useCallback(
        (e: React.ChangeEvent<HTMLInputElement>) => {
            setError('');
            const files = e.target.files;
            if (files) {
                const fileList = multiple ? Array.from(files) : [files[0]];
                if (validateFiles(fileList)) {
                    onFileUpload(fileList);
                }
            }
        },
        [multiple, validateFiles, onFileUpload]
    );

    return (
        <div className="w-full h-96">
            <div
                className={`
                    relative flex flex-col items-center justify-center
                    w-full h-full border-2 border-dashed rounded-lg 
                    p-8 text-center cursor-pointer transition-colors
                    ${
                        isDragging
                            ? 'border-highlight bg-primary/10'
                            : 'border-primary hover:border-primary/80'
                    }
                `}
                onDragEnter={handleDrag}
                onDragLeave={handleDrag}
                onDragOver={handleDrag}
                onDrop={handleDrop}
                onClick={() => document.getElementById('fileInput')?.click()}
            >
                <input
                    id="fileInput"
                    type="file"
                    className="hidden"
                    accept={accept}
                    multiple={multiple}
                    onChange={handleFileInput}
                />

                <MdFileUpload className="w-12 h-12 text-primary" />

                <Text.SmallText className="mt-4 text-gray-600">
                    {isDragging ? (
                        '파일을 여기에 놓아주세요'
                    ) : (
                        <>
                            <span className="font-semibold text-primary">
                                파일을 선택
                            </span>
                            하거나 여기로 드래그하세요
                        </>
                    )}
                </Text.SmallText>

                <Text.SmallText className="mt-2  text-gray-500">
                    {multiple ? '여러 개의 파일' : '하나의 파일'}을 {maxSize}MB
                    이하로 업로드해주세요
                </Text.SmallText>

                {error && (
                    <Text.SmallText className="mt-2 text-red-500">
                        {error}
                    </Text.SmallText>
                )}
            </div>
        </div>
    );
};

export default FileUpload;
