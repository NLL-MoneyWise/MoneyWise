import type { NextConfig } from 'next';

const nextConfig: NextConfig = {
    /* config options here */
    webpack: (config) => {
        // stories 파일 빌드 제외
        config.module.rules.push({
            test: /(\.stories\.tsx?$|stories\/)/,
            loader: 'ignore-loader'
        });
        return config;
    },
    eslint: {
        ignoreDuringBuilds: true
    }
};

export default nextConfig;
