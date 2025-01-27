import type { Meta, StoryObj } from '@storybook/react';

import TopBar from './TopBar';

const meta: Meta<typeof TopBar> = {
    component: TopBar,
    title: 'atoms/TopBar',
    tags: ['autodocs'],
    argTypes: {}
};
export default meta;

type Story = StoryObj<typeof TopBar>;

export const Default: Story = {
    args: {}
};
