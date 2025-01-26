import React from 'react';
import Link from 'next/link';
import { IoMdHome } from '@react-icons/all-files/io/IoMdHome';
import { IoIosSend } from '@react-icons/all-files/io/IoIosSend';
import { IoMap } from '@react-icons/all-files/io5/IoMap';
import { FaUserCircle } from '@react-icons/all-files/fa/FaUserCircle';
import { usePathname } from 'next/navigation';

interface NavigationItemProps {
    icon: React.ReactNode;
    label: string;
    path: string;
    isActive: boolean;
}

const NavigationItem = ({
    icon,
    label,
    path,
    isActive
}: NavigationItemProps) => (
    <Link
        href={path}
        className={`flex flex-col items-center justify-center p-2 ${
            isActive ? 'text-primary' : 'text-gray-400'
        }`}
    >
        <div>{icon}</div>
        <span className="text-sm">{label}</span>
    </Link>
);

const navItems = [
    { id: 0, icon: <IoMdHome size={24} />, label: '홈', path: '/' },
    { id: 1, icon: <IoIosSend size={24} />, label: '영수증 분석', path: '/a' },
    { id: 2, icon: <IoMap size={24} />, label: '지도', path: '/b' },
    { id: 3, icon: <FaUserCircle size={24} />, label: '마이페이지', path: '/c' }
];

const NavigationBar = () => {
    const pathname = usePathname();

    return (
        <nav className="flex justify-around bg-white border p-3 max-w-[473px] min-w-[375px] w-full z-[9999]">
            {navItems.map((item) => (
                <div className="flex justify-center flex-1" key={item.id}>
                    <NavigationItem
                        {...item}
                        isActive={pathname === item.path}
                    />
                </div>
            ))}
        </nav>
    );
};

export default NavigationBar;
