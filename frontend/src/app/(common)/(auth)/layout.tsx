import Logo from '@/app/common/components/Logo/Logo';
import TopBar from '@/app/common/components/TopBar/TopBar';

export default function AuthLayout({
    children
}: {
    children: React.ReactNode;
}) {
    return (
        <>
            <TopBar />

            <div className="flex flex-col w-full h-full items-center justify-center">
                <Logo variant="dark" width={400} height={41.7} />
                <div className="w-full space-y-4 mt-10">{children}</div>
            </div>
        </>
    );
}
