import type { Meta, StoryObj } from '@storybook/react';

import Logo from './Logo';

const meta: Meta<typeof Logo> = {
    component: Logo,
    title: 'atoms/Logo',
    tags: ['autodocs'],
    argTypes: {}
};
export default meta;

type Story = StoryObj<typeof Logo>;

export const Primary: Story = {
    args: {
        variant: 'primary'
    }
};

export const Dark: Story = {
    args: {
        variant: 'dark'
    }
};
