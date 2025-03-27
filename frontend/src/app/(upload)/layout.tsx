export default function CommonLayout({
    children
}: Readonly<{
    children: React.ReactNode;
}>) {
    return (
        <div className="bg-primary h-screen flex items-center justify-center">
            {children}
        </div>
    );
}
