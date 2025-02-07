export default function AuthLayout({
    children
}: {
    children: React.ReactNode;
}) {
    return (
        <>
            <div className="w-full space-y-4">{children}</div>
        </>
    );
}
