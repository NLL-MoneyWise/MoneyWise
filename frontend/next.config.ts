import type { NextConfig } from 'next';

const nextConfig: NextConfig = {
    async rewrites() {
        const apiUrl = process.env.API_URL;

        return [
            {
                source: '/api/:path*',
                destination: `${apiUrl}/api/:path*`
            }
        ];
    },

    webpack: (config, { isServer }) => {
        if (isServer) {
            if (Array.isArray(config.resolve.alias))
                config.resolve.alias.push({
                    name: 'msw/browser',
                    alias: false
                });
            else config.resolve.alias['msw/browser'] = false;
        } else {
            if (Array.isArray(config.resolve.alias))
                config.resolve.alias.push({ name: 'msw/node', alias: false });
            else config.resolve.alias['msw/node'] = false;
        }
        return config;
    },
    eslint: {
        ignoreDuringBuilds: true
    }
};

export default nextConfig;
