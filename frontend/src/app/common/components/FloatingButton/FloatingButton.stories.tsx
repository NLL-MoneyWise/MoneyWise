import type { Meta, StoryObj } from '@storybook/react';

import FloatingButton from './FloatingButton';

const meta: Meta<typeof FloatingButton> = {
    component: FloatingButton,
    title: 'atoms/FloatingButton',
    tags: ['autodocs'],
    argTypes: {}
};
export default meta;

type Story = StoryObj<typeof FloatingButton>;

export const Default: Story = {
    args: {}
};
