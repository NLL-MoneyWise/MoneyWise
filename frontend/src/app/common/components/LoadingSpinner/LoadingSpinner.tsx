import React from 'react';
import { RotateLoader } from 'react-spinners';
import Overlay from '../Overlay/Overlay';

export const LoadingSpinner = () => {
    return (
        <div className="fixed inset-0 flex justify-center items-center z-[9]">
            <RotateLoader color="#6BAF2F" />
        </div>
    );
};

export const OverlayLoadingSpinner = () => {
    return (
        <Overlay>
            <LoadingSpinner />
        </Overlay>
    );
};
