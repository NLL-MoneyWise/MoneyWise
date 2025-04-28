'use client';

import React from 'react';

import { useEffect } from 'react';
import { gsap } from 'gsap';
import { ScrollTrigger } from 'gsap/dist/ScrollTrigger';
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
                <section className="bg-primary/30 w-screen slide shrink-0"></section>
                <section className="bg-primary/40 0 w-screen slide shrink-0"></section>
                <section className="bg-primary/50  w-screen slide shrink-0"></section>
                <section className="bg-primary/60  w-screen slide shrink-0"></section>
                <section className="bg-primary/70  w-screen slide shrink-0"></section>
            </div>
        </article>
    );
};

export default InstructionArticle;
