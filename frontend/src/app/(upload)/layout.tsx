export default function CommonLayout({
    children
}: Readonly<{
    children: React.ReactNode;
}>) {
    return (
        <div className="bg-primary min-h-screen flex items-center justify-center relative">
            {children}
        </div>
    );
}
