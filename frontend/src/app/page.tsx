'use client';
import useIsLoggedIn from './auth/hooks/useIsLoggedIn';
import ArrowIcon from './common/components/ArrowIcon/ArrowIcon';
import InstructionArticle from './common/components/Articles/InstructionArticle/InstructionArticle';
import IntroduceArticle from './common/components/Articles/IntroduceArticle/IntroduceArticle';
import FloatingActionButton from './common/components/FloatingButton/FloatingButton';
import Header from './common/components/Header/Header';

export default function Home() {
    const { isLoggedIn } = useIsLoggedIn();
    return (
        <div className="overflow-auto no-scrollbar ">
            <Header />
            <IntroduceArticle />
            <InstructionArticle />
            <div className="fixed bottom-[3%] left-1/2 -translate-x-1/2 z-10">
                <ArrowIcon className="animate-bounce " color="#b0aeae" />
            </div>
            {isLoggedIn && <FloatingActionButton />}
        </div>
    );
}
