import ArrowIcon from './common/components/ArrowIcon/ArrowIcon';
import InstructionArticle from './common/components/Articles/InstructionArticle/InstructionArticle';
import IntroduceArticle from './common/components/Articles/IntroduceArticle/IntroduceArticle';
import FloatingActionButton from './common/components/FloatingButton/FloatingButton';
import Header from './common/components/Header/Header';

export default function Home() {
    return (
        <div className="overflow-auto no-scrollbar ">
            <Header />
            <IntroduceArticle />
            <InstructionArticle />
            <ArrowIcon className="animate-bounce" color="#b0aeae" />
            <FloatingActionButton />
        </div>
    );
}
