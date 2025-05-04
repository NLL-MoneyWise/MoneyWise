import type { Meta, StoryObj } from '@storybook/react';

import DateNavigator from './DateNavigator';

const meta: Meta<typeof DateNavigator> = {
    component: DateNavigator,
    title: 'DateNavigator',
    tags: ['autodocs'],
    argTypes: {}
};
export default meta;

type Story = StoryObj<typeof DateNavigator>;

export const Default: Story = {
    args: {}
};
