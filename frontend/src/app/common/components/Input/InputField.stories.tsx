import type { Meta, StoryObj } from '@storybook/react';

import InputField from './InputField';

const meta: Meta<typeof InputField> = {
    component: InputField,
    title: 'atoms/InputField',
    tags: ['autodocs'],
    argTypes: {}
};
export default meta;

type Story = StoryObj<typeof InputField>;

export const Default: Story = {
    args: {}
};
