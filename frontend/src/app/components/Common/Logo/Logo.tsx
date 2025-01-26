import React from 'react';

const PrimaryLogo = () => {
    return (
        <>
            <img src={'/logo.svg'} alt="primary logo"></img>
        </>
    );
};

const DarkLogo = () => {
    return (
        <>
            <img src={'/darklogo.svg'} alt="dark logo"></img>
        </>
    );
};

interface LogoProps {
    variant: 'primary' | 'dark';
}

const Logo = ({ variant = 'primary' }: LogoProps) => {
    if (variant === 'primary') {
        return <PrimaryLogo></PrimaryLogo>;
    } else {
        return <DarkLogo></DarkLogo>;
    }
};

export default Logo;
