'use client';
import React from 'react';
import Button from '../../Button/Button';
import DeviceImg from '../../DeviceImg/DeviceImg';
import Text from '../../Text/Text';

const IntroduceArticle = () => {
    return (
        <figure className="flex items-center gap-[150px] relative justify-center h-screen overflow-hidden max-w-screen-xl m-auto ">
            <figcaption className=" animate-fade-up animate-duration-1000 animate-delay-1000 animate-ease-out z-20 ">
                <h2 className="whitespace-nowrap font-bold leading-[1.2] mb-6 text-[50px] sm:text-[70px] md:text-[90px] ">
                    <span className="text-primary">M</span>oney Talks, <br />
                    <span className="text-primary">W</span>ise Listens
                </h2>
                <Text>{'Money Wise에서 소비습관을 분석하세요'}</Text>

                <Button className="w-52 h-12 text-base mt-5">
                    분석하러 가기
                </Button>
            </figcaption>
            <div className="hidden lg:flex  w-[600px]">
                <div className="-rotate-[4deg] absolute">
                    <div className=" animate-fade-right animate-once animate-duration-1000 animate-ease-out animate-normal">
                        <DeviceImg />
                    </div>
                </div>
                <div className="absolute translate-x-[90px]">
                    <div className="animate-fade-up animate-once animate-duration-1000 animate-delay-500 animate-ease-out animate-normal">
                        <DeviceImg />
                    </div>
                </div>
            </div>
        </figure>
    );
};

export default IntroduceArticle;
