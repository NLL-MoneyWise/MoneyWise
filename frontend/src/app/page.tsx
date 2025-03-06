import Header from './common/components/Header/Header';

export default function Home() {
    return (
        <div>
            <Header />
            <div
                className="h-screen snap-y snap-mandatory overflow-y-scroll "
                style={{ scrollbarWidth: 'none' }}
            >
                <div className="h-screen flex justify-center bg-mockupbg/60 snap-start"></div>
                <div className="h-screen flex justify-center bg-slate-500 snap-start"></div>
                <div className="h-screen flex justify-center bg-yellow-300 snap-start"></div>
            </div>
        </div>
    );
}
