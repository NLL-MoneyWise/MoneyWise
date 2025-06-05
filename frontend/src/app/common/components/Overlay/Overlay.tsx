import React from 'react';

interface OverlayProps {
    closeHandler?: () => void;
    children: React.ReactNode;
}

const Overlay = ({ closeHandler, children }: OverlayProps) => {
    return (
        <div className="fixed inset-0 flex justify-center items-center z-[9]">
            <div
                className="absolute inset-0 bg-black/60 backdrop-blur-sm "
                onClick={closeHandler}
            />
            {children}
        </div>
    );
};

export default Overlay;
