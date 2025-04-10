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
        <div className="fixed inset-0 flex justify-center items-center z-50">
            <div
                className="absolute inset-0 bg-black/60 backdrop-blur-sm "
                onClick={closeHandler}
            ></div>

            <div
                className="relative w-[600px] aspect-[4/2.5] bg-white rounded-[20px] p-4 shadow-lg animate-fadeIn z-10"
                onClick={(e) => e.stopPropagation()}
            >
                <div className="flex justify-end">
                    <IoMdClose
                        className={`${closeStyles} cursor-pointer hover:text-gray-700`}
                        onClick={closeHandler}
                    />
                </div>
                <div className="px-7">{children}</div>
            </div>
        </div>,
        document.getElementById('modal-portal') || document.body
    );
};

export default Modal;
