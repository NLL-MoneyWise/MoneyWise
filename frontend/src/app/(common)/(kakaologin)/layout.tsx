export default function KakaoAuthLayout({
    children
}: Readonly<{
    children: React.ReactNode;
}>) {
    return (
        <div className="absolute inset-0 flex justify-center items-center text-xl flex-col ">
            {children}
        </div>
    );
}
