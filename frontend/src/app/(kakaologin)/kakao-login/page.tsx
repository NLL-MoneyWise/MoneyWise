'use client';
import useAuthMutation from '@/app/auth/hooks/useAuthMutation';
import Text from '@/app/common/components/Text/Text';
import Image from 'next/image';
import { useRouter, useSearchParams } from 'next/navigation';
import React, { Suspense, useEffect } from 'react';

const KakaoLoginPage = () => {
    const router = useRouter();
    const searchParams = useSearchParams();
    const { kakaoLogin } = useAuthMutation();
    useEffect(() => {
        const code = searchParams.get('code');

        if (!code) {
            router.push('/login-failed');
            return;
        }

        kakaoLogin.mutate({ code });
    }, []);

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
