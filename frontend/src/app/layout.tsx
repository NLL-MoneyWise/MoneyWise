import ToastContainer from './components/Common/ToastContainer/ToastContainer';
import './globals.css';

export default function RootLayout({
    children
}: Readonly<{
    children: React.ReactNode;
}>) {
    return (
        <html lang="en">
            <body>
                {children}
                <div id="toast-portal"></div>
                <ToastContainer />
            </body>
        </html>
    );
}
