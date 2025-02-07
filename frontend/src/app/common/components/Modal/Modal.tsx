import React from 'react';
import { createPortal } from 'react-dom';
import { IoMdClose } from '@react-icons/all-files/io/IoMdClose';
import { ModalContainerProps } from '@/app/common/types';
import useClientMount from '@/app/common/hooks/useClinetMount';
const closeStyles = 'w-5 h-5 md:w-6 md:h-6 text-gray-500';

interface ModalProps extends ModalContainerProps {
    closeHandler: () => void;
}

const Modal = ({ children, closeHandler }: ModalProps) => {
    const mounted = useClientMount();

    if (!mounted) return null;

    return createPortal(
        <div className="fixed inset-0 flex justify-center items-center">
            <div className="relative w-[370px] h-[200px] bg-white rounded-[20px] p-4 shadow-lg animate-fadeIn">
                <div className="flex justify-end">
                    <IoMdClose className={closeStyles} onClick={closeHandler} />
                </div>
                <div className="px-7">{children}</div>
            </div>
        </div>,
        document.getElementById('modal-portal') || document.body
    );
};

export default Modal;
