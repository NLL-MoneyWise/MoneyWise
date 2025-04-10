'use client';
import ArrowIcon from './common/components/ArrowIcon/ArrowIcon';
import InstructionArticle from './common/components/Articles/InstructionArticle/InstructionArticle';
import IntroduceArticle from './common/components/Articles/IntroduceArticle/IntroduceArticle';
import Header from './common/components/Header/Header';

export default function Home() {
    return (
        <div className="overflow-auto no-scrollbar ">
            <Header />
            <IntroduceArticle />
            <InstructionArticle />
            <div className="fixed bottom-[3%] left-1/2 -translate-x-1/2 z-10">
                <ArrowIcon className="animate-bounce " color="#b0aeae" />
            </div>
        </div>
    );
}
