import type { Meta, StoryObj } from '@storybook/react';

import ToastContainer from './ToastContainer';

const meta: Meta<typeof ToastContainer> = {
    component: ToastContainer,
    title: 'molecule/ToastContainer',
    tags: ['autodocs'],
    argTypes: {}
};
export default meta;

type Story = StoryObj<typeof ToastContainer>;

export const Default: Story = {
    args: {}
};
