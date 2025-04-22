import React from 'react';
import Logo from '../Logo/Logo';
import Button from '../Button/Button';
import { useUserStore } from '@/stores/userStore';
import { useRouter } from 'next/navigation';
import Link from 'next/link';

const navLinks = [
    { href: '/upload', label: '업로드' },
    { href: '/mypage', label: '개인 정보' },
    { href: '/history', label: '분석' },
    { href: '/history', label: '분석' }
];

const Header = () => {
    const { logout, isLoggedIn } = useUserStore();
    const router = useRouter();

    const handleClick = () => {
        if (isLoggedIn()) {
            logout();
        } else {
            router.push('login');
        }
    };

    return (
        <header className=" py-6 fixed left-0 right-0 top-0  bg-white z-10">
            <div className=" max-w-screen-xl m-auto">
                <div className="items-center flex justify-center sm:justify-between w-full px-1 ">
                    <h1 aria-label="로고" className="">
                        <Logo variant="dark" width={250} height={40} />
                    </h1>
                    {isLoggedIn() && (
                        <nav className="hidden md:block flex-grow mx-16 font-medium ">
                            <ul className="flex justify-between">
                                {navLinks.map((link, index) => (
                                    <li key={index}>
                                        <Link
                                            href={link.href}
                                            className="transition-colors duration-200 py-2 hover:text-primary"
                                        >
                                            {link.label}
                                        </Link>
                                    </li>
                                ))}
                            </ul>
                        </nav>
                    )}

                    <Button
                        className="hidden sm:block w-32 h-10"
                        onClick={handleClick}
                    >
                        {isLoggedIn() ? '로그아웃' : '로그인'}
                    </Button>
                </div>
            </div>
        </header>
    );
};

export default Header;
