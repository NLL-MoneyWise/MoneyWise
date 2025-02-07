import ToastContainer from './common/components/Toast/ToastContainer';
import './globals.css';

import { Noto_Sans_KR } from 'next/font/google';

const notoSansKr = Noto_Sans_KR({
    subsets: ['latin'],
    weight: ['400', '500', '700'],
    display: 'swap',
    preload: true
});

export default function RootLayout({
    children
}: Readonly<{
    children: React.ReactNode;
}>) {
    return (
        <html>
            <body lang="ko" className={notoSansKr.className}>
                <main
                    className="min-w-[375px] 
                             max-w-[475px] 
                             w-full 
                             h-screen
                             min-h-screen
                             bg-white
                             flex
                             flex-col
                             mx-auto
                             box-border
                             border-x
                             border-gray-200"
                >
                    {children}
                </main>
                <div id="toast-portal"></div>
                <div id="modal-portal"></div>
                <ToastContainer />
            </body>
        </html>
    );
}
