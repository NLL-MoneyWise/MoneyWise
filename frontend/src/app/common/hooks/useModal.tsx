'use client';

import React, { useCallback, useState } from 'react';
import { ModalContainerProps } from '../types';
import Modal from '../components/Modal/Modal';

const useModal = () => {
    const [isOpen, setIsOpen] = useState<boolean>(false);

    const openModal = useCallback(() => {
        setIsOpen(true);
    }, []);

    const closeModal = useCallback(() => {
        setIsOpen(false);
    }, []);

    const ModalWrapper = ({ children }: ModalContainerProps) => {
        return (
            <>{isOpen && <Modal closeHandler={closeModal}>{children}</Modal>}</>
        );
    };

    const ModalComponent = React.memo(ModalWrapper);

    return { openModal, ModalComponent, closeModal };
};

export default useModal;
