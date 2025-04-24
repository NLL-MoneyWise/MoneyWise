import ToastContainer from './common/components/Toast/ToastContainer';
import './globals.css';
import { Noto_Sans_KR } from 'next/font/google';
import { QueryProviders } from './QueryProvider';
import MSWProvider from './MockingProiver';

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
                <QueryProviders>
                    <main>
                        <MSWProvider />
                        {children}
                    </main>
                </QueryProviders>
                <div id="toast-portal"></div>
                <ToastContainer />
                <div id="modal-portal"></div>
            </body>
        </html>
    );
}
