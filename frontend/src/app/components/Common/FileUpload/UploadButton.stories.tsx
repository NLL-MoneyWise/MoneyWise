import type { Meta, StoryObj } from '@storybook/react';

import UploadButton from './UploadButton';

const meta: Meta<typeof UploadButton> = {
    component: UploadButton,
    title: 'atoms/UploadButton',
    tags: ['autodocs'],
    argTypes: {}
};
export default meta;

type Story = StoryObj<typeof UploadButton>;

export const Default: Story = {
    args: {
        onFileUpload: () => {
            alert('파일 전송');
        },
        maxSize: 5,
        accept: 'image/*',
        multiple: true
    }
};
