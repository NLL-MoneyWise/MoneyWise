import React from 'react';
import { IoMdHome } from '@react-icons/all-files/io/IoMdHome';
import { IoIosSend } from '@react-icons/all-files/io/IoIosSend';
import { IoMap } from '@react-icons/all-files/io5/IoMap';
import { FaUserCircle } from '@react-icons/all-files/fa/FaUserCircle';
import { NavLink } from 'react-router-dom';

type NavigationItemProps = {
    icon: React.ReactNode;
    label: string;
    path: string;
};

const NavigationItem = ({ icon, label, path }: NavigationItemProps) => {
    return (
        <NavLink
            to={path}
            className={({ isActive }) =>
                `flex flex-col items-center justify-center p-2 ${
                    isActive ? 'text-primary' : 'text-gray-400'
                }`
            }
        >
            <div>{icon}</div>
            <span className="text-sm">{label}</span>
        </NavLink>
    );
};

const navItems = [
    { id: 0, icon: <IoMdHome />, label: '홈', path: '/' },
    {
        id: 1,
        icon: <IoIosSend />,
        label: '영수증 분석',
        path: '/a'
    },
    { id: 2, icon: <IoMap />, label: '지도', path: '/b' },
    { id: 3, icon: <FaUserCircle />, label: '마이페이지', path: '/c' }
];

const NavigationBar = () => {
    return (
        <nav className="flex justify-around bg-white border p-3 max-w-[473px] min-w-[375px] w-full z-[9999] ">
            {navItems.map((item) => (
                <div className="flex justify-center flex-1" key={item.id}>
                    <NavigationItem
                        icon={item.icon}
                        label={item.label}
                        path={item.path}
                    />
                </div>
            ))}
        </nav>
    );
};

export default NavigationBar;
