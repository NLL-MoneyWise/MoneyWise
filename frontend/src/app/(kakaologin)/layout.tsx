import { Suspense } from 'react';
import { OverlayLoadingSpinner } from '../common/components/LoadingSpinner/LoadingSpinner';

export default function KakaoAuthLayout({
    children
}: Readonly<{
    children: React.ReactNode;
}>) {
    return (
        <div className="absolute inset-0 flex justify-center items-center text-xl flex-col ">
            <Suspense fallback={<OverlayLoadingSpinner />}>{children}</Suspense>
        </div>
    );
}
