import Image from 'next/image';
import React from 'react';

const BLUR_URL =
    'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTAwIiBoZWlnaHQ9IjUwIiB2aWV3Qm94PSIwIDAgMTAwIDUwIiBmaWxsPSJub25lIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciPjxyZWN0IHdpZHRoPSIxMDAiIGhlaWdodD0iNTAiIGZpbGw9IiNFNUU3RUIiLz48L3N2Zz4=';

interface LogoSizeType {
    width?: number;
    height?: number;
}
const PrimaryLogo = ({ width, height }: LogoSizeType) => {
    return (
        <Image
            src="/logo.svg"
            alt="primary logo"
            width={width}
            height={height}
            blurDataURL={BLUR_URL}
            placeholder="blur"
            unoptimized={true}
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
            blurDataURL={BLUR_URL}
            placeholder="blur"
            unoptimized={true}
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
