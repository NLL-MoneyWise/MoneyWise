'use client';
import { kakaoLogin } from '@/app/(auth)/api/action';
import Text from '@/app/common/components/Text/Text';
import { useToastStore } from '@/app/common/hooks/useToastStore';
import Image from 'next/image';
import { useRouter, useSearchParams } from 'next/navigation';
import React, { useEffect } from 'react';

const KakaoLoginPage = () => {
    const searchParams = useSearchParams();
    const { addToast } = useToastStore();
    const router = useRouter();

    useEffect(() => {
        const fetchData = async () => {
            try {
                const code = searchParams.get('code');

                if (!code) {
                    router.push('/login-failed');
                    return;
                }

                const response = await kakaoLogin({ code });
                addToast(response.message, 'success');
                router.push('/');
            } catch {
                router.push('/login-failed');
            }
        };

        fetchData();
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
