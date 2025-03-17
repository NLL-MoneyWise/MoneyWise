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
            <div className="flex justify-center mt-[40%] mb-10">
                <Logo variant="dark" width={320} height={41.7} />
            </div>
            <div className="w-full space-y-4">{children}</div>
        </>
    );
}
