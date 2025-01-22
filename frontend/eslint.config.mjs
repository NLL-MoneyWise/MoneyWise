import { dirname } from 'path';
import { fileURLToPath } from 'url';
import { FlatCompat } from '@eslint/eslintrc';
import prettier from 'eslint-plugin-prettier';

const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);

const compat = new FlatCompat({
    baseDirectory: __dirname
});

const eslintConfig = [
    {
        ignores: [
            'src/stories',
            'src/**/*.stories.{js,ts,tsx}',
            '**/stories/**/*'
        ]
    },
    ...compat.extends('next/core-web-vitals', 'next/typescript'),
    {
        files: ['**/*.{js,ts,tsx}'],
        plugins: {
            prettier: prettier
        },
        rules: {
            'react/react-in-jsx-scope': 'off',
            '@typescript-eslint/no-unused-vars': 'error',
            'prettier/prettier': 'error'
        }
    }
];

export default eslintConfig;
