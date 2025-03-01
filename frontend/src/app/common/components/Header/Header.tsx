import React from 'react';
import Logo from '../Logo/Logo';
import Button from '../Button/Button';

const Header = () => {
    return (
        <header className="flex justify-between w-[80%] m-auto items-center h-[50px]">
            <h1 aria-label="로고">
                <Logo variant="dark" width={250} height={200} />
            </h1>
            <Button width={130} height={35}>
                분석 시작하기
            </Button>
        </header>
    );
};

export default Header;
