import ToastContainer from './common/components/Toast/ToastContainer';
import './globals.css';
import { Noto_Sans_KR } from 'next/font/google';
import { QueryProviders } from './QueryProvider';
import MSWProvider from './MockingProiver';
import AuthWrapper from './common/components/AuthWrapper/AuthWrapper';

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
                    <AuthWrapper>
                        <main>
                            <MSWProvider />
                            {children}
                        </main>
                    </AuthWrapper>
                </QueryProviders>
                <div id="toast-portal" />
                <ToastContainer />
                <div id="modal-portal" />
            </body>
        </html>
    );
}
