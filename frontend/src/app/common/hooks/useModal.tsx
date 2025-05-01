'use client';

import React, { useState } from 'react';
import { ModalContainerProps } from '../types';
import Modal from '../components/Modal/Modal';

const useModal = () => {
    const [isOpen, setIsOpen] = useState<boolean>(false);

    const openModal = () => {
        setIsOpen(true);
    };

    const closeModal = () => {
        setIsOpen(false);
    };

    const ModalWrapper = ({ children }: ModalContainerProps) => {
        return (
            <>{isOpen && <Modal closeHandler={closeModal}>{children}</Modal>}</>
        );
    };

    const ModalComponent = React.memo(ModalWrapper);

    return { openModal, ModalComponent, closeModal };
};

export default useModal;
