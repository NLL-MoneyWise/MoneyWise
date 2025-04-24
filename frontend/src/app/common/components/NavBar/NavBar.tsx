'use client';
import Link from 'next/link';
import React from 'react';
const navLinks = [
    { href: '/upload', label: '업로드' },
    { href: '/mypage', label: '개인 정보' },
    { href: '/calendar', label: '달력' },
    { href: '/history', label: '분석' }
];

const NavBar = () => {
    return (
        <>
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
        </>
    );
};

export default NavBar;
