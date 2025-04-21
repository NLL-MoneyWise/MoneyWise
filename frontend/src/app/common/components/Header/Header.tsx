import React from 'react';
import Logo from '../Logo/Logo';
import Button from '../Button/Button';
import { useUserStore } from '@/stores/userStore';
import { useRouter } from 'next/navigation';

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
