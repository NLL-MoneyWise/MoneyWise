'use client';
import { useUserStore } from '@/stores/userStore';
import ArrowIcon from './common/components/ArrowIcon/ArrowIcon';
import InstructionArticle from './common/components/Articles/InstructionArticle/InstructionArticle';
import IntroduceArticle from './common/components/Articles/IntroduceArticle/IntroduceArticle';
import FloatingActionButton from './common/components/FloatingButton/FloatingButton';
import Header from './common/components/Header/Header';
import useClientMount from './common/hooks/useClinetMount';

export default function Home() {
    const { isLoggedIn } = useUserStore();
    const { isMounted } = useClientMount();

    if (!isMounted) return null;

    return (
        <div className="overflow-auto no-scrollbar ">
            <Header />
            <IntroduceArticle />
            <InstructionArticle />
            <ArrowIcon className="animate-bounce" color="#b0aeae" />
            {isLoggedIn() && <FloatingActionButton />}
        </div>
    );
}
