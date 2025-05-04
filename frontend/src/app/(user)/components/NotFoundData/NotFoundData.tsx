import React from 'react';
import Image from 'next/image';
import Text from '@/app/common/components/Text/Text';
import Button from '@/app/common/components/Button/Button';

interface NotFoundDataProps {
    handleClick?: () => void;
}

const NotFoundData = ({ handleClick }: NotFoundDataProps) => {
    return (
        <div className="w-full flex-1  flex items-center justify-center ">
            <div className="w-fit text-center flex flex-col">
                <Image
                    src={'/마스코트 울상.png'}
                    alt="마스코트 이미지"
                    width={320}
                    height={320}
                />
                <Text.Title>NOT FOUND</Text.Title>

                <Text.SubTitle>데이터가 존재하지 않습니다.</Text.SubTitle>
                {handleClick !== undefined && (
                    <Button onClick={handleClick}>다시 불러오기</Button>
                )}
            </div>
        </div>
    );
};

export default NotFoundData;
