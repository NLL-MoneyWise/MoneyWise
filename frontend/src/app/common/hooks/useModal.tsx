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

    const ModalComponent = ({ children }: ModalContainerProps) => {
        return (
            <>{isOpen && <Modal closeHandler={closeModal}>{children}</Modal>}</>
        );
    };

    return { openModal, ModalComponent };
};

export default useModal;
