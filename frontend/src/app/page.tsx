import ArrowIcon from './common/components/ArrowIcon/ArrowIcon';
import IntroduceArticle from './common/components/Articles/IntroduceArticle/IntroduceArticle';

import Header from './common/components/Header/Header';

export default function Home() {
    return (
        <div>
            <Header />
            <div
                className="h-screen snap-y snap-mandatory overflow-y-scroll "
                style={{ scrollbarWidth: 'none' }}
            >
                <IntroduceArticle />
                <div className="h-screen flex justify-center bg-slate-500 snap-start "></div>
                <div className="h-screen flex justify-center bg-yellow-300 snap-start"></div>
            </div>
            <div className="fixed bottom-[3%] left-1/2 -translate-x-1/2 z-10">
                <ArrowIcon className="animate-bounce " color="white" />
            </div>
        </div>
    );
}
