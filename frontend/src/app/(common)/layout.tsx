export default function CommonLayout({
    children
}: Readonly<{
    children: React.ReactNode;
}>) {
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
            border-x
            border-gray-200"
        >
            {children}
        </div>
    );
}
