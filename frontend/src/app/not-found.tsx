'use client';
import Image from 'next/image';
import Text from './common/components/Text/Text';
import Link from '@/app/common/components/Link/Link';

export default function NotFound() {
    return (
        <div className="absolute inset-0 flex justify-center items-center text-xl flex-col">
            <div className="w-fit text-center ">
                <Image
                    src={'/찾을 수 없는 마스코트.png'}
                    alt="마스코트 이미지"
                    width={320}
                    height={320}
                />
                <Text.Title>NOT FOUND</Text.Title>

                <Text.SubTitle>페이지를 찾을 수 없어요</Text.SubTitle>

                <Link address="/" className="w-full">
                    홈으로 돌아가기
                </Link>
            </div>
        </div>
    );
}
