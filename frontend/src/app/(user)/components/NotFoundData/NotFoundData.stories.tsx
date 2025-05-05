import type { Meta, StoryObj } from '@storybook/react';

import NotFoundData from './NotFoundData';

const meta: Meta<typeof NotFoundData> = {
    component: NotFoundData,
    title: 'NotFoundData',
    tags: ['autodocs'],
    argTypes: {}
};
export default meta;

type Story = StoryObj<typeof NotFoundData>;

export const Default: Story = {
    args: {}
};
