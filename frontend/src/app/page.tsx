'use client';
import ArrowIcon from './common/components/ArrowIcon/ArrowIcon';
import InstructionArticle from './common/components/Articles/InstructionArticle/InstructionArticle';
import IntroduceArticle from './common/components/Articles/IntroduceArticle/IntroduceArticle';
import Header from './common/components/Header/Header';
import useClientMount from './common/hooks/useClinetMount';

export default function Home() {
    const { isMounted } = useClientMount();

    if (!isMounted) return null;

    return (
        <div className="overflow-auto no-scrollbar ">
            <Header />
            <IntroduceArticle />
            <InstructionArticle />
            <ArrowIcon className="animate-bounce" color="#b0aeae" />
        </div>
    );
}
