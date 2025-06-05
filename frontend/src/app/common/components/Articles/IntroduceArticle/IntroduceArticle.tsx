'use client';
import React from 'react';

import Text from '../../Text/Text';
import Link from '../../Link/Link';

const IntroduceArticle = () => {
    return (
        <figure className="flex items-center gap-[150px] relative justify-center h-screen  max-w-screen-xl mx-auto ">
            <figcaption className=" animate-fade-up animate-duration-1000 animate-ease-out z-20 ">
                <h2 className="whitespace-nowrap font-bold leading-[1.2] mb-6 text-[50px] sm:text-[70px] md:text-[90px] ">
                    <span className="text-primary">M</span>oney Talks, <br />
                    <span className="text-primary">W</span>ise Listens
                </h2>
                <Text>{'Money Wise에서 소비습관을 분석하세요'}</Text>

                <Link address="/analyze" className="w-52 h-12 text-base mt-5">
                    분석하러 가기
                </Link>
            </figcaption>
        </figure>
    );
};

export default IntroduceArticle;
