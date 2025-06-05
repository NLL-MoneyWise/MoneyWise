import Link from '@/app/common/components/Link/Link';
import Text from '@/app/common/components/Text/Text';
import Image from 'next/image';
import React from 'react';

const FaildPage = () => {
    return (
        <div className="w-fit text-center">
            <Image
                src={'/마스코트 울상.png'}
                alt="울상짓는 마스코트 이미지"
                width={320}
                height={320}
            />
            <Text.Title>로그인에 실패했습니다..</Text.Title>
            <Text.SubTitle className="mb-4">잠시만 기다려주세요</Text.SubTitle>
            <Link address="/" className="w-full">
                홈으로 돌아가기
            </Link>
        </div>
    );
};

export default FaildPage;
