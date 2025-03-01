import type { Meta, StoryObj } from '@storybook/react';

import Modal from './Modal';

const meta: Meta<typeof Modal> = {
    component: Modal,
    title: 'atoms/Modal',
    tags: ['autodocs'],
    argTypes: {}
};
export default meta;

type Story = StoryObj<typeof Modal>;

export const Default: Story = {
    args: {
        children: <>안녕</>,
        closeHandler: () => {
            alert('닫기');
        }
    }
};
