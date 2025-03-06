import React from 'react';
import Logo from '../Logo/Logo';
import Button from '../Button/Button';

const Header = () => {
    return (
        <header className=" py-6 fixed left-0 right-0 top-0  bg-white shadow-sm">
            <div className=" max-w-[1200px] m-auto">
                <div className="flex justify-between w-full px-1">
                    <h1 aria-label="로고" className="flex items-center">
                        <Logo variant="dark" width={300} height={200} />
                    </h1>
                    <Button width={120} height={40} fontSize={16}>
                        시작하기
                    </Button>
                </div>
            </div>
        </header>
    );
};

export default Header;
