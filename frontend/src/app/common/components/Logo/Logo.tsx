import Image from 'next/image';
import React from 'react';

interface LogoSizeType {
    width?: number;
    height?: number;
}

const LogoStlye = 'mx-auto';

const PrimaryLogo = ({ width, height }: LogoSizeType) => {
    return (
        <Image
            src="/logo.svg"
            alt="primary logo"
            width={width}
            height={height}
            unoptimized={true}
            className={`${LogoStlye}`}
        />
    );
};

const DarkLogo = ({ width, height }: LogoSizeType) => {
    return (
        <Image
            src="/darklogo.svg"
            alt="primary logo"
            width={width}
            height={height}
            unoptimized={true}
            className={`${LogoStlye}`}
        />
    );
};

interface LogoProps extends LogoSizeType {
    variant: 'primary' | 'dark';
}

const Logo = ({ variant = 'primary', ...props }: LogoProps) => {
    if (variant === 'primary') {
        return <PrimaryLogo {...props}></PrimaryLogo>;
    } else {
        return <DarkLogo {...props}></DarkLogo>;
    }
};

export default Logo;
