'use client';

import React from 'react';

import { useEffect } from 'react';
import { gsap } from 'gsap';
import { ScrollTrigger } from 'gsap/dist/ScrollTrigger';
import InstructionSlide from './InstructionSlide';

const InstructionArticle = () => {
    gsap.registerPlugin(ScrollTrigger);

    useEffect(() => {
        const slides = gsap.utils.toArray('.slide');

        const tween = gsap.to(slides, {
            xPercent: -100 * (slides.length - 1),
            ease: 'none'
        });

        ScrollTrigger.create({
            trigger: '.slides-container',
            pin: true,
            start: 'top top',
            end: () => `+=${slides.length * 100}%`,
            scrub: 1,
            animation: tween
            //markers: true
        });

        return () => {
            ScrollTrigger.getAll().forEach((trigger) => trigger.kill());
        };
    }, []);

    return (
        <article className="slides-container h-screen ">
            <div className="slides flex h-screen">
                <section className="bg-gradient-to-r from-primary/20 via-primary/30 to-primary/40 w-screen slide shrink-0">
                    <InstructionSlide
                        title={'당신의 소비, 한눈에 확인하세요'}
                        description={
                            <>
                                달력으로 날짜별 지출을 한눈에! <br />
                                언제, 어디서 얼마나 썼는지 쉽게 파악하고
                                <br />
                                스마트하게 소비 패턴을 관리하세요.
                            </>
                        }
                        imageSrc="/calendar.png"
                    />
                </section>
                <section className="bg-gradient-to-r from-primary/40 via-primary/50 to-primary/60 w-screen slide shrink-0">
                    <InstructionSlide
                        title={'어디에 얼마나 썼는지 한눈에!'}
                        description={
                            <>
                                카테고리·가게별 지출 내역을 자동으로 분석해줘요.{' '}
                                <br />
                                식비, 음료, 쇼핑까지!
                                <br />
                                소비 습관을 시각적으로 쉽게 파악할 수 있어요.
                            </>
                        }
                        imageSrc="/category.png"
                        imgWidth={500}
                        imagePosition="left"
                    />
                </section>
                <section className="bg-gradient-to-r from-primary/60 via-primary/70 to-primary/80 w-screen slide shrink-0">
                    <InstructionSlide
                        title={'영수증만 올리면 끝!'}
                        description={
                            <>
                                사진 한 장으로 지출 내역을 자동 분석해줘요.
                                <br />
                                카테고리, 가게, 날짜까지 똑똑하게 정리해서
                                <br />
                                당신의 소비 패턴을 한눈에 보여줍니다.
                            </>
                        }
                        imageSrc="/upload.png"
                        imgWidth={600}
                    />
                </section>
            </div>
        </article>
    );
};

export default InstructionArticle;
