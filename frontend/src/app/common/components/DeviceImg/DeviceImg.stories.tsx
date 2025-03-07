import type { Meta, StoryObj } from '@storybook/react';

import DeviceImg from './DeviceImg';

const meta: Meta<typeof DeviceImg> = {
    component: DeviceImg,
    title: 'atoms/DeviceImg',
    tags: ['autodocs'],
    argTypes: {}
};
export default meta;

type Story = StoryObj<typeof DeviceImg>;

export const Default: Story = {
    args: {}
};
