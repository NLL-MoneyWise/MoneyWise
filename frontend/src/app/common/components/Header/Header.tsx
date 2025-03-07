import React from 'react';
import Logo from '../Logo/Logo';
import Button from '../Button/Button';

const Header = () => {
    return (
        <header className=" py-6 fixed left-0 right-0 top-0  bg-white shadow-md z-50">
            <div className=" max-w-[1200px] m-auto">
                <div className="items-center flex justify-center sm:justify-between w-full px-1 ">
                    <h1 aria-label="로고" className="">
                        <Logo variant="dark" width={250} height={40} />
                    </h1>
                    <Button width={120} height={40} className="hidden sm:block">
                        시작하기
                    </Button>
                </div>
            </div>
        </header>
    );
};

export default Header;
