import React, { ReactNode, HTMLAttributes } from 'react';
import clsx from 'clsx';

interface TextProps extends HTMLAttributes<HTMLDivElement> {
    children: ReactNode;
}

const Text = ({ children, className, ...props }: TextProps) => {
    return (
        <span className={clsx('text-lg font-medium ', className)} {...props}>
            {children}
        </span>
    );
};

// Title 컴포넌트
const Title = ({ children, className, ...props }: TextProps) => {
    return (
        <h1 className={clsx('text-2xl font-bold', className)} {...props}>
            {children}
        </h1>
    );
};
Title.displayName = 'Text.Title';

// SubTitle 컴포넌트
const SubTitle = ({ children, className, ...props }: TextProps) => {
    return (
        <h2
            className={clsx('text-xl font-bold text-gray-400', className)}
            {...props}
        >
            {children}
        </h2>
    );
};

SubTitle.displayName = 'Text.SubTitle';

// BoldText 컴포넌트
const BoldText = ({ children, className, ...props }: TextProps) => {
    return (
        <span className={clsx('font-bold text-xl', className)} {...props}>
            {children}
        </span>
    );
};
BoldText.displayName = 'Text.BoldText';

// SemiBoldText 컴포넌트
const SemiBoldText = ({ children, className, ...props }: TextProps) => {
    return (
        <span className={clsx('font-semibold text-xl', className)} {...props}>
            {children}
        </span>
    );
};
SemiBoldText.displayName = 'Text.SemiBoldText';

// SmallText 컴포넌트
const SmallText = ({ children, className, ...props }: TextProps) => {
    return (
        <h2 className={clsx('text-sm', className)} {...props}>
            {children}
        </h2>
    );
};
SmallText.displayName = 'Text.SmallText';

// LagreText 컴포넌트
const LagreText = ({ children, className, ...props }: TextProps) => {
    return (
        <h2 className={clsx('text-xl', className)} {...props}>
            {children}
        </h2>
    );
};
LagreText.displayName = 'Text.LagreText';

// 컴포넌트 연결
Text.Title = Title;
Text.SubTitle = SubTitle;
Text.BoldText = BoldText;
Text.SemiBoldText = SemiBoldText;
Text.SmallText = SmallText;
Text.LagreText = LagreText;
export default Text;
