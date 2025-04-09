import Text from '@/app/common/components/Text/Text';
import Image from 'next/image';
import React from 'react';

const KakaoLoginPage = () => {
    return (
        <>
            <Image
                src={'/마스코트.png'}
                alt="마스코트 이미지"
                width={320}
                height={320}
            />
            <Text.Title>로그인 중입니다.</Text.Title>

            <Text.SubTitle>잠시만 기다려주세요</Text.SubTitle>
        </>
    );
};

export default KakaoLoginPage;
