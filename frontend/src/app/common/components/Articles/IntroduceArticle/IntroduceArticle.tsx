import React from 'react';
import Button from '../../Button/Button';
import DeviceImg from '../../DeviceImg/DeviceImg';

const IntroduceArticle = () => {
    return (
        <div className="h-screen bg-mockupbg snap-start overflow-hidden">
            <div className="flex py-[300px] gap-[60px] relative justify-center">
                <div className="text-black animate-fade-up animate-duration-1000 animate-delay-1000 animate-ease-out z-20 ">
                    <h2 className="text-[90px] font-bold leading-[1.2] mb-6 ">
                        Money Talks, <br />
                        Wise Listens
                    </h2>
                    <p className="mb-6">Money Wise에서 소비습관을 분석하세요</p>
                    <Button width={200} height={50} fontSize={16}>
                        분석하러 가기
                    </Button>
                </div>
                <div className="flex w-[600px]">
                    <div className="-rotate-[4deg] absolute">
                        <div className=" animate-fade-right animate-once animate-duration-1000 animate-ease-out animate-normal">
                            <DeviceImg />
                        </div>
                    </div>
                    <div className="absolute translate-x-[150px]">
                        <div className="animate-fade-up animate-once animate-duration-1000 animate-delay-500 animate-ease-out animate-normal">
                            <DeviceImg />
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default IntroduceArticle;
