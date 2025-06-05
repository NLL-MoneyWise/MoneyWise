import React from 'react';
import { createPortal } from 'react-dom';
import { ModalContainerProps } from '@/app/common/types';
import useClientMount from '@/app/common/hooks/useClinetMount';
import Overlay from '../Overlay/Overlay';

interface ModalProps extends ModalContainerProps {
    closeHandler: () => void;
}

const Modal = ({ children, closeHandler }: ModalProps) => {
    const mounted = useClientMount();

    if (!mounted) return null;

    return createPortal(
        <Overlay closeHandler={closeHandler}>
            <div
                className="relative min-w-[600px] aspect-[4/2.5] bg-white rounded-[20px] p-4 shadow-lg animate-fadeIn z-[10]"
                onClick={(e) => e.stopPropagation()}
            >
                <div className="p-6 w-full h-fit min-h-full flex flex-col gap-4">
                    {children}
                </div>
            </div>
        </Overlay>,
        document.getElementById('modal-portal') || document.body
    );
};

export default Modal;
