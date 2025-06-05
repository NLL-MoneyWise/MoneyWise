import React from 'react';
import Text from '@/app/common/components/Text/Text';
import Image from 'next/image';

interface InstructionSlideProps {
    title: React.ReactNode;
    description: React.ReactNode;
    imageSrc: string;
    imageAlt?: string;
    className?: string;
    imgWidth?: number;
    imagePosition?: 'left' | 'right';
}

const InstructionSlide = ({
    title,
    description,
    imageSrc,
    imageAlt = '예시 이미지',
    className = '',
    imgWidth = 700,
    imagePosition = 'right'
}: InstructionSlideProps) => {
    const isLeft = imagePosition === 'left';
    return (
        <div
            className={`flex items-center h-full max-w-screen-xl mx-auto justify-between ${isLeft ? 'flex-row-reverse' : ''} ${className}`}
        >
            <div className="text-gray-500 flex flex-col gap-4">
                <Text.LaddingTitle>{title}</Text.LaddingTitle>
                <Text>{description}</Text>
            </div>
            <Image
                alt={imageAlt}
                width={imgWidth}
                height={300}
                className="rounded-xl"
                src={imageSrc}
            />
        </div>
    );
};

export default InstructionSlide;
