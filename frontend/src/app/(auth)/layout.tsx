import Logo from '@/app/common/components/Logo/Logo';
import TopBar from '@/app/common/components/TopBar/TopBar';
import { Suspense } from 'react';

export default function AuthLayout({
    children
}: {
    children: React.ReactNode;
}) {
    return (
        <div
            className="
        min-w-[375px] 
        max-w-[768px] 
        w-full 
        h-screen
        min-h-screen
        flex
        bg-white
        flex-col
        mx-auto
        box-border
        "
        >
            <Suspense fallback={'로딩 중..'}>
                <TopBar />
                <div className="flex flex-col w-full h-full items-center justify-center">
                    <Logo variant="dark" width={400} height={41.7} />
                    <div className="w-full space-y-4 mt-10">{children}</div>
                </div>
            </Suspense>
        </div>
    );
}
