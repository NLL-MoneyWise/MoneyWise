import type { Meta, StoryObj } from '@storybook/react';

import Button from './Button';

const meta: Meta<typeof Button> = {
    component: Button,
    title: 'atoms/Button',
    tags: ['autodocs'],
    argTypes: {}
};
export default meta;

type Story = StoryObj<typeof Button>;

export const MiddleButton: Story = {
    args: {
        children: <p>하위</p>,
        size: 'md'
    }
};

export const LargeButton: Story = {
    args: {
        children: <p>하위</p>,
        size: 'lg'
    }
};
